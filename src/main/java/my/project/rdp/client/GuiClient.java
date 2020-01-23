package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.MouseEvents;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GuiClient extends JPanel {
    private static ScheduledExecutorService service = Executors.newScheduledThreadPool(1);


    public GuiClient(Size size) throws Exception {
        super(new FlowLayout());

        final Image image = GuiClient.resize(getImage(), size.newWidth, size.newHeight);
        final ImageIcon icon = new ImageIcon(image);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        lbl.addMouseListener(MouseEvents.mouseListener(size));
        lbl.addMouseMotionListener(MouseEvents.mouseMotionListener(size));

        lbl.addMouseWheelListener(MouseEvents.mouseWheelListener());

        add(lbl);
        service.scheduleWithFixedDelay(new ReloadScreen(lbl, size.newWidth, size.newHeight), 1000, 500, TimeUnit.MILLISECONDS);
    }

    private static class ReloadScreen implements Runnable {

        private final JLabel label;
        private final int width;
        private final int height;

        public ReloadScreen(JLabel label, int width, int height) {
            this.label = label;
            this.width = width;
            this.height = height;
        }

        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final Image image = resize(getImage(), width, height);
                    SwingUtilities.invokeLater(() -> {
                        label.setIcon(new ImageIcon(image));
                        label.repaint();
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }


    private static Image getImage() throws Exception {
        final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL);
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(answer.getData());
        return ImageIO.read(inputStream);
    }

    private static Image resize(Image img, int newWith, int newHeifht) {
        return img.getScaledInstance(newWith, newHeifht, Image.SCALE_SMOOTH);
    }


    public static class Size {
        public final double kX;
        public final double kY;
        public final int newWidth;
        public final int newHeight;

        public Size(double kX, double kY, int newWidth, int newHeight) {
            this.kX = kX;
            this.kY = kY;
            this.newWidth = newWidth;
            this.newHeight = newHeight;
        }
    }

    private static Size getScreenSize() throws Exception {
        final Command command = new Command(CommandRegistry.GET_SCREEN_SIZE);
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final Point screenSizeRemote = answer.getDataObj();
        System.out.println("remote screen w,h = " + screenSizeRemote);
        final Point screenSizeLocal = ScreenService.INSTANCE.getScreenSize();
        final int localX = screenSizeLocal.x;
        final int remoteX = screenSizeRemote.x;
        final int localY = screenSizeLocal.y;
        final int remoteY = screenSizeRemote.y;
        if (localX >= remoteX && localY >= remoteY) {
            return new Size(1, 1, remoteX, remoteY);
        }

        final int newWidth = Math.min(localX, remoteX);
        final int newHeight = Math.min(localY, remoteY);
        return new Size(1. * remoteX / newWidth, 1. * remoteY / newHeight, newWidth, newHeight);
    }

    private static void createAndShowGUI() throws Exception {
        //Create and set up the window.
        JFrame frame = new JFrame("GuiClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final Size screenSize = getScreenSize();

        frame.setSize(screenSize.newWidth, screenSize.newHeight);
        //Create and set up the content pane.
        frame.addKeyListener(MouseEvents.keyListener());
        JComponent newContentPane = new GuiClient(screenSize);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        MouseClient.INSTANCE.start("192.168.1.33", 1112);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Utils.rethrowVoid(GuiClient::createAndShowGUI);
            }
        });
    }
}

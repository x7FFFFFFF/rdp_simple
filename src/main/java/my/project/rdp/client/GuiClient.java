package my.project.rdp.client;

import my.project.rdp.other.DefaultThreadFactory;
import my.project.rdp.other.Utils;
import my.project.rdp.server.InputEvents;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static my.project.rdp.Main.*;
import static my.project.rdp.client.ScreenSizeQueue.getScreenSize;
import static my.project.rdp.other.Utils.getArg;
import static my.project.rdp.other.Utils.getArgInt;

public class GuiClient extends JPanel {
    private static ScheduledExecutorService service = Executors
        .newScheduledThreadPool(2, new DefaultThreadFactory("GuiClient"));

    public GuiClient(ScreenSizeQueue.Size size) throws Exception {
        super(new FlowLayout());
        final Image image = GuiClient.resize(getImage(), size.newWidth, size.newHeight);
        final ImageIcon icon = new ImageIcon(image);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        lbl.addMouseListener(InputEvents.mouseListener(size));
        lbl.addMouseMotionListener(InputEvents.mouseMotionListener(size));
        lbl.addMouseWheelListener(InputEvents.mouseWheelListener());
        add(lbl);
        service.scheduleWithFixedDelay(new ReloadScreen(lbl, size.newWidth, size.newHeight), 1000, 500,
                                       TimeUnit.MILLISECONDS);
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

    private static Image getImage() {
        return ScreenShotQueue.take();
    }

    private static Image resize(Image img, int newWith, int newHeifht) {
        return img.getScaledInstance(newWith, newHeifht, Image.SCALE_SMOOTH);
    }

    private static void createAndShowGUI() throws Exception {
        //Create and set up the window.
        JFrame frame = new JFrame("GuiClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final ScreenSizeQueue.Size screenSize = getScreenSize();
        frame.setSize(screenSize.newWidth, screenSize.newHeight);
        //Create and set up the content pane.
        frame.addKeyListener(InputEvents.keyListener());
        JComponent newContentPane = new GuiClient(screenSize);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        MouseClient.INSTANCE.start(getArg(HOST), getArgInt(PORT_2));
        final ScreenHandler command = new ScreenHandler(new Socket(getArg(HOST), getArgInt(PORT_1)));
        service.scheduleWithFixedDelay(command, 0, 200, TimeUnit.MILLISECONDS);
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(() -> Utils.rethrowVoid(GuiClient::createAndShowGUI));
    }

}

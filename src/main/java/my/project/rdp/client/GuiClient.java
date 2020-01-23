package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.MouseEvents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GuiClient extends JPanel {

    public GuiClient(double k, double newWidth, double newHeight) throws Exception {
        super(new FlowLayout());

        final BufferedImage image = getImage();
        GuiClient.resize(image, (int) newWidth, (int) newHeight);
        final ImageIcon icon = new ImageIcon(image);
        JLabel lbl = new JLabel();
        lbl.setIcon(icon);
        lbl.addMouseListener(MouseEvents.mouseListener(k));
        lbl.addMouseMotionListener(MouseEvents.mouseMotionListener(k));

        lbl.addMouseWheelListener(MouseEvents.mouseWheelListener());
        lbl.addKeyListener(MouseEvents.keyListener());
        add(lbl);
    }

    private static BufferedImage getImage() throws Exception {
        final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL);
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(answer.getData());
        return ImageIO.read(inputStream);
    }

    private static Image resize(Image img, int newWith, int newHeifht) {
        return img.getScaledInstance(newWith, newHeifht, Image.SCALE_SMOOTH);
    }


    private static void createAndShowGUI() throws Exception {
        //Create and set up the window.
        JFrame frame = new JFrame("GuiClient");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        final Command command = new Command(CommandRegistry.GET_SCREEN_SIZE);
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final Point point = answer.getDataObj();
        System.out.println("point = " + point);
        final double k = 2;
        final double newWidth = point.getX() / k;
        final double newHeight = point.getY() / k;

        frame.setSize((int) newWidth, (int) newHeight);
        //Create and set up the content pane.
        JComponent newContentPane = new GuiClient(k, newWidth, newHeight);
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

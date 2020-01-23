package my.project.rdp.client;

import my.project.rdp.server.MouseEvents;
import my.project.rdp.server.SimpleServer;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class MouseClientTest {

    public static void main(String[] args) throws Exception {
        SimpleServer.MOUSE_SERVER.start();
        MouseClient.INSTANCE.start("localhost", 1112);
        final int width = 500;
        final int height = 500;
        //ImageIcon icon = new ImageIcon(resize(image, width, height));
        JFrame frame = new JFrame();
        final FlowLayout manager = new FlowLayout();

        frame.setLayout(manager);

        frame.setSize(width, height);
        JLabel lbl = new JLabel();

        frame.addMouseListener(MouseEvents.mouseListener());
        frame.addMouseMotionListener(MouseEvents.mouseMotionListener());
        frame.addMouseWheelListener(MouseEvents.mouseWheelListener());

        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    public static void main1(String[] args) throws AWTException {
        final Robot robot = new Robot();
        robot.mouseMove(100, 100);
        robot.mousePress(1);
    }

}
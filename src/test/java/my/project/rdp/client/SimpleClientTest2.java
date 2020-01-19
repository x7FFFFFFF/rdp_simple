package my.project.rdp.client;

import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

public class SimpleClientTest2 {

    public static void main1(String[] args) throws Exception {

        final int width = 500;
        final int height = 500;
        //ImageIcon icon = new ImageIcon(resize(image, width, height));
        JFrame frame = new JFrame();
        final FlowLayout manager = new FlowLayout();

        frame.setLayout(manager);

                frame.setSize(width, height);
        JLabel lbl = new JLabel();

        frame.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("x = " + e.getX() +" y = "+ e.getY());


                super.mouseMoved(e);
            }
        });
       // lbl.setIcon(icon);
        //panel.add(lbl);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public static void main(String[] args) throws InterruptedException, AWTException {
        final Robot robot = new Robot();
        while (true) {
            System.out.println("args = " + args);
            final Point location = MouseInfo.getPointerInfo().getLocation();
            System.out.println("location = " + location);
            Thread.sleep(100);
        }

    }

}

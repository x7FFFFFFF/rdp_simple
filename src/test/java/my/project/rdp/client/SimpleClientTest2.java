package my.project.rdp.client;

import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.io.IOException;

public class SimpleClientTest2 {

    public static void main(String[] args) throws Exception {

        final int width = 500;
        final int height = 500;
        //ImageIcon icon = new ImageIcon(resize(image, width, height));
        JFrame frame = new JFrame();
        final FlowLayout manager = new FlowLayout();

        frame.setLayout(manager);

                frame.setSize(width, height);
        JLabel lbl = new JLabel();

        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("point = " + e.paramString());

            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("point = " + e.paramString());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("point = " + e.paramString());
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("point = " + e.paramString());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("point = " + e.paramString());
            }
        });


/*        frame.addMouseMotionListener(new MouseMotionAdapter() {

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("point = " + e.paramString());
                super.mouseDragged(e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                System.out.println("point = " + e.paramString());
                super.mouseMoved(e);
            }
        });*/
       // lbl.setIcon(icon);
        //panel.add(lbl);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


    public static void main1(String[] args) throws InterruptedException {
        while (true) {
            System.out.println("args = " + args);
            final Point location = MouseInfo.getPointerInfo().getLocation();
            System.out.println("location = " + location);
            Thread.sleep(100);
        }

    }

}

package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.model.PointSt;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;

import static my.project.rdp.other.Utils.rethrowVoid;

public class SimpleClientTest {


    private static ConcurrentLinkedDeque<Point> deque = new ConcurrentLinkedDeque<>(); //todo: point not immutable

    public static void main(String[] args) throws Exception {
        // try (final SimpleServer server = SimpleServer.INSTANCE.start()) {
        MouseClient.INSTANCE.start("192.168.1.33", 1112);
        //Thread.sleep(2000);
        final double k = 1;
        final BufferedImage image = getImage(k);

        final int width = (int) (image.getWidth() / k);
        final int height = (int) (image.getHeight() / k);
        final Image img0 = resize(image, width, height);
        final ImageIcon icon = new ImageIcon(img0);

        final JFrame frame = new JFrame();
        final FlowLayout manager = new FlowLayout();
        frame.setLayout(manager);
        frame.setSize(width, height);

       // final JPanel panel  = new JPanel();




        JLabel lbl = new JLabel();
        lbl.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                //deque.offer(new Point(e.getX(), e.getY()));
                /*rethrowVoid(() ->
                        SimpleClient.INSTANCE.send(new Command(CommandRegistry.MOUSE_MOVE, Param
                                .ofInt(e.getX(), e.getY())))
                );*/
                System.out.println("point = " + e.getX() +":"+ e.getY());
                //rethrowVoid(() -> MouseClient.INSTANCE.send(e.getX(), e.getY()));
                super.mouseMoved(e);
            }
        });
        lbl.setIcon(icon);
        //panel.add(lbl);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        while (true) {
            Thread.sleep(10);
              final BufferedImage img = getImage(k);

            /*final Point mousePoint = deque.poll();
            if (mousePoint != null) {
                final Answer answer = SimpleClient.INSTANCE.send(new Command(CommandRegistry.MOUSE_MOVE, Param
                        .ofInt(mousePoint.x, mousePoint.y)));
                if (answer == null) {
                    continue;
                }
                Point point = answer.getDataObj();
                System.out.println("point = " + point);
                final Graphics graphics = image.getGraphics();
                int x = (int) (point.x / k);
                int y = (int) (point.y / k);
                graphics.drawLine(x - 20, y, x + 20, y);
                graphics.drawLine(x, y - 20, x, y + 20);
            }*/


            // final Answer mouse = SimpleClient.INSTANCE.send(new Command(CommandRegistry.GET_MOUSE));
            // Point point = mouse.getDataObj();


            icon.setImage(img);
            lbl.repaint();
        }


       /* } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SimpleClient.INSTANCE.close();
        }*/


    }

    private static BufferedImage getImage(double k) throws Exception {
        final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE, Param
                .ofInt(0, 0, 800, 600));
        final Answer answer = SimpleClient.INSTANCE.send(command);
        if (answer == null) {
            return null;
        }
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(answer.getData());
        final BufferedImage image = ImageIO.read(inputStream);

      /*  final Answer mouse = SimpleClient.INSTANCE.send(new Command(CommandRegistry.GET_MOUSE));
        Point point = mouse.getDataObj();
        System.out.println("point = " + point);
        final Graphics graphics = image.getGraphics();
        int x = (int) (point.x / k);
        int y = (int) (point.y / k);
        graphics.drawLine(x - 20, y, x + 20, y);
        graphics.drawLine(x, y - 20, x, y + 20);*/
        return image;
    }

    private static Image resize(Image img, int newWith, int newHeifht) {
        return img.getScaledInstance(newWith, newHeifht, Image.SCALE_SMOOTH);


    }

}
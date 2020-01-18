package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class SimpleClientTest {

    public static void main(String[] args) throws IOException {
        try (final SimpleServer server = SimpleServer.INSTANCE.start()) {

            //Thread.sleep(2000);
            final double k = 2;
            final BufferedImage image = getImage(k);

            final int width = (int) (image.getWidth() / k);
            final int height = (int) (image.getHeight() / k);
            ImageIcon icon = new ImageIcon(resize(image, width, height));
            JFrame frame = new JFrame();
            frame.setLayout(new FlowLayout());

            frame.setSize(width, height);
            JLabel lbl = new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           /* frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    SimpleClient.INSTANCE.close();
                    super.windowClosing(e);
                }
            });*/
            while (true) {
                Thread.sleep(10);
                final BufferedImage img = getImage(k);
                icon.setImage(img);
                lbl.repaint();
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            SimpleClient.INSTANCE.close();
        }


    }

    private static BufferedImage getImage(double k) throws Exception {
        final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL);
        final Answer answer = SimpleClient.INSTANCE.send(command);
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(answer.getData());
        final BufferedImage image = ImageIO.read(inputStream);

        final Answer mouse = SimpleClient.INSTANCE.send(new Command(CommandRegistry.GET_MOUSE));
        Point point = mouse.getDataObj();
        System.out.println("point = " + point);
        final Graphics graphics = image.getGraphics();
        int x = (int) (point.x / k);
        int y = (int) (point.y / k);
        graphics.drawLine(x - 20, y, x + 20, y);
        graphics.drawLine(x, y-20, x , y+20);
        return image;
    }

    private static Image resize(Image img, int newWith, int newHeifht) {
        return img.getScaledInstance(newWith, newHeifht, Image.SCALE_SMOOTH);


    }

}
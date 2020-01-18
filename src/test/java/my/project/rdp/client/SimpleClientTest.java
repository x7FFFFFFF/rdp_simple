package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class SimpleClientTest {

    public static void main(String[] args) throws IOException {
        try (final SimpleServer server = SimpleServer.INSTANCE.start()) {

            //Thread.sleep(2000);
            final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL);
            final Answer answer = SimpleClient.INSTANCE.send(command);
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(answer.getData());
            final BufferedImage image = ImageIO.read(inputStream);

            ImageIcon icon=new ImageIcon(image);
            JFrame frame=new JFrame();
            frame.setLayout(new FlowLayout());
            frame.setSize(200,300);
            JLabel lbl=new JLabel();
            lbl.setIcon(icon);
            frame.add(lbl);
            frame.setVisible(true);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
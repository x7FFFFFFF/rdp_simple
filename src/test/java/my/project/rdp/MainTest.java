package my.project.rdp;

import my.project.rdp.client.GuiClient;
import my.project.rdp.client.ScreenShotQueue;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.InputHandler;
import my.project.rdp.server.OutputHandler;
import my.project.rdp.server.SimpleServer;
import my.project.rdp.services.ScreenService;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static my.project.rdp.Main.*;
import static my.project.rdp.other.Utils.getArgInt;
import static org.junit.Assert.*;

public class MainTest {

    @Test
    @Ignore
    public void testRemote() throws IOException {
        System.setProperty(PORT_1, "1111");
        System.setProperty(PORT_2, "1112");
        new SimpleServer(getArgInt(PORT_1), OutputHandler.class).start();
        new SimpleServer(getArgInt(PORT_2), InputHandler.class).start();
        waitFor();
    }

    private void waitFor() throws IOException {
        System.out.println("exit (0):");
        while (true){
            final int v = System.in.read();
            if (v==0) {
                System.exit(0);
            }
        }
    }

    @Test
    @Ignore
    public void testLocal() throws IOException {
        System.setProperty(HOST, "192.168.1.33");
        System.setProperty(PORT_1, "1111");
        System.setProperty(PORT_2, "1112");

        GuiClient.main(new String[]{});
        waitFor();
    }

    @Test
    @Ignore
    public void testImage() throws Exception {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        CommandRegistry.CREATE_SCREEN_CAPTURE_FULL.send(dataOutputStream);
        dataOutputStream.flush();
        final ByteArrayInputStream is = new ByteArrayInputStream(outputStream.toByteArray());
        final DataInputStream dataInputStream = new DataInputStream(is);
        CommandRegistry.CREATE_SCREEN_CAPTURE_FULL.handle(dataInputStream);
        final BufferedImage image = ScreenShotQueue.take();
        ImageIO.write(image, "JPEG", new File("/tmp/test-screen.jpg"));
    }

}
package my.project.rdp;

import my.project.rdp.client.GuiClient;
import my.project.rdp.server.InputHandler;
import my.project.rdp.server.OutputHandler;
import my.project.rdp.server.SimpleServer;
import my.project.rdp.services.ScreenService;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static my.project.rdp.Main.*;
import static my.project.rdp.other.Utils.getArgInt;
import static org.junit.Assert.*;

public class MainTest {

    @Test
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
    public void testLocal() throws IOException {
        System.setProperty(HOST, "localhost");
        System.setProperty(PORT_1, "1111");
        System.setProperty(PORT_2, "1112");

        GuiClient.main(new String[]{});
        waitFor();
    }

    @Test
    public void testImage() throws IOException {
        final int[] pixels = ScreenService.INSTANCE.getScreenCaptureFull();
        final BufferedImage image = ScreenService.INSTANCE.getScreenCaptureFull(pixels);
        ImageIO.write(image, "JPEG", new File("C:\\Users\\Мария\\Documents\\test.jpg"));
    }

}
package my.project.rdp;

import my.project.rdp.client.GuiClient;
import my.project.rdp.server.InputHandler;
import my.project.rdp.server.OutputHandler;
import my.project.rdp.server.SimpleServer;
import org.junit.Test;

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

    public static void main1(String[] args) throws IOException {

    }

}
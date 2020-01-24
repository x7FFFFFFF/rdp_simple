package my.project.rdp;

import my.project.rdp.client.GuiClient;
import my.project.rdp.other.Utils;
import my.project.rdp.server.InputHandler;
import my.project.rdp.server.OutputHandler;
import my.project.rdp.server.SimpleServer;

import java.io.IOException;

import static my.project.rdp.other.Utils.getArgInt;

public class Main {
    public static final String PORT_1 = "p1";
    public static final String PORT_2 = "p2";
    public static final String HOST = "host";

    public static void main1(String[] args) throws Exception {
        for (String arg : args) {
            System.out.println("arg = " + arg);
        }
        if (!Utils.checkArgs(PORT_1, PORT_2)) {
            System.out.println("Remote desktop ");
            System.out.println("Usage:");
            System.out.println("On remote computer:");
            System.out.println("java -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar");
            System.out.println("On local computer:");
            System.out.println("java -Dhost=<IPV4_REMOTE_HOST> -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar");
        } else {
            final String host = System.getProperty(HOST);
            if (host == null) {
                new SimpleServer(getArgInt(PORT_1), OutputHandler.class).start();
                new SimpleServer(getArgInt(PORT_2), InputHandler.class).start();
            } else {
                GuiClient.main(args);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        System.setProperty(HOST, "localhost");
        System.setProperty(PORT_1, "1111");
        System.setProperty(PORT_2, "1112");
        new SimpleServer(getArgInt(PORT_1), OutputHandler.class).start();
        new SimpleServer(getArgInt(PORT_2), InputHandler.class).start();
        GuiClient.main(args);
    }
}

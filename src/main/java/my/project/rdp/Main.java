package my.project.rdp;

import my.project.rdp.client.GuiClient;
import my.project.rdp.other.Utils;
import my.project.rdp.server.SimpleServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            System.out.println("arg = " + arg);
        }

        if ( !Utils.checkArgs("run", "p1", "p2")) {
            System.out.println(" Remote desktop ");
            System.out.println("Usage:");
            System.out.println("On remote computer:");
            System.out.println("java -Drun=remote -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar");
            System.out.println("On local computer:");
            System.out.println("java -Drun=local -Dhost=<IPV4_REMOTE_HOST> -Dp1=<port1> -Dp2=<port2> -jar rdp-0.1.jar");
            return;
        } else {
            final String run = Utils.getArg("run");
            switch (run) {
                case "remote":
                    SimpleServer.INSTANCE.start();
                    SimpleServer.MOUSE_SERVER.start();
                    break;
                case "local":
                    GuiClient.main(args);
                    break;
                default:
                    throw new IllegalArgumentException("-Drun=" + run);
            }
        }


    }
}

package my.project.rdp.client;

import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.SimpleServer;

import java.io.IOException;

public class SimpleClientTest2 {

    public static void main(String[] args) throws Exception {
        SimpleServer.INSTANCE.start();
        SimpleClient.INSTANCE.send(new Command(CommandRegistry.MOUSE_MOVE, Param
                .ofInt(0, 0)));
    }

}

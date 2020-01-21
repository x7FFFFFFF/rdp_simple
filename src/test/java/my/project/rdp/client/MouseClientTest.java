package my.project.rdp.client;

import my.project.rdp.server.SimpleServer;

import static org.junit.Assert.*;

public class MouseClientTest {

    public static void main(String[] args) throws Exception {
        SimpleServer.MOUSE_SERVER.start();
        MouseClient.INSTANCE.start("localhost", 1112);
        MouseClient.INSTANCE.send(0,0);
        MouseClient.INSTANCE.send(100,100);


    }

}
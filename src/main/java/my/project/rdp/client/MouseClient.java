package my.project.rdp.client;

import my.project.rdp.server.MouseEvents;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrow;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum MouseClient implements AutoCloseable {
    // INSTANCE("192.168.1.33", 1112);
    //INSTANCE("localhost", 1111);
    INSTANCE;
    private volatile Socket clientSocket;
    private volatile DataOutputStream out;
    private volatile DataInputStream in;

    MouseClient() {
        clientSocket = rethrow(Socket::new);

    }

    public MouseClient start(String host, int port) throws IOException {
        System.out.println("Starting client host = " + host + " port = " + port);
        System.out.println(".");
        clientSocket = new Socket(host, port);
        out = rethrow(() -> new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream())));
        in = rethrow(() -> new DataInputStream(
                new BufferedInputStream(clientSocket.getInputStream())));
        while (!clientSocket.isBound() && !clientSocket.isConnected()) {
            System.out.print(".");
        }
        System.out.println("client successfuly started");
        return this;
    }


    public synchronized void send(MouseEvents mouseEvents, InputEvent evt) {
        try {
            mouseEvents.send(out, evt);
            out.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

/*    public void send(int x, int y) throws Exception {
        try {
            MouseEvents.MOVE.send(out, x, y);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }
    }*/

    @Override
    public void close() {
        rethrowVoid(() -> {
            out.close();
            in.close();
            clientSocket.close();
        });
    }


}

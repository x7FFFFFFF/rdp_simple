package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.server.SimpleServer;

import java.io.*;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrow;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum MouseClient implements AutoCloseable {
    INSTANCE("192.168.1.33", 1112);
    //INSTANCE("localhost", 1111);
    private final Socket clientSocket;
    private final DataOutputStream out;
    private final DataInputStream in;

    MouseClient(String host, int port) {
        clientSocket = rethrow(() -> new Socket(host, port));
        out = rethrow(() -> new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream())));
        in = rethrow(() -> new DataInputStream(
                new BufferedInputStream(clientSocket.getInputStream())));
    }

    public void send(int x, int y) throws Exception {
        try {
            out.writeShort(x);
            out.writeShort(y);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace(); //todo
        }
    }

    @Override
    public void close() {
        rethrowVoid(() -> {
            out.close();
            in.close();
            clientSocket.close();
        });
    }



}

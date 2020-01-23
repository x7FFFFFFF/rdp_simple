package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.server.MouseEvents;

import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrow;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum SimpleClient implements AutoCloseable {
    INSTANCE;
    //INSTANCE("localhost", 1111);
    private volatile Socket clientSocket;
    private volatile DataOutputStream out;
    private volatile DataInputStream in;



    public SimpleClient start(String host, int port) throws IOException {
        System.out.println("Starting client host = " + host + " port = " + port);
        System.out.println("getClass() = " + getClass());
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


    public synchronized Answer send(Command command) throws Exception {
        try {
            command.writeObject(out);
            out.flush();
            final Answer answer = new Answer();
            answer.readObject(in);
            return answer;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        //return null;
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

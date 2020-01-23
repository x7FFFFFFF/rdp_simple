package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.server.MouseEvents;

import java.awt.event.MouseEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrow;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum SimpleClient implements AutoCloseable {
    INSTANCE("192.168.1.33", 1111);
    //INSTANCE("localhost", 1111);
    private final Socket clientSocket;
    private final DataOutputStream out;
    private final DataInputStream in;

    SimpleClient(String host, int port) {
        clientSocket = rethrow(() -> new Socket(host, port));
        out = rethrow(() -> new DataOutputStream(clientSocket.getOutputStream()));
        in = rethrow(() -> new DataInputStream(
                clientSocket.getInputStream()));
    }


    public synchronized Answer send(Command command) throws Exception {
        try {
            command.writeObject(out);
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

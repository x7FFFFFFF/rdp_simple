package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;
import my.project.rdp.server.SimpleServer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrow;

public enum SimpleClient implements AutoCloseable {
    INSTANCE("localhost", 1111);
    private final Socket clientSocket;

    SimpleClient(String host, int port) {
        clientSocket = rethrow(() -> new Socket(host, port));
    }

    public Answer send(Command command) {
        try (final DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
             final DataInputStream in = new DataInputStream(
                     clientSocket.getInputStream())) {
            command.writeObject(out);
            final Answer answer = new Answer();
            answer.readObject(in);
            return answer;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        clientSocket.close();
    }





}

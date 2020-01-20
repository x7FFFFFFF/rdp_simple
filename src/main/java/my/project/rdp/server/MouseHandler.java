package my.project.rdp.server;

import my.project.rdp.services.ScreenService;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrowVoid;

public class MouseHandler implements Runnable {
    private final Socket clientSocket;

    public MouseHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    @Override
    public void run() {
        try (final DataOutputStream out = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
             final DataInputStream in = new DataInputStream(
                     new BufferedInputStream(clientSocket.getInputStream()))) {
            final int len = 4;
            //final byte[] val = new byte[len];
            while (!Thread.currentThread().isInterrupted()) {


                final int x = in.readUnsignedShort();
                final int y = in.readUnsignedShort();

                final Point point = new Point(x, y);
                System.out.println("point = " + point);
                ScreenService.INSTANCE.mouseMove(point);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);//TODO error response
        } finally {
            rethrowVoid(clientSocket::close);
        }
    }
}

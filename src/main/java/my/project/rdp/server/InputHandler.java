package my.project.rdp.server;

import my.project.rdp.other.Utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Optional;

import static my.project.rdp.other.Utils.rethrowVoid;

public class InputHandler implements Runnable {
    private final Socket clientSocket;

    public InputHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (final DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
            while (!Thread.currentThread().isInterrupted()) {
                final int ordinal = in.read();
                final Optional<InputEvents> enumOpt = Utils.getEnumOpt(InputEvents.class, ordinal);
                if (!enumOpt.isPresent()) {
                    System.out.println("Missed enumOpt = " + enumOpt);
                    continue;
                }
                System.out.println("MouseEvents = " + enumOpt.get());
                enumOpt.get().handle(in);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);//TODO error response
        } finally {
            rethrowVoid(clientSocket::close);
        }
    }
}

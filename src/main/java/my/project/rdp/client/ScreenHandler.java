package my.project.rdp.client;

import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;
import my.project.rdp.server.InputEvents;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Optional;

import static my.project.rdp.other.Utils.rethrowVoid;

public class ScreenHandler implements Runnable {
    private final Socket clientSocket;

    public ScreenHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (final DataInputStream in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()))) {
            while (!Thread.currentThread().isInterrupted()) {
                final int ordinal = in.read();
                final Optional<CommandRegistry> enumOpt = Utils.getEnumOpt(CommandRegistry.class, ordinal);
                if (!enumOpt.isPresent()) {
                    System.out.println("Missed enumOpt = " + ordinal);
                    continue;
                }
                System.out.println("MouseEvents = " + enumOpt.get());
                enumOpt.get().handle(in);
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);//TODO error response
        } finally {
            rethrowVoid(clientSocket::close);
        }
    }
}

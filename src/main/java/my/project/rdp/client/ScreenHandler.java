package my.project.rdp.client;

import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.Optional;

import static my.project.rdp.other.Utils.rethrowVoid;

public class ScreenHandler implements Runnable {
    private final Socket clientSocket;
    private static final Logger LOGGER = LoggerFactory.getLogger(ScreenHandler.class);

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
            LOGGER.error("ScreenHandler", e);
            throw new RuntimeException(e);//TODO error response
        } finally {
            rethrowVoid(clientSocket::close);
        }
    }
}

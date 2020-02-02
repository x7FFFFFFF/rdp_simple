package my.project.rdp.server;

import my.project.rdp.other.OutStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrowVoid;

public class OutputHandler implements Runnable {
    private final Socket clientSocket;
    private final int timeoutMs = 100;
    private static final Logger LOGGER = LoggerFactory.getLogger(OutputHandler.class);

    public OutputHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (final DataOutputStream out = OutStream.of(clientSocket).buffered().data().get()) {
            send(CommandRegistry.GET_SCREEN_SIZE, out);
            while (!Thread.currentThread().isInterrupted()) {
                send(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL, out);
                Thread.sleep(timeoutMs);
            }
        } catch (Exception e) {
            LOGGER.error("OutputHandler", e);
            throw new RuntimeException(e);//TODO error response
        } finally {
            rethrowVoid(clientSocket::close);
        }
    }

    private void send(CommandRegistry command, DataOutputStream out) throws Exception {
        out.writeByte(command.ordinal());
        command.send(out);
        out.flush();
    }
}

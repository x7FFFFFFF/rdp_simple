package my.project.rdp.server;

import java.io.DataOutputStream;
import java.net.Socket;

import static my.project.rdp.other.Utils.rethrowVoid;

public class OutputHandler implements Runnable{
    private final Socket clientSocket;
    private final int timeoutMs = 100;

    public OutputHandler(Socket socket) {
        this.clientSocket = socket;
    }
    @Override
    public void run() {
        try (final DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {
            send(CommandRegistry.GET_SCREEN_SIZE, out);
            while (!Thread.currentThread().isInterrupted()) {
                send(CommandRegistry.CREATE_SCREEN_CAPTURE_FULL, out);
                Thread.sleep(timeoutMs);
            }
        } catch (Exception e) {
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

package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static my.project.rdp.other.Utils.rethrow;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum SimpleServer implements AutoCloseable {
    INSTANCE(1111);
    private final ServerSocket serverSocket;
    private final ExecutorService executor;
    private final List<Future> futureList = new ArrayList<>();
    private final AtomicBoolean started  = new AtomicBoolean(false);

    SimpleServer(int port) {
        serverSocket = rethrow(() -> new ServerSocket(port));
        executor = Executors.newFixedThreadPool(2);
    }

    public SimpleServer start() throws IOException {
        System.out.print("Starting server");
        futureList.add(executor.submit(new ServerMain()));
        while (!started.get()){
            System.out.print(".");
        }
        System.out.print("server started");
        return this;
    }

    private class ServerMain implements Runnable {

        @Override
        public void run() {
            started.set(true);
            while (!Thread.currentThread().isInterrupted()) {
                rethrowVoid(() -> futureList.add(
                        executor.submit(new ClientHandler(serverSocket.accept()))));
            }
        }
    }


    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try (final DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                 final DataInputStream in = new DataInputStream(
                         clientSocket.getInputStream())) {
                while (!Thread.currentThread().isInterrupted()) {
                    final Command command = new Command();
                    command.readObject(in);
                    final Answer answer = CommandRegistry.exec(command);
                    answer.writeObject(out);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);//TODO error response
            } finally {
                rethrowVoid(clientSocket::close);
            }


        }

    /*    private Answer processCommand(Command command) throws IOException {
            switch (command.getName()) {
                case CREATE_SCREEN_CAPTURE:
                    final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCapture(
                            new Rectangle(command.getIntParam(0), command.getIntParam(1), command.getIntParam(2),
                                    command.getIntParam(3)));
                    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    ImageIO.write(screenCapture, "JPEG", outputStream);
                    return new Answer(1, outputStream.toByteArray());
                default:
                    throw new IllegalArgumentException();
            }
        }*/

    }


    @Override
    public void close() throws Exception {
        for (Future future : futureList) {
            future.cancel(true);
        }
        executor.shutdownNow();
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException {
        SimpleServer.INSTANCE.start();
    }
}

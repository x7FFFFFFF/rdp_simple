package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import static my.project.rdp.other.Utils.getArgInt;
import static my.project.rdp.other.Utils.rethrowVoid;

public enum SimpleServer implements AutoCloseable {
    INSTANCE(getArgInt("p1"), ClientHandler.class),
    MOUSE_SERVER(getArgInt("p2"), MouseHandler.class);
    private final ServerSocket serverSocket;
    private final Constructor<? extends Runnable> constructor;
    private final ExecutorService executor;
    private final List<Future> futureList = new ArrayList<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final int port;
    private final Class<? extends Runnable> clientHandlerClass;

    SimpleServer(int port, Class<? extends Runnable> clientHandlerClass) {
        this.port = port;
        this.clientHandlerClass = clientHandlerClass;
        try {
            constructor = clientHandlerClass.getConstructor(Socket.class);
            serverSocket = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SimpleServer start() throws IOException {
        System.out.print("Starting server port=" + port);
        System.out.println("clientHandlerClass = " + clientHandlerClass);
        futureList.add(executor.submit(new ServerMain()));
        while (!started.get()) {
            System.out.print(".");
        }
        System.out.print("server started");
        return this;
    }

    private class ServerMain implements Callable<Void> {

        @Override
        public Void call() throws Exception {
            started.set(true);

            while (!Thread.currentThread().isInterrupted()) {
                futureList.add(
                        executor.submit(constructor.newInstance(serverSocket.accept())));
            }
            return null;
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
        SimpleServer.MOUSE_SERVER.start();
    }
}

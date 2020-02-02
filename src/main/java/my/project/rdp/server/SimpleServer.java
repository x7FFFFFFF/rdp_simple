package my.project.rdp.server;

import my.project.rdp.other.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class SimpleServer implements AutoCloseable {
    //INSTANCE(getArgInt("p1"), ScreenHandler.class),
    //MOUSE_SERVER(getArgInt("p2"), MouseHandler.class);
    private final ServerSocket serverSocket;
    private final Constructor<? extends Runnable> constructor;
    private final ExecutorService executor;
    private final List<Future> futureList = new ArrayList<>();
    private final AtomicBoolean started = new AtomicBoolean(false);
    private final int port;
    private final Class<? extends Runnable> clientHandlerClass;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    public SimpleServer(int port, Class<? extends Runnable> clientHandlerClass) {
        this.port = port;
        this.clientHandlerClass = clientHandlerClass;
        try {
            constructor = clientHandlerClass.getConstructor(Socket.class);
            serverSocket = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(2, new DefaultThreadFactory("SimpleServer"));
        } catch (Exception e) {
            LOGGER.error("SimpleServer", e);
            throw new RuntimeException(e);
        }
    }

    public SimpleServer start() {
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
                futureList.add(executor.submit(constructor.newInstance(serverSocket.accept())));
            }
            return null;
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

 }

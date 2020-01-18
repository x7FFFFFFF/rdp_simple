package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.spi.IIORegistry;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {



    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("localhost", 1111));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 1);
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                if (key.isAcceptable()) {
                    register(selector, serverSocket);
                }
                if (key.isReadable()) {
                    readCommand(buffer, key);
                }
                iter.remove();
            }
        }
    }

    private static void readCommand(ByteBuffer buffer, SelectionKey key) throws Exception {
        SocketChannel client = (SocketChannel) key.channel();
        client.read(buffer);
        buffer.rewind();
        final Command command = new Command();
        command.readObject(buffer);
        switch (command.getName()) {
        case CREATE_SCREEN_CAPTURE:
            final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCapture(
                new Rectangle(command.getIntParam(0), command.getIntParam(1), command.getIntParam(2),
                              command.getIntParam(3)));
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(screenCapture, "JPEG", outputStream);
            final Answer answer = new Answer(1, outputStream.toByteArray());
            buffer.clear();
            answer.writeObject(buffer);
            buffer.flip();
            client.write(buffer);
            buffer.clear();
            break;
        default:
            throw new IllegalArgumentException();
        }



    /*    if (new String(buffer.array()).trim().equals(POISON_PILL)) {
            client.close();
            System.out.println("Not accepting client messages anymore");
        }*/
       /* buffer.flip();
        client.write(buffer);
        buffer.clear();*/
    }

    private static void register(Selector selector, ServerSocketChannel serverSocket) throws IOException {
        SocketChannel client = serverSocket.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
    }

}





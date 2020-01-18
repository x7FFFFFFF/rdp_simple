package my.project.rdp.client;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.Param;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public enum Client {
    INSTANCE;
    private final SocketChannel client;
    private final ByteBuffer buffer;



    public void stop() throws IOException {
        client.close();
    }

    Client() {
        try {
            client = SocketChannel.open(new InetSocketAddress("localhost", 1111));
            buffer = ByteBuffer.allocate(1024 * 1024 * 1);
        } catch (IOException e) {
           throw new IllegalStateException();
        }
    }

    public Answer sendMessage(Command command) throws Exception {
        buffer.clear();
        command.writeObject(buffer);
        buffer.flip();
        Answer response = null;
        try {
            client.write(buffer);
            buffer.clear();
            client.read(buffer);
            final Answer answer = new Answer();
            buffer.rewind();
            answer.readObject(buffer);
            response = answer;
            System.out.println("response=" + response);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;

    }

    public static void main(String[] args) throws Exception {
        final Command command = new Command(Command.SupportedCommands.CREATE_SCREEN_CAPTURE, Param
            .of(new Param(Param.SupportedTypes.INT, 0), new Param(Param.SupportedTypes.INT, 0),
                new Param(Param.SupportedTypes.INT, 100), new Param(Param.SupportedTypes.INT, 100)));
        final Answer answer = Client.INSTANCE.sendMessage(command);
        System.out.println("answer = " + answer);

    }
}

package my.project.rdp.model;

import my.project.rdp.server.CommandRegistry;
import org.junit.Test;

import java.nio.ByteBuffer;


public class CommandTest {
    @Test
    public void test() throws Exception {
        final Command command = new Command(CommandRegistry.CREATE_SCREEN_CAPTURE, Param
                .ofInt(0, 0,
                        100, 100));
 /*       final ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        command.writeObject(byteBuffer);
       // byteBuffer.flip();
        final Command command1 = new Command();
        command1.readObject(byteBuffer);
        System.out.println("command1 = " + command1);*/
        //checkSerialization(command);
        //Utils.checkSerializationByteBuffer(command);
        Utils.checkSerializationDataIO(command);

    }

}
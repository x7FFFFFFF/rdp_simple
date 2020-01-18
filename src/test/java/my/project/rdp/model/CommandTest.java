package my.project.rdp.model;

import org.junit.Test;

import java.nio.ByteBuffer;

import static my.project.rdp.model.Utils.checkSerialization;

public class CommandTest {
    @Test
    public void test() throws Exception {
        final Command command = new Command(Command.SupportedCommands.CREATE_SCREEN_CAPTURE, Param
            .of(new Param(Param.SupportedTypes.INT, 0), new Param(Param.SupportedTypes.INT, 0),
                new Param(Param.SupportedTypes.INT, 100), new Param(Param.SupportedTypes.INT, 100)));
        final ByteBuffer byteBuffer = ByteBuffer.allocate(256);
        command.writeObject(byteBuffer);
       // byteBuffer.flip();
        final Command command1 = new Command();
        command1.readObject(byteBuffer);
        System.out.println("command1 = " + command1);
        //checkSerialization(command);

    }

}
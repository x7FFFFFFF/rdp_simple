package my.project.rdp.model;

import my.project.rdp.server.CommandRegistry;
import org.junit.Test;

import static org.junit.Assert.*;

public class AnswerTest {
    @Test
    public void test() throws Exception {
        {
            final Answer answer = new Answer(1, CommandRegistry.CREATE_SCREEN_CAPTURE, new byte[]{1, 2, 3});
            //Utils.checkSerializationByteBuffer(answer);
            Utils.checkSerializationDataIO(answer);

        }

        {

            //Utils.checkSerializationDataIO(new Answer(1, CommandRegistry.MOUSE_MOVE));
        }

    }
}
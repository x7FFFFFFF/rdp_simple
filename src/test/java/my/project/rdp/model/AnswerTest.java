package my.project.rdp.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AnswerTest {
    @Test
    public void test() throws Exception {
        final Answer answer = new Answer(1, new byte[] { 1, 2, 3 });
        Utils.checkSerialization(answer);

    }
}
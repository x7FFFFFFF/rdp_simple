package my.project.rdp.model;

import org.junit.Assert;

import java.io.*;
import java.nio.ByteBuffer;

final class Utils {
    private Utils() {
    }

    static void checkSerialization(Storable storable) throws Exception {
        final ByteBuffer byteBuffer  = ByteBuffer.allocate(255);
        storable.writeObject(byteBuffer);
        byteBuffer.flip();
        final Storable storable1 = storable.getClass().newInstance();
        storable1.readObject(byteBuffer);
        Assert.assertEquals(storable, storable1);
    }
}

package my.project.rdp.model;

import org.junit.Assert;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;

final class Utils {
    private Utils() {
    }

    static void checkSerializationByteBuffer(Storable storable) throws Exception {
        final ByteBuffer byteBuffer  = ByteBuffer.allocate(255);
        storable.writeObject(byteBuffer);
        byteBuffer.flip();
        final Storable storable1 = storable.getClass().newInstance();
        storable1.readObject(byteBuffer);
        Assert.assertEquals(storable, storable1);
    }


    static void checkSerializationDataIO(Storable storable) throws Exception {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try(final DataOutputStream dos = new DataOutputStream(bos)){
            storable.writeObject(dos);
            try(final DataInputStream bis = new DataInputStream((new ByteArrayInputStream(bos.toByteArray())))){
                final Storable storable1 = storable.getClass().newInstance();
                storable1.readObject(bis);
                Assert.assertEquals(storable, storable1);
            }
        }
    }
}

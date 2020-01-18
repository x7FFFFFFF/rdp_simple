package my.project.rdp.model;

import java.io.*;
import java.nio.ByteBuffer;

public interface Storable {
    default void readObject(ByteBuffer dis) throws Exception {
        throw new UnsupportedOperationException();
    }

    default  void writeObject(ByteBuffer dos) throws Exception {
        throw new UnsupportedOperationException();
    }


    void readObject(DataInput dis) throws Exception;

    void writeObject(DataOutput dos) throws Exception;
}

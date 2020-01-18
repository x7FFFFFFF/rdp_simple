package my.project.rdp.model;

import java.io.*;
import java.nio.ByteBuffer;

public interface Storable {
    void readObject(ByteBuffer dis) throws Exception;

    void writeObject(ByteBuffer dos) throws Exception;


    void readObject(DataInput dis) throws Exception;

    void writeObject(DataOutput dos) throws Exception;
}

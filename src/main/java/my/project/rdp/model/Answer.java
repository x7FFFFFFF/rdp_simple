package my.project.rdp.model;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Answer implements Storable {
    private byte status;
    private int length;
    private byte[] data;

    public Answer() {
    }

    public Answer(int status, byte[] data) {
        this.status = (byte) status;
        this.length = data.length;
        this.data = data;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }


    @Override
    public void readObject(ByteBuffer buffer) throws Exception {
        status = buffer.get();
        length = buffer.getInt();
        data = new byte[length];
        for (int i = 0; i < length; i++) {
             data[i] = buffer.get();
        }
    }

    @Override
    public void writeObject(ByteBuffer buffer) throws Exception {
        buffer.put(status);
        buffer.putInt(length);
        for (int i = 0; i < length; i++) {
            buffer.put(data[i]);
        }
    }

    @Override
    public void readObject(DataInput dis) throws Exception {
        status = dis.readByte();
        length = dis.readInt();
        data = new byte[length];
        dis.readFully(data);
    }

    @Override
    public void writeObject(DataOutput dos) throws Exception {
        dos.writeByte(status);
        dos.writeInt(length);
        dos.write(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Answer answer = (Answer) o;
        if (status != answer.status)
            return false;
        if (length != answer.length)
            return false;
        return Arrays.equals(data, answer.data);
    }

    @Override
    public int hashCode() {
        int result = (int) status;
        result = 31 * result + length;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}

package my.project.rdp.server;

import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DataOutputBuffer implements DataOutput {
    /**
     * The number of bytes written to the data output stream so far.
     */
    protected int written;

    private ByteBuffer byteBuffer;


    public DataOutputBuffer(int initialCapacity) {
        byteBuffer = ByteBuffer.allocate(initialCapacity);
    }

    private void checkSize(){
        if (!byteBuffer.hasRemaining()) {
            byteBuffer = byteBuffer.duplicate();
        }
    }

    @Override
    public void write(int b) throws IOException {




    }

    @Override
    public void write(byte[] b) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBoolean(boolean v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeByte(int v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeShort(int v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChar(int v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeInt(int v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeLong(long v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeFloat(float v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeDouble(double v) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeBytes(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeChars(String s) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new UnsupportedOperationException();
    }
}

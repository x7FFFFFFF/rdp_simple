package my.project.rdp.model;

import java.awt.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.nio.ByteBuffer;

public class PointSt extends java.awt.Point implements Storable {

    public PointSt() {
    }

    public PointSt(Point point) {
        super(point);
    }

    @Override
    public void readObject(ByteBuffer dis) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void writeObject(ByteBuffer dos) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public void readObject(DataInput dis) throws Exception {
        x = dis.readInt();
        y = dis.readInt();

    }

    @Override
    public void writeObject(DataOutput dos) throws Exception {
        dos.writeInt(x);
        dos.writeInt(y);

    }
}

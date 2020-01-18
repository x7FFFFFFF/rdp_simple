package my.project.rdp.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Objects;
import java.util.stream.Stream;

import static my.project.rdp.other.Utils.*;

public class Param implements Storable {
    private SupportedTypes type;
    private Object value;

    public enum SupportedTypes implements Types {
        INT,
        STR
    }

    interface Types {
    }

    interface Converter {
        void read(Param param, ByteBuffer dis) throws IOException;
        void read(Param param, DataInput dis) throws IOException;
        void write(Param param, ByteBuffer dos) throws IOException;
        void write(Param param, DataOutput dos) throws IOException;
    }

    // private static final int INT = 0;
    //private static final int STR = 1;
    private static final EnumMap<SupportedTypes, Param.Converter> map = new EnumMap<>(SupportedTypes.class);

    static {
        map.put(SupportedTypes.INT, new Converter() {
            @Override
            public void read(Param param, ByteBuffer buffer) throws IOException {
                param.type = SupportedTypes.INT;
                param.value = buffer.getInt();
            }

            @Override
            public void read(Param param, DataInput dis) throws IOException {
                param.type = SupportedTypes.INT;
                param.value = dis.readInt();
            }

            @Override
            public void write(Param param, ByteBuffer buffer) throws IOException {
                buffer.putInt(param.type.ordinal());
                buffer.putInt((Integer) param.value);

            }

            @Override
            public void write(Param param, DataOutput dos) throws IOException {
                dos.writeInt(param.type.ordinal());
                dos.writeInt((Integer) param.value);
            }
        });
        map.put(SupportedTypes.STR, new Converter() {
            @Override
            public void read(Param param, ByteBuffer buffer) throws IOException {
                param.type = SupportedTypes.STR;
                param.value = readUTF(buffer);
            }

            @Override
            public void read(Param param, DataInput dis) throws IOException {
                param.type = SupportedTypes.STR;
                param.value = dis.readUTF();
            }

            @Override
            public void write(Param param, ByteBuffer buffer) throws IOException {
                buffer.putInt(param.type.ordinal());
                writeUTF((String) param.value, buffer);
            }

            @Override
            public void write(Param param, DataOutput dos) throws IOException {
                dos.writeInt(param.type.ordinal());
                dos.writeUTF((String) param.value);

            }
        });
    }

    public Param() {
    }

    public Param(SupportedTypes type, Object value) {
        this.type = type;
        this.value = value;
    }

    public SupportedTypes getType() {
        return type;
    }

    public void setType(SupportedTypes type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public void readObject(ByteBuffer buffer) throws Exception {
        final int type = buffer.getInt();
        map.get(getEnum(SupportedTypes.class, type)).read(this, buffer);
    }



    @Override
    public void writeObject(ByteBuffer buffer) throws Exception {
        map.get(type).write(this, buffer);
    }

    @Override
    public void readObject(DataInput dis) throws Exception {
        final int type = dis.readInt();
        map.get(getEnum(SupportedTypes.class, type)).read(this, dis);
    }

    @Override
    public void writeObject(DataOutput dos) throws Exception {
        map.get(type).write(this, dos);
    }

    public static Param[] of(Param... params) {
        return params;
    }


    public static Param[] ofInt(Integer... params) {
        return Stream.of(params).map(i->new Param(SupportedTypes.INT, i)).toArray(Param[]::new);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Param param = (Param) o;
        return type == param.type &&
                Objects.equals(value, param.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value);
    }
}

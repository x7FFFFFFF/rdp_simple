package my.project.rdp.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.EnumMap;

import static my.project.rdp.Utils.*;

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

        void write(Param param, ByteBuffer dos) throws IOException;

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
            public void write(Param param, ByteBuffer buffer) throws IOException {
                buffer.putInt(param.type.ordinal());
                buffer.putInt((Integer) param.value);

            }
        });
        map.put(SupportedTypes.STR, new Converter() {
            @Override
            public void read(Param param, ByteBuffer buffer) throws IOException {
                param.type = SupportedTypes.STR;
                param.value = readUTF(buffer);
            }

            @Override
            public void write(Param param, ByteBuffer buffer) throws IOException {
                buffer.putInt(param.type.ordinal());
                writeUTF((String) param.value, buffer);
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

    public static Param[] of(Param... params) {
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Param param = (Param) o;
        if (type != param.type)
            return false;
        return value.equals(param.value);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }
}

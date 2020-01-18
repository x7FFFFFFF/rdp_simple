package my.project.rdp;

import my.project.rdp.model.Param;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class Utils {
    private Utils() {
    }

    public static <E extends Enum<E>> E getEnum(Class<E> clz, int val) {
        for (E value : clz.getEnumConstants()) {
            if (value.ordinal() == val) {
                return value;
            }
        }
        throw new IllegalArgumentException("" + val);
    }

    public static String readUTF(ByteBuffer buffer) throws IOException {
        final int size = buffer.getInt();
        final byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[i] = buffer.get();
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static void writeUTF(String val, ByteBuffer buffer) throws IOException {
        final int size = val.length();
        final byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
        buffer.putInt(size);
        for (int i = 0; i < size; i++) {
            buffer.put(bytes[i]);
        }
    }
}

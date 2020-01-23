package my.project.rdp.other;

import my.project.rdp.model.Param;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

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

    public static <E extends Enum<E>> Optional<E> getEnumOpt(Class<E> clz, int val) {
        for (E value : clz.getEnumConstants()) {
            if (value.ordinal() == val) {
                return Optional.of(value);
            }
        }
        return Optional.empty();
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


    public static <T, V> V rethrow(T val, FunctionWithEx<T, V> func) {
        try {
            return func.apply(val);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static <T> T rethrow(SupplierWithEx<T> supl) {
        try {
            return supl.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void rethrowVoid(SupplierVoidWithEx supl) {
        try {
            supl.doSmth();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static int getArgInt(String name) {
        return Integer.parseInt(getArg(name));
    }

    public static String getArg(String name) {
        final String host = System.getProperty(name);
        if (host == null) {
            throw new IllegalArgumentException("missing -D" + name);
        }
        return host;
    }


    public static boolean checkArgs(String... names) {
        for (String name : names) {
            final String value = System.getProperty(name);
            if (value == null) {
                return false;
            }
        }
        return true;
    }


}

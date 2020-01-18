package my.project.rdp.model;

import my.project.rdp.Utils;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Command implements Storable {
    private SupportedCommands name;
    private byte length;
    private Param[] params;

    public enum SupportedCommands {
        CREATE_SCREEN_CAPTURE;
    }

    public Command() {
    }

    public Command(SupportedCommands name, Param[] params) {
        this.name = name;
        this.length = (byte) params.length;
        this.params = params;
    }

    public SupportedCommands getName() {
        return name;
    }

    public Integer getIntParam(int i) {
        final Param param = params[i];
        if (param.getType() != Param.SupportedTypes.INT) {
            throw new IllegalArgumentException();
        }
        return (Integer) param.getValue();
    }

    public String getStrParam(int i) {
        final Param param = params[i];
        if (param.getType() != Param.SupportedTypes.STR) {
            throw new IllegalArgumentException();
        }
        return (String) param.getValue();
    }

    public void setName(SupportedCommands name) {
        this.name = name;
    }

    public Param[] getParams() {
        return params;
    }

    public void setParams(Param[] params) {
        this.params = params;
    }

    public byte getLength() {
        return length;
    }

    public void setLength(byte length) {
        this.length = length;
    }

    @Override
    public void readObject(ByteBuffer buffer) throws Exception {
        name = Utils.getEnum(SupportedCommands.class, buffer.getInt());
        length = buffer.get();
        params = new Param[length];
        for (int i = 0; i < params.length; i++) {
            final Param param = new Param();
            param.readObject(buffer);
            params[i] = param;
        }
    }

    @Override
    public void writeObject(ByteBuffer buffer) throws Exception {
        buffer.putInt(name.ordinal());
        buffer.put(length);
        for (Param param : params) {
            param.writeObject(buffer);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Command command = (Command) o;
        if (length != command.length)
            return false;
        if (!name.equals(command.name))
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(params, command.params);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) length;
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}

package my.project.rdp.model;

import my.project.rdp.other.Utils;
import my.project.rdp.other.ConsumerWithEx;
import my.project.rdp.server.CommandRegistry;

import java.io.DataInput;
import java.io.DataOutput;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Command implements Storable {
    private CommandRegistry name;
    private byte length;
    private Param[] params;


    public Command() {
    }

    public Command(CommandRegistry name, Param[] params) {
        this.name = name;
        this.length = (byte) params.length;
        this.params = params;
    }

    public Command(CommandRegistry  name) {
        this.name = name;
        this.params = new Param[0];
    }

    public CommandRegistry getName() {
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

    public void setName(CommandRegistry name) {
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
        name = Utils.getEnum(CommandRegistry.class, buffer.getInt());
        length = buffer.get();
        params = new Param[length];
        params = readParams(length, param -> param.readObject(buffer));
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
    public void readObject(DataInput dis) throws Exception {
        name = Utils.getEnum(CommandRegistry.class, dis.readInt());
        length = dis.readByte();
        params = readParams(length, param -> param.readObject(dis));
    }


    private static Param[] readParams(int length, ConsumerWithEx<Param> consusmer) throws Exception {
        final Param[] params = new Param[length];
        for (int i = 0; i < params.length; i++) {
            final Param param = new Param();
            consusmer.accept(param);
            params[i] = param;
        }
        return params;
    }

    @Override
    public void writeObject(DataOutput dos) throws Exception {
        dos.writeInt(name.ordinal());
        dos.writeByte(length);
        for (Param param : params) {
            param.writeObject(dos);
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

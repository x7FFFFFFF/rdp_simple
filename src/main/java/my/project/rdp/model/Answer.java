package my.project.rdp.model;

import my.project.rdp.other.Utils;
import my.project.rdp.server.CommandRegistry;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Objects;

public class Answer implements Storable {
    private byte status;
    private CommandRegistry command;
    private int length;
    private byte[] data;

    public Answer() {
    }

    public Answer(int status, CommandRegistry command, byte[] data) {
        this.status = (byte) status;
        this.command = command;
        this.length = data.length;
        this.data = data;
    }

    public Answer(int status,  CommandRegistry command) {
        this.status = (byte) status;
        this.command = command;
        this.length = 0;
        this.data = new byte[0];
    }

    @SuppressWarnings("unchecked")
    public <T> T getDataObj() throws Exception {
        return (T) command.decryptAnsver(data);
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


 /*   @Override
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
    }*/

    @Override
    public void readObject(DataInput dis) throws Exception {
        status = dis.readByte();
        command = Utils.getEnum(CommandRegistry.class, dis.readInt());
        length = dis.readInt();
        data = new byte[length];
        dis.readFully(data);
    }

    @Override
    public void writeObject(DataOutput dos) throws Exception {
        dos.writeByte(status);
        dos.writeInt(command.ordinal());
        dos.writeInt(length);
        dos.write(data);
    }

    public CommandRegistry getCommand() {
        return command;
    }

    public void setCommand(CommandRegistry command) {
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return status == answer.status &&
                length == answer.length &&
                command == answer.command &&
                Arrays.equals(data, answer.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(status, command, length);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}

package my.project.rdp.server;

import my.project.rdp.services.ScreenService;

import java.awt.*;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public enum MouseEvents implements Event {

    MOVE{
        @Override
        public void handle(DataInput in) throws Exception {
            final int x = in.readUnsignedShort();
            final int y = in.readUnsignedShort();

            final Point point = new Point(x, y);
            System.out.println("point = " + point);
            ScreenService.INSTANCE.mouseMove(point);
        }
        @Override
        public void send(DataOutput out, int... coordinates) throws Exception {
            out.write(ordinal());
            out.writeShort(coordinates[0]); //x
            out.writeShort(coordinates[1]); //Y
        }

    };
}

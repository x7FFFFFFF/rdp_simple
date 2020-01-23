package my.project.rdp.server;

import my.project.rdp.client.MouseClient;
import my.project.rdp.client.SimpleClient;
import my.project.rdp.services.ScreenService;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.function.BiConsumer;

public enum MouseEvents implements Event {

    MOVE {
        @Override
        public void handle(DataInput in) throws Exception {
            final int x = in.readUnsignedShort();
            final int y = in.readUnsignedShort();

            final Point point = new Point(x, y);
            System.out.println("point = " + point);
            ScreenService.INSTANCE.mouseMove(point);
        }

        @Override
        public void send(DataOutput out, MouseEvent e) throws Exception {
            out.write(ordinal());
            out.writeShort(e.getX()); //x
            out.writeShort(e.getY()); //Y
        }
    },
    PRESS {
        @Override
        public void handle(DataInput in) throws Exception {
            handle(in, ScreenService.INSTANCE::mousePress);
        }

        @Override
        public void send(DataOutput out, MouseEvent e) throws Exception {
            sendImpl(out, e);
        }


    },
    RELEASE {
        @Override
        public void handle(DataInput in) throws Exception {
            handle(in,  ScreenService.INSTANCE::mouseRelease);
        }

        @Override
        public void send(DataOutput out, MouseEvent e) throws IOException {
            sendImpl(out, e);
        }

    };

    protected void sendImpl(DataOutput out, MouseEvent e) throws IOException {
        out.write(ordinal());
        out.writeShort(e.getX()); //x
        out.writeShort(e.getY()); //Y
        out.writeShort(MouseEvent.getMaskForButton(e.getButton())); //buttons
    }

    protected void handle(DataInput in, BiConsumer<Point, Integer> consumer) throws IOException {
        final int x = in.readUnsignedShort();
        final int y = in.readUnsignedShort();
        final int buttons = in.readUnsignedShort();
        final Point point = new Point(x, y);
        System.out.println("point = " + point);
        consumer.accept(point, buttons);
    }


    public static MouseMotionListener mouseMotionListener() {
        return new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                MouseClient.INSTANCE.send(MouseEvents.MOVE, e);
            }
        };
    }

    public static MouseListener mouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                MouseClient.INSTANCE.send(MouseEvents.PRESS, e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                MouseClient.INSTANCE.send(MouseEvents.RELEASE, e);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }


}

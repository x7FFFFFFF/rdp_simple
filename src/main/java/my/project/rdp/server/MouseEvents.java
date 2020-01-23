package my.project.rdp.server;

import my.project.rdp.client.MouseClient;
import my.project.rdp.client.SimpleClient;
import my.project.rdp.services.ScreenService;

import java.awt.*;
import java.awt.event.*;
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
        public void send(DataOutput out, InputEvent e) throws Exception {
            final MouseEvent mouseEvent = (MouseEvent) e;
            out.write(ordinal());
            out.writeShort(mouseEvent.getX()); //x
            out.writeShort(mouseEvent.getY()); //Y
        }
    },
    PRESS {
        @Override
        public void handle(DataInput in) throws Exception {
            handle(in, ScreenService.INSTANCE::mousePress);
        }

        @Override
        public void send(DataOutput out, InputEvent e) throws Exception {
            final MouseEvent mouseEvent = (MouseEvent) e;
            sendImpl(out, mouseEvent);
        }


    },
    RELEASE {
        @Override
        public void handle(DataInput in) throws Exception {
            handle(in, ScreenService.INSTANCE::mouseRelease);
        }

        @Override
        public void send(DataOutput out, InputEvent e) throws IOException {
            final MouseEvent mouseEvent = (MouseEvent) e;
            sendImpl(out, mouseEvent);
        }

    },
    WHEEL {
        @Override
        public void handle(DataInput input) throws Exception {
            final int wheel = input.readInt();
            System.out.println("wheel = " + wheel);
            ScreenService.INSTANCE.mouseWheel(wheel);
        }

        @Override
        public void send(DataOutput out, InputEvent e) throws Exception {
            out.write(ordinal());
            final int wheelRotation = ((MouseWheelEvent) e).getWheelRotation();
            System.out.println("wheelRotation = " + wheelRotation);
            out.writeInt(wheelRotation);
        }
    },
    KEY_PRESSED {
        @Override
        public void handle(DataInput input) throws Exception {
            System.out.println("handle = " + this);
            final int keyCode = input.readInt();
            System.out.println("keyCode = " + keyCode);
            ScreenService.INSTANCE.keyPressed(keyCode);
        }

        @Override
        public void send(DataOutput out, InputEvent e) throws Exception {
            sendKey(out, (KeyEvent) e);

        }


    },
    KEY_RELEASED {
        @Override
        public void handle(DataInput input) throws Exception {
            System.out.println("handle = " + this);
            final int keyCode = input.readInt();
            System.out.println("keyCode = " + keyCode);
            ScreenService.INSTANCE.keyRealeased(keyCode);
        }

        @Override
        public void send(DataOutput out, InputEvent e) throws Exception {
            sendKey(out, (KeyEvent) e);

        }
    };

    protected void sendKey(DataOutput out, KeyEvent e) throws IOException {
        System.out.println("send = " + this);
        out.write(ordinal());
        final int keyCode = e.getKeyCode();
        System.out.println("keyCode = " + keyCode);
        out.writeInt(keyCode);
    }

    public static KeyListener keyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                // System.out.println("e.paramString() = " + e.paramString());
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("e.paramString() = " + e.paramString());
                MouseClient.INSTANCE.send(MouseEvents.KEY_PRESSED, e);

            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println("e.paramString() = " + e.paramString());
                MouseClient.INSTANCE.send(MouseEvents.KEY_RELEASED, e);
            }
        };

    }

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

    public static MouseWheelListener mouseWheelListener() {
        return e -> MouseClient.INSTANCE.send(MouseEvents.WHEEL, e);
    }


}

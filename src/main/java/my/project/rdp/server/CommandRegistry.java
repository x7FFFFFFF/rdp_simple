package my.project.rdp.server;

import my.project.rdp.client.ScreenShotQueue;
import my.project.rdp.client.ScreenSizeQueue;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;

public enum CommandRegistry implements CommandExecutor {


    CREATE_SCREEN_CAPTURE_FULL {
        @Override
        public void handle(DataInput input) throws Exception {
            final int width = input.readInt();
            final int height = input.readInt();
            final int length = input.readInt();
            final int[] data = new int[length];
            for (int i = 0; i < length; i++) {
                 data[i]= input.readInt();
            }
            final BufferedImage image = ScreenService.INSTANCE.getScreenCaptureFull(width, height, data);
            ScreenShotQueue.put(image);
        }

        @Override
        public void send(DataOutput out) throws Exception {
            final int[] rgb = ScreenService.INSTANCE.getScreenCaptureFull();
            //final int resLen = rgb.length * 4;
            final int length = rgb.length;
            final Point screenSize = ScreenService.INSTANCE.getScreenSize();
            out.writeInt(screenSize.x);
            out.writeInt(screenSize.y);
            out.writeInt(length);
           // out.write(res);
            for (int i = 0; i < length; i++) {
                out.writeInt(rgb[i]);
            }
        }

    },
    GET_SCREEN_SIZE {
        @Override
        public void handle(DataInput input) throws Exception {
            final int width = input.readUnsignedShort();
            final int height = input.readUnsignedShort();
            ScreenSizeQueue.put(new Point(width, height));
        }

        @Override
        public void send(DataOutput out) throws Exception {
            final Point point = ScreenService.INSTANCE.getScreenSize();
            out.writeShort(point.x);
            out.writeShort(point.y);
        }
    };


}

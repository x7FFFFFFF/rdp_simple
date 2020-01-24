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
            final int length = input.readInt();
            final byte[] data = new byte[length];
            input.readFully(data);
            final ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
            final BufferedImage image = ImageIO.read(inputStream);
            ScreenShotQueue.put(image);
        }

        @Override
        public void send(DataOutput out) throws Exception {
         /*   final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCaptureFull();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(screenCapture, "JPEG", outputStream);
            final byte[] bytes = outputStream.toByteArray();
            out.writeInt(bytes.length);
            out.write(bytes);*/
            out.write(ScreenService.INSTANCE.getScreenCaptureFull());
        }

    },
    GET_SCREEN_SIZE{
        @Override
        public void handle(DataInput input) throws Exception {
            final int width = input.readUnsignedShort();
            final int height = input.readUnsignedShort();
            ScreenSizeQueue.put(new Point(width,height));
        }

        @Override
        public void send(DataOutput out) throws Exception {
            final Point point = ScreenService.INSTANCE.getScreenSize();
            out.writeShort(point.x);
            out.writeShort(point.y);
        }
    };


}

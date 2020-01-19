package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.model.PointSt;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public enum CommandRegistry implements CommandExecutor {


    CREATE_SCREEN_CAPTURE {
        @Override
        public Answer execute(Command command) throws IOException {
            final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCapture(
                    new Rectangle(command.getIntParam(0), command.getIntParam(1), command.getIntParam(2),
                            command.getIntParam(3)));
            return getAnswer(command, screenCapture);
        }
    },
    CREATE_SCREEN_CAPTURE_FULL {
        @Override
        public Answer execute(Command command) throws Exception {
            final BufferedImage screenCaptureFull = ScreenService.INSTANCE.createScreenCaptureFull();
            return getAnswer(command, screenCaptureFull);
        }
    },
    GET_MOUSE {
        @Override
        public Answer execute(Command command) throws Exception {
            final Point mouse = ScreenService.INSTANCE.getMouse();
            final PointSt pointSt = new PointSt(mouse);
            final ByteArrayOutputStream out = new ByteArrayOutputStream();  //TODO  - избыточно как то
            DataOutput dos = new DataOutputStream(out);
            pointSt.writeObject(dos);
            return new Answer(1, command.getName(), out.toByteArray());
        }

        @Override
        public Object decryptAnsver(byte[] data) throws Exception { //TODO  - избыточно как то
            final DataInputStream is = new DataInputStream(new ByteArrayInputStream(data));
            final PointSt pointSt = new PointSt();
            pointSt.readObject(is);
            return pointSt;
        }

    },
    MOUSE_MOVE {
        @Override
        public Answer execute(Command command) throws Exception {
            ScreenService.INSTANCE.mouseMove(new Point(command.getIntParam(0), command.getIntParam(1)));
            return GET_MOUSE.execute(command);
        }

        @Override
        public Object decryptAnsver(byte[] data) throws Exception {
            return GET_MOUSE.decryptAnsver(data);
        }
    }
    ;

    private static Answer getAnswer(Command command, BufferedImage screenCapture) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "JPEG", outputStream);
        return new Answer(1, command.getName(), outputStream.toByteArray());
    }


    public static Answer exec(Command command) throws Exception {
        final CommandExecutor registry = command.getName();
        return registry.execute(command);
    }


}

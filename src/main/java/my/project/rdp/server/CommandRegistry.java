package my.project.rdp.server;

import my.project.rdp.model.Answer;
import my.project.rdp.model.Command;
import my.project.rdp.services.ScreenService;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public enum CommandRegistry implements CommandExecutor {


    CREATE_SCREEN_CAPTURE {
        @Override
        public Answer execute(Command command) throws IOException {
            final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCapture(
                    new Rectangle(command.getIntParam(0), command.getIntParam(1), command.getIntParam(2),
                            command.getIntParam(3)));
            return getAnswer(screenCapture);
        }
    },
    CREATE_SCREEN_CAPTURE_FULL{
        @Override
        public Answer execute(Command command) throws Exception {
            final BufferedImage screenCaptureFull = ScreenService.INSTANCE.createScreenCaptureFull();
            return getAnswer(screenCaptureFull);
        }
    }

    ;

    private static Answer getAnswer(BufferedImage screenCapture) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(screenCapture, "JPEG", outputStream);
        return new Answer(1, outputStream.toByteArray());
    }


    public static Answer exec(Command command) throws Exception {
        final CommandExecutor registry = command.getName();
        return registry.execute(command);
    }


}

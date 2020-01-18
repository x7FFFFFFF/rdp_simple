package my.project.rdp.services;

import java.awt.*;
import java.awt.image.BufferedImage;

public enum  ScreenService {
    INSTANCE;
    private final Robot robot;

    ScreenService()  {
        try {
            robot = new Robot();
        } catch (AWTException e) {
           throw new RuntimeException(e);
        }
    }


    public synchronized BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }



}

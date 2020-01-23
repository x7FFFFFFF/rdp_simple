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

    public synchronized void  mouseWheel(int wheel) {
        robot.mouseWheel(wheel);
    }

    public synchronized void  mouseMove(Point point) {
        robot.mouseMove(point.x, point.y);
    }

    public synchronized void  mousePress(Point point, int buttons) {
        robot.mouseMove(point.x, point.y);
        robot.mousePress(buttons);
    }
    public synchronized void mouseRelease(Point point, int buttons) {
        robot.mouseMove(point.x, point.y);
        robot.mouseRelease(buttons);
    }

    public synchronized Point getMouse(){
       return MouseInfo.getPointerInfo().getLocation();
    }

    public synchronized BufferedImage createScreenCaptureFull() {
        //size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return robot.createScreenCapture(new Rectangle(0,0, (int)screenSize.getWidth(), (int)screenSize.getHeight()));
    }



    public synchronized BufferedImage createScreenCapture(Rectangle screenRect) {
        return robot.createScreenCapture(screenRect);
    }


    public synchronized void keyPressed(int keyCode) {
        robot.keyPress(keyCode);
    }

    public synchronized void keyRealeased(int keyCode) {
        robot.keyRelease(keyCode);
    }

    public Point getScreenSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        System.out.println("width = " + width);
        System.out.println("height = " + height);
        final Point point = new Point((int) width, (int) height);
        System.out.println("point = " + point);
        return point;
    }

}

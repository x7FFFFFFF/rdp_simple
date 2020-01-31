package my.project.rdp.services;

import sun.awt.image.SunWritableRaster;

import java.awt.*;
import java.awt.image.*;
import java.awt.peer.RobotPeer;

public enum ScreenService {
    INSTANCE;
    private final RobotPeer robot;
    private final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private final Rectangle bounds = new Rectangle(screenSize.width, screenSize.height);
    private final DirectColorModel screenCapCM = new DirectColorModel(24,
            /* red mask */    0x00FF0000,
            /* green mask */  0x0000FF00,
            /* blue mask */   0x000000FF);

    ScreenService() {
        robot = RobotPeerImpl.INSTANCE;
    }

    public void mouseWheel(int wheel) {
        robot.mouseWheel(wheel);
    }

    public void mouseMove(Point point) {
        robot.mouseMove(point.x, point.y);
    }

    public synchronized void mousePress(Point point, int buttons) {
        robot.mouseMove(point.x, point.y);
        robot.mousePress(buttons);
    }

    public synchronized void mouseRelease(Point point, int buttons) {
        robot.mouseMove(point.x, point.y);
        robot.mouseRelease(buttons);
    }

    public Point getMouse() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public int[] getScreenCaptureFull() {
        Toolkit.getDefaultToolkit().sync();
        return robot.getRGBPixels(bounds);
    }

    public BufferedImage getScreenCaptureFull(int[] pixels) {
        final DataBufferInt bufferInt = new DataBufferInt(pixels, pixels.length);
        final int[] bandmasks = new int[3];
        bandmasks[0] = screenCapCM.getRedMask();
        bandmasks[1] = screenCapCM.getGreenMask();
        bandmasks[2] = screenCapCM.getBlueMask();
        final WritableRaster raster = Raster.createPackedRaster(bufferInt, screenSize.width, screenSize.height, screenSize.width, bandmasks, null);
        SunWritableRaster.makeTrackable(bufferInt);
       return  new BufferedImage(screenCapCM, raster, false, null);
    }




    public void keyPressed(int keyCode) {
        robot.keyPress(keyCode);
    }

    public void keyRealeased(int keyCode) {
        robot.keyRelease(keyCode);
    }

    public Point getScreenSize() {
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

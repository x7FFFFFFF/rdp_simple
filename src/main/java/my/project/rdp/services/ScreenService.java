package my.project.rdp.services;

import my.project.rdp.other.DefaultThreadFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.*;

public enum ScreenService {
    INSTANCE;
    private final Robot robot;
    private final ScheduledExecutorService executorService = Executors
        .newScheduledThreadPool(2, new DefaultThreadFactory("ScreenService"));
    private final BlockingQueue<byte[]> screnShotsQueue = new ArrayBlockingQueue<>(100);

    ScreenService() {
        try {
            robot = new Robot();
            executorService.scheduleWithFixedDelay(new ScreenShotRunnable(), 0, 100, TimeUnit.MILLISECONDS);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    private class ScreenShotRunnable implements Runnable {
        @Override
        public void run() {
            final BufferedImage captureFull = createScreenCaptureFull();
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(captureFull, "JPEG", outputStream);
                final byte[] bytes = outputStream.toByteArray();
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final int length = bytes.length;
                writeInt(out, length);
                out.write(bytes);
                screnShotsQueue.put(out.toByteArray());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void writeInt(ByteArrayOutputStream out, int length) {
            out.write((length >>> 24) & 0xFF);
            out.write((length >>> 16) & 0xFF);
            out.write((length >>>  8) & 0xFF);
            out.write((length >>>  0) & 0xFF);
        }
    }

    public synchronized void mouseWheel(int wheel) {
        robot.mouseWheel(wheel);
    }

    public synchronized void mouseMove(Point point) {
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

    public synchronized Point getMouse() {
        return MouseInfo.getPointerInfo().getLocation();
    }

    public byte[] getScreenCaptureFull() throws InterruptedException {// synchronized ?
        return screnShotsQueue.take();
    }

    public synchronized BufferedImage createScreenCaptureFull() {
        //size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return robot
            .createScreenCapture(new Rectangle(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight()));
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

package my.project.rdp.client;

import my.project.rdp.other.Utils;
import my.project.rdp.services.ScreenService;

import java.awt.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ScreenSizeQueue {
    private static final BlockingQueue<Point> SIZES = new ArrayBlockingQueue<>(10); //TODO immutable point

    public static void put(Point image) {
        Utils.rethrowVoid(() -> SIZES.put(image));
    }

    private static Point take() {
        return Utils.rethrow(SIZES::take);
    }

    public static Size getScreenSize() throws Exception {
        final Point screenSizeRemote = take();
        System.out.println("remote screen w,h = " + screenSizeRemote);
        final Point screenSizeLocal = ScreenService.INSTANCE.getScreenSize();
        final int localX = screenSizeLocal.x;
        final int remoteX = screenSizeRemote.x;
        final int localY = screenSizeLocal.y;
        final int remoteY = screenSizeRemote.y;
        if (localX >= remoteX && localY >= remoteY) {
            return new Size(1, 1, remoteX, remoteY);
        }
        final int newWidth = Math.min(localX, remoteX);
        final int newHeight = Math.min(localY, remoteY);
        return new Size(1. * remoteX / newWidth, 1. * remoteY / newHeight, newWidth, newHeight);
    }

    public static class Size {
        public final double kX;
        public final double kY;
        public final int newWidth;
        public final int newHeight;

        public Size(double kX, double kY, int newWidth, int newHeight) {
            this.kX = kX;
            this.kY = kY;
            this.newWidth = newWidth;
            this.newHeight = newHeight;
        }
    }
}

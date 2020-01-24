package my.project.rdp.client;

import my.project.rdp.other.Utils;

import java.awt.image.BufferedImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ScreenShotQueue {
    private static final BlockingQueue<BufferedImage> IMAGES = new ArrayBlockingQueue<>(100);

    public static void put(BufferedImage image) {
        Utils.rethrowVoid(() -> IMAGES.put(image));
    }

    public static BufferedImage take() {
        return Utils.rethrow(IMAGES::take);
    }

}

package my.project.rdp.services;

import my.project.rdp.other.Utils;
import sun.awt.ComponentFactory;

import java.awt.*;
import java.awt.peer.RobotPeer;

public enum RobotPeerImpl implements RobotPeer {
    INSTANCE;
    private final RobotPeer peer;

    RobotPeerImpl() {
        peer = init();
    }

    public static RobotPeer init() {
        final GraphicsDevice screenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        if (toolkit instanceof ComponentFactory) {
            return Utils.rethrow(() -> ((ComponentFactory) toolkit).createRobot(null, screenDevice));
        }
        throw new IllegalStateException();
    }

    @Override
    public void mouseMove(int x, int y) {
        peer.mouseMove(x, y);
    }

    @Override
    public void mousePress(int buttons) {
        peer.mousePress(buttons);
    }

    @Override
    public void mouseRelease(int buttons) {
        peer.mouseRelease(buttons);
    }

    @Override
    public void mouseWheel(int wheelAmt) {
        peer.mouseWheel(wheelAmt);
    }

    @Override
    public void keyPress(int keycode) {
        peer.keyPress(keycode);
    }

    @Override
    public void keyRelease(int keycode) {
        peer.keyRelease(keycode);
    }

    @Override
    public int getRGBPixel(int x, int y) {
        return peer.getRGBPixel(x, y);
    }

    @Override
    public int[] getRGBPixels(Rectangle bounds) {
        return peer.getRGBPixels(bounds);
    }

    @Override
    public void dispose() {
        peer.dispose(); //TODO
    }
}

package my.project.rdp;

import my.project.rdp.services.ScreenService;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScreenServiceTest {

    private Row[] rows1 = new  Row[1000];
    private Row[] rows2 = new  Row[1000];

    @Test
    public void testCreateScreenCapture() throws IOException {
        final BufferedImage screenCapture = ScreenService.INSTANCE.createScreenCapture(new Rectangle(1000, 1000));
        final BufferedImage image = new BufferedImage(screenCapture.getWidth(), screenCapture.getHeight(),
                                                      screenCapture.getType());
        //int[] rgb = new int[screenCapture.getHeight()*screenCapture.getWidth()];
        //screenCapture.getRGB(0, 0, screenCapture.getWidth(), screenCapture.getHeight(), rgb, 0, 1);
        //image.setRGB(0,0, screenCapture.getWidth(), screenCapture.getHeight(), rgb, 0,1);
        //screenCapture.copyData(image.getRaster());
        final int[][] rastr =copyData(screenCapture.getRaster(), image.getRaster());//getRastr(screenCapture.getRaster());
        setRastr(rastr, image.getRaster());
        // final int[][] ints = copyData(screenCapture.getRaster(), image.getRaster());
        // final int[][] rastr = getRastr(screenCapture.getRaster());
/*        for (int i = 0; i < ints.length; i++) {
            int[] ints1 = ints[i];
            int[] ints2 = rastr[i];
            if (! Arrays.equals(ints1, ints2)){
                throw new IllegalArgumentException();
            }
            System.out.println("Arrays.equals(ints1, ints2) = " + Arrays.equals(ints1, ints2));

        }*/
        ImageIO.write(image, "JPEG", new File("/tmp/screen555.jpg"));

    }

    public int[][] getRastr(WritableRaster raster) {
        int width = raster.getWidth();
        int height = raster.getHeight();
        int startX = raster.getMinX();
        int startY = raster.getMinY();
        final int[][] res = new int[height][width];
        Object tdata = null;
        for (int i = startY; i < startY + height; i++) {
            tdata = raster.getDataElements(startX, i, width, 1, tdata);
            res[i] = (int[]) tdata;
        }
        return res;
    }

    public void setRastr(int[][] rastr, WritableRaster raster) {
        final int startX = 0;
        final int width = rastr[0].length;
        final int height = rastr.length;
        for (int i = 0; i < height; i++) {
            raster.setDataElements(0, i, width, 1, rastr[i]);
        }
    }

    public int[][] copyData(WritableRaster from, WritableRaster to) {
        int width = to.getWidth();
        int height = to.getHeight();
        int startX = to.getMinX();
        int startY = to.getMinY();
        Object tdata = null;
        final int[][] res = new int[height][width];
        for (int i = startY; i < startY + height; i++) {
            tdata = from.getDataElements(startX, i, width, 1, tdata);
            res[i] = (int[]) tdata;
            //to.setDataElements(startX, i, width, 1, tdata);
        }
        return res;

    }

    private static class Row {
        int x,  y,  w,  h;
        int[] inData;

        public Row(int x, int y, int w, int h, Object inData) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.inData = (int[]) inData;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Row row = (Row) o;
            if (x != row.x)
                return false;
            if (y != row.y)
                return false;
            if (w != row.w)
                return false;
            if (h != row.h)
                return false;
            return Arrays.equals(inData, row.inData);
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + w;
            result = 31 * result + h;
            result = 31 * result + Arrays.hashCode(inData);
            return result;
        }
    }
}
package my.project.rdp;

import my.project.rdp.services.ScreenService;
import org.junit.Ignore;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ScreenServiceTest {

    private Row[] rows1 = new  Row[1000];
    private Row[] rows2 = new  Row[1000];


    private static class Timer {
        final long start;
        private final String name;

        public Timer(String name) {
            this.name = name;
            this.start =  System.currentTimeMillis();
        }

        public  void stop(){
            System.out.println(name+" " + (System.currentTimeMillis()-start));
        }
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
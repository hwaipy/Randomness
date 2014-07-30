package D20140729.OAMDrawer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class FunctionDrawer {

    public FunctionDrawer() {
    }

    public void draw(Function function, int width, int height, double xStart, double xEnd, double yStart, double yEnd, File file) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double[][] values = new double[width][height];
        double maxValue = Double.MIN_VALUE;
        double minValue = Double.MAX_VALUE;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double xValue = x * (xEnd - xStart) / (width) + xStart;
                double yValue = (height - y) * (yEnd - yStart) / (height) + yStart;
                double value = function.valueOf(xValue, yValue).getReal();
                values[x][y] = value;
                if (value > maxValue) {
                    maxValue = value;
                }
                if (value < minValue) {
                    minValue = value;
                }
            }
        }
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double value = values[x][y];
                double normal = (value - minValue) / (maxValue - minValue);
                int gray = (int) ((normal) * 256);
                if (gray > 255) {
                    gray = 255;
                }
                if (gray < 0) {
                    gray = 0;
                }
                image.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }
        ImageIO.write(image, "png", file);
    }
}

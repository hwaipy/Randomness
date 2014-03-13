package D20140313.SLMGraphicGenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class LGModeGenerator {

    public static void main(String[] args) throws IOException {
        for (int i = 10; i < 11; i++) {
            draw(i);
        }
    }

    public static void draw(int index) throws IOException {
        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_BYTE_GRAY);
        double centerX = image.getWidth() / 5 + 0.5;
        double centerY = image.getHeight() / 3 + 0.5;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int yy = image.getHeight() - y;
                double theta = (theta(x - centerX, y - centerY));
                float gray = (float) gray(theta, index);
//                System.out.println(gray);
                image.setRGB(x, y, new Color(gray, gray, gray).getRGB());
//                System.out.println();
            }
        }
        ImageIO.write(image, "BMP", new File(index + ".bmp"));
    }

    private static double theta(double x, double y) {
//        System.out.print(x + "\t" + y + "\t");
        double tan = y / x;
        double theta = Math.atan(tan);
//        System.out.print(theta + "\t");
        double result = 0;
        if (x > 0 && y > 0) {
            result = theta;
        } else if (x > 0 && y < 0) {
            result = theta + Math.PI * 2;
        } else if (x < 0 && y > 0) {
            result = theta + Math.PI;
        } else if (x < 0 && y < 0) {
            result = theta + Math.PI;
        }
//        System.out.print(result + "\t");
        return result;
    }

    private static double gray(double theta, int index) {
        double value = theta / 2 / Math.PI * index;
        int v = (int) value;
        return value - v;
    }
}

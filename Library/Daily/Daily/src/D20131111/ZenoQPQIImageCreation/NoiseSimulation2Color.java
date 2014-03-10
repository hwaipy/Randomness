package D20131111.ZenoQPQIImageCreation;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class NoiseSimulation2Color {

//    private static final double RANDOM_SHRESHOLED_0 = 0.912;
//    private static final double RANDOM_SHRESHOLED_1 = 0.834;
    private static final double RANDOM_SHRESHOLED_0 = 0.834;
    private static final double RANDOM_SHRESHOLED_1 = 0.912;

    public static void main(String[] args) throws IOException {
        Random random = new Random();
        BufferedImage originalImage = ImageIO.read(new File("QPQI-100-2Color-original.png"));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = originalImage.getRGB(x, y);
                if (rgb == Color.WHITE.getRGB()) {
                    double r = random.nextDouble();
                    if (r > RANDOM_SHRESHOLED_1) {
                        originalImage.setRGB(x, y, Color.BLACK.getRGB());
                    }
                } else if (rgb == Color.BLACK.getRGB()) {
                    double r = random.nextDouble();
                    if (r > RANDOM_SHRESHOLED_0) {
                        originalImage.setRGB(x, y, Color.WHITE.getRGB());
                    }
                }
            }
        }
        ImageIO.write(originalImage, "png", new File("output.png"));
    }
}

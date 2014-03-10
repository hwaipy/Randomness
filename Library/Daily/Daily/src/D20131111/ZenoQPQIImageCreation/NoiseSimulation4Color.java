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
public class NoiseSimulation4Color {

    private static final Random random = new Random();
    private static final double BITFLIP_SHRESHOLED_0 = 0.912;
    private static final double BITFLIP_SHRESHOLED_1 = 0.834;
    private static final int RGB_LEFT = new Color(5, 5, 5).getRGB();//01=1
    private static final int RGB_RIGHT = new Color(120, 120, 120).getRGB();//00=0
    private static final int RGB_RIVER = new Color(240, 240, 240).getRGB();//11=3
    private static final int RGB_RING = new Color(200, 200, 200).getRGB();//10=2

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File("QPQI_100-4Color-original.png"));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = originalImage.getRGB(x, y);
                int colorIndex;
                if (rgb == RGB_RIGHT) {
                    colorIndex = 0;
                } else if (rgb == RGB_LEFT) {
                    colorIndex = 1;
                } else if (rgb == RGB_RING) {
                    colorIndex = 2;
                } else if (rgb == RGB_RIVER) {
                    colorIndex = 3;
                } else {
                    throw new RuntimeException();
                }
                int bitHigh = noise(colorIndex / 2);
                int bitLow = noise(colorIndex % 2);
                int newColorIndex = bitHigh * 2 + bitLow;
                int newRgb;
                switch (newColorIndex) {
                    case 0:
                        newRgb = RGB_RIGHT;
                        break;
                    case 1:
                        newRgb = RGB_LEFT;
                        break;
                    case 2:
                        newRgb = RGB_RING;
                        break;
                    case 3:
                        newRgb = RGB_RIVER;
                        break;
                    default:
                        throw new RuntimeException();
                }
                originalImage.setRGB(x, y, newRgb);
            }
        }
        ImageIO.write(originalImage, "png", new File("output.png"));
    }

    private static int noise(int input) {
        double r = random.nextDouble();
        if (input == 0) {
            if (r > BITFLIP_SHRESHOLED_0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            if (r > BITFLIP_SHRESHOLED_1) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}

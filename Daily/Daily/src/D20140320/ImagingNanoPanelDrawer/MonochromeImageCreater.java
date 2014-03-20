package D20140320.ImagingNanoPanelDrawer;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class MonochromeImageCreater {

    public static void main(String[] args) throws IOException {
        File src = new File("/Users/Hwaipy/Desktop/JS Bell.png");
        BufferedImage srcImage = ImageIO.read(src);
        BufferedImage[] results = create(srcImage);
        for (int i = 0; i < results.length; i++) {
            ImageIO.write(results[i], "png", new File("MonochromeImage" + i + ".png"));
        }
    }
    private static final Color COLOR_TRANS_COLOR = new Color(0, 0, 0, 0);
    private static final Color COLOR_COLORED_COLOR = new Color(0, 0, 0, 255);
    private static final int threshold = 150;

    private static BufferedImage[] create(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage result1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        BufferedImage result2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int srcRGB = src.getRGB(x, y);
                Color srcColor = new Color(srcRGB);
                int gray = (srcColor.getRed() + srcColor.getGreen() + srcColor.getBlue()) / 3;
                Color color1 = gray > threshold ? COLOR_COLORED_COLOR : COLOR_TRANS_COLOR;
                Color color2 = gray <= threshold ? COLOR_COLORED_COLOR : COLOR_TRANS_COLOR;
                result1.setRGB(x, y, color1.getRGB());
                result2.setRGB(x, y, color2.getRGB());
            }
        }
        return new BufferedImage[]{result1, result2};
    }
}

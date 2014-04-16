package D20140412.BeamCenterCalculate;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import sun.awt.AWTIcon32_java_icon16_png;

/**
 *
 * @author Hwaipy
 */
public class BeamCenteror {

    private final BufferedImage image;

    public BeamCenteror(BufferedImage image) {
        this.image = grayedImage(image);
    }

    public BufferedImage getGrayedImage() {
        return image;
    }

    BeamCenterResult center(Rectangle area, boolean bright) {
        BufferedImage dest = coloredImage(image);
        Graphics2D g2 = dest.createGraphics();
        
        //
        //
        //
        //
        g2.setColor(Color.BLUE);
        g2.drawRect(area.x, area.y, area.width, area.height);
        return new BeamCenterResult(dest, -1, -1);
    }

    private BufferedImage grayedImage(BufferedImage sourceImage) {
        BufferedImage destImage = new BufferedImage(
                sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < destImage.getWidth(); x++) {
            for (int y = 0; y < destImage.getHeight(); y++) {
                int rgb = sourceImage.getRGB(x, y);
                Color color = new Color(rgb);
                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color grayColor = new Color(gray, gray, gray);
                destImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return destImage;
    }

    private BufferedImage coloredImage(BufferedImage sourceImage) {
        BufferedImage destImage = new BufferedImage(
                sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < destImage.getWidth(); x++) {
            for (int y = 0; y < destImage.getHeight(); y++) {
                int rgb = sourceImage.getRGB(x, y);
                Color color = new Color(rgb);
                int gray = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color grayColor = new Color(gray, gray, gray);
                destImage.setRGB(x, y, grayColor.getRGB());
            }
        }
        return destImage;
    }

    class BeamCenterResult {

        private final BufferedImage image;
        private final double x;
        private final double y;

        public BeamCenterResult(BufferedImage image, double x, double y) {
            this.image = image;
            this.x = x;
            this.y = y;
        }

        public BufferedImage getImage() {
            return image;
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

    }
}

package D20140822.ZaiDuZhengMing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class ZDZM {

    public static void main(String[] args) throws IOException {
        File fileOriginal = new File("/Users/Hwaipy/Documents/Dropbox/Projects/2014-04 CLEO/台湾签证/扫描件/在读证明.png");
        BufferedImage imageOriginal = ImageIO.read(fileOriginal);
        File file8 = new File("/Users/Hwaipy/Documents/Dropbox/Projects/2014-04 CLEO/台湾签证/扫描件/8.png");
        BufferedImage image8 = ImageIO.read(file8);
        File fileZhang = new File("/Users/Hwaipy/Documents/Dropbox/Projects/2014-04 CLEO/台湾签证/扫描件/zhang.png");
        BufferedImage imageZhang = ImageIO.read(fileZhang);
        File file8en = new File("/Users/Hwaipy/Documents/Dropbox/Projects/2014-04 CLEO/台湾签证/扫描件/8en.png");
        BufferedImage image8en = ImageIO.read(file8en);
        File fileE = new File("/Users/Hwaipy/Documents/Dropbox/Projects/2014-04 CLEO/台湾签证/扫描件/在读证明E1.png");

        int zhangOffsetX = 1987;
        int zhangOffsetY = 1575;

        Graphics2D g2 = imageOriginal.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(zhangOffsetX, zhangOffsetY, imageZhang.getWidth(), imageZhang.getHeight());
        g2.drawImage(image8, 1968, 1548, null);
        g2.drawImage(image8en, 1850, 2981, null);
//        g2.drawImage(imageZhang, 1987, 1575, null);

        for (int x = 0; x < imageZhang.getWidth(); x++) {
            for (int y = 0; y < imageZhang.getHeight(); y++) {
                int rgbOriginal = imageOriginal.getRGB(x + zhangOffsetX, y + zhangOffsetY);
                int rgbZhang = imageZhang.getRGB(x, y);
                Color colorOriginal = new Color(rgbOriginal);
                Color colorZhang = new Color(rgbZhang);
                int grayOriginal = colorOriginal.getRed() + colorOriginal.getGreen() + colorOriginal.getBlue();
                int grayZhang = colorZhang.getRed() + colorZhang.getGreen() + colorZhang.getBlue();
                Color colorNew;
//                if (colorOriginal.getRed() < colorZhang.getRed()) {
//                    colorNew = colorOriginal;
//                } else {
//                    colorNew = colorZhang;
//                }
                int newR = mergeColor(colorOriginal.getRed(), colorZhang.getRed());
                int newG = mergeColor(colorOriginal.getGreen(), colorZhang.getGreen());
                int newB = mergeColor(colorOriginal.getBlue(), colorZhang.getBlue());
                if (newR < 0) {
                    newR = 0;
                }
                if (newG < 0) {
                    newG = 0;
                }
                if (newB < 0) {
                    newB = 0;
                }
                colorNew = new Color(newR, newG, newB);
                imageOriginal.setRGB(x + zhangOffsetX, y + zhangOffsetY, colorNew.getRGB());
            }
        }

        ImageIO.write(imageOriginal, "png", fileE);
    }

    private static int mergeColor(int a, int b) {
        int whiteA = 255 - a;
        int whiteB = 255 - b;
        int white = (int) Math.sqrt(whiteA * whiteA + whiteB * whiteB);
        return 255 - white;
    }
}

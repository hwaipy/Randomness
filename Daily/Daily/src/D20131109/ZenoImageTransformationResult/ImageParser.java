package D20131109.ZenoImageTransformationResult;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class ImageParser {

    public static void main(String[] args) throws IOException {
        BufferedImage original = ImageIO.read(new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-07-14 Zeno实验传输图片/original.png"));
        BufferedImage result = ImageIO.read(new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-07-14 Zeno实验传输图片/result.png"));
        int countOfWhite = 0;
        int countOfWhiteCorrect = 0;
        int countOfRed = 0;
        int countOfRedCorrect = 0;
        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                boolean originalIsWhite = original.getRGB(x, y) == Color.WHITE.getRGB();
                boolean resultIsWhite = result.getRGB(x, y) == Color.WHITE.getRGB();
                if (originalIsWhite) {
                    countOfWhite++;
                    if (resultIsWhite) {
                        countOfWhiteCorrect++;
                    }
                } else {
                    countOfRed++;
                    if (!resultIsWhite) {
                        countOfRedCorrect++;
                    }
                }
            }
        }
        System.out.println("White " + countOfWhite + ", Correct " + countOfWhiteCorrect + ", " + ((double) countOfWhiteCorrect / countOfWhite));
        System.out.println("Red   " + countOfRed + ", Correct " + countOfRedCorrect + ", " + ((double) countOfRedCorrect / countOfRed));
    }
}

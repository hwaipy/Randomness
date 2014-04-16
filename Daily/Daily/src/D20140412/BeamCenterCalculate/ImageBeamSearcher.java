package D20140412.BeamCenterCalculate;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class ImageBeamSearcher {

    public static void main(String[] args) throws IOException {
        File directory = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2014-04-12 极化检测光LD组件振动试验/");
        BufferedImage image = ImageIO.read(new File(directory, "振动前.jpg"));
        BeamCenteror beamCenteror = new BeamCenteror(image);
        ImageIO.write(beamCenteror.getGrayedImage(), "png", new File(directory, "Before_Grayed.png"));
        BeamCenteror.BeamCenterResult centerResultMark1 = beamCenteror.center(new Rectangle(1655, 1658, 18, 18), true);
        ImageIO.write(centerResultMark1.getImage(), "png", new File(directory, "Before_Marker1.png"));
    }
}

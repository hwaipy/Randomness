package D20140320.ImagingNanoPanelDrawer;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class CharacterDrawer {

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(2000, 1000, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = image.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2.setColor(Color.BLACK);
        Font font = new java.awt.Font("Arial", 0, 500);
        g2.setFont(font);
        g2.drawString("QPQI", 0, 500);
        ImageIO.write(image, "png", new File("qpqi.png"));
    }
}

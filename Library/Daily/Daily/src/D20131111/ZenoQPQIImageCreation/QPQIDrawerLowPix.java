package D20131111.ZenoQPQIImageCreation;

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
public class QPQIDrawerLowPix {

    private static final int IMAGE_SIZE = 100;
    private static final double OUTER_ELLI_LONG_AXIS = 0.85;
    private static final double OUTER_ELLI_SHORT_AXIS = 0.5;
    private static final double INNER_ELLI_LONG_AXIS = 0.70;
    private static final double INNER_ELLI_SHORT_AXIS = 0.42;
    private static final double ELLI_OFFSET = 0.02;
    private static final double RIVER_SIZE = 0.08;
    private static final double RIVER_INSET = 0.06;

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        drawQPQI(g);

        g.dispose();
        ImageIO.write(image, "png", new File("QPQI.png"));
    }

    private static void drawQPQI(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);

        g.setColor(Color.WHITE);
        g.fillOval((int) (IMAGE_SIZE * ((1 - OUTER_ELLI_LONG_AXIS) / 2 - ELLI_OFFSET)),
                (int) (IMAGE_SIZE * (1 - OUTER_ELLI_SHORT_AXIS) / 2),
                (int) (IMAGE_SIZE * OUTER_ELLI_LONG_AXIS),
                (int) (IMAGE_SIZE * OUTER_ELLI_SHORT_AXIS));
        g.setColor(Color.BLACK);
        g.fillOval((int) (IMAGE_SIZE * ((1 - INNER_ELLI_LONG_AXIS) / 2 + ELLI_OFFSET)),
                (int) (IMAGE_SIZE * (1 - INNER_ELLI_SHORT_AXIS) / 2),
                (int) (IMAGE_SIZE * INNER_ELLI_LONG_AXIS),
                (int) (IMAGE_SIZE * INNER_ELLI_SHORT_AXIS));

        g.setColor(Color.WHITE);
        for (int p = -IMAGE_SIZE; p < 2 * IMAGE_SIZE; p++) {
            int x = (int) (riverFunction2(((double) p) / IMAGE_SIZE) * IMAGE_SIZE);
            int y = (int) ((((double) p / IMAGE_SIZE) * (1 - RIVER_INSET) + RIVER_INSET / 2) * IMAGE_SIZE);
            System.out.println(y + "\t" + x);
            g.fillOval(x - (int) (IMAGE_SIZE * RIVER_SIZE / 2),
                    y - (int) (IMAGE_SIZE * RIVER_SIZE / 2),
                    (int) (IMAGE_SIZE * RIVER_SIZE),
                    (int) (IMAGE_SIZE * RIVER_SIZE));
        }
    }

    private static double riverFunctionTan(double x) {
        double RIVER_TAN_RUNGE = Math.PI / 2 - 0.1;
        return -Math.tan((x - 0.5) * 2 * RIVER_TAN_RUNGE) / Math.tan(RIVER_TAN_RUNGE) / 2 + 0.5;
    }

    private static double riverFunction2(double x) {
        double RIVER_TAN_RUNGE = 50;
        return -Math.pow((x - 0.5) * 2 * RIVER_TAN_RUNGE, 5) / Math.pow(RIVER_TAN_RUNGE, 5) / 2 + 0.5;
    }
}

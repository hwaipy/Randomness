package D20140320.ImagingNanoPanelDrawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class QPQIDrawer {

    private static final int IMAGE_SIZE = 1000;
    private static final double OUTER_ELLI_LONG_AXIS = 0.88;
    private static final double OUTER_ELLI_SHORT_AXIS = 0.5;
    private static final double INNER_ELLI_LONG_AXIS = 0.73;
    private static final double INNER_ELLI_SHORT_AXIS = 0.42;
    private static final double ELLI_OFFSET = 0.02;
    private static final double ELLI_OVERALL_OFFSET = 0.017;
    private static final double RIVER_SIZE = 0.08;
    private static final double RIVER_INSET = 0.06;
    private static final double RIGHT_BALL_X = 0.64;
    private static final double RIGHT_BALL_Y = 0.25;
    private static final double RIGHT_BALL_R = 0.08;
    private static final double LEFT_BALL_X = 0.16;
    private static final double LEFT_BALL_Y = 0.60;
    private static final double LEFT_BALL_R = 0.12;

    private static final Color COLOR_TRANS_COLOR = new Color(0, 0, 0, 0);
    private static final Color COLOR_COLORED_COLOR = new Color(0, 0, 0, 255);
    private static final Color COLOR_LEFT = COLOR_TRANS_COLOR;
    private static final Color COLOR_RIGHT = COLOR_TRANS_COLOR;
    private static final Color COLOR_RIVER = COLOR_COLORED_COLOR;
    private static final Color COLOR_RING = COLOR_COLORED_COLOR;
    private static final Color COLOR_BALL = COLOR_COLORED_COLOR;

    public static void main(String[] args) throws IOException {
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        drawQPQI(g);

        g.dispose();
        ImageIO.write(image, "png", new File("QPQI_" + IMAGE_SIZE + ".png"));
    }

    private static void drawQPQI(Graphics2D g) {
        g.setColor(COLOR_LEFT);
        g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        g.setColor(COLOR_RIGHT);
        for (int p = 0; p < IMAGE_SIZE; p++) {
            int x = (int) (riverFunction(((double) p) / IMAGE_SIZE) * IMAGE_SIZE);
            int y = (int) ((((double) p / IMAGE_SIZE) * (1 - RIVER_INSET) + RIVER_INSET / 2) * IMAGE_SIZE);
            g.fillRect(x, y, IMAGE_SIZE - x, 1);
        }
        g.fillRect(0, (int) (IMAGE_SIZE * (1 - RIVER_INSET / 2)), IMAGE_SIZE, IMAGE_SIZE);

        g.setColor(COLOR_RIVER);
        for (int p = 0; p < IMAGE_SIZE; p++) {
            int x = (int) (riverFunction(((double) p) / IMAGE_SIZE) * IMAGE_SIZE);
            int y = (int) ((((double) p / IMAGE_SIZE) * (1 - RIVER_INSET) + RIVER_INSET / 2) * IMAGE_SIZE);
            System.out.println(y + "\t" + x);
            g.fillOval(x - (int) (IMAGE_SIZE * RIVER_SIZE / 2),
                    y - (int) (IMAGE_SIZE * RIVER_SIZE / 2),
                    (int) (IMAGE_SIZE * RIVER_SIZE),
                    (int) (IMAGE_SIZE * RIVER_SIZE));
        }

        g.setColor(COLOR_RING);
        Area ring = new Area();
        Ellipse2D outer = new Ellipse2D.Double(
                IMAGE_SIZE * ((1 - OUTER_ELLI_LONG_AXIS) / 2 - ELLI_OFFSET + ELLI_OVERALL_OFFSET),
                IMAGE_SIZE * (1 - OUTER_ELLI_SHORT_AXIS) / 2,
                IMAGE_SIZE * OUTER_ELLI_LONG_AXIS,
                IMAGE_SIZE * OUTER_ELLI_SHORT_AXIS);
        Ellipse2D inner = new Ellipse2D.Double(
                IMAGE_SIZE * ((1 - INNER_ELLI_LONG_AXIS) / 2 + ELLI_OFFSET + ELLI_OVERALL_OFFSET),
                IMAGE_SIZE * (1 - INNER_ELLI_SHORT_AXIS) / 2,
                IMAGE_SIZE * INNER_ELLI_LONG_AXIS,
                IMAGE_SIZE * INNER_ELLI_SHORT_AXIS);
        ring.add(new Area(outer));
        ring.subtract(new Area(inner));
        g.fill(ring);

        g.setColor(COLOR_BALL);
        g.fillOval((int) (IMAGE_SIZE * RIGHT_BALL_X), (int) (IMAGE_SIZE * RIGHT_BALL_Y),
                (int) (IMAGE_SIZE * RIGHT_BALL_R), (int) (IMAGE_SIZE * RIGHT_BALL_R));
        g.setColor(COLOR_BALL);
        g.fillOval((int) (IMAGE_SIZE * LEFT_BALL_X), (int) (IMAGE_SIZE * LEFT_BALL_Y),
                (int) (IMAGE_SIZE * LEFT_BALL_R), (int) (IMAGE_SIZE * LEFT_BALL_R));
    }

    private static double riverFunctionTan(double x) {
        double RIVER_TAN_RUNGE = Math.PI / 2 - 0.1;
        return -Math.tan((x - 0.5) * 2 * RIVER_TAN_RUNGE) / Math.tan(RIVER_TAN_RUNGE) / 2 + 0.5;
    }

    private static double riverFunction(double x) {
        double RIVER_TAN_RUNGE = 50;
        return -Math.pow((x - 0.5) * 2 * RIVER_TAN_RUNGE, 5) / Math.pow(RIVER_TAN_RUNGE, 5) / 2 + 0.5;
    }
}

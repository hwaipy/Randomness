package com.hwaipy.prompt.viewer;

import com.sun.javafx.font.CompositeStrike;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 *
 * @author Hwaipy
 */
public class TimerDrawer {

    private static final int MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255, 0);
    private static final BufferedImage renderer = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private static final int TOP_POSITION = 30;
    private final Font font;
    private final Color color;
    private final String timeString;

    public TimerDrawer(Font font, Color color, String timeString) {
        this.font = font;
        this.color = color;
        this.timeString = timeString;
        init();
    }

    public BufferedImage getImage() {
        return renderer;
    }

    private void init() {
        Graphics2D g2d = renderer.createGraphics();
        Composite oldComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.Clear);
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        g2d.setComposite(oldComposite);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        FontMetrics fontMetrics = g2d.getFontMetrics(font);
        Rectangle bounds = fontMetrics.getStringBounds(timeString, g2d).getBounds();
        int x = (MAX_WIDTH - bounds.width) / 2;
        int y = TOP_POSITION;
        g2d.setFont(font);
        g2d.setColor(color);
        g2d.drawString(timeString, x, y + fontMetrics.getAscent());
        g2d.dispose();
    }

    public static BufferedImage draw(Font font, Color color, String timeString) {
        TimerDrawer drawer = new TimerDrawer(font, color, timeString);
        return drawer.getImage();
    }
}

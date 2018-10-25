package com.hwaipy.prompt.viewer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Hwaipy
 */
public class ParagraphDrawer {

    private static final int MAX_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int MAX_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final BufferedImage renderer = new BufferedImage(MAX_WIDTH, MAX_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    private final Font font;
    private final Color color;
    private final String paragraph;
    private final int width;
    private final ArrayList<BufferedImage> imageList = new ArrayList<>();
    private final ArrayList<Integer> wordsCountList = new ArrayList<>();

    public ParagraphDrawer(Font font, Color color, String paragraph, int width) {
        this.font = font;
        this.color = color;
        this.paragraph = paragraph;
        this.width = width;
        init();
    }

    public ArrayList<BufferedImage> getImages() {
        return imageList;
    }

    private void init() {
        Graphics2D g2d = renderer.createGraphics();
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        String[] words = paragraph.split(" ");
        int x = 0;
        int y = 0;
        FontMetrics fontMetrics = g2d.getFontMetrics(font);
        int lineHeight = fontMetrics.getHeight();
        int ascent = fontMetrics.getAscent();
        int descent = fontMetrics.getDescent();
        Rectangle blankBounds = fontMetrics.getStringBounds(" ", g2d).getBounds();
        g2d.setFont(font);
        g2d.setColor(color);
        int painted = 0;
        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            Rectangle wordBounds = fontMetrics.getStringBounds(word, g2d).getBounds();
            if (x + wordBounds.width > width) {
                BufferedImage image = new BufferedImage(width, lineHeight, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2dLine = image.createGraphics();
                g2dLine.drawImage(renderer, 0, 0, null);
                g2dLine.dispose();
                imageList.add(image);
                wordsCountList.add(painted);
                painted = 0;
                y = 0;
                x = 0;
                i--;
                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRect(0, 0, MAX_WIDTH, MAX_HEIGHT);
                g2d.setColor(color);
            } else {
                g2d.drawString(word, x, y + ascent);
                x += wordBounds.width;
                x += blankBounds.width;
                painted++;
            }
        }
        g2d.dispose();
        BufferedImage image = new BufferedImage(width, lineHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2dLine = image.createGraphics();
        g2dLine.drawImage(renderer, 0, 0, null);
        g2dLine.dispose();
        imageList.add(image);
        wordsCountList.add(painted);
    }

    public static Collection<BufferedImage> draw(Font font, Color color, String paragraph, int width) {
        ParagraphDrawer sentenceDrawer = new ParagraphDrawer(font, color, paragraph, width);
        return sentenceDrawer.getImages();
    }

    public static BufferedImage getSlideGappingImage(int width) {
        int height = 5;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        LinearGradientPaint paint = new LinearGradientPaint(0, 0, width, 0,
                new float[]{0f, 0.5f, 1f},
                new Color[]{Color.WHITE, Color.GRAY, Color.WHITE});
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(paint);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return image;
    }
//    public static void main(String[] args) throws IOException {
//        Font SENTENCE_FONT = new Font("Helvetica", Font.PLAIN, 50);
//        ParagraphDrawer sentenceDrawer = new ParagraphDrawer(SENTENCE_FONT, Color.BLACK,
//                "Some 80 vehicles carrying heavy weapons and Iraqi Kurdish "
//                + "fighters crossed into Turkey after dawn, as Jim Muir reports",
//                (int) (Toolkit.getDefaultToolkit().getScreenSize().width * 0.9));
//        Collection< BufferedImage> images = sentenceDrawer.getImages();
//        int i = 0;
//        for (BufferedImage image : images) {
//            ImageIO.write(image, "png", new File("SentenceDrawer " + i + ".png"));
//            i++;
//        }
//        BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
//        Graphics2D g = image.createGraphics();
//        FontMetrics fontMetrics = g.getFontMetrics(new Font("Helvetica", Font.PLAIN, 50));
//        System.out.println(fontMetrics.getHeight());
//        System.out.println(fontMetrics.getStringBounds("abcABCGHPhg", g));
//    }

    public ArrayList<Integer> getWordsCounts() {
        return wordsCountList;
    }
}

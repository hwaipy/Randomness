package com.hwaipy.prompt.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Hwaipy
 */
public class PresentationViewer extends JPanel {

    private static final int TOTAL_TIME = 40 * 60;
    private static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private static final Font SENTENCE_FONT = new Font("Helvetica", Font.PLAIN, 50);
    private static final Font TIMER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 100);
    private static final Color HIGHLIGHT_COLOR = Color.BLACK;
    private static final Color TIMER_COLOR = Color.GRAY.darker();
    private static final double ACCELERATION = 400;
    private static final double ANIMATION_REFRESH = 50;
    private static final double INSET = 0.02;
    private static final int LINE_GAP = 20;
    private final BufferedImage contentFilterImage;
    private final ArrayList<LineImageEntry> lineImageEntries = new ArrayList<>();
    private final ArrayList<LineImageEntry> lineGappingImageEntries = new ArrayList<>();
    private int viewPosition = 0;
    private int currentLine = 0;
    private int width;
    private final ScrollAnimator lineViewAnimator;
    private final TimerAnimator timerAnimator;

    public PresentationViewer(PresentationViewerContent viewerContent) {
        contentFilterImage = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        initLineImages(viewerContent);
        viewPosition = getViewPositionOfLine(0);
        lineViewAnimator = new ScrollAnimator(viewPosition, ACCELERATION, System.nanoTime() / 1000000000.);
        timerAnimator = new TimerAnimator();
        Timer animationTimer = new Timer((int) (1000 / ANIMATION_REFRESH), (ActionEvent e) -> {
            repaint();
        });
        animationTimer.setRepeats(true);
        animationTimer.setCoalesce(true);
        animationTimer.setInitialDelay(0);
        animationTimer.start();
        initAlarm();
    }

    private void initLineImages(PresentationViewerContent viewerContent) {
        width = (int) (screenSize.width * (1 - 2 * INSET));
        int y = 0;
        boolean firstSlide = true;
        BufferedImage slideGappingImage = ParagraphDrawer.getSlideGappingImage(width);
        for (int i = 0; i < viewerContent.getParagraphCount(); i++) {
            String paragraph = viewerContent.getParagraph(i);
            int time = viewerContent.getParagraphTime(i);
            if (viewerContent.isFirstParagraphOfSlide(i)) {
                if (firstSlide) {
                    firstSlide = false;
                } else {
                    lineGappingImageEntries.add(new LineImageEntry(slideGappingImage, y, y + slideGappingImage.getHeight(), 0));
                    y += slideGappingImage.getHeight() + LINE_GAP;
                }
            }
            Collection<BufferedImage> lineImages = ParagraphDrawer.draw(SENTENCE_FONT, HIGHLIGHT_COLOR, paragraph, width);
            for (BufferedImage lineImage : lineImages) {
                LineImageEntry entry = new LineImageEntry(lineImage, y, y + lineImage.getHeight(), time);
                lineImageEntries.add(entry);
                y += lineImage.getHeight() + LINE_GAP;
            }
        }
        lineImageEntries.remove(lineImageEntries.size() - 1);
        int lineHeight = lineImageEntries.get(0).image.getHeight();
        int centerTop = (screenSize.height - lineHeight - LINE_GAP) / 2 - lineHeight - LINE_GAP;
        int centerBottom = centerTop + 2 * (lineHeight + LINE_GAP);
        float centerTopF = ((float) centerTop) / screenSize.height;
        float centerBottomF = ((float) centerBottom) / screenSize.height;
        float whiteRegion = 0.15f;
        float fastDecayRegion = 0.01f;
        Graphics2D g2d = contentFilterImage.createGraphics();
        LinearGradientPaint linearGradientPaint = new LinearGradientPaint(0f, 0f, 0f, screenSize.height,
                new float[]{whiteRegion, centerTopF - fastDecayRegion, centerTopF,
                    centerBottomF, centerBottomF + fastDecayRegion, 1 - whiteRegion},
                new Color[]{
                    new Color(255, 255, 255, 255),
                    new Color(255, 255, 255, 200),
                    new Color(255, 255, 255, 0),
                    new Color(255, 255, 255, 0),
                    new Color(255, 255, 255, 100),
                    new Color(255, 255, 255, 255)});
        g2d.setPaint(linearGradientPaint);
        g2d.fillRect(0, 0, screenSize.width, screenSize.height);
        g2d.dispose();
    }

    private void initAlarm() {
        int readingLineCount = lineImageEntries.size();
        int totalVideoTime = 0;
        for (LineImageEntry lineImageEntry : lineImageEntries) {
            int time = lineImageEntry.videoTime;
            if (time > 0) {
                readingLineCount--;
                totalVideoTime += time;
            }
        }
        double readingTime = TOTAL_TIME - totalVideoTime;
        double lineTime = readingTime / readingLineCount;
        double time = 0;
        for (LineImageEntry lineImageEntry : lineImageEntries) {
            lineImageEntry.startTime = time;
            int videoTime = lineImageEntry.videoTime;
            if (videoTime > 0) {
                time += videoTime;
            } else {
                time += lineTime;
            }
            lineImageEntry.endTime = time;
        }
    }

    public void setViewLine(int index) {
        currentLine = index;
        int targetPosition = getViewPositionOfLine(index);
        lineViewAnimator.setTargetPosition(targetPosition);
    }

    public void increaseViewLine(boolean positive) {
        if (positive) {
            currentLine++;
        } else {
            currentLine--;
        }
        if (currentLine < 0) {
            currentLine = 0;
        } else if (currentLine >= lineImageEntries.size()) {
            currentLine = lineImageEntries.size() - 1;
        }
        int targetPosition = getViewPositionOfLine(currentLine);
        lineViewAnimator.setTargetPosition(targetPosition);
    }

    public void timerStart() {
        timerAnimator.start(System.nanoTime());
    }

    public void timerReset() {
        timerAnimator.reset();
    }

    private int getViewPositionOfLine(int index) {
        if (index < 0) {
            index = 0;
        } else if (index >= lineImageEntries.size()) {
            index = lineImageEntries.size() - 1;
        }
        return lineImageEntries.get(index).getCenter();
    }

    @Override
    protected void paintComponent(Graphics g) {
        long time = System.nanoTime();
        //Draw background.
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());
        //Draw lines.
        viewPosition = (int) lineViewAnimator.updatePosition(time / 1000000000.);
        int viewOffset = viewPosition - getHeight() / 2 + lineImageEntries.get(0).image.getHeight() + LINE_GAP;
        lineImageEntries.stream().forEach((lineImageEntry) -> {
            if (((lineImageEntry.top - viewOffset) > getHeight()) || ((lineImageEntry.bottom - viewOffset) < 0)) {
            } else {
                BufferedImage image = lineImageEntry.image;
                g.drawImage(image, (int) (getWidth() * INSET), lineImageEntry.top - viewOffset, this);
            }
        });
        lineGappingImageEntries.stream().forEach((lineGappingImageEntry) -> {
            if (((lineGappingImageEntry.top - viewOffset) > getHeight()) || ((lineGappingImageEntry.bottom - viewOffset) < 0)) {
            } else {
                BufferedImage image = lineGappingImageEntry.image;
                g.drawImage(image, (int) (getWidth() * INSET), lineGappingImageEntry.top - viewOffset, this);
            }
        });
        //Draw filter.
        g.drawImage(contentFilterImage, 0, 0, this);
        //Draw timer
        String timeString = timerAnimator.updateTime(time);
        BufferedImage timerImage = TimerDrawer.draw(TIMER_FONT, TIMER_COLOR, timeString);
        g.drawImage(timerImage, 0, 0, this);
    }

    private class LineImageEntry {

        private final BufferedImage image;
        private final int top;
        private final int bottom;
        private final int videoTime;
        private double startTime;
        private double endTime;

        private LineImageEntry(BufferedImage image, int top, int bottom, int videoTime) {
            this.image = image;
            this.top = top;
            this.bottom = bottom;
            this.videoTime = videoTime;
        }

        private int getCenter() {
            return (top + bottom) / 2;
        }
    }
}

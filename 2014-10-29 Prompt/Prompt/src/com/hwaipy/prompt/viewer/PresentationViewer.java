package com.hwaipy.prompt.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
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
    private static final Font SENTENCE_FONT = new Font("Helvetica", Font.PLAIN, 100);
    private static final Font TIMER_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 100);
    private static final Color HIGHLIGHT_COLOR = Color.BLACK;
    private static final Color TIMER_COLOR = Color.GRAY.darker();
    private static final double ACCELERATION = 1000;
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
    private String timerString = "Standby";
    private long time = 0;

    public PresentationViewer(PresentationViewerContent viewerContent) {
        contentFilterImage = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
        initLineImages(viewerContent);
        viewPosition = getViewPositionOfLine(0);
        lineViewAnimator = new ScrollAnimator(viewPosition, ACCELERATION, System.nanoTime() / 1000000000.);
        timerAnimator = new TimerAnimator();
        Timer animationTimer = new Timer((int) (1000 / ANIMATION_REFRESH), (ActionEvent e) -> {
            if (prepareView()) {
                repaint();
            }
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
                    lineGappingImageEntries.add(new LineImageEntry(slideGappingImage, y, y + slideGappingImage.getHeight(), 0, 0));
                    y += slideGappingImage.getHeight() + LINE_GAP;
                }
            }
            ParagraphDrawer paragraphDrawer = new ParagraphDrawer(SENTENCE_FONT, HIGHLIGHT_COLOR, paragraph, width);
            ArrayList<BufferedImage> lineImages = paragraphDrawer.getImages();
            ArrayList<Integer> wordsCounts = paragraphDrawer.getWordsCounts();
            for (int j = 0; j < lineImages.size(); j++) {
                BufferedImage lineImage = lineImages.get(j);
                LineImageEntry entry = new LineImageEntry(lineImage, y, y + lineImage.getHeight(), time, wordsCounts.get(j));
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
                    centerBottomF, centerBottomF + fastDecayRegion + 0.2f, 1 - whiteRegion + 0.1f},
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
        System.out.println("view: "+index);
        int wordCount = 0;
        int lineNumber = 0;
        for (int i = 0; i < lineImageEntries.size(); i++) {
            LineImageEntry entry = lineImageEntries.get(i);
            int lineWordCount = entry.wordCount;
            if (index >= wordCount && index <= (wordCount + lineWordCount)) {
                lineNumber = i;
                break;
            }
            wordCount += lineWordCount;
        }

        currentLine = lineNumber;
        int targetPosition = getViewPositionOfLine(lineNumber);
        lineViewAnimator.setTargetPosition(targetPosition);
    }

//    public void increaseViewLine(boolean positive) {
//        if (positive) {
//            currentLine++;
//        } else {
//            currentLine--;
//        }
//        if (currentLine < 0) {
//            currentLine = 0;
//        } else if (currentLine >= lineImageEntries.size()) {
//            currentLine = lineImageEntries.size() - 1;
//        }
//        int targetPosition = getViewPositionOfLine(currentLine);
//        lineViewAnimator.setTargetPosition(targetPosition);
//    }
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

    private int getTimeLine(double time) {
        if (time < 0) {
            return 0;
        } else if (time >= TOTAL_TIME) {
            return lineImageEntries.get(lineImageEntries.size() - 1).bottom;
        }
        int index = 0;
        for (int i = 0; i < lineImageEntries.size(); i++) {
            LineImageEntry entry = lineImageEntries.get(i);
            if (entry.startTime <= time && entry.endTime > time) {
                index = i;
                break;
            }
        }
        double startTime = lineImageEntries.get(index).startTime;
        double endTime = lineImageEntries.get(index).endTime;
        double startPosition = lineImageEntries.get(index).top;
        double endPosition = lineImageEntries.get(index).bottom;
        if (index + 1 < lineImageEntries.size()) {
            endPosition = lineImageEntries.get(index + 1).top;
        }
        double position = (time - startTime) / (endTime - startTime) * (endPosition - startPosition) + startPosition;
        return (int) position;
    }
    private final BufferedImage canvas = new BufferedImage(screenSize.width, screenSize.height, BufferedImage.TYPE_INT_ARGB);
    private boolean viewNeedChange = true;
    private boolean timeLineNeedChange = true;

    @Override
    protected void paintComponent(Graphics gO) {
        if (viewNeedChange || timeLineNeedChange) {
            Graphics2D g = canvas.createGraphics();
            int viewOffset = viewPosition - getHeight() / 2 + lineImageEntries.get(0).image.getHeight() + LINE_GAP;
            if (viewNeedChange) {
                //Draw background.
                g.setColor(Color.WHITE);
                g.fillRect(0, 0, getWidth(), getHeight());
//        //Draw lines.
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
                BufferedImage timerImage = TimerDrawer.draw(TIMER_FONT, TIMER_COLOR, timerString);
                g.drawImage(timerImage, 0, 0, this);
            }
            if (timeLineNeedChange) {
                //Draw timeline
                if (timerAnimator.isRunning()) {
                    int timeLine = getTimeLine(timerAnimator.getTime(time));
                    int timeLineY = timeLine - viewOffset;
                    int delta = timeLineY - screenSize.height / 2;
                    if (delta < 0) {
                        delta = -delta;
                    }
                    double deltaD = delta / (double) screenSize.height - 0.3;
                    double redD = deltaD;
                    double greenD = 1 - deltaD;
                    int red = (int) (redD * 256);
                    int green = (int) (greenD * 256);
                    if (red < 0) {
                        red = 0;
                    }
                    if (red > 255) {
                        red = 255;
                    }
                    if (timeLineY < screenSize.height * 0.2) {
                        timeLineY = (int) (screenSize.height * 0.2);
                    }
                    if (timeLineY > screenSize.height * 0.8) {
                        timeLineY = (int) (screenSize.height * 0.8);
                    }
                    Color timeLineColor = Color.getHSBColor((float) (0.322 * (1 - red / 256.)), 0.86f, 0.79f);
//            Color timeLineColor = new Color(ColorSpace.getInstance(ColorSpace.TYPE_HSV),
//                    new float[]{116f, 0.66f, 0.69f}, 255);
                    Color timeLineTrans = new Color(timeLineColor.getRed(), timeLineColor.getGreen(), timeLineColor.getBlue(), 0);
                    RadialGradientPaint paint = new RadialGradientPaint(-220f, timeLineY, 245f,
                            new float[]{0f, 0.9f, 1f},
                            new Color[]{
                                timeLineColor,
                                timeLineColor,
                                timeLineTrans});
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setPaint(paint);
//            g2d.fillRect(0, timeLine - viewOffset, screenSize.width, timeLineHeight);
                    g2d.fillRect(0, timeLineY - 250, 25, 500);
                }
            }
            g.dispose();
            viewNeedChange = false;
        }
        gO.drawImage(canvas, 0, 0, this);
    }

    private boolean prepareView() {
        boolean needRepaint = false;
        time = System.nanoTime();
        int newViewPosition = (int) lineViewAnimator.updatePosition(time / 1000000000.);
        if (newViewPosition != viewPosition) {
            viewPosition = newViewPosition;
            needRepaint = true;
        }
        String newTimerString = timerAnimator.updateTime(time);
        if (!newTimerString.equals(timerString)) {
            timerString = newTimerString;
            needRepaint = true;
        }
        viewNeedChange = viewNeedChange || needRepaint;
        return needRepaint;
    }

    private class LineImageEntry {

        private final BufferedImage image;
        private final int top;
        private final int bottom;
        private final int videoTime;
        private double startTime;
        private double endTime;
        private int wordCount;

        private LineImageEntry(BufferedImage image, int top, int bottom, int videoTime, int wordCount) {
            this.image = image;
            this.top = top;
            this.bottom = bottom;
            this.videoTime = videoTime;
            this.wordCount = wordCount;
        }

        private int getCenter() {
            return (top + bottom) / 2;
        }
    }
}

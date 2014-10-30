package com.hwaipy.prompt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Hwaipy
 */
public class Presentation {

    private final ArrayList<Slide> slideList = new ArrayList<>();

    public void addSlide(Slide slide) {
        slideList.add(slide);
    }

    public Collection<Slide> getSlides() {
        return Collections.unmodifiableCollection(slideList);
    }

    public Collection<Paragraph> getSentences() {
        ArrayList<Paragraph> sentences = new ArrayList<>();
        Collection<Slide> slides = getSlides();
        slides.stream().forEach((slide) -> {
            sentences.addAll(slide.getParagraphs());
        });
        return Collections.unmodifiableCollection(sentences);
    }

    public static Presentation load(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        Presentation presentation = new Presentation();
        Slide currentSlide = null;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            if (line.startsWith("@Slide ")) {
                String slideName = line.substring(7);
                currentSlide = new Slide(slideName);
                presentation.addSlide(currentSlide);
            } else if (line.startsWith("<Video>")) {
                String videoTimeString = line.substring(7);
                int videoTime = Integer.parseInt(videoTimeString);
                if (currentSlide != null) {
                    currentSlide.addParagraph(new VideoParagraph(videoTime));
                }
            } else {
                if (currentSlide != null) {
                    currentSlide.addParagraph(new Paragraph(line));
                }
            }
        }
        return presentation;
    }
}

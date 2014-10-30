package com.hwaipy.prompt.viewer;

import com.hwaipy.prompt.Presentation;
import com.hwaipy.prompt.Paragraph;
import com.hwaipy.prompt.Slide;
import com.hwaipy.prompt.VideoParagraph;
import java.util.ArrayList;

/**
 *
 * @author Hwaipy
 */
public class PresentationViewerContent {

    private final Presentation presentation;
    private final ArrayList<ParagraphEntry> paragraphEntrys = new ArrayList<>();

    public PresentationViewerContent(Presentation presentation) {
        this.presentation = presentation;
        init();
    }

    private void init() {
        presentation.getSlides().stream().forEach((slide) -> {
            boolean first = true;
            for (Paragraph sentence : slide.getParagraphs()) {
                paragraphEntrys.add(new ParagraphEntry(sentence, slide, first));
                first = false;
            }
        });
    }

    public int getParagraphCount() {
        return paragraphEntrys.size();
    }

    public String getParagraph(int index) {
        return paragraphEntrys.get(index).paragraph.getContent();
    }

    public boolean isFirstParagraphOfSlide(int index) {
        return paragraphEntrys.get(index).firstParagraph;
    }

    public int getParagraphTime(int index) {
        Paragraph paragraph = paragraphEntrys.get(index).paragraph;
        if (paragraph instanceof VideoParagraph) {
            VideoParagraph videoParagraph = (VideoParagraph) paragraph;
            return videoParagraph.getTime();
        }
        return 0;
    }

    private class ParagraphEntry {

        private final Paragraph paragraph;
        private final Slide slide;
        private final boolean firstParagraph;

        private ParagraphEntry(Paragraph paragraph, Slide slide, boolean firstParagraph) {
            this.paragraph = paragraph;
            this.slide = slide;
            this.firstParagraph = firstParagraph;
        }
    }
}

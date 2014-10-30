package com.hwaipy.prompt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Hwaipy
 */
public class Slide {

    private final String name;
    private final ArrayList<Paragraph> paragraphList = new ArrayList<>();

    public Slide(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addParagraph(Paragraph sentence) {
        paragraphList.add(sentence);
    }

    public Collection<Paragraph> getParagraphs() {
        return Collections.unmodifiableCollection(paragraphList);
    }
}

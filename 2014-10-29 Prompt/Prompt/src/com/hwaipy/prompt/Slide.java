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
    private final ArrayList<Sentence> sentenceList = new ArrayList<>();

    public Slide(String name) {
        this.name = name;
    }

    public void addSentence(Sentence sentence) {
        sentenceList.add(sentence);
    }

    public Collection<Sentence> getSentences() {
        return Collections.unmodifiableCollection(sentenceList);
    }
}

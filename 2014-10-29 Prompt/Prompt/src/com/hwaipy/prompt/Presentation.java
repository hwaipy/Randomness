package com.hwaipy.prompt;

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
}

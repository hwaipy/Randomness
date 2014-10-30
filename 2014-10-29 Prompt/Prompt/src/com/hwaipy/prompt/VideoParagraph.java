package com.hwaipy.prompt;

/**
 *
 * @author Hwaipy
 */
public class VideoParagraph extends Paragraph {

    private final int time;

    public VideoParagraph(int time) {
        super("<Video>");
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}

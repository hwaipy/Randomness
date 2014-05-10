package com.hwaipy.apple.iso.backup;

/**
 *
 * @author Hwaipy
 */
public class DurationFormat {

    public static String format(int duration) {
        int seconds = duration % 60;
        int d = duration / 60;
        int minites = d % 60;
        int hours = d / 60;
        StringBuilder sb = new StringBuilder();
        sb.append(hours < 10 ? "0" : "").append(hours).append(":")
                .append(minites < 10 ? "0" : "").append(minites).append(":")
                .append(seconds < 10 ? "0" : "").append(seconds);
        return sb.toString();
    }
}

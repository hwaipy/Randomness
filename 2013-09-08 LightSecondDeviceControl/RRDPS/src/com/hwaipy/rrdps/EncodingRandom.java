package com.hwaipy.rrdps;

/**
 *
 * @author Hwaipy
 */
public class EncodingRandom {

    private final int[] random;

    public EncodingRandom(int[] random) {
        this.random = random;
        if (random.length != 128) {
            throw new RuntimeException();
        }
    }

    public int getEncode(int pulseIndex, int delay) {
        if (pulseIndex >= delay && pulseIndex <= 127) {
            int a = random[pulseIndex];
            int b = random[pulseIndex - delay];
            return a ^ b;
        } else {
            return -1;
        }
    }
}

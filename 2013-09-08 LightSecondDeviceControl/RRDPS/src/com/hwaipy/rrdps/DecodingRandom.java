package com.hwaipy.rrdps;

/**
 *
 * @author Hwaipy
 */
public class DecodingRandom {

    private final int random;
    private final int delay1;
    private final int delay2;

    public DecodingRandom(int b) {
        int[] RrandomList = new int[7];
        int[] delaypulse = new int[2];
        byte R = (byte) b;
        for (int i = 0; i < 7; i++) {
            if (((R >>> i) & 0x01) == 0x01) {
                RrandomList[i] = 1;
            } else {
                RrandomList[i] = 0;
            }
        }
        delaypulse[0] = (RrandomList[0] + RrandomList[1] * 2 + RrandomList[2] * 4 + RrandomList[3] * 8);
        delaypulse[1] = RrandomList[4] * 16 + RrandomList[5] * 32 + RrandomList[6] * 64;
        this.delay1 = delaypulse[0];
        this.delay2 = delaypulse[1];
        this.random = b % 128;
    }

    public int getDelay1() {
        return delay1;
    }

    public int getDelay2() {
        return delay2;
    }

    public int getDelay() {
        return delay2 + 15 - delay1;
    }

    public int getRandom() {
        return random;
    }
}

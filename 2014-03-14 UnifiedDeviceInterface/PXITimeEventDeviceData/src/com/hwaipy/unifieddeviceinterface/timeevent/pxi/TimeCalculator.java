package com.hwaipy.unifieddeviceinterface.timeevent.pxi;

import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy
 */
class TimeCalculator {

    private static final long COARSE_TIME_LIMIT = 1 << 28;
    private long carry = 0;
    private long lastCoarseTime = -1;

    long calculate(ByteBuffer buffer, int channel) {
        long[] b = new long[8];
        for (int i = 0; i < 8; i++) {
            b[i] = buffer.get(i);
            if (b[i] < 0) {
                b[i] += 256;
            }
        }
        long fineTime = ((b[3] & 0x10) << 4) | b[7];
        long coarseTime = (b[4]) | (b[5] << 8)
                | (b[2] << 16) | ((b[3] & 0x0F) << 24);
        if (coarseTime < lastCoarseTime && (lastCoarseTime > COARSE_TIME_LIMIT / 2) && (coarseTime < COARSE_TIME_LIMIT / 2)) {
            carry++;
        }
        lastCoarseTime = coarseTime;
        long time = -getExactTime(fineTime, channel)
                + ((coarseTime + (carry << 28)) * 6250);
        return time;
    }

    private long getExactTime(long fineTime, int channel) {
        if (fineTime >= 273) {
            return 6250;
        } else {
            return (long) (6250. / 273 * fineTime);
        }
    }
}

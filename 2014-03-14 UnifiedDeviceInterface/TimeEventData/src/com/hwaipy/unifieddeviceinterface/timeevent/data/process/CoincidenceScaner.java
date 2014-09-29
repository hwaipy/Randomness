package com.hwaipy.unifieddeviceinterface.timeevent.data.process;

import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventListData;

/**
 *
 * @author Administrator
 */
public class CoincidenceScaner {

    public int[] scan(TimeEventListData list1, TimeEventListData list2,
            int scanRange, int coincidenceGateChannel, int stepLength) {
        int channelCount = scanRange * 2 / stepLength + 1;
        int[] stat = new int[channelCount];
        int indexStart2 = 0;
        for (int index1 = 0; index1 < list1.size(); index1++) {
            long time1 = list1.get(index1).getTime();
            long time2Start = time1 - scanRange;
            long time2End = time1 + scanRange;
            while (indexStart2 < list2.size()) {
                long time2 = list2.get(indexStart2).getTime();
                if (time2 > time2Start) {
                    break;
                }
                indexStart2++;
            }
            int index2 = indexStart2;
            while (index2 < list2.size()) {
                long time2 = list2.get(index2).getTime();
                if (time2 > time2End) {
                    break;
                }
                if (time2 <= 0) {
                    continue;
                }
                int diffTime = (int) (time2 - time1 + scanRange);
                int diffChannel = diffTime / stepLength;
                stat[diffChannel]++;
                index2++;
            }
        }
        int channelPrev = coincidenceGateChannel / 2;
        int channelLate = coincidenceGateChannel - channelPrev - 1;
        int maxIndex = max(stat, channelPrev, channelCount - channelLate);
        int maxDelay = maxIndex * stepLength - scanRange + stepLength / 2;
        int coincidenceCount = sum(stat, maxIndex - channelPrev, maxIndex + 1 + channelLate);
        return new int[]{coincidenceCount, maxDelay};
    }

    private int max(int[] array, int from, int to) {
        int max = Integer.MIN_VALUE;
        int maxIndex = -1;
        for (int i = from; i < to && i < array.length; i++) {
            if (i == from) {
                max = array[from];
                maxIndex = from;
            } else {
                if (max < array[i]) {
                    max = array[i];
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }

    private int sum(int[] array, int from, int to) {
        int sum = 0;
        for (int i = from; i < to && i < array.length; i++) {
            sum += array[i];
        }
        return sum;
    }
}

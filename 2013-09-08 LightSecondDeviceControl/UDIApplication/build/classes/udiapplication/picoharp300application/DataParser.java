package udiapplication.picoharp300application;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.StreamTimeEventSegment;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 *
 * @author Hwaipy Lab
 */
public abstract class DataParser {

    private static final int CHANNEL_COUNT = 8;
    private static final long TIME_UNIT = 1 << 24;
    private long carry = 0;
    private long lastTime = 0;
    private final StreamTimeEventSegment timeEventSegment =
            StreamTimeEventSegment.newStreamTimeEventSegment(CHANNEL_COUNT);

    public void offer(ByteBuffer data) throws IOException {
        if (data.limit() % 4 != 0) {
            throw new RuntimeException();
        }
        int imOrder = 0;
        while (data.hasRemaining()) {
            int unit = data.getInt();
            int special = unit & 0x80000000;
            if (special == 0x80000000) {
                carry++;
            } else {
                int channel = (unit & 0x7E000000) >> 25;
                int time = (unit & 0x1FFFFFF) >> 1;
                long fullTime = carry * TIME_UNIT + time;
                if (fullTime < lastTime) {
                    imOrder++;
//                    System.out.println(imOrder + "\t" + (lastTime - fullTime) + "\t" + (lastTime / 1000000000000.));
                    continue;
                }
                lastTime = fullTime;
                if (channel < 0 || channel >= CHANNEL_COUNT) {
                    throw new RuntimeException("" + channel);
                }
                timeEventSegment.offer(new TimeEvent(fullTime, channel));
            }
        }
        parse(timeEventSegment);
    }

    protected abstract void parse(TimeEventSegment timeEventSegment);
}

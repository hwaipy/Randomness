package com.hwaipy.unifieddeviceinterface.timeevent.pxi;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataFileLoader;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventSerializer;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author Hwaipy
 */
public class PXITimeEventDataFileLoader implements TimeEventDataFileLoader {

    private static final int CHANNEL_COUNT = 8;
    private File file;
    private boolean available = false;
    private RandomAccessFile raf;
    private FileChannel fileChannel;
    private MappingReader mappingReader;
    private final TimeCalculator tc;

    public PXITimeEventDataFileLoader(File dataFile) throws IOException {
        this.file = dataFile;
        if (file != null && file.exists() && file.isFile() && file.length() > 0) {
            available = true;
        }
        if (available) {
            raf = new RandomAccessFile(file, "rw");
            fileChannel = raf.getChannel();
            mappingReader = new MappingReader(fileChannel);
        }
        tc = new TimeCalculator();
    }

    @Override
    public int getChannelCount() {
        return CHANNEL_COUNT;
    }

    @Override
    public TimeEventData loadNext() throws IOException {
        if (!available) {
            return null;
        }
        while (true) {
            ByteBuffer unit = mappingReader.getNextUnit();
            if (unit == null) {
                return null;
            }
            boolean check = checkDataUnit(unit.array());
            if (check) {
                unit.position(0);
                return parse(unit);
            } else {
                mappingReader.rollBackUnit();
                ByteBuffer fixBuffer = mappingReader.getFixBuffer();
                if (fixBuffer == null) {
                    return null;
                }
                int offset = checkOffset(fixBuffer);
                if (offset == -1) {
                    throw new RuntimeException("Fix failed.");
                }
                mappingReader.skip(offset);
            }
        }
    }

    @Override
    public void complete(TimeEventClusterData cluster) {
    }

    private boolean checkDataUnit(byte[] data) {
        return !(((data[3] & 0xE0) != 0x00) || ((data[6] & 0XF0) != 0x40) || (data[1] != (byte) 0xFF));
    }

    private int checkOffset(ByteBuffer map) {
        int fixPreLength = 100;
        if (map.remaining() < 817) {
            return -1;
        }
        int originPosition = map.position();
        while (map.position() - originPosition < 16) {
            byte get = map.get();
            if (get == -1) {
                int p = map.position();
                map.position(p + 2);
                byte[] data = new byte[8];
                boolean isOK = true;
                for (int i = 0; i < fixPreLength; i++) {
                    map.get(data);
                    boolean check = checkDataUnit(data);
                    if (!check) {
                        isOK = false;
                        break;
                    }
                }
                if (isOK) {
                    map.position(originPosition);
                    return p - originPosition + 2;
                } else {
                    map.position(p);
                }
            }
        }
        map.position(originPosition);
        return -1;
    }
    private final long[] lastTime = new long[CHANNEL_COUNT];

    private TimeEventData parse(ByteBuffer unit) {
        long time;
        int channel = unit.get(6) & 0x0F;
        time = tc.calculate(unit, channel);
        if (channel >= 0 && channel < CHANNEL_COUNT) {
            if (time < lastTime[channel]) {
                return TimeEventData.ERROR_EVENT;
            }
            lastTime[channel] = time;
            return new TimeEventData(time, channel);
        } else {
            throw new RuntimeException("Channel number " + channel + " is not available.");
        }
    }

    private String p(int v) {
        if (v < 0) {
            v += 256;
        }
        return Integer.toHexString(v).toUpperCase();
    }

    @Override
    public TimeEventSerializer getSerializer() {
        return new TimeEventSerializer();
    }
}

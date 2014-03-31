package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.io.TimeEventSerializer;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 *
 * @author Hwaipy
 */
public class HydraHarp400T3Loader implements TimeEventLoader {

    public static final int CHANNEL_MARKER_1 = 4;
    public static final int CHANNEL_MARKER_2 = 5;
    public static final int CHANNEL_MARKER_3 = 6;
    public static final int CHANNEL_MARKER_4 = 7;
    public static final int CHANNEL_MARKER_OFFSET = 3;
    private static final int CHANNEL_COUNT = 8;
    private final File file;
    private boolean available = false;
    private RandomAccessFile raf;
    private FileChannel fileChannel;
    private MappingReader reader;
    private final long period;
    private int step = 1;

    public HydraHarp400T3Loader(File file, long period) throws IOException {
        this.file = file;
        if (file != null && file.exists() && file.isFile() && file.length() > 0) {
            available = true;
        }
        if (available) {
            raf = new RandomAccessFile(file, "rw");
            fileChannel = raf.getChannel();
            reader = new MappingReader(fileChannel);
            readHead();
////            reader.skip(8000);
        }
        this.period = period;
    }

    @Override
    public int getChannelCount() {
        return CHANNEL_COUNT;
    }
    private long lastTime = 0;
    private long syncOverflow = 0;

    @Override
    public TimeEvent loadNext() throws IOException, DeviceException {
        if (!available) {
            return null;
        }
        int imOrder = 0;
        while (reader.hasNextInt()) {
            int unit = reader.getInt();
            int special = unit & 0x80000000;
            int channel = (unit & 0x7E000000) >> 25;
            int time = (unit & 0x01FFFC00) >> 10;
            int nsync = (unit & 0x000003FF);
            if (special == 0x80000000) {
                //special event
                if (channel == 63) {
                    syncOverflow++;
//                } else if (channel == 1) {
//                    long fullTime = carry * TIME_UNIT + time;
//                    if (fullTime < lastTime) {
//                        imOrder++;
//                        System.out.println(imOrder + "\t" + (lastTime - fullTime));
//                    }
//                    lastTime = fullTime;
//                    return new TimeEvent(fullTime, CHANNEL_MARKER_1);
                } else if (channel == 1 || channel == 2 || channel == 4 || channel == 8) {
                    //Marker event record
                    long fullTime = (syncOverflow * 1024 + nsync) * period + time * step;
                    lastTime = fullTime;
                    channel = channel == 1 ? 1 : (channel == 2 ? 2 : (channel == 4 ? 3 : 4));
                    return new TimeEvent(fullTime, channel + CHANNEL_MARKER_OFFSET);
                } else {
                    System.out.println("Illigel channel " + channel + ", position=" + reader.position() + ", remaining=" + reader.remaining());
//                    throw new RuntimeException();
                }
            } else {
                //regular event record
                long fullTime = (syncOverflow * 1024 + nsync) * period + time * step;
//                System.out.println(time * 4);
//                System.out.println(fullTime);
                if (fullTime < lastTime) {
                    imOrder++;
                    System.out.println("ImOrdered " + imOrder + "\t" + (lastTime - fullTime));
                }
                lastTime = fullTime;
                return new TimeEvent(fullTime, channel);
            }
        }
        return null;
    }

    @Override
    public void complete(TimeEventSegment segment) {
    }

    @Override
    public TimeEventSerializer getSerializer() {
        return new TimeEventSerializer();
    }

    private void readHead() {
        String ident = reader.getStringByASCII(16);
        reader.getStringByASCII(6);
        reader.getStringByASCII(18);
        reader.getStringByASCII(12);
        reader.getStringByASCII(18);
        reader.skip(2);
        reader.getStringByASCII(256);
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        int binning = reader.getInt();
        reader.getDouble();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.skip(8 * 2 * 4);
        reader.skip(3 * 3 * 4);
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getStringByASCII(20);
        reader.getStringByASCII(16);
        reader.getStringByASCII(8);
        reader.getInt();
        reader.getInt();
        reader.skip(10 * 2 * 4);
        reader.getDouble();
        reader.getLong();
        int inputChannelsPresent = reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.getInt();
        reader.skip(inputChannelsPresent * 5 * 4);
        int syncRate = reader.getInt();
        reader.getInt();
        reader.getInt();
        int imgHdrSize = reader.getInt();
        reader.getLong();
        reader.skip(imgHdrSize * 1 * 4);
//        System.out.println(binning);
//        System.out.println(syncRate);
        step = 1;
        for (int i = 0; i < binning; i++) {
            step *= 2;
        }
    }
}

package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.io.TimeEventSerializer;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 *
 * @author Hwaipy
 */
public class HydraHarp400T2Loader implements TimeEventLoader {

    public static final int CHANNEL_SYNC = 8;
    public static final int CHANNEL_MARKER_OFFSET = 3;
    public static final int CHANNEL_MARKER_1 = 4;
    public static final int CHANNEL_MARKER_2 = 5;
    public static final int CHANNEL_MARKER_3 = 6;
    public static final int CHANNEL_MARKER_4 = 7;
    private static final int CHANNEL_COUNT = 9;
    private static final long TIME_UNIT = 1 << 24;
    private final File file;
    private boolean available = false;
    private RandomAccessFile raf;
    private FileChannel fileChannel;
    private Description description;
    private MappingReader reader;
    private long carry = 0;

    public HydraHarp400T2Loader(File file) throws IOException {
        this.file = file;
        if (file != null && file.exists() && file.isFile() && file.length() > 0) {
            available = true;
        }
        if (available) {
            raf = new RandomAccessFile(file, "rw");
            fileChannel = raf.getChannel();
            reader = new MappingReader(fileChannel);
//            readHead();
            reader.skip(800);
        }
    }

    @Override
    public int getChannelCount() {
        return CHANNEL_COUNT;
    }
    private long lastTime = 0;

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
            int time = (unit & 0x1FFFFFF) >> 1;
            if (special == 0x80000000) {
                if (channel == 63) {
                    carry++;
                } else if (channel == 0) {
                    long fullTime = carry * TIME_UNIT + time;
                    if (fullTime < lastTime) {
                        imOrder++;
//                        System.out.println("A" + imOrder + "\t" + (lastTime - fullTime));
                    }
                    lastTime = fullTime;
                    return new TimeEvent(fullTime, CHANNEL_SYNC);
                } else if (channel == 1 || channel == 2 || channel == 4 || channel == 8) {
                    long fullTime = carry * TIME_UNIT + time;
                    if (fullTime < lastTime) {
                        imOrder++;
//                        System.out.println("B" + imOrder + "\t" + (lastTime - fullTime));
                    }
                    lastTime = fullTime;
                    channel = channel == 1 ? 1 : (channel == 2 ? 2 : (channel == 4 ? 3 : 4));
                    return new TimeEvent(fullTime, CHANNEL_MARKER_OFFSET + channel);
                } else {
//                    System.out.println(channel);
//                    System.out.println(reader.position());
//                    System.out.println(reader.remaining());
//                    throw new RuntimeException();
                }
            } else {
                long fullTime = carry * TIME_UNIT + time;
                if (fullTime < lastTime) {
                    imOrder++;
//                    System.out.println("C" + imOrder + "\t" + (lastTime - fullTime));
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

    public Description getDescription() {
        return description;
    }

    private void readHead() {
        description = new Description();
        description.setIdent(reader.getStringByASCII(16));
        description.setFormatVersion(reader.getStringByASCII(6));
        description.setCreatorName(reader.getStringByASCII(18));
        description.setCreatorVersion(reader.getStringByASCII(12));
        description.setFileTime(reader.getStringByASCII(18));
        description.setCR_LF(reader.get(), reader.get());
        description.setComment(reader.getStringByASCII(256));
        description.setNumberOfCurves(reader.getInt());
        description.setBitsPerRecord(reader.getInt());
        description.setActiveCurve(reader.getInt());
        description.setMeasurementMode(reader.getInt());
        description.setSubMode(reader.getInt());
        description.setBinning(reader.getInt());
        description.setResolution(reader.getDouble());
        description.setOffset(reader.getInt());
        description.setAcquisitionTime(reader.getInt());
        description.setStopAt(reader.getInt());
        description.setStopOnOvfl(reader.getInt());
        description.setRestart(reader.getInt());
        description.setDisplayLinLog(reader.getInt());
        description.setDisplayTimeAxisFrom(reader.getInt());
        description.setDisplayTimeAxisTo(reader.getInt());
        description.setDisplayCountAxisFrom(reader.getInt());
        description.setDisplayCountAxisTo(reader.getInt());
    }
//    private boolean checkDataUnit(byte[] data) {
//        if (data[4] < 0 || data[4] >= CHANNEL_COUNT || data[5] != -1) {
//            return false;
//        }
//        return true;
//    }
//
//    private int checkOffset(ByteBuffer map) {
//        int fixPreLength = 100;
//        if (map.remaining() < 817) {
//            return -1;
//        }
//        int originPosition = map.position();
//        while (map.position() - originPosition < 16) {
//            byte get = map.get();
//            if (get == -1) {
//                int p = map.position();
//                map.position(p + 2);
//                byte[] data = new byte[8];
//                boolean isOK = true;
//                for (int i = 0; i < fixPreLength; i++) {
//                    map.get(data);
//                    boolean check = checkDataUnit(data);
//                    if (!check) {
//                        isOK = false;
//                        break;
//                    }
//                }
//                if (isOK) {
//                    map.position(originPosition);
//                    return p - originPosition + 2;
//                } else {
//                    map.position(p);
//                }
//            }
//        }
//        map.position(originPosition);
//        return -1;
//    }
//
//    private TimeEvent parse(ByteBuffer unit) throws TimeEventException {
//        long time;
//        time = tc.calculate(new int[]{unit.get(), unit.get(), unit.get(), unit.get()});
//        int channel = unit.get();
//        if (channel >= 0 && channel < CHANNEL_COUNT) {
//            return new TimeEvent(time, channel);
//        } else {
//            throw new TimeEventException("Channel number " + channel + " is not available.");
//        }
//    }

    @Override
    public TimeEventSerializer getSerializer() {
        return new TimeEventSerializer();
    }
}

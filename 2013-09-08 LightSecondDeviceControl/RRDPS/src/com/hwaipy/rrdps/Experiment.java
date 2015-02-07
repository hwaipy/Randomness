package com.hwaipy.rrdps;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.StreamTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class Experiment {

    private static final boolean DEBUG = true;
    private static final int CHANNEL_APD1 = 2;
    private static final int CHANNEL_APD2 = 3;
    private static final Map<String, String[]> FILENAME_MAP = new HashMap<>();

    static {
        //发射端时间测量数据，发射端随机数数据，接收端时间测量数据，接收端随机数数据，(接收端稳相数据)
        FILENAME_MAP.put("20150130125829", new String[]{"20150130125829-S-固定-2_时间测量数据.dat", "20150130125829-S-固定-2_发射端随机数.dat", "20150130125829-R-固定-单路-APD2-2_时间测量数据.dat", "rnd-00.dat"});
    }
    private final String id;
    private final File path;
    private TimeEventList aliceRandomList;
    private TimeEventList bobRandomList;
    private TimeEventList apd1List;
    private TimeEventList apd2List;
    private TimeEventList apdList;

    public Experiment(String id, File path) {
        this.id = id;
        this.path = path;
    }

    public void loadData() throws IOException, DeviceException {
        String[] fileNames = FILENAME_MAP.get(id);
        if (fileNames == null) {
            throw new RuntimeException();
        }
        File AliceTDCFile = new File(path, fileNames[0]);
        File AliceQRNGFile = new File(path, fileNames[1]);
        File BobTDCFile = new File(path, fileNames[2]);
        File BobQRNGFile = new File(path, fileNames[3]);
        TimeEventSegment aliceSegment = loadTDCFile(AliceTDCFile);
        TimeEventSegment bobSegment = loadTDCFile(BobTDCFile);

        ArrayList<EncodingRandom> aliceQRNGList = loadEncodingQRNGFile(AliceQRNGFile);
        ArrayList<DecodingRandom> bobQRNGList = loadDecodingQRNGFile(BobQRNGFile);
        aliceRandomList = timingQRNG(aliceQRNGList, aliceSegment.getEventList(1));
        bobRandomList = timingQRNG(bobQRNGList, bobSegment.getEventList(1));

        apd1List = bobSegment.getEventList(CHANNEL_APD1);
        apd2List = bobSegment.getEventList(CHANNEL_APD2);
    }

    public void sync(long delay1, long delay2) {
//        TimeEventList aliceGPSList = aliceSegment.getEventList(0);
//        TimeEventList aliceSyncList = aliceSegment.getEventList(1);
//        TimeEventList bobGPSList = bobSegment.getEventList(0);
//        TimeEventList bobSyncList = bobSegment.getEventList(1);
//        long coarseDelay = aliceGPSList.get(0).getTime() - bobGPSList.get(0).getTime();
//        RecursionCoincidenceMatcher syncMatcher = new RecursionCoincidenceMatcher(bobSyncList, aliceSyncList, 1000000, coarseDelay);

        for (int i = 0; i < apd1List.size(); i++) {
            TimeEvent e = apd1List.get(i);
            apd1List.set(new TimeEvent(e.getTime() - delay1, e.getChannel()), i);
        }
        for (int i = 0; i < apd2List.size(); i++) {
            TimeEvent e = apd2List.get(i);
            apd2List.set(new TimeEvent(e.getTime() - delay2, e.getChannel()), i);
        }
    }

    public void filterAndMerge(long before, long after) {
        apd1List = doFilter(apd1List, bobRandomList, before, after);
        apd2List = doFilter(apd2List, bobRandomList, before, after);
        apdList = new MergedTimeEventList(apd1List, apd2List);
    }

    public ArrayList<Decoder.Entry> decoding(long gate) {
        Tagger tagger = new Tagger(bobRandomList, apdList, gate);
        ArrayList<Tagger.Entry> tags = tagger.tag();

        Decoder decoder = new Decoder(tags, aliceRandomList, bobRandomList);
        ArrayList<Decoder.Entry> result = decoder.decode();
        return result;
    }

    private TimeEventList doFilter(TimeEventList apdList, TimeEventList bobRandomList, long before, long after) {
        Iterator<TimeEvent> apdIterator = apdList.iterator();
        Iterator<TimeEvent> syncIterator = bobRandomList.iterator();
        TimeEvent syncEvent = syncIterator.next();
        long startTime = syncEvent.getTime() - before;
        long endTime = syncEvent.getTime() + after;
        TimeEvent apdEvent = apdIterator.next();
        StreamTimeEventList newList = new StreamTimeEventList();
        while (true) {
            long time = apdEvent.getTime();
            if (time <= endTime) {
                if (time >= startTime) {
                    newList.offer(apdEvent);
                }
                if (apdIterator.hasNext()) {
                    apdEvent = apdIterator.next();
                } else {
                    apdEvent = null;
                }
            } else {
                if (syncIterator.hasNext()) {
                    syncEvent = syncIterator.next();
                    startTime = syncEvent.getTime() - before;
                    endTime = syncEvent.getTime() + after;
                } else {
                    syncEvent = null;
                }
            }
            if (apdEvent == null || syncEvent == null) {
                break;
            }
        }
        return newList;
    }

    private TimeEventSegment loadTDCFile(File file) throws IOException, DeviceException {
        TimeEventLoader loader = new PXI40PS1Loader(file, null);
        return TimeEventDataManager.loadTimeEventSegment(loader);
    }

    private ArrayList<EncodingRandom> loadEncodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        byte[] b = new byte[16];
        int[] randomList = new int[128];
        ArrayList<EncodingRandom> list = new ArrayList<>();
        while (true) {
            int read = input.read(b);
            if (read < 16) {
                break;
            }
            for (int i = 0; i < 128; i++) {
                if (((b[(i / 8)] >>> (7 - (i % 8))) & 0x01) == 0x01) {
                    randomList[i] = 0;
                } else {
                    randomList[i] = 1;
                }
            }
            list.add(new EncodingRandom(randomList));
        }
        return list;
    }

    private ArrayList<DecodingRandom> loadDecodingQRNGFile(File file) throws IOException, DeviceException {
        FileInputStream input = new FileInputStream(file);
        int b;
        ArrayList<DecodingRandom> list = new ArrayList<>();
        while (true) {
            b = input.read();
            if (b == -1) {
                break;
            }
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
            list.add(new DecodingRandom(delaypulse[0], delaypulse[1]));
        }
        return list;
    }

    private <T> TimeEventList timingQRNG(ArrayList<T> QRNGList, TimeEventList timingList) {
        //数据校验
        if (DEBUG) {
            Iterator<TimeEvent> iterator = timingList.iterator();
            TimeEvent t1 = iterator.next();
            while (iterator.hasNext()) {
                TimeEvent t2 = iterator.next();
                long deltaT = t2.getTime() - t1.getTime();
                if (Math.abs(deltaT) > 150000000 && Math.abs(deltaT) < 1000000000000l) {
                    throw new RuntimeException();
                }
                t1 = t2;
            }
        }

        int length = Math.min(QRNGList.size(), timingList.size());
        StreamTimeEventList streamTimeEventList = new StreamTimeEventList();
        Iterator<TimeEvent> iterator = timingList.iterator();
        for (int i = 0; i < length; i++) {
            T random = QRNGList.get(i);
            TimeEvent timeEvent;
            if (iterator.hasNext()) {
                timeEvent = iterator.next();
            } else {
                throw new RuntimeException();
            }
            ExtandedTimeEvent<T> ete = new ExtandedTimeEvent<>(timeEvent.getTime(), timeEvent.getChannel(), random);
            streamTimeEventList.offer(ete);
        }
        return streamTimeEventList;
    }

    void test() {
        MergedTimeEventList m = new MergedTimeEventList(apd1List, apd2List);
        System.out.println(m.size());
        Iterator<TimeEvent> iterator = m.iterator();
        long t = 0;
        int i = 0 ;
        while (iterator.hasNext()) {
            TimeEvent next = iterator.next();
            if (next.getTime() < t) {
                System.out.println("wrong");
            } else {
                t = next.getTime();
            }
        }
    }
}

package lightseconddataanalyzer.g2.zeno;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import lightseconddataanalyzer.fourfold.CoincidenceEventList;

/**
 *
 * @author Hwaipy
 */
public class Df {

    private static final long GAP = 13158;

    public static void main(String[] args) throws Exception {
        mainA1(args);
    }

    public static void mainA1(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/data/201412282154litter.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList triggerList = segment.getEventList(8);
        TimeEventList signalList0 = segment.getEventList(0);
        TimeEventList signalListF = segment.getEventList(3);
        System.out.println("Dt: " + triggerList.size() / 60 + "\t" + triggerList.size());
        System.out.println("D0: " + signalList0.size() / 60 + "\t" + signalList0.size());
        System.out.println("Df: " + signalListF.size() / 60 + "\t" + signalListF.size());

//        CoincidenceMatcher cmT0 = new CoincidenceMatcher(triggerList, signalList0, 500, 27800);
//        for (int delay = 26000; delay < 30000; delay += 200) {
//            cmT0.setDelay(delay);
//            System.out.println(delay + "\t" + cmT0.find() / 60);
//        }
//        CoincidenceMatcher cmTF = new CoincidenceMatcher(triggerList, signalListF, 500, 25600);
//        for (int delay = 25000; delay < 27000; delay += 200) {
//            cmTF.setDelay(delay);
//            System.out.println(delay + "\t" + cmTF.find() / 60);
//        }
//        
        CoincidenceMatcher cmT0 = new CoincidenceMatcher(triggerList, signalList0, 1000, 27800);
        System.out.println("符合T&0:\t" + cmT0.find() / 60 + "\t" + cmT0.find());
        CoincidenceMatcher cmTF = new CoincidenceMatcher(triggerList, signalListF, 1000, 25600);
        System.out.println("符合T&F:\t" + cmTF.find() / 60 + "\t" + cmTF.find());
        CoincidenceEventList cT0List = new CoincidenceEventList(cmT0, 1);
        CoincidenceMatcher cmT0F = new CoincidenceMatcher(cT0List, signalListF, 1000, 25600);
//        System.out.println(cmT0F.find() / 60.);
        for (int i = -5; i <= 5; i++) {
            long delay = 25600 + i * GAP / 10;
            cmT0F.setDelay(delay);
            int coincidence = cmT0F.find();
            System.out.printf("%d\t%f4\t%d", delay, coincidence / 60., coincidence);
            System.out.println();
//            System.out.println(delay + "\t" + cmT0F.find() / 60.);
        }
    }

    public static void mainA2(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/data/201412282211.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList triggerList = segment.getEventList(8);
        TimeEventList signalList0 = segment.getEventList(0);
        TimeEventList signalListF = segment.getEventList(3);
        System.out.println("Dt: " + triggerList.size() / 60 + "\t" + triggerList.size());
        System.out.println("D0: " + signalList0.size() / 60 + "\t" + signalList0.size());
        System.out.println("Df: " + signalListF.size() / 60 + "\t" + signalListF.size());

//        CoincidenceMatcher cmT0 = new CoincidenceMatcher(triggerList, signalList0, 500, 27800);
//        for (int delay = 26000; delay < 30000; delay += 200) {
//            cmT0.setDelay(delay);
//            System.out.println(delay + "\t" + cmT0.find() / 60);
//        }
//        CoincidenceMatcher cmTF = new CoincidenceMatcher(triggerList, signalListF, 500, 33400);
//        for (int delay = 32000; delay < 35000; delay += 200) {
//            cmTF.setDelay(delay);
//            System.out.println(delay + "\t" + cmTF.find() / 60);
//        }
//        
        CoincidenceMatcher cmT0 = new CoincidenceMatcher(triggerList, signalList0, 500, 27800);
        System.out.println("符合T&0:\t" + cmT0.find() / 60 + "\t" + cmT0.find());
        CoincidenceMatcher cmTF = new CoincidenceMatcher(triggerList, signalListF, 500, 33400);
        System.out.println("符合T&F:\t" + cmTF.find() / 60 + "\t" + cmTF.find());
        CoincidenceEventList cT0List = new CoincidenceEventList(cmT0, 1);
        CoincidenceMatcher cmT0F = new CoincidenceMatcher(cT0List, signalListF, 500, 33400);
//        System.out.println(cmT0F.find() / 60.);
        for (int i = - 5; i <= 5; i++) {
            long delay = 33400 + i * GAP / 10;
            cmT0F.setDelay(delay);
            int coincidence = cmT0F.find();
            System.out.printf("%d\t%f4\t%d", delay, coincidence / 60., coincidence);
            System.out.println();
//            System.out.println(delay + "\t" + cmT0F.find() / 60.);
        }
    }

    public static void main2(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/201412252140.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList triggerList = segment.getEventList(8);
        TimeEventList signalList0 = segment.getEventList(0);
        TimeEventList signalListF = segment.getEventList(2);
        System.out.println("Dt: " + triggerList.size() / 60 + "\t" + triggerList.size());
        System.out.println("D0: " + signalList0.size() / 60 + "\t" + signalList0.size());
        System.out.println("Df: " + signalListF.size() / 60 + "\t" + signalListF.size());

        CoincidenceMatcher cmT0 = new CoincidenceMatcher(triggerList, signalList0, 1000, 28000);
        System.out.println("符合T&0:\t" + cmT0.find() / 60 + "\t" + cmT0.find());
        CoincidenceEventList cT0List = new CoincidenceEventList(cmT0, 1);
        CoincidenceMatcher cmTF = new CoincidenceMatcher(triggerList, signalListF, 1000, 29600);
        System.out.println("符合T&F:\t" + cmTF.find() / 60 + "\t" + cmTF.find());
        CoincidenceEventList cTFList = new CoincidenceEventList(cmTF, 1);
        CoincidenceMatcher cmT0F = new CoincidenceMatcher(cT0List, cTFList);
        for (int i = - 500; i <= 500; i++) {
            long delay = 0 + i * GAP / 10;
            cmT0F.setDelay(delay);
            int coincidence = cmT0F.find();
            System.out.printf("%d\t%f4\t%d", delay, coincidence / 60., coincidence);
            System.out.println();
//            System.out.println(delay + "\t" + cmT0F.find() / 60.);
        }
    }

    public static void main3(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/201412252140.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList triggerList = segment.getEventList(8);
        TimeEventList signalList0 = segment.getEventList(0);
        TimeEventList signalListF = segment.getEventList(2);
        System.out.println("Dt: " + triggerList.size() / 60 + "\t" + triggerList.size());
        System.out.println("D0: " + signalList0.size() / 60 + "\t" + signalList0.size());
        System.out.println("Df: " + signalListF.size() / 60 + "\t" + signalListF.size());

        CoincidenceMatcher cmT0F = new CoincidenceMatcher(signalList0, signalListF);
        for (int i = - 500; i <= 500; i++) {
            long delay = i * 100;
            cmT0F.setDelay(delay);
            int coincidence = cmT0F.find();
            System.out.printf("%d\t%f4\t%d", delay, coincidence / 60., coincidence);
            System.out.println();
//            System.out.println(delay + "\t" + cmT0F.find() / 60.);
        }
    }
}

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
public class DfCoherence {

    private static final long GAP = 13158;

    public static void main(String[] args) throws Exception {
        main1(args);
    }

    public static void main1(String[] args) throws IOException, DeviceException {
        File file = new File("/Users/Hwaipy/Desktop/data/201412281738.ht2");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList signalList = segment.getEventList(8);
        TimeEventList signalListF = segment.getEventList(0);
        System.out.println("D0: " + signalList.size() / 60 + "\t" + signalList.size());
        System.out.println("Df: " + signalListF.size() / 60 + "\t" + signalListF.size());

        CoincidenceMatcher cmT0 = new CoincidenceMatcher(signalList, signalListF, 1000, 0);
        int coincidence = cmT0.find();
        System.out.println("符合T&0:\t" + coincidence / 60 + "\t" + coincidence
                + "\t" + coincidence / (double) signalList.size());
//        CoincidenceEventList cT0List = new CoincidenceEventList(cmT0, 1);
//        CoincidenceMatcher cmT0F = new CoincidenceMatcher(cT0List, signalListF, 1000, 29600);
//        for (int i = - 50; i <= 50; i++) {
//            long delay = 29600 + i * GAP / 10;
//            cmT0F.setDelay(delay);
//            int coincidence = cmT0F.find();
//            System.out.printf("%d\t%f4\t%d", delay, coincidence / 60., coincidence);
//            System.out.println();
//        }
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

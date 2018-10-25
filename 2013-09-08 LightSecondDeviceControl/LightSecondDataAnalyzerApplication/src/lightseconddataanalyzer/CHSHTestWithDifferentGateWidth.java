package lightseconddataanalyzer;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CopiedTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.MergedTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;

/**
 *
 * @author Hwaipy
 */
public class CHSHTestWithDifferentGateWidth {

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Desktop/LightSecond/201401130049.ht2");
//    
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401121712.ht2");
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401121750.ht2");
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401121935.ht2");
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401122050.ht2");
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401122327.ht2");
//        File file = new File("/Users/Hwaipy/Desktop/2014-01-12 单TDC采数尝试/201401130049.ht2");
//
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

        TimeEventList signalList0 = segment.getEventList(0);
        TimeEventList signalList1 = segment.getEventList(1);
        TimeEventList signalList2 = segment.getEventList(2);
        TimeEventList signalList3 = segment.getEventList(3);
        TimeEventList markerList1High = segment.getEventList(HydraHarp400T2Loader.CHANNEL_MARKER_1);
        TimeEventList markerList1Low = segment.getEventList(HydraHarp400T2Loader.CHANNEL_MARKER_2);
        TimeEventList markerList2High = segment.getEventList(HydraHarp400T2Loader.CHANNEL_MARKER_3);
        TimeEventList markerList2Low = segment.getEventList(HydraHarp400T2Loader.CHANNEL_MARKER_4);
        System.out.println("Signal 0: " + signalList0.size());
        System.out.println("Signal 1: " + signalList1.size());
        System.out.println("Signal 2: " + signalList2.size());
        System.out.println("Signal 3: " + signalList3.size());
        System.out.println("Marker 1 High: " + markerList1High.size());
        System.out.println("Marker 1 Low: " + markerList1Low.size());
        System.out.println("Marker 2 High: " + markerList2High.size());
        System.out.println("Marker 2 Low: " + markerList2Low.size());

//        System.exit(0);
        TimeEventList markerList1 = new CopiedTimeEventList(new MergedTimeEventList(markerList1High, markerList1Low));
        TimeEventList markerList2 = new CopiedTimeEventList(new MergedTimeEventList(markerList2High, markerList2Low));
        //Delay for Channel 0 & 2 is 15925ps
        //Delay for Channel 0 & 3 is 10885ps
        //Delay for Channel 1 & 2 is 26545ps
        //Delay for Channel 1 & 3 is 21505ps
        //Marker 2 for Channel 1 & 2
//        CoincidenceMatcher cm = new CoincidenceMatcher(signalList1, signalList3, 40, 0);
//        for (int delay = 21000; delay < 29000; delay += 5) {
//            cm.setDelay(delay);
//            System.out.println(delay + "\t" + cm.find());
//        }
//        System.exit(0);
//
        ChannelMarkeredTimeEventList list0MarkeredHigh = new ChannelMarkeredTimeEventList(signalList0, markerList1, HydraHarp400T2Loader.CHANNEL_MARKER_1);
        ChannelMarkeredTimeEventList list0MarkeredLow = new ChannelMarkeredTimeEventList(signalList0, markerList1, HydraHarp400T2Loader.CHANNEL_MARKER_2);
        ChannelMarkeredTimeEventList list1MarkeredHigh = new ChannelMarkeredTimeEventList(signalList1, markerList1, HydraHarp400T2Loader.CHANNEL_MARKER_1);
        ChannelMarkeredTimeEventList list1MarkeredLow = new ChannelMarkeredTimeEventList(signalList1, markerList1, HydraHarp400T2Loader.CHANNEL_MARKER_2);
        ChannelMarkeredTimeEventList list2MarkeredHigh = new ChannelMarkeredTimeEventList(signalList2, markerList2, HydraHarp400T2Loader.CHANNEL_MARKER_3);
        ChannelMarkeredTimeEventList list2MarkeredLow = new ChannelMarkeredTimeEventList(signalList2, markerList2, HydraHarp400T2Loader.CHANNEL_MARKER_4);
        ChannelMarkeredTimeEventList list3MarkeredHigh = new ChannelMarkeredTimeEventList(signalList3, markerList2, HydraHarp400T2Loader.CHANNEL_MARKER_3);
        ChannelMarkeredTimeEventList list3MarkeredLow = new ChannelMarkeredTimeEventList(signalList3, markerList2, HydraHarp400T2Loader.CHANNEL_MARKER_4);
        System.out.println("List0 High: " + list0MarkeredHigh.size());
        System.out.println("List0 Low: " + list0MarkeredLow.size());
        System.out.println("List1 High: " + list1MarkeredHigh.size());
        System.out.println("List1 Low: " + list1MarkeredLow.size());
        System.out.println("List2 High: " + list2MarkeredHigh.size());
        System.out.println("List2 Low: " + list2MarkeredLow.size());
        System.out.println("List3 High: " + list3MarkeredHigh.size());
        System.out.println("List3 Low: " + list3MarkeredLow.size());

        int gate = 100;
        CoincidenceMatcher cm02HH = new CoincidenceMatcher(list0MarkeredHigh, list2MarkeredHigh, gate, 15925);
        CoincidenceMatcher cm02HL = new CoincidenceMatcher(list0MarkeredHigh, list2MarkeredLow, gate, 15925);
        CoincidenceMatcher cm02LH = new CoincidenceMatcher(list0MarkeredLow, list2MarkeredHigh, gate, 15925);
        CoincidenceMatcher cm02LL = new CoincidenceMatcher(list0MarkeredLow, list2MarkeredLow, gate, 15925);

        CoincidenceMatcher cm03HH = new CoincidenceMatcher(list0MarkeredHigh, list3MarkeredHigh, gate, 10885);
        CoincidenceMatcher cm03HL = new CoincidenceMatcher(list0MarkeredHigh, list3MarkeredLow, gate, 10885);
        CoincidenceMatcher cm03LH = new CoincidenceMatcher(list0MarkeredLow, list3MarkeredHigh, gate, 10885);
        CoincidenceMatcher cm03LL = new CoincidenceMatcher(list0MarkeredLow, list3MarkeredLow, gate, 10885);

        CoincidenceMatcher cm12HH = new CoincidenceMatcher(list1MarkeredHigh, list2MarkeredHigh, gate, 26545);
        CoincidenceMatcher cm12HL = new CoincidenceMatcher(list1MarkeredHigh, list2MarkeredLow, gate, 26545);
        CoincidenceMatcher cm12LH = new CoincidenceMatcher(list1MarkeredLow, list2MarkeredHigh, gate, 26545);
        CoincidenceMatcher cm12LL = new CoincidenceMatcher(list1MarkeredLow, list2MarkeredLow, gate, 26545);

        CoincidenceMatcher cm13HH = new CoincidenceMatcher(list1MarkeredHigh, list3MarkeredHigh, gate, 21505);
        CoincidenceMatcher cm13HL = new CoincidenceMatcher(list1MarkeredHigh, list3MarkeredLow, gate, 21505);
        CoincidenceMatcher cm13LH = new CoincidenceMatcher(list1MarkeredLow, list3MarkeredHigh, gate, 21505);
        CoincidenceMatcher cm13LL = new CoincidenceMatcher(list1MarkeredLow, list3MarkeredLow, gate, 21505);

        int c02HH = cm02HH.find();
        int c02HL = cm02HL.find();
        int c02LH = cm02LH.find();
        int c02LL = cm02LL.find();

        int c03HH = cm03HH.find();
        int c03HL = cm03HL.find();
        int c03LH = cm03LH.find();
        int c03LL = cm03LL.find();

        int c12HH = cm12HH.find();
        int c12HL = cm12HL.find();
        int c12LH = cm12LH.find();
        int c12LL = cm12LL.find();

        int c13HH = cm13HH.find();
        int c13HL = cm13HL.find();
        int c13LH = cm13LH.find();
        int c13LL = cm13LL.find();

          //
//        c02HH = 44;
//        c02HL = 27;
//        c02LH = 56;
//        c02LL = 71;
//        c03HH = 8;
//        c03HL = 70;
//        c03LH = 18;
//        c03LL = 23;
//        c12HH = 19;
//        c12HL = 63;
//        c12LH = 9;
//        c12LL = 21;
//        c13HH = 65;
//        c13HL = 30;
//        c13LH = 69;
//        c13LL = 57;
        //
        System.out.println("--------------------------------------------------");
        System.out.println("CM02HH " + c02HH);
        System.out.println("CM02HL " + c02HL);
        System.out.println("CM02LH " + c02LH);
        System.out.println("CM02LL " + c02LL);
        System.out.println();
        System.out.println("CM03HH " + c03HH);
        System.out.println("CM03HL " + c03HL);
        System.out.println("CM03LH " + c03LH);
        System.out.println("CM03LL " + c03LL);
        System.out.println();
        System.out.println("CM12HH " + c12HH);
        System.out.println("CM12HL " + c12HL);
        System.out.println("CM12LH " + c12LH);
        System.out.println("CM12LL " + c12LL);
        System.out.println();
        System.out.println("CM13HH " + c13HH);
        System.out.println("CM13HL " + c13HL);
        System.out.println("CM13LH " + c13LH);
        System.out.println("CM13LL " + c13LL);
        System.out.println();
        System.out.println("--------------------------------------------------");

        System.out.println(c02HH + "\t" + c02HL + "\t" + c02LH + "\t" + c02LL + "\t"
                + c03HH + "\t" + c03HL + "\t" + c03LH + "\t" + c03LL + "\t"
                + c12HH + "\t" + c12HL + "\t" + c12LH + "\t" + c12LL + "\t"
                + c13HH + "\t" + c13HL + "\t" + c13LH + "\t" + c13LL);

        System.out.println("--------------------------------------------------");

        double eHH = E(c02HH, c13HH, c03HH, c12HH);
        double eHL = E(c02HL, c13HL, c03HL, c12HL);
        double eLH = E(c02LH, c13LH, c03LH, c12LH);
        double eLL = E(c02LL, c13LL, c03LL, c12LL);

        System.out.println(eHH);
        System.out.println(eHL);
        System.out.println(eLH);
        System.out.println(eLL);

        double s = Math.abs(-eHH + eHL - eLH - eLL);
        System.out.println("S = " + s);
//        System.out.println(list0MarkerHigh.size());
//        System.out.println(list1MarkerHigh.size() / (double) list0MarkerHigh.size());
//        Iterator<TimeEvent> iterator = markerList2.iterator();
//        while (iterator.hasNext()) {
//            TimeEvent timeEvent = iterator.next();
//            System.out.println(timeEvent.getChannel() + "\t" + timeEvent.getTime() / 1000000000.);
//        }
    }

    private static double E(double c11, double c22, double c12, double c21) {
        return (c11 + c22 - c12 - c21) / (c11 + c22 + c12 + c21);
    }
}

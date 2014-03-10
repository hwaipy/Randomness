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
public class ProcessTest {

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-01-12 单TDC采数尝试/201401121430.ht2");
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
        //Delay for Channel 1 & 3 is 21505ps1
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
        CoincidenceMatcher cm02High = new CoincidenceMatcher(list0MarkeredHigh, list2MarkeredHigh, 100, 15925);
        CoincidenceMatcher cm03High = new CoincidenceMatcher(list0MarkeredHigh, list3MarkeredHigh, 100, 10885);
        CoincidenceMatcher cm12High = new CoincidenceMatcher(list1MarkeredHigh, list2MarkeredHigh, 100, 26545);
        CoincidenceMatcher cm13High = new CoincidenceMatcher(list1MarkeredHigh, list3MarkeredHigh, 100, 21505);
        CoincidenceMatcher cm02Low = new CoincidenceMatcher(list0MarkeredLow, list2MarkeredLow, 100, 15925);
        CoincidenceMatcher cm03Low = new CoincidenceMatcher(list0MarkeredLow, list3MarkeredLow, 100, 10885);
        CoincidenceMatcher cm12Low = new CoincidenceMatcher(list1MarkeredLow, list2MarkeredLow, 100, 26545);
        CoincidenceMatcher cm13Low = new CoincidenceMatcher(list1MarkeredLow, list3MarkeredLow, 100, 21505);
        System.out.println("--------------------------------------------------");
        System.out.println("CM02High " + cm02High.find());
        System.out.println("CM02Low " + cm02Low.find());
        System.out.println("CM03High " + cm03High.find());
        System.out.println("CM03Low " + cm03Low.find());
        System.out.println("CM12High " + cm12High.find());
        System.out.println("CM12Low " + cm12Low.find());
        System.out.println("CM13High " + cm13High.find());
        System.out.println("CM13Low " + cm13Low.find());
        System.out.println("--------------------------------------------------");

//        System.out.println(list0MarkerHigh.size());
//        System.out.println(list1MarkerHigh.size() / (double) list0MarkerHigh.size());
//        Iterator<TimeEvent> iterator = markerList2.iterator();
//        while (iterator.hasNext()) {
//            TimeEvent timeEvent = iterator.next();
//            System.out.println(timeEvent.getChannel() + "\t" + timeEvent.getTime() / 1000000000.);
//        }
    }
}

package lightseconddataanalyzer.convert;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventLoader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.MergedTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.StandardPeriodTimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T3Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.pxi40ps1data.PXI40PS1Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.standardfile.NaiveFormat;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class NaiveConverter {

    public static void convert(TimeEventList input, File output) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(output, "rw");) {
            Iterator<TimeEvent> iterator = input.iterator();
            while (iterator.hasNext()) {
                TimeEvent timeEvent = iterator.next();
                long naiveFormat = NaiveFormat.naiveFormat(timeEvent);
                raf.writeLong(naiveFormat);
            }
        }
    }

    public static void main(String[] args) throws IOException, DeviceException {
        File pxiFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.dat");
        File caliFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/cali.csv");
        TimeEventLoader pxiLoader = new PXI40PS1Loader(pxiFile, caliFile);
        TimeEventSegment pxiSegment = TimeEventDataManager.loadTimeEventSegment(pxiLoader);
        File hhFile = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-12-03 光秒恢复状态采数/2014-03-25 双TDC同步测试/20140331002141TDC.ht3");
        TimeEventLoader hhLoader = new HydraHarp400T3Loader(hhFile, 100000);
        TimeEventSegment hhSegment = TimeEventDataManager.loadTimeEventSegment(hhLoader);

        TimeEventList pxiSyncList = pxiSegment.getEventList(0);
        TimeEventList pxiSignalList = pxiSegment.getEventList(1);
        TimeEventList hhSyncList = hhSegment.getEventList(0);
        TimeEventList hhSignalList = hhSegment.getEventList(1);
        System.out.println("PXI:\tSync " + pxiSyncList.size() + "\tSignal " + pxiSignalList.size());
        System.out.println("HH:\tSync " + hhSyncList.size() + "\tSignal " + hhSignalList.size());

        long pxiStartTime = pxiSyncList.get(0).getTime();
        long pxiEndTime = pxiSyncList.get(pxiSyncList.size() - 1).getTime();
        long pxiDurationSecond = (pxiEndTime - pxiStartTime) / 1000000000000l + 1;
        long hhStartTime = hhSyncList.get(1).getTime();
        long hhEndTime = hhSyncList.get(hhSyncList.size() - 1).getTime();
        long hhDurationSecond = (hhEndTime - hhStartTime) / 1000000000000l + 1;

        StandardPeriodTimeEventList pxiGPSList = new StandardPeriodTimeEventList(pxiStartTime, 1000000000000l, (int) pxiDurationSecond, 2);
        StandardPeriodTimeEventList hhGPSList = new StandardPeriodTimeEventList(hhStartTime, 1000000000000l, (int) hhDurationSecond, 2);

        MergedTimeEventList pxiList = new MergedTimeEventList(new MergedTimeEventList(pxiSyncList, pxiSignalList), pxiGPSList);
        MergedTimeEventList hhList = new MergedTimeEventList(new MergedTimeEventList(hhSyncList, hhSignalList), hhGPSList);

//        convert(pxiList, new File("pxi.tdc"));
//        convert(hhList, new File("hh.tdc"));
        check(pxiGPSList, pxiSyncList);
    }

    private static void check(TimeEventList gpsList, TimeEventList signalList) {
    }
}

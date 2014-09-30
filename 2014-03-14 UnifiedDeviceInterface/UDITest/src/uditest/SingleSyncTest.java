package uditest;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.SetableTimeEventListData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventListData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeevent.data.process.RecursionCoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeevent.data.process.TimeCalibrator;
import com.hwaipy.unifieddeviceinterface.timeevent.pxi.PXITimeEventDataFileLoaderFactory;
import java.io.File;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class SingleSyncTest {

    public static void main(String[] args) throws Exception {
        TimeEventDataManager.registerLoaderFactory("PXI", new PXITimeEventDataFileLoaderFactory());
        doPerform();
    }

    private static void doPerform() throws Exception {
        File tdcFile1 = new File("/Users/Hwaipy/Desktop/DAT/sim1/20140926172651-gnd1-01-hTDC.dat");
        File tdcFile2 = new File("/Users/Hwaipy/Desktop/DAT/sim1/GND2_01_HTDC.dat");
        TimeEventListData[] calibratedData1 = new TimeEventListData[4];
        TimeEventListData[] calibratedData2 = new TimeEventListData[4];

        //Load TDC file 1.
        TimeEventClusterData clusterData1 = TimeEventDataManager.loadTimeEventClusterData(tdcFile1, "PXI");

        //Load TDC file 2.
        TimeEventClusterData clusterData2 = TimeEventDataManager.loadTimeEventClusterData(tdcFile2, "PXI");

        System.out.println("1:\t" + clusterData1.getEventListData(1).get(0).getTimeInSecond());
        System.out.println("2:\t" + clusterData2.getEventListData(1).get(0).getTimeInSecond());
        System.out.println("GPS1:\t" + clusterData1.getEventListData(0).get(0).getTimeInSecond());
        System.out.println("GPS2:\t" + clusterData2.getEventListData(0).get(1).getTimeInSecond());
        System.out.println("SYNC1:\t" + clusterData1.getEventListData(5).get(0).getTimeInSecond());
        System.out.println("SYNC2:\t" + clusterData2.getEventListData(5).get(1).getTimeInSecond());

        Iterator<TimeEventData> iterator = clusterData2.getEventListData(5).iterator();
        TimeEventData lastData = iterator.next();
        while (iterator.hasNext()) {
            TimeEventData data = iterator.next();
            long timeDiff = data.getTime() - lastData.getTime();
            lastData = data;
            double diff = timeDiff / 100000000.;
            if (diff > 1.5 || diff < 0.5) {
                System.out.println("DIFF: " + diff);
            }
        }

        //GPS Calibration
        int gpsOffset = 1;
        long gpsGate = 1000000000;
        RecursionCoincidenceMatcher gpsRecursionCoincidenceMatcher
                = new RecursionCoincidenceMatcher(clusterData1.getEventListData(0), clusterData2.getEventListData(0),
                        gpsGate, gpsOffset);
        int gpsMatchCount = gpsRecursionCoincidenceMatcher.find();
        System.out.println("GPS Matched " + gpsMatchCount);
        for (int i = 0; i < 6; i++) {
            TimeCalibrator.calibrate(gpsRecursionCoincidenceMatcher, (SetableTimeEventListData) clusterData2.getEventListData(i));
        }

        System.out.println("GPS calied");
        System.out.println("1:\t" + clusterData1.getEventListData(1).get(0).getTimeInSecond());
        System.out.println("2:\t" + clusterData2.getEventListData(1).get(0).getTimeInSecond());
        System.out.println("GPS1:\t" + clusterData1.getEventListData(0).get(0).getTimeInSecond());
        System.out.println("GPS2:\t" + clusterData2.getEventListData(0).get(1).getTimeInSecond());
        System.out.println("SYNC1:\t" + clusterData1.getEventListData(5).get(0).getTimeInSecond());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            double time = clusterData2.getEventListData(5).get(i).getTimeInSecond();
            if (time > 0) {
                System.out.println("SYNC2:\t" + time);
                break;
            }
        }

        //Sync Calibration
        int syncOffset = 0;
        long syncGate = 1000000;

        RecursionCoincidenceMatcher syncRecursionCoincidenceMatcher
                = new RecursionCoincidenceMatcher(clusterData1.getEventListData(5), clusterData2.getEventListData(5),
                        syncGate, syncOffset);
        int syncMatchCount = syncRecursionCoincidenceMatcher.find();
        System.out.println("SYNC matched " + syncMatchCount);
        for (int i = 0; i < 5; i++) {
            TimeCalibrator.calibrate(syncRecursionCoincidenceMatcher, (SetableTimeEventListData) clusterData2.getEventListData(i));
        }

        System.out.println("SYNC calid");
        System.out.println("1:\t" + clusterData1.getEventListData(1).get(0).getTimeInSecond());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            double time = clusterData2.getEventListData(1).get(i).getTimeInSecond();
            if (time > 0) {
                System.out.println("2:\t" + time);
                break;
            }
        }
        System.out.println("GPS1:\t" + clusterData1.getEventListData(0).get(0).getTimeInSecond());
        System.out.println("GPS2:\t" + clusterData2.getEventListData(0).get(1).getTimeInSecond());
        System.out.println("SYNC1:\t" + clusterData1.getEventListData(5).get(0).getTimeInSecond());
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            double time = clusterData2.getEventListData(5).get(i).getTimeInSecond();
            if (time > 0) {
                System.out.println("SYNC2:\t" + time);
                break;
            }
        }

        //Coincidence
//            int coincidenceGate = getInt(coincidenceGateField);
//            int coincidenceScanRange = 1000 * getInt(coincidenceScanRangeField);
//            int coincidenceSteplength = getInt(coincidenceSteplengthField);
//            long[][] coincidences = new long[4][4];
//            long[][] delay = new long[4][4];
//            CoincidenceScaner scaner = new CoincidenceScaner();
//            System.out.println("1:\t" + clusterData1.getEventListData(1).get(0).getTime());
//            System.out.println("2:\t" + clusterData2.getEventListData(1).get(0).getTime());
//            for (int x = 0; x < 4; x++) {
//                for (int y = 0; y < 4; y++) {
//                    int[] scan = scaner.scan(clusterData1.getEventListData(x + 1), clusterData2.getEventListData(y + 1),
//                            coincidenceScanRange, coincidenceGate / coincidenceSteplength, coincidenceSteplength);
//                    coincidences[x][y] = scan[0];
//                    delay[x][y] = scan[1];
//                }
//            }
//            SwingUtilities.invokeLater((Runnable) () -> {
//                out.append("Coincidence scaned." + System.lineSeparator());
//                for (int x = 0; x < 4; x++) {
//                    for (int y = 0; y < 4; y++) {
//                        out.append("\tC" + (x + 1) + "&C" + (y + 1) + ":\t" + coincidences[x][y] + "\t@" + delay[x][y] + "ps" + System.lineSeparator());
//                    }
//                }
//            });
    }
}

package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *
 * @author Hwaipy
 */
public class FourFold {

    private static final int CHANNEL_I1 = 8;
    private static final int CHANNEL_I2 = 0;
    private static final int CHANNEL_T1 = 2;
    private static final int CHANNEL_T2 = 3;
    private static final long SINGLE_LENGTH = 100 * 1024 * 1024;
    private static final int HALF_GATE = 1500;

    public static void calc(File file) throws IOException, DeviceException {
        long length = file.length();
        StringBuilder sb = new StringBuilder();

        for (long position = 0; position < length; position += SINGLE_LENGTH) {
            long size = SINGLE_LENGTH;
            if (position + size > length) {
                size = length - position;
            }
            HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file, position, size);
            TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);

            TimeEventList signalListI1 = segment.getEventList(CHANNEL_I1);
            TimeEventList signalListI2 = segment.getEventList(CHANNEL_I2);
            TimeEventList signalListT1 = segment.getEventList(CHANNEL_T1);
            TimeEventList signalListT2 = segment.getEventList(CHANNEL_T2);

            scanDelay(signalListT2, signalListI2);

            CoincidenceMatcher cm11 = new CoincidenceMatcher(signalListT1, signalListI1, HALF_GATE, -89800);
            CoincidenceMatcher cm12 = new CoincidenceMatcher(signalListT1, signalListI2, HALF_GATE, 7600);
            CoincidenceMatcher cm21 = new CoincidenceMatcher(signalListT2, signalListI1, HALF_GATE, 0);
            CoincidenceMatcher cm22 = new CoincidenceMatcher(signalListT2, signalListI2, HALF_GATE, 897);
            int coincidence11 = cm11.find();
            int coincidence12 = cm12.find();
            int coincidence21 = cm21.find();
            int coincidence22 = cm22.find();
            CoincidenceEventList coincidenceEventList1 = new CoincidenceEventList(cm11, 1);
            CoincidenceEventList coincidenceEventList2 = new CoincidenceEventList(cm22, 1);
            CoincidenceMatcher cm4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, HALF_GATE, 700);
            int coincidence4 = cm4.find();

            long startTime = signalListT1.get(0).getTime();
            long endTime = signalListT1.get(signalListT1.size() - 1).getTime();
            long time = endTime - startTime;
            double timeInSecond = time / 1e12;
            double efficiency11 = coincidence11 / 1. / signalListI1.size();
            double efficiency12 = coincidence12 / 1. / signalListI2.size();
            double efficiency21 = coincidence21 / 1. / signalListI1.size();
            double efficiency22 = coincidence22 / 1. / signalListI2.size();
            double expectC4 = 1. * coincidence11 * coincidence22 / 76000000. / timeInSecond;

            //12500
            CoincidenceMatcher shiftedCM4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, HALF_GATE, 0);
            int[] shiftedCoincidence4 = new int[21];
            for (int i = -10; i < 11; i++) {
                long delay = 700 + i * 12500;
                shiftedCM4.setDelay(delay);
                shiftedCoincidence4[i + 10] = shiftedCM4.find();
            }

//            for (int delay = -200000; delay < 200000; delay += 1000) {
//                shiftedCM4.setDelay(delay);
//                System.out.println(delay + "\t" + shiftedCM4.find());
//            }
            sb.append(timeInSecond).append("\t")
                    .append(signalListI1.size()).append("\t")
                    .append(signalListI2.size()).append("\t")
                    .append(signalListT1.size()).append("\t")
                    .append(signalListT2.size()).append("\t")
                    .append(coincidence11).append("\t")
                    .append(coincidence12).append("\t")
                    .append(coincidence21).append("\t")
                    .append(coincidence22).append("\t")
                    .append(efficiency11).append("\t")
                    .append(efficiency12).append("\t")
                    .append(efficiency21).append("\t")
                    .append(efficiency22).append("\t")
                    .append(coincidence4).append("\t")
                    .append(expectC4).append("\t");
            for (int shiftedC4 : shiftedCoincidence4) {
                sb.append(shiftedC4).append("\t");
            }
            sb.append(System.lineSeparator());
            break;
        }
        String newFileName = file.getAbsolutePath() + ".ana";
        try (PrintWriter printWriter = new PrintWriter(newFileName)) {
            printWriter.println(sb.toString());
        }
        System.out.print(sb.toString());
    }

    private static void scanDelay(TimeEventList list1, TimeEventList list2) {
        CoincidenceMatcher cm = new CoincidenceMatcher(list1, list2, 50, 0);
        for (int delay = -200000; delay < 200000; delay += 100) {
            cm.setDelay(delay);
            System.out.println(delay + "\t" + cm.find());
        }
    }

    public static void main(String[] args) throws Exception {
        process();
    }

    private static void process() throws Exception {
        File dir = new File("/Users/Hwaipy/Desktop/ff/");
        File[] files = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".ht2");
            }
        });
        for (File file : files) {
            System.out.print(file.getName().split("\\.")[0] + "\t");
            calc(file);
        }
    }
}

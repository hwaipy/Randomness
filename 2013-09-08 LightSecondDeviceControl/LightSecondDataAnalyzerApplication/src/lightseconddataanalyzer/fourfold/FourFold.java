package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
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
    private static final long SINGLE_LENGTH = 128 * 1024 * 1024;

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

            CoincidenceMatcher cm11 = new CoincidenceMatcher(signalListT1, signalListI1, 2500, 10500);
            CoincidenceMatcher cm12 = new CoincidenceMatcher(signalListT1, signalListI2, 2500, 10500);
            CoincidenceMatcher cm21 = new CoincidenceMatcher(signalListT2, signalListI1, 2500, 10500);
            CoincidenceMatcher cm22 = new CoincidenceMatcher(signalListT2, signalListI2, 2500, 10500);
            int coincidence11 = cm11.find();
            int coincidence12 = cm12.find();
            int coincidence21 = cm21.find();
            int coincidence22 = cm22.find();

            CoincidenceEventList coincidenceEventList1 = new CoincidenceEventList(cm11, 1);
            CoincidenceEventList coincidenceEventList2 = new CoincidenceEventList(cm22, 1);
            CoincidenceMatcher cm4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, 2500, 0);
            int coincidence4 = cm4.find();

            long startTime = signalListT1.get(0).getTime();
            long endTime = signalListT1.get(signalListT1.size() - 1).getTime();
            long time = endTime - startTime;
            double timeInSecond = time / 1e12;
            double efficiency11 = coincidence11 / 1. / signalListI1.size();
            double efficiency12 = coincidence12 / 1. / signalListI2.size();
            double efficiency21 = coincidence21 / 1. / signalListI1.size();
            double efficiency22 = coincidence22 / 1. / signalListI2.size();
            double expectC4 = 1. * coincidence11 * coincidence22 / 76000000 / timeInSecond;
            CoincidenceMatcher shiftedCM4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, 2500, 13158);
            int shiftedCoincidence4 = shiftedCM4.find();

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
                    .append(expectC4).append("\t")
                    .append(shiftedCoincidence4).append("\t")
                    .append(System.lineSeparator());
        }
        String newFileName = file.getAbsolutePath() + ".ana";
        try (PrintWriter printWriter = new PrintWriter(newFileName)) {
            printWriter.println(sb.toString());
        }
        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Desktop/t.ph400");
        calc(file);
    }

}

package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.FileOutputStream;
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

    public static void calc(File file) throws IOException, DeviceException {
        System.out.println("Begin");
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        TimeEventSegment segment = TimeEventDataManager.loadTimeEventSegment(loader);
        System.out.println("Loaded");

        TimeEventList signalListI1 = segment.getEventList(CHANNEL_I1);
        TimeEventList signalListI2 = segment.getEventList(CHANNEL_I2);
        TimeEventList signalListT1 = segment.getEventList(CHANNEL_T1);
        TimeEventList signalListT2 = segment.getEventList(CHANNEL_T2);

        StringBuilder sb = new StringBuilder();
        sb.append(signalListI1.size()).append("\t")
                .append(signalListI2.size()).append("\t")
                .append(signalListT1.size()).append("\t")
                .append(signalListT2.size()).append("\t");

        CoincidenceScaner scaner11 = new CoincidenceScaner(signalListT1, signalListI1);
        CoincidenceScaner scaner12 = new CoincidenceScaner(signalListT1, signalListI2);
        CoincidenceScaner scaner21 = new CoincidenceScaner(signalListT2, signalListI1);
        CoincidenceScaner scaner22 = new CoincidenceScaner(signalListT2, signalListI2);
        int[] stat11 = scaner11.scan();
        int[] stat12 = scaner12.scan();
        int[] stat21 = scaner21.scan();
        int[] stat22 = scaner22.scan();
        for (int i = 0; i < stat11.length; i++) {
            sb.append(i).append("\t")
                    .append(stat11[i]).append("\t")
                    .append(stat12[i]).append("\t")
                    .append(stat21[i]).append("\t")
                    .append(stat22[i]).append(System.lineSeparator());
        }
        System.out.println(sb.toString());

        String newFileName = file.getAbsolutePath() + ".ana";
        PrintWriter printWriter = new PrintWriter(newFileName);
        printWriter.println(sb.toString());
        printWriter.close();
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Desktop/1416918446735.ph400");
        calc(file);
    }

}

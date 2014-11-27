package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.io.File;
import java.io.IOException;

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

        CoincidenceMatcher cm11 = new CoincidenceMatcher(signalListT1, signalListI1, 2500, 10500);
        CoincidenceMatcher cm12 = new CoincidenceMatcher(signalListT1, signalListI2, 2500, 10500);
        CoincidenceMatcher cm21 = new CoincidenceMatcher(signalListT2, signalListI1, 2500, 10500);
        CoincidenceMatcher cm22 = new CoincidenceMatcher(signalListT2, signalListI2, 2500, 10500);

        System.out.println(cm11.find());
        System.out.println(cm12.find());
        System.out.println(cm21.find());
        System.out.println(cm22.find());

//        String newFileName = file.getAbsolutePath() + ".ana";
//        PrintWriter printWriter = new PrintWriter(newFileName);
//        printWriter.println(sb.toString());
//        printWriter.close();
    }

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/Hwaipy/Desktop/t.ph400");
        calc(file);
    }

}

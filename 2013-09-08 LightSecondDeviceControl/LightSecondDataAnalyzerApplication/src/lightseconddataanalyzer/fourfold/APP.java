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
public class APP {

    //数据文件的目录
    private static final File dir = new File("/Users/Hwaipy/Desktop/ff2/");
    //数据文件的扩展名
    private static final String ext = "ht2";
//    private static final String ext = "hp400";
    //单次处理容量B
    private static final long SINGLE_LENGTH = 100 * 1024 * 1024;
    //通道定义  8代表同步，其他是0~3
    //I1 I2是干涉的两路，T1 T2是另外两路
    private static final int CHANNEL_I1 = 8;
    private static final int CHANNEL_I2 = 0;
    private static final int CHANNEL_T1 = 2;
    private static final int CHANNEL_T2 = 3;

    //任务1：扫延时,延时的定义：Channel1 + delay = Channel2
    private static final int SCAN_CHANNEL_1 = CHANNEL_T2;
    private static final int SCAN_CHANNEL_2 = CHANNEL_I2;
    private static final int DELAY_START = -1000;
    private static final int DELAY_STOP = 1000;
    private static final int SCAN_HALF_GATE = 100;
    private static final int SCAN_STEP_LENGTH = 200;

    //任务2：4体
    private static final int DELAY_T1_I1 = -100800;
    private static final int DELAY_T1_I2 = 1000;
    private static final int DELAY_T2_I1 = -101400;
    private static final int DELAY_T2_I2 = 200;
    private static final int HALF_GATE = 1500;
    private static final int PERIOD = 12500;

    private static void process(TimeEventSegment segment, StringBuilder sb) {
        //定义一个当前任务
/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
//        任务1：扫延时
   //    delayScan.process(segment, sb);
//        任务2：4体
        fourFold.process(segment, sb);
        /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    }

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
            process(segment, sb);
        }
        String newFileName = file.getAbsolutePath() + ".ana";
        try (PrintWriter printWriter = new PrintWriter(newFileName)) {
            printWriter.println(sb.toString());
        }
        System.out.print(sb.toString());
    }

    public static void main(String[] args) throws Exception {
        process();
    }

    private static void process() throws Exception {
        File[] files = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(ext);
            }
        });
        for (File file : files) {
            System.out.println(file.getName().split("\\.")[0] + "\t");
            calc(file);
        }
    }

    private interface Process {

        public void process(TimeEventSegment segment, StringBuilder sb);
    }

    private static final Process delayScan = new Process() {

        @Override
        public void process(TimeEventSegment segment, StringBuilder sb) {
            TimeEventList list1 = segment.getEventList(SCAN_CHANNEL_1);
            TimeEventList list2 = segment.getEventList(SCAN_CHANNEL_2);

            CoincidenceMatcher cm = new CoincidenceMatcher(list1, list2, SCAN_HALF_GATE, 0);
            for (int delay = DELAY_START; delay < DELAY_STOP; delay += SCAN_STEP_LENGTH) {
                cm.setDelay(delay);
                System.out.println(delay + "\t" + cm.find());
            }
        }
    };
    private static final Process fourFold = new Process() {

        @Override
        public void process(TimeEventSegment segment, StringBuilder sb) {
            TimeEventList signalListI1 = segment.getEventList(CHANNEL_I1);
            TimeEventList signalListI2 = segment.getEventList(CHANNEL_I2);
            TimeEventList signalListT1 = segment.getEventList(CHANNEL_T1);
            TimeEventList signalListT2 = segment.getEventList(CHANNEL_T2);

            CoincidenceMatcher cm11 = new CoincidenceMatcher(signalListT1, signalListI1, HALF_GATE, DELAY_T1_I1);
            CoincidenceMatcher cm12 = new CoincidenceMatcher(signalListT1, signalListI2, HALF_GATE, DELAY_T1_I2);
            CoincidenceMatcher cm21 = new CoincidenceMatcher(signalListT2, signalListI1, HALF_GATE, DELAY_T2_I1);
            CoincidenceMatcher cm22 = new CoincidenceMatcher(signalListT2, signalListI2, HALF_GATE, DELAY_T2_I2);
            int coincidence11 = cm11.find();
            int coincidence12 = cm12.find();
            int coincidence21 = cm21.find();
            int coincidence22 = cm22.find();
            CoincidenceEventList coincidenceEventList1 = new CoincidenceEventList(cm11, 1);
            CoincidenceEventList coincidenceEventList2 = new CoincidenceEventList(cm22, 1);
            int ffDelay = DELAY_T1_I1 - DELAY_T2_I1;
            CoincidenceMatcher cm4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, HALF_GATE, ffDelay);
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

            CoincidenceMatcher shiftedCM4 = new CoincidenceMatcher(coincidenceEventList1, coincidenceEventList2, HALF_GATE, 0);
            int[] shiftedCoincidence4 = new int[21];
            for (int i = -10; i < 11; i++) {
                long delay = ffDelay + i * PERIOD;
                shiftedCM4.setDelay(delay);
                shiftedCoincidence4[i + 10] = shiftedCM4.find();
            }

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
        }
    };
}

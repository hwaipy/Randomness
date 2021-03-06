package com.hwaipy.science.polarizationcontrol.approach;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class ApproachProcess5 {

    private final Random random;
    private double fiberTransformTheta1;
    private double fiberTransformTheta2;
    private double fiberTransformTheta3;
    private final double stepLength = 1. / 180 * Math.PI;
    private final int maxStepsCount = 10000;
    private final double innerThreshold = 0.9;
    private final double outerThreshold = 1.1;
    private final WavePlate[] wavePlates = new WavePlate[3];
    FiberTransform ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
    private int loggingLevel = -1;

    public ApproachProcess5() {
        random = new Random();
        wavePlates[0] = new WavePlate(Math.PI / 2, 0);
        wavePlates[1] = new WavePlate(Math.PI / 2, 0);
        wavePlates[2] = new WavePlate(Math.PI, 0);
    }

    public void generateRandomFiberTransform() {
        fiberTransformTheta1 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta2 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta3 = (random.nextDouble() - 0.5) * Math.PI;
        ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
    }

    private double[] contrasts() {
        return new double[]{contrastH(), contrastD()};
    }

    private double contrastH() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        double mHH = measurementH.getH();
        double mHV = measurementH.getV();
        return mHH / mHV;
    }

    private double contrastD() {
        Polarization measurementD = Polarization.D.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        double mDD = measurementD.getD();
        double mDA = measurementD.getA();
        return mDD / mDA;
    }

    private void logging(int level, String info, boolean status) {
        if (level > loggingLevel) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append("\t");
        }
        sb.append(info);
        if (status) {
            DecimalFormat format1 = new DecimalFormat("000.0");
            if (info != null && info.length() > 0) {
                sb.append("\t");
            }
            sb.append(format1.format(wavePlates[0].getTheta() / Math.PI * 180)).append(",")
                    .append(format1.format(wavePlates[1].getTheta() / Math.PI * 180)).append(",")
                    .append(format1.format(wavePlates[2].getTheta() / Math.PI * 180)).append(", ")
                    .append(format1.format(contrastH())).append(",")
                    .append(format1.format(contrastD())).append(",");
        }
        System.out.println(sb.toString());
    }

    public ApproachProcess5.ApproachResult approach() {
        logging(0, "Start Approching", false);
        logging(0, "", true);
        int left = rotate();
        logging(0, "End Approching", false);
        logging(0, "", true);
        return new ApproachProcess5.ApproachResult(left > 0,
                fiberTransformTheta1, fiberTransformTheta2, fiberTransformTheta3,
                contrastH(), contrastD(), maxStepsCount - left);
    }

    private int rotate() {
        int rotationCountMax = maxStepsCount;
        while (rotationCountMax > 0) {
            if (contrastH() > 300 && contrastD() > 300) {
                break;
            }
            logging(0, "Start RotateCollective @ H", false);
            logging(0, "", true);
            rotationCountMax = rotateCollective(0, 0, rotationCountMax);
            logging(0, "End RotateCollective @ H", false);
            logging(0, "", true);
            logging(0, "Start RotateCollective @ D", false);
            logging(0, "", true);
            rotationCountMax = rotateCollective(1, 1, rotationCountMax);
            logging(0, "End RotateCollective @ D", false);
            logging(0, "", true);
        }
        return rotationCountMax;
    }

    private int rotateCollective(int index, int contrast, int rotationCountMax) {
        recordWavePlatesStatus();
        rotationCountMax = doRotateCollective(index, false, contrast, rotationCountMax);
//        logging(1, "WavePlates Status Checkpoint", false);
        if (wavePlatesStatusUnchanged()) {
            wavePlates[1 - index].increase(Math.PI * 2 * random.nextDouble());
//            wavePlates[index].increase(Math.PI / 2);
//            logging(1, "HWP increased Pi/2", false);
            rotationCountMax = doRotateCollective(index, true, contrast, rotationCountMax);
        }
        return rotationCountMax;
    }

    private int doRotateCollective(int index, boolean hwpFirst, int contrast, int rotationCountMax) {
        int indexSum = index + 2;
        if (hwpFirst) {
            index = 2;
        }
        double lastContrast = -1;
        while (rotationCountMax > 0) {
            logging(1, "Start RotateInner " + index, false);
            logging(1, "", true);
            rotationCountMax = rotateInner(index, contrast, rotationCountMax);
            logging(1, "End RotateInner " + index, false);
            logging(1, "", true);
            double c = contrasts()[contrast];
            if (lastContrast >= 0 && c < lastContrast * outerThreshold) {
                break;
            }
            lastContrast = c;
            index = indexSum - index;
        }
        return rotationCountMax;
    }

    private int rotateInner(int index, int contrast, int rotationCountMax) {
        double step = stepLength;
        double c1 = contrasts()[contrast];
        logging(2, "Detected Contrast 1:", false);
        logging(2, "", true);
        wavePlates[index].increase(step);
        double c2 = contrasts()[contrast];
        logging(2, "Detected Contrast 2:", false);
        logging(2, "", true);
        double cMax = c2;
        double thetaMax = wavePlates[index].getTheta();
        if (c2 < c1) {
            step = -step;
            logging(2, "Reverse", false);
        }
        while (rotationCountMax > 0) {
            wavePlates[index].increase(step);
            logging(2, "Steped", false);
            logging(2, "", true);
            rotationCountMax--;
            double c = contrasts()[contrast];
            if (c > cMax) {
                cMax = c;
                thetaMax = wavePlates[index].getTheta();
                logging(2, "New Max", false);
            }
            if (c < cMax * innerThreshold) {
                break;
            }
        }
        wavePlates[index].setTheta(thetaMax);
        return rotationCountMax;
    }

    private double[] status = new double[3];

    private void recordWavePlatesStatus() {
        status = new double[]{wavePlates[0].getTheta(), wavePlates[1].getTheta(), wavePlates[2].getTheta()};
    }

    private boolean wavePlatesStatusUnchanged() {
        double threshold = stepLength / 10;
        return Math.abs(status[0] - wavePlates[0].getTheta()) < threshold
                && Math.abs(status[1] - wavePlates[1].getTheta()) < threshold
                && Math.abs(status[2] - wavePlates[2].getTheta()) < threshold;
    }

    public class ApproachResult {

        private final boolean success;
        private final double fiberTheta1;
        private final double fiberTheta2;
        private final double fiberTheta3;
        private final double cH;
        private final double cD;
        private final int stepCount;

        public ApproachResult(boolean success,
                double fiberTheta1, double fiberTheta2, double fiberTheta3,
                double cH, double cD, int stepCount) {
            this.success = success;
            this.fiberTheta1 = fiberTheta1;
            this.fiberTheta2 = fiberTheta2;
            this.fiberTheta3 = fiberTheta3;
            this.cH = cH;
            this.cD = cD;
            this.stepCount = stepCount;
        }
    }

    /**
     * Total: 10000 Success: 9066 MeanStep: 681.6668872711228
     *
     * @param args
     */
    public static void main(String[] args) {
        ApproachProcess5 p = new ApproachProcess5();
        p.loggingLevel = -1;
        int count = 0;
        int overallCount = 0;
        int successCount = 0;
        int overallSuccessSteps = 0;
        for (int i = 0; i < 10000; i++) {
            p.generateRandomFiberTransform();
            ApproachProcess5.ApproachResult result = p.approach();
            overallCount++;
            if (result.success) {
                successCount++;
                overallSuccessSteps += result.stepCount;
            }
            if (i % 100 == 0) {
                System.out.println((i / 100) + "%");
            }
        }
        System.out.println("Total: " + overallCount);
        System.out.println("Success: " + successCount);
        System.out.println("MeanStep: " + ((double) overallSuccessSteps) / successCount);
    }
}

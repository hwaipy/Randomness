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
public class ApproachProcessContrast {

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

    public ApproachProcessContrast() {
        random = new Random(100);
        wavePlates[0] = new WavePlate(Math.PI / 2, 0);
        wavePlates[1] = new WavePlate(Math.PI / 2, 0);
        wavePlates[2] = new WavePlate(Math.PI, 0);
    }

    public void generateRandomFiberTransform() {
        ft = FiberTransform.createRandomFiber(random);
    }

    public void setFiberTransform(double theta1, double theta2, double theta3) {
        fiberTransformTheta1 = theta1;
        fiberTransformTheta2 = theta2;
        fiberTransformTheta3 = theta3;
        ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
    }

    public ApproachResult approach() {
        logging(0, "Start Approching", false);
        logging(0, "", true);
        int left = rotate();
        logging(0, "End Approching", false);
        logging(0, "", true);
        return new ApproachResult(left > 0,
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
//        if (wavePlatesStatusUnchanged()) {
//            wavePlates[index].increase(Math.PI / 2);
//            logging(1, "HWP increased Pi/2", false);
//            rotationCountMax = doRotateCollective(index, true, contrast, rotationCountMax);
//        }
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

    private int rotateBoth(int index, int contrast, boolean reverse, int rotationCountMax) {
        double step = stepLength;
        double c1 = contrasts()[contrast];
        logging(2, "Detected Contrast 1:", false);
        logging(2, "", true);
        wavePlates[index].increase(step);
        wavePlates[2].increase(reverse ? -step : step);
        double c2 = contrasts()[contrast];
        logging(2, "Detected Contrast 2:", false);
        logging(2, "", true);
        double cMax = c2;
        double thetaMaxI = wavePlates[index].getTheta();
        double thetaMax2 = wavePlates[2].getTheta();
        if (c2 < c1) {
            step = -step;
            logging(2, "Reverse", false);
        }
        while (rotationCountMax > 0) {
            wavePlates[index].increase(step);
            wavePlates[2].increase(reverse ? -step : step);
            logging(2, "Steped", false);
            logging(2, "", true);
            rotationCountMax--;
            double c = contrasts()[contrast];
            if (c > cMax) {
                cMax = c;
                thetaMaxI = wavePlates[index].getTheta();
                thetaMax2 = wavePlates[2].getTheta();
                logging(2, "New Max", false);
            }
            if (c < cMax * innerThreshold) {
                break;
            }
        }
        wavePlates[index].setTheta(thetaMaxI);
        wavePlates[2].setTheta(thetaMax2);
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

    public static void main(String[] args) {
        ApproachProcessContrast p = new ApproachProcessContrast();
        p.loggingLevel = 5;
        int count = 0;
        p.generateRandomFiberTransform();
        ApproachProcessContrast.ApproachResult result = p.approach();
        System.out.println(result.success);
        System.out.println(result.cH);
        System.out.println(result.cD);
    }
}

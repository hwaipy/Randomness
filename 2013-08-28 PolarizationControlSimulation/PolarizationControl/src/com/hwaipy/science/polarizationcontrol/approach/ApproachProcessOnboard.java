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
public class ApproachProcessOnboard {

    private final Random random;
    private FiberTransform ft = FiberTransform.createRandomFiber(new Random());
    private final WavePlate[] wavePlates = new WavePlate[3];
    private LoggingLevel loggingLevel = LoggingLevel.DEBUG;
    private Polarization laser = Polarization.ZERO;

    public ApproachProcessOnboard() {
        random = new Random();
        wavePlates[0] = new WavePlate(Math.PI / 2, 0);
        wavePlates[1] = new WavePlate(Math.PI / 2, 0);
        wavePlates[2] = new WavePlate(Math.PI, 0);
    }

    public ApproachProcessOnboard(long seed) {
        random = new Random(seed);
        wavePlates[0] = new WavePlate(Math.PI / 2, 0);
        wavePlates[1] = new WavePlate(Math.PI / 2, 0);
        wavePlates[2] = new WavePlate(Math.PI, 0);
    }

    public void generateRandomFiberTransform() {
        ft = FiberTransform.createRandomFiber(random);
    }

    public void operationOpenH() {
        laser = Polarization.H;
    }

    public void operationCloseH() {
        laser = Polarization.ZERO;
    }

    public void operationOpenD() {
        laser = Polarization.D;
    }

    public void operationCloseD() {
        laser = Polarization.ZERO;
    }

    public double[] readPower() {
        Polarization measurement = laser.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{measurement.getH(), measurement.getV(), measurement.getD(), measurement.getA()};
    }

    public double constract() {
        double[] powers = readPower();
        if (Polarization.H.equals(laser)) {
            return powers[0] / powers[1];
        }
        if (Polarization.D.equals(laser)) {
            return powers[2] / powers[3];
        }
        return 0;
    }

    private void logging(LoggingLevel level, String message) {
        if (level.compareTo(loggingLevel) > 0) {
            return;
        }
        System.out.println("[" + level + "]" + message);
    }

    private enum LoggingLevel {

        ERROR, DEBUG, INFO, DETAIL
    }

    public ApproachResult approach(int maxCount, double thresholdOuter, double thresholdInner, double stepLength) {
        int left = rotate(maxCount, thresholdOuter, thresholdInner, stepLength);
        return new ApproachResult(left > 0, _contrastH(), _contrastD(), maxCount - left);
    }

    private int rotate(int maxCount, double thresholdOuter, double thresholdInner, double stepLength) {
        int rotationCountRemaining = maxCount;
        double validConstractH = 0;
        double validConstractD = 0;
        while (rotationCountRemaining > 0) {
            operationOpenH();
            validConstractH = constract();
            if (validConstractH >= 300 && validConstractD >= 300) {
                break;
            }
            if (validConstractH < 300) {
                rotationCountRemaining = rotateOuter(rotationCountRemaining, 0, thresholdOuter, thresholdInner, stepLength);
            }
            validConstractH = constract();
            operationCloseH();
            operationOpenD();
            validConstractD = constract();
            if (validConstractH >= 300 && validConstractD >= 300) {
                break;
            }
            if (validConstractD < 300) {
                rotationCountRemaining = rotateOuter(rotationCountRemaining, 1, thresholdOuter, thresholdInner, stepLength);
            }
            validConstractD = constract();
            operationCloseD();
        }
        return rotationCountRemaining;
    }

    private int rotateOuter(int maxCount, int qwpIndex, double thresholdOuter, double thresholdInner, double stepLength) {
        int rotationCountRemaining = maxCount;

        return rotationCountRemaining;
    }
    ////////////////////////////////////////////////////////////////////////////
    private final double stepLength = 1. / 180 * Math.PI;
    private final double innerThreshold = 0.9;
    private final double outerThreshold = 1.1;
    private int loggingLevelO = -1;

    private int _rotateCollective(int index, int contrast, int rotationCountMax) {
        _recordWavePlatesStatus();
        rotationCountMax = _doRotateCollective(index, false, contrast, rotationCountMax);
//        logging(1, "WavePlates Status Checkpoint", false);
//        if (wavePlatesStatusUnchanged()) {
//            wavePlates[index].increase(Math.PI / 2);
//            logging(1, "HWP increased Pi/2", false);
//            rotationCountMax = doRotateCollective(index, true, contrast, rotationCountMax);
//        }
        return rotationCountMax;
    }

    private int _doRotateCollective(int index, boolean hwpFirst, int contrast, int rotationCountMax) {
        int indexSum = index + 2;
        if (hwpFirst) {
            index = 2;
        }
        double lastContrast = -1;
        while (rotationCountMax > 0) {
            _logging(1, "Start RotateInner " + index, false);
            _logging(1, "", true);
            rotationCountMax = _rotateInner(index, contrast, rotationCountMax);
            _logging(1, "End RotateInner " + index, false);
            _logging(1, "", true);
            double c = _contrasts()[contrast];
            if (lastContrast >= 0 && c < lastContrast * outerThreshold) {
                break;
            }
            lastContrast = c;
            index = indexSum - index;
        }
        return rotationCountMax;
    }

    private int _rotateBoth(int index, int contrast, boolean reverse, int rotationCountMax) {
        double step = stepLength;
        double c1 = _contrasts()[contrast];
        _logging(2, "Detected Contrast 1:", false);
        _logging(2, "", true);
        wavePlates[index].increase(step);
        wavePlates[2].increase(reverse ? -step : step);
        double c2 = _contrasts()[contrast];
        _logging(2, "Detected Contrast 2:", false);
        _logging(2, "", true);
        double cMax = c2;
        double thetaMaxI = wavePlates[index].getTheta();
        double thetaMax2 = wavePlates[2].getTheta();
        if (c2 < c1) {
            step = -step;
            _logging(2, "Reverse", false);
        }
        while (rotationCountMax > 0) {
            wavePlates[index].increase(step);
            wavePlates[2].increase(reverse ? -step : step);
            _logging(2, "Steped", false);
            _logging(2, "", true);
            rotationCountMax--;
            double c = _contrasts()[contrast];
            if (c > cMax) {
                cMax = c;
                thetaMaxI = wavePlates[index].getTheta();
                thetaMax2 = wavePlates[2].getTheta();
                _logging(2, "New Max", false);
            }
            if (c < cMax * innerThreshold) {
                break;
            }
        }
        wavePlates[index].setTheta(thetaMaxI);
        wavePlates[2].setTheta(thetaMax2);
        return rotationCountMax;
    }

    private int _rotateInner(int index, int contrast, int rotationCountMax) {
        double step = stepLength;
        double c1 = _contrasts()[contrast];
        _logging(2, "Detected Contrast 1:", false);
        _logging(2, "", true);
        wavePlates[index].increase(step);
        double c2 = _contrasts()[contrast];
        _logging(2, "Detected Contrast 2:", false);
        _logging(2, "", true);
        double cMax = c2;
        double thetaMax = wavePlates[index].getTheta();
        if (c2 < c1) {
            step = -step;
            _logging(2, "Reverse", false);
        }
        while (rotationCountMax > 0) {
            wavePlates[index].increase(step);
            _logging(2, "Steped", false);
            _logging(2, "", true);
            rotationCountMax--;
            double c = _contrasts()[contrast];
            if (c > cMax) {
                cMax = c;
                thetaMax = wavePlates[index].getTheta();
                _logging(2, "New Max", false);
            }
            if (c < cMax * innerThreshold) {
                break;
            }
        }
        wavePlates[index].setTheta(thetaMax);
        return rotationCountMax;
    }
    private double[] status = new double[3];

    private void _recordWavePlatesStatus() {
        status = new double[]{wavePlates[0].getTheta(), wavePlates[1].getTheta(), wavePlates[2].getTheta()};
    }

    private boolean _wavePlatesStatusUnchanged() {
        double threshold = stepLength / 10;
        return Math.abs(status[0] - wavePlates[0].getTheta()) < threshold
                && Math.abs(status[1] - wavePlates[1].getTheta()) < threshold
                && Math.abs(status[2] - wavePlates[2].getTheta()) < threshold;
    }

    private double[] _contrasts() {
        return new double[]{_contrastH(), _contrastD()};
    }

    private double _contrastH() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        double mHH = measurementH.getH();
        double mHV = measurementH.getV();
        return mHH / mHV;
    }

    private double _contrastD() {
        Polarization measurementD = Polarization.D.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        double mDD = measurementD.getD();
        double mDA = measurementD.getA();
        return mDD / mDA;
    }

    private void _logging(int level, String info, boolean status) {
        if (level > loggingLevelO) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(info);
        if (status) {
            DecimalFormat format1 = new DecimalFormat("000.0");
            if (info != null && info.length() > 0) {
                sb.append("\t");
            }
            sb.append(format1.format(wavePlates[0].getTheta() / Math.PI * 180)).append(",")
                    .append(format1.format(wavePlates[1].getTheta() / Math.PI * 180)).append(",")
                    .append(format1.format(wavePlates[2].getTheta() / Math.PI * 180)).append(", ")
                    .append(format1.format(_contrastH())).append(",")
                    .append(format1.format(_contrastD())).append(",");
        }
        System.out.println(sb.toString());
    }

    public class ApproachResult {

        private final boolean success;
        private final double cH;
        private final double cD;
        private final int stepCount;

        public ApproachResult(boolean success, double cH, double cD, int stepCount) {
            this.success = success;
            this.cH = cH;
            this.cD = cD;
            this.stepCount = stepCount;
        }
    }

    public static void main(String[] args) {
        ApproachProcessOnboard p = new ApproachProcessOnboard(100);
        p.generateRandomFiberTransform();
        ApproachProcessOnboard.ApproachResult result = p.approach(10000, 1.1, 0.9);
//        System.out.println(result.success);
//        System.out.println(result.cH);
//        System.out.println(result.cD);
    }
}

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

    public double contrast() {
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

    private void loggingPower() {
        StringBuilder sb = new StringBuilder();
        if (Polarization.H.equals(laser)) {
            sb.append("H,");
        } else if (Polarization.D.equals(laser)) {
            sb.append("D,");
        } else {
            throw new RuntimeException();
        }
        DecimalFormat formatPower = new DecimalFormat("0.000");
        double[] powers = readPower();
        for (double power : powers) {
            sb.append(formatPower.format(power)).append(",");
        }
        logging(LoggingLevel.DEBUG, sb.substring(0, sb.length() - 1));
    }

    private void loggingWaveplates() {
        StringBuilder sb = new StringBuilder();
        DecimalFormat formatWaveplates = new DecimalFormat("0.000");
        for (WavePlate wavePlate : wavePlates) {
            sb.append(formatWaveplates.format(wavePlate.getTheta() / Math.PI * 180)).append(",");
        }
        logging(LoggingLevel.DEBUG, sb.substring(0, sb.length() - 1));
    }

    private void wavePlateRotate(int wpIndex, double delta) {
        loggingPower();
        wavePlates[wpIndex].increase(delta);
        loggingWaveplates();
    }

    private void wavePlateSet(int wpIndex, double theta) {
        loggingPower();
        wavePlates[wpIndex].setTheta(theta);
        loggingWaveplates();
    }

    public ApproachResult approach(int maxCount, double thresholdOuter, double thresholdInner, double stepLength) {
        return rotate(maxCount, thresholdOuter, thresholdInner, stepLength);
    }

    private ApproachResult rotate(int maxCount, double thresholdOuter, double thresholdInner, double stepLength) {
        loggingWaveplates();
        int rotationCountRemaining = maxCount;
        double validConstractH = 0;
        double validConstractD = 0;
        while (rotationCountRemaining > 0) {
            operationOpenH();
            validConstractH = contrast();
            if (validConstractH >= 300 && validConstractD >= 300) {
                break;
            }
            if (validConstractH < 300) {
                rotationCountRemaining = rotateOuter(rotationCountRemaining, 0, thresholdOuter, thresholdInner, stepLength);
            }
            validConstractH = contrast();
            operationCloseH();
            operationOpenD();
            validConstractD = contrast();
            if (validConstractH >= 300 && validConstractD >= 300) {
                break;
            }
            if (validConstractD < 300) {
                rotationCountRemaining = rotateOuter(rotationCountRemaining, 1, thresholdOuter, thresholdInner, stepLength);
            }
            validConstractD = contrast();
            operationCloseD();
        }
        return new ApproachResult(rotationCountRemaining > 0, validConstractH, validConstractD, maxCount - rotationCountRemaining);
    }

    private int rotateOuter(int maxCount, int qwpIndex, double thresholdOuter, double thresholdInner, double stepLength) {
        int rotationCountRemaining = maxCount;
        int wpIndexSum = qwpIndex + 2;
        int wpIndex = qwpIndex;
        double lastContrast = 0;
        double[] stepLengthes = new double[]{stepLength, stepLength, stepLength};
        while (rotationCountRemaining > 0) {
            Object[] result = rotateSingle(rotationCountRemaining, wpIndex, stepLengthes[wpIndex], thresholdInner);
            rotationCountRemaining = (int) result[0];
            stepLengthes[wpIndex] = (double) result[1];
            double contrast = contrast();
            if (contrast < lastContrast * thresholdOuter) {
                break;
            }
            lastContrast = contrast;
            wpIndex = wpIndexSum - wpIndex;
        }
        return rotationCountRemaining;
    }

    private Object[] rotateSingle(int maxCount, int wpIndex, double stepLength, double threshold) {
        int rotationCountRemaining = maxCount;
        if (rotationCountRemaining <= 0) {
            return new Object[]{rotationCountRemaining, stepLength};
        }
        double contrast1 = contrast();
        wavePlateRotate(wpIndex, stepLength);
        rotationCountRemaining--;
        double contrast2 = contrast();
        boolean reverse = contrast2 < contrast1;
        double maxContrast = contrast1 > contrast2 ? contrast1 : contrast2;
        double maxTheta = contrast1 > contrast2 ? (wavePlates[wpIndex].getTheta() - stepLength) : wavePlates[wpIndex].getTheta();
        while (rotationCountRemaining > 0) {
            if (reverse) {
                stepLength = -stepLength;
                wavePlateRotate(wpIndex, 2 * stepLength);
                reverse = false;
            } else {
                wavePlateRotate(wpIndex, stepLength);
            }
            rotationCountRemaining--;
            double contrast = contrast();
            if (contrast > maxContrast) {
                maxContrast = contrast;
                maxTheta = wavePlates[wpIndex].getTheta();
            }
            if (contrast < maxContrast * threshold) {
                break;
            }
        }
        wavePlateSet(wpIndex, maxTheta);
        return new Object[]{rotationCountRemaining, stepLength};
    }

    public class ApproachResult {

        private final boolean success;
        private final double contrastH;
        private final double contrastD;
        private final int stepCount;

        public ApproachResult(boolean success, double contrastH, double contrastD, int stepCount) {
            this.success = success;
            this.contrastH = contrastH;
            this.contrastD = contrastD;
            this.stepCount = stepCount;
        }
    }

    public static void main(String[] args) {
        ApproachProcessOnboard p = new ApproachProcessOnboard(0);
        p.generateRandomFiberTransform();
        ApproachProcessOnboard.ApproachResult result = p.approach(10000, 1.1, 0.9, 1. / 180 * Math.PI);
        System.out.println(result.success);
        System.out.println(result.contrastH);
        System.out.println(result.contrastD);
        System.out.println(result.stepCount);
    }
}

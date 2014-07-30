package com.hwaipy.science.polarizationcontrol.approach;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.io.File;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private int validNumber = 4;

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

    public double fixPower(double power) {
        double accu = Math.pow(10, validNumber);
        long p = (long) (power * accu);
        return p / accu;
//        return power;
    }

    public double[] readPower() {
        Polarization measurement = laser.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{
            fixPower(measurement.getH()),
            fixPower(measurement.getV()),
            fixPower(measurement.getD()),
            fixPower(measurement.getA())};
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
//        System.out.println("[" + level + "]" + message);
        System.out.println(message);
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
        DecimalFormat formatPower = new DecimalFormat("0.0000");
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
//        int wpIndexSum = qwpIndex + 2;
//        int wpIndex = qwpIndex;
        double lastContrast = 0;
        double[] stepLengthes = new double[]{stepLength, stepLength, stepLength};
        while (rotationCountRemaining > 0) {
            Object[] resultQWP = rotateSingle(rotationCountRemaining, qwpIndex, stepLengthes[qwpIndex], thresholdInner);
            rotationCountRemaining = (int) resultQWP[0];
            stepLengthes[qwpIndex] = (double) resultQWP[1];
            Object[] resultHWP = rotateSingle(rotationCountRemaining, 2, stepLengthes[2], thresholdInner);
            rotationCountRemaining = (int) resultHWP[0];
            stepLengthes[2] = (double) resultHWP[1];
            double contrast = contrast();
//            System.out.println("\t\t\t\t\tRotated, contrast = " + contrast + ", last contrast = " + lastContrast);
            if (contrast < lastContrast * thresholdOuter) {
//                System.out.println("\t\t\t\t\tbreak!");
                break;
            }
            lastContrast = contrast;
//            wpIndex = wpIndexSum - wpIndex;
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
//        int succeedHigh = 0;
//        int succeedLow = 0;
//        for (int i = 0; i < 1000; i++) {
//            ApproachProcessOnboard p = new ApproachProcessOnboard(i * 12000);
//            p.validNumber = 10;
//            p.generateRandomFiberTransform();
//            ApproachProcessOnboard.ApproachResult resultHigh = p.approach(1024, 1.1, 0.9, 1. / 180 * Math.PI);
//            if (resultHigh.success) {
//                succeedHigh++;
//            }
//            p = new ApproachProcessOnboard(i * 12000);
//            p.validNumber = 4;
//            p.generateRandomFiberTransform();
//            ApproachProcessOnboard.ApproachResult resultLow = p.approach(1024, 1.1, 0.9, 1. / 180 * Math.PI);
//            if (resultLow.success) {
//                succeedLow++;
//            }
//            if (resultHigh.success != resultLow.success) {
//                System.out.println(resultHigh.success + ", " + resultLow.success);
//            }
//        }
//        System.out.println(succeedHigh);
//        System.out.println(succeedLow);
        outputSingle(args);
    }

    public static void outputSingle(String[] args) {
        try (PrintStream out = new PrintStream(new File("test.txt"))) {
            long randomSeed = 1000;
            int maxCount = 10000;
            double thresholdOuter = 1.1;
            double thresholdInner = 0.9;
            double stepLength = 1;
            if (args != null && args.length > 0) {
                randomSeed = Long.parseLong(args[0]);
                maxCount = Integer.parseInt(args[1]);
                thresholdOuter = Double.parseDouble(args[2]);
                thresholdInner = Double.parseDouble(args[3]);
                stepLength = Double.parseDouble(args[4]);
                System.setOut(out);
            }
            ApproachProcessOnboard p = new ApproachProcessOnboard(randomSeed);
            p.generateRandomFiberTransform();
            ApproachProcessOnboard.ApproachResult result = p.approach(maxCount, thresholdOuter, thresholdInner, stepLength / 180 * Math.PI);
            System.out.println("Step " + result.stepCount);
            System.out.println("Contrast H: " + result.contrastH);
            System.out.println("Contrast D: " + result.contrastD);
        } catch (Exception ex) {
            Logger.getLogger(ApproachProcessOnboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

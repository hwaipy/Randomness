package com.hwaipy.science.polarizationcontrol.approach.annealing;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class AnnealingTest {

    private Random random = new Random();
    private FiberTransform ft = FiberTransform.createRandomFiber(random);
    private WavePlate[] wavePlates = new WavePlate[]{new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI, 0)};
    private final double r;
    private final double jC;

    public AnnealingTest(double r, double jC) {
        this.r = r;
        this.jC = jC;
    }

    public void process() {
        double T = 6000;
        double Tmin = 25;
        double lastJ = J();
        double J = J();
        double stepCount = 0;
        while (T > Tmin) {
            move();
            lastJ = J;
            J = J();
            double dE = J - lastJ;
            if (dE > 0) {
            } else {
                if (Math.exp((dE / T)) > random.nextDouble()) {
                } else {
                    rollback();
                }
            }
            T = r * T;
            stepCount++;
        }
    }

    private double[] preservedThetas = new double[3];

    private void move() {
        for (int i = 0; i < 3; i++) {
            preservedThetas[i] = wavePlates[i].getTheta();
        }
        int indexOfMove;
        float rf = random.nextFloat();
        if (rf < 1. / 3) {
            indexOfMove = 0;
        } else if (rf < 2. / 3) {
            indexOfMove = 1;
        } else {
            indexOfMove = 2;
        }
        double stepLength = (random.nextDouble() * 4 - 2) / 180 * Math.PI;
        wavePlates[indexOfMove].increase(stepLength);
    }

    private void rollback() {
        for (int i = 0; i < 3; i++) {
            wavePlates[i].setTheta(preservedThetas[i]);
        }
    }

    private double[] readPower() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        Polarization measurementD = Polarization.D.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{
            measurementH.getH(), measurementH.getV(), measurementH.getD(), measurementH.getA(),
            measurementD.getH(), measurementD.getV(), measurementD.getD(), measurementD.getA()};
    }

    private double J() {
        return jointContrastDB() * jC;
    }

    private double jointContrastDB() {
        double jointContrast = jointContrast();
        double db = Math.log10(jointContrast);
        return db * 10;
    }

    private double jointContrast() {
        double[] powers = readPower();
        double contrastH = powers[0] / powers[1];
        double contrastD = powers[6] / powers[7];
        double v = Math.sqrt(1 / contrastH / contrastH + 1 / contrastD / contrastD);
        return 1 / v;
    }

    public static void main(String[] args) {
        for (double r = 0.950; r < 1; r += 0.001) {
            System.out.println(r);
            for (double jc = 1; jc < 1000000; jc *= 2) {
                testRound(r, jc, 100);
            }
        }
    }

    private static void testRound(double r, double jC, int count) {
        double resultSum = 0;
        double resultMin = 150;
        for (int i = 0; i < count; i++) {
            AnnealingTest annealingTest = new AnnealingTest(r, jC);
            annealingTest.process();
            double result = annealingTest.jointContrastDB();
            resultSum += result;
            if (result < resultMin) {
                resultMin = result;
            }
        }
        double resultMean = resultSum / count;
        if (resultMean > 20) {
            System.out.println("R=" + r + "\tJC=" + jC + "\tmean=" + (resultSum / count) + "\tmin=" + resultMin);
        }
    }
}

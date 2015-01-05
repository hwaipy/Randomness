package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class Simulation {

    private final Random random;
    private final PolarizationControl m6PC;
    private final PolarizationControl m24PC;
    private double fiberTransformTheta1;
    private double fiberTransformTheta2;
    private double fiberTransformTheta3;
    private final MeasurementFactory measurementFactory;

    public Simulation(MeasurementFactory measurementFactory) {
        this.random = new Random(0);
        this.measurementFactory = measurementFactory;
        m6PC = new PolarizationControl(new M6MeasurementProcess(measurementFactory));
        m24PC = new PolarizationControl(new M24MeasurementProcess(measurementFactory));
    }

    public void generateRandomFiberTransform() {
        fiberTransformTheta1 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta2 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta3 = (random.nextDouble() - 0.5) * Math.PI;
    }

    public void generateRandomMeasurementError() {
        m6PC.generateNewMeasurement();
        m24PC.generateNewMeasurement();
    }

    public int singleM6() {
        FiberTransform ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
        double control = m6PC.control(ft);
        return dB(control);
    }

    public int singleM24() {
        FiberTransform ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
        double control = m24PC.control(ft);
        return dB(control);
    }

    private int dB(double value) {
        double dB = Math.log10(value) * 10;
        return Double.isFinite(dB) ? (int) dB : 150;
    }
    private static final int SIMULATE_TIME = 10000;

    public static void main(String[] args) {
//        MeasurementFactory factory = ScaleMeasurement.getRandomFactory(0.5);
        MeasurementFactory factory = LinearMeasurement.getRandomFactory(0.5, 0.5);
//        MeasurementFactory factory = MNMeasurement.getRandomFactory(0.5, 0.5);
        Simulation simulation = new Simulation(factory);
        Stat stat6 = new Stat();
        Stat stat24 = new Stat();
        for (int i = 0; i < SIMULATE_TIME; i++) {
            simulation.generateRandomFiberTransform();
            simulation.generateRandomMeasurementError();
            stat6.newResult(simulation.singleM6());
            stat24.newResult(simulation.singleM24());
        }

        int sum6 = 0;
        int sum24 = 0;
        System.out.println("Index\tM6\tM24");
        for (int i = 0; i < 151; i++) {
            sum6 += stat6.results[i];
            sum24 += stat24.results[i];
            System.out.println(i + "\t" + sum6 * 100. / SIMULATE_TIME + "\t" + sum24 * 100. / SIMULATE_TIME);
        }
    }

    private static class Stat {

        private final int[] results = new int[151];

        private Stat() {
        }

        private void newResult(int result) {
            if (result > 150) {
                result = 150;
            } else if (result < 0) {
                result = 0;
            }
            results[result]++;
        }
    }
}

package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class Simulation {

    private final Random random;
    private final PolarizationControl m1PC;
    private double fiberTransformTheta1;
    private double fiberTransformTheta2;
    private double fiberTransformTheta3;

    public Simulation() {
        this.random = new Random();
        m1PC = new PolarizationControl(new M6MeasurementProcess());
    }

    public void generateRandomFiberTransform() {
        fiberTransformTheta1 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta2 = (random.nextDouble() - 0.5) * Math.PI;
        fiberTransformTheta3 = (random.nextDouble() - 0.5) * Math.PI;
    }

    public double singleM1() {
        FiberTransform ft = FiberTransform.createReverse(fiberTransformTheta1 + Math.PI / 2, fiberTransformTheta2 + Math.PI / 2, fiberTransformTheta3 + Math.PI / 2);
        double control = m1PC.control(ft);
        return dB(control);
    }

    private double dB(double value) {
        double dB = Math.log10(value) * 10;
        return Double.isFinite(dB) ? (int) dB : 150;
    }

    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.generateRandomFiberTransform();
        System.out.println(simulation.singleM1());
    }
}

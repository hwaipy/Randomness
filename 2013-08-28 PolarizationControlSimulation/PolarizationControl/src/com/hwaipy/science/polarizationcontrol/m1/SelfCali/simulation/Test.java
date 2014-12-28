package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) {
//        testScaleMeasurement();
//        testLinearMeasurement();
        testMNMeasurement();
    }

    private static void testScaleMeasurement() {
        ScaleMeasurement scaleMeasurement = new ScaleMeasurement(0.1);
        for (double i = 0; i < 500; i += 1) {
            System.out.println(i + "\t" + scaleMeasurement.measurement(i));
        }
    }

    private static void testLinearMeasurement() {
        LinearMeasurement measurement = new LinearMeasurement(0.1, 0.2);
        for (double i = 0; i < 500; i += 1) {
            System.out.println(i + "\t" + measurement.measurement(i));
        }
    }

    private static void testMNMeasurement() {
        MNMeasurement measurement = new MNMeasurement(0.1, 0.2);
        for (double i = 0; i < 500; i += 1) {
            System.out.println(i + "\t" + measurement.measurement(i));
        }
    }
}

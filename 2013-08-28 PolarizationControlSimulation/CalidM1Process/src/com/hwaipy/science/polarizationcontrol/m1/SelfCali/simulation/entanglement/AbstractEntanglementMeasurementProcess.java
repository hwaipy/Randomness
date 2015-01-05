package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.*;

/**
 *
 * @author Hwaipy
 */
public abstract class AbstractEntanglementMeasurementProcess implements EntanglementPolarizationControlMeasurementProcess {

    private final MeasurementFactory measurementFactory;
    private final Measurement[] measurements = new Measurement[8];

    public AbstractEntanglementMeasurementProcess(MeasurementFactory measurementFactory) {
        this.measurementFactory = measurementFactory;
        generateNewMeasurement();
    }

    @Override
    public final void generateNewMeasurement() {
        for (int i = 0; i < measurements.length; i++) {
            measurements[i] = measurementFactory.createMeasurement();
        }
    }

    public double[][] directMeasurement(EntanglementModel model) {
        double[][] results = model.readCoindicences();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                double v = results[i][j];
                v = measurements[i].measurement(v);
                v = measurements[j + 4].measurement(v);
                results[i][j] = v;
            }
        }
        return results;
    }
}

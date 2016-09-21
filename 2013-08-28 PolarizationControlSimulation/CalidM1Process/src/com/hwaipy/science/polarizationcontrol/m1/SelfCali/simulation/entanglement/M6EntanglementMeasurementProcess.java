package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.*;

/**
 *
 * @author Hwaipy
 */
public class M6EntanglementMeasurementProcess extends AbstractEntanglementMeasurementProcess {

  private final int controlGroup;

  public M6EntanglementMeasurementProcess(MeasurementFactory measurementFactory, int controlGroup) {
    super(measurementFactory);
    this.controlGroup = controlGroup;
  }

  @Override
  public double[] measurement(EntanglementModel model) {
    double[] result = new double[12];
    model.set(controlGroup, 1, 0);
    model.set(controlGroup, 2, 0);
    model.set(controlGroup, 3, 0);

    double[][] m1 = directMeasurement(model);

    for (int i = 0; i < 4; i++) {
      result[i] = m1[1][i];
      result[i + 6] = m1[3][i];
    }

    model.set(controlGroup, 2, -Math.PI / 4);
    model.set(controlGroup, 3, -Math.PI / 8);
    double[][] m2 = directMeasurement(model);
    for (int i = 0; i < 2; i++) {
      result[i + 4] = m2[1][i];
      result[i + 10] = m2[3][i];
    }

    return result;
  }
}

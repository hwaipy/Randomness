package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.LinearMeasurement;
import com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.MeasurementFactory;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class EntanglementSimulation {

  private final Random random;
  private final EntanglementPolarizationControl m6PC;
  private final EntanglementPolarizationControl m24PC;
  private final EntanglementModel model;

  public EntanglementSimulation(MeasurementFactory measurementFactory, ErroredTwoPhotonStateFactory twoPhotonStateFactory, int controlGroup) {
    this.random = new Random(0);
    model = new EntanglementModel(twoPhotonStateFactory);
    m6PC = new EntanglementPolarizationControl(new M6EntanglementMeasurementProcess(measurementFactory, controlGroup), controlGroup);
    m24PC = new EntanglementPolarizationControl(new M24EntanglementMeasurementProcess(measurementFactory, controlGroup), controlGroup);
  }

  public void generateRandomFiberTransform() {
    model.generateRandomFiberTransform();
  }

  public void generateRandomMeasurementError() {
    m6PC.generateNewMeasurement();
    m24PC.generateNewMeasurement();
  }

  public void generateRandomTwoPhotonStateError() {
    model.generateRandomTwoPhotonStateError();
  }

  public int singleM6() {
    double control = m6PC.control(model);
//        System.out.println(control);
    return dB(control);
  }

  public int singleM24() {
    double control = m24PC.control(model);
//        System.out.println(control);
    return dB(control);
  }

  private int dB(double value) {
    double dB = Math.log10(value) * 10;
    return Double.isFinite(dB) ? (int) dB : 150;
  }
  private static final int SIMULATE_TIME = 1000;

  public static void main(String[] args) {
    MeasurementFactory measurementFactory = LinearMeasurement.getRandomFactory(0.0, 0.0);
    ErroredTwoPhotonStateFactory twoPhotonStateFactory = new ErroredTwoPhotonStateFactory(0.0, 0.0);
    EntanglementSimulation simulation = new EntanglementSimulation(measurementFactory, twoPhotonStateFactory, 2);
    Stat stat6 = new Stat();
    Stat stat24 = new Stat();
    for (int i = 0; i < SIMULATE_TIME; i++) {
      simulation.generateRandomFiberTransform();
      simulation.generateRandomMeasurementError();
      simulation.generateRandomTwoPhotonStateError();
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

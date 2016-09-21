package telescopecalibration;

import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;

/**
 *
 * @author HwaipyLab
 */
public class M1Simulation {

  private double fiberTransformTheta1;
  private double fiberTransformTheta2;
  private double fiberTransformTheta3;

  public M1Simulation() {
  }

  public M1SimulationResult calculate(double rH, double rV) {
    rH = -(rH - 32.75);
    rV = -(rV);
//    TelescopeTransform tt = TelescopeTransform.create(0.2310963252426152, -1.236449209171316, 0.419646965349516, (-45.0001 / 180. * Math.PI), (-20.0000001 / 180. * Math.PI));
    TelescopeTransform tt = TelescopeTransform.create(0.2310963252426152, -1.236449209171316, 0.419646965349516, (rV / 180. * Math.PI), (rH / 180. * Math.PI));
    WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    WavePlate hwp = new WavePlate(Math.PI, 0);

    Polarization measurementH1 = Polarization.H.transform(tt)
            .transform(qwp1).transform(qwp2).transform(hwp);

    double mHH = measurementH1.getH();
    double mHV = measurementH1.getV();
    double mHD = measurementH1.getD();
    double mHA = measurementH1.getA();
    Polarization measurementD1 = Polarization.D.transform(tt)
            .transform(qwp1).transform(qwp2).transform(hwp);
    double mDH = measurementD1.getH();
    double mDV = measurementD1.getV();
    double mDD = measurementD1.getD();
    double mDA = measurementD1.getA();
    qwp2.increase(-Math.PI / 4);
    hwp.increase(-Math.PI / 8);
    Polarization measurementH2 = Polarization.H.transform(tt)
            .transform(qwp1).transform(qwp2).transform(hwp);
    double mHL = measurementH2.getH();
    double mHR = measurementH2.getV();
    Polarization measurementD2 = Polarization.D.transform(tt)
            .transform(qwp1).transform(qwp2).transform(hwp);
    double mDL = measurementD2.getH();
    double mDR = measurementD2.getV();

    M1Process m1Process = null;
    double cH = 0;
    double cD = 0;

//    System.out.println(Arrays.toString(new double[]{mHH, mHV, mHD, mHA, mHL, mHR}));
//    System.out.println(Arrays.toString(new double[]{mDH, mDV, mDD, mDA, mDL, mDR}));
    try {
      m1Process = M1Process.calculate(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR});
    } catch (M1ProcessException ex) {
    }
    if (m1Process != null) {
      double[] result = m1Process.getResults();
      qwp1.setTheta(result[0]);
      qwp2.setTheta(result[1]);
      hwp.setTheta(result[2]);
      System.out.println("" + (result[0] / Math.PI * 180 - 15.1));
      System.out.println("" + (result[1] / Math.PI * 180 + 51.3));
      System.out.println("" + (result[2] / Math.PI * 180 - 15.3));
      Polarization resultH = Polarization.H.transform(tt).transform(qwp1).transform(qwp2).transform(hwp);
      Polarization resultD = Polarization.D.transform(tt).transform(qwp1).transform(qwp2).transform(hwp);
      cH = resultH.getH() / resultH.getV();
      cD = resultD.getD() / resultD.getA();
    }
    return new M1SimulationResult(m1Process != null, fiberTransformTheta1, fiberTransformTheta2, fiberTransformTheta3, cH, cD);
  }

  public class M1SimulationResult {

    private final boolean success;
    private final double fiberTheta1;
    private final double fiberTheta2;
    private final double fiberTheta3;
    private final double cH;
    private final double cD;

    public M1SimulationResult(boolean success, double fiberTheta1, double fiberTheta2, double fiberTheta3, double cH, double cD) {
      this.success = success;
      this.fiberTheta1 = fiberTheta1;
      this.fiberTheta2 = fiberTheta2;
      this.fiberTheta3 = fiberTheta3;
      this.cH = cH;
      this.cD = cD;
    }
  }

  public static void main(String[] args) {
    M1Simulation s = new M1Simulation();
//    s.generateRandomTelescopeTransform();
    M1SimulationResult result = s.calculate(20 + 32.75, 45);
//    System.out.println(result.success + ", " + result.cH + ",   " + result.cD);
  }
}

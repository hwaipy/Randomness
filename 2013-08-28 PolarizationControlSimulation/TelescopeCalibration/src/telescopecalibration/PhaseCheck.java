package telescopecalibration;

/**
 *
 * @author Hwaipy
 */
public class PhaseCheck {

  public static void main(String[] args) {
    double rH = 32.7501;
    double rV = 0.01;
    double phase = 56.01 / 180 * Math.PI;
    double rotate = 0.001;
    M1Simulation.M1SimulationResult r = new M1Simulation().calculate(rH, rV, phase, rotate, true);
    System.out.println(r.success);
    double[] angle = r.getAngles();
    double a1 = angle[0] / Math.PI * 180 - 14.471;
    double a2 = angle[1] / Math.PI * 180 + 51.315;
    double a3 = angle[2] / Math.PI * 180 - 15.865;
    System.out.println(a1);
    System.out.println(a2);
    System.out.println(a3);
  }
}

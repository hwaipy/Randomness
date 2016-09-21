package telescopecalibration;

/**
 *
 * @author Hwaipy
 */
public class Console {

  public static void main(String[] args) {
//    args = new String[]{"52.75", "45"};
    double rH = Double.parseDouble(args[0]);
    double rV = Double.parseDouble(args[1]);
    M1Simulation s = new M1Simulation();
    M1Simulation.M1SimulationResult result = s.calculate(rH, rV);
  }
}

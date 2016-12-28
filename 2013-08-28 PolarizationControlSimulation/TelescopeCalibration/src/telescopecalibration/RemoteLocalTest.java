package telescopecalibration;

import com.hwaipy.science.polarizationcontrol.device.MuellerMatrix;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.Rotate;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.util.Arrays;

/**
 *
 * @author Hwaipy
 */
public class RemoteLocalTest {

  public static void main(String[] args) {
    double PA = 0.28;
    double PB = -1.33;
    double PC = 0.48;
    double rH = 52.76;
    double rV = 40;
    double phase = 0.9;
    double rotate = 0.5000000000001;
    M1Simulation.M1SimulationResult r = new M1Simulation().calculate(rH, rV, phase, rotate, true);
    double[] angle = r.getAngles();
    System.out.println(Arrays.toString(angle));
    rH = -(rH - 32.75) / 180 * Math.PI;
    rV = -(rV) / 180 * Math.PI;
    MuellerMatrix mm = MuellerMatrix.merge(
            new WavePlate(phase, 0),
            new Rotate(rotate),
            //            new WavePlate(Math.PI, Math.PI / 4),
            new WavePlate(PA, 0),
            new Rotate(rV),
            new WavePlate(PB, 0),
            new Rotate(rH),
            new WavePlate(PC, 0),
            new WavePlate(Math.PI / 2, angle[0]),
            new WavePlate(Math.PI / 2, angle[1]),
            new WavePlate(Math.PI, angle[2])
    );

    Polarization mH = Polarization.H.transform(mm);
    Polarization mD = Polarization.D.transform(mm);
    System.out.println(mH.getH());
    System.out.println(mD.getD());
  }
}

package simulationconsole;

import simulationconsole.entanglement.EntanglementSimulationConsole;
import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

/**
 *
 * @author Hwaipy
 */
public class SimulationConsole {

//    private static final double t1 = 0.983010844557681;
//    private static final double t2 = 0.6037286611324978;
//    private static final double t3 = 0.4370992437357415;
    private static FiberTransform ft = FiberTransform.createRandomFiber();
//    private static final FiberTransform ft = FiberTransform.createReverse(t1 + Math.PI / 2, t2 + Math.PI / 2, t3 + Math.PI / 2);
    private static final WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
    private static final WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
    private static final WavePlate hwp = new WavePlate(Math.PI, 0);
    private static int laser = 0;//1 for H, 2 for D

    public static void main(String[] args) throws IOException {
        if (args != null && args.length > 0 && "-e".equals(args[0])) {
            EntanglementSimulationConsole.main(args);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                try {
                    if (line.equals("open H")) {
                        openLaser(1);
                    } else if (line.equals("open D")) {
                        openLaser(2);
                    } else if (line.equals("close H") || line.equals("close D")) {
                        closeLaser();
                    } else if (line.startsWith("set")) {
                        String[] splits = line.split(" ");
                        int number = Integer.parseInt(splits[1]);
                        double angle = Double.parseDouble(splits[2]);
                        set(number, angle);
                    } else if (line.equals("read")) {
                        read();
                    } else if (line.equals("reset")) {
                        reset();
                    }
                } catch (NumberFormatException numberFormatException) {
                    numberFormatException.printStackTrace(System.err);
                }
            }
        }
    }

    private static void openLaser(int code) {
        laser = code;
    }

    private static void closeLaser() {
        laser = 0;
    }

    private static void set(int number, double angle) {
        WavePlate wavePlate;
        switch (number) {
            case 1:
                wavePlate = qwp1;
                break;
            case 2:
                wavePlate = qwp2;
                break;
            case 3:
                wavePlate = hwp;
                break;
            default:
                throw new RuntimeException();
        }
        wavePlate.setTheta(angle / 180 * Math.PI);
    }

    private static void read() {
        Polarization polarization;
        switch (laser) {
            case 0:
                System.out.println("0.0 0.0 0.0 0.0");
                return;
            case 1:
                polarization = Polarization.H;
                break;
            case 2:
                polarization = Polarization.D;
                break;
            default:
                throw new RuntimeException();
        }
        Polarization p = polarization.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
        double H = p.getH();
        double V = p.getV();
        double D = p.getD();
        double A = p.getA();
        DecimalFormat format = new DecimalFormat("0.0000");
        System.out.println(format.format(H) + " " + format.format(V) + " " + format.format(D) + " " + format.format(A));
    }

    private static void reset() {
        ft = FiberTransform.createRandomFiber();
    }
}

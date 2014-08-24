package D20140516.GroupVelocity_Reconstructed;

import com.hwaipy.utilities.system.PathsUtilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class CorrelationTest {

    private static final double minOmigaS = 1555;
    private static final double maxOmigaS = 1565;
    private static final double minOmigaI = 1555;
    private static final double maxOmigaI = 1565;
    private static final int width = 50;
    private static final int height = 50;

    public static void main(String[] args) throws IOException {
//        pump();
//        phaseMatch();
//        join();
//        System.out.println(Light.k(405e-9, true));
//        System.out.println(Light.k(810e-9, true));
//        System.out.println(Light.k(810e-9, false));
//        System.out.println((Light.k(402.5e-9, true) - Light.k(805e-9, true) - Light.k(805e-9, false) - 2 * Math.PI / 10.032e-6) * 0.015 / 2);
        System.out.println(functionPump.value(1560, 1559));
        System.out.println(functionPhaseMatch.value(1559, 1560));
    }

    public static void join() throws IOException {
        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionJoin, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "join.png"));
        double[] statistics = correlationPloter.statistics(true);
        for (int i = 0; i < statistics.length; i++) {
            System.out.println((809 + i / 1000. * 2) + "\t" + statistics[i]);
        }
//        System.out.println(correlationPloter.totalIntensity());
//        System.out.println(correlationPloter.filtedIntensity(779.973, 780.027, 0.0148));
//        double maxFilterdIntensity = 0;
//        double maxS = 0;
//        double maxI = 0;
//        for (double s = 779.95; s < 779.99; s += 1. / 1000) {
//            System.out.println(s);
//            for (double i = 780.01; i < 780.05; i += 1. / 1000) {
//                double fi = correlationPloter.filtedIntensity(s, i, 0.0148);
//                if (fi > maxFilterdIntensity) {
//                    maxFilterdIntensity = fi;
//                    maxS = s;
//                    maxI = i;
//                }
//            }
//        }
//        System.out.println(maxS);
//        System.out.println(maxI);
//        System.out.println(maxFilterdIntensity);
    }

    private static final CorrelationFunction functionJoin = new CorrelationFunction() {

        @Override
        public double value(double lamdaSignal, double lamdaIdler) {
            double result = functionPump.value(lamdaSignal, lamdaIdler) * functionPhaseMatch.value(lamdaSignal, lamdaIdler);
            return result;
        }
    };

    private static final CorrelationFunction functionPhaseMatch = new CorrelationFunction() {

        @Override
        public double value(double lamdaSignal, double lamdaIdler) {
            double lengthOfCrystal = 15e-3;
            double lamdaPump = 1 / (1 / lamdaSignal + 1 / lamdaIdler);
            double kPump = Light.k(lamdaPump / 1e9, true);
            double kSignal = Light.k(lamdaSignal / 1e9, true);
            double kIdler = Light.k(lamdaIdler / 1e9, false);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 7.8825e-6);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 3.4377532602689514e-6);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 10.035e-6);
            System.out.println("KP=" + kPump);
            System.out.println("KS=" + kSignal);
            System.out.println("KI=" + kIdler);
            System.out.println((kPump - kSignal - kIdler));
            System.out.println((2 * Math.PI / 46.1e-6));
            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler + 2 * Math.PI / 46.1e-6);
            System.out.println("x=" + arg);
            double result = Math.sin(arg) / arg;
            return result * result;
        }
    };
    private static final CorrelationFunction functionPump = new CorrelationFunction() {

        @Override
        public double value(double arg1, double arg2) {
            double mu = 780;
            double sigma = 1. / 2.35;
            double lamdaPump = 1 / (1 / arg1 + 1 / arg2);
            double result = Math.exp(-(lamdaPump - mu) * (lamdaPump - mu) / 2 / sigma / sigma);
            return result * result;
        }
    };

    public static void phaseMatch() throws IOException {
        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPhaseMatch, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "phaseMatch.png"));
    }

    public static void pump() throws IOException {
        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPump, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "pump.png"));
    }
}

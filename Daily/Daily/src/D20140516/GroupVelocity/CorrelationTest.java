package D20140516.GroupVelocity;

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

    public static void main(String[] args) throws IOException {
        join();
    }

    public static void join() throws IOException {
        double minOmigaS = 780;
        double maxOmigaS = 840;
        double minOmigaI = 780;
        double maxOmigaI = 840;
        int width = 1000;
        int height = 1000;

        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionJoin, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "phaseMatch.png"));
    }

    private static final CorrelationFunction functionJoin = new CorrelationFunction() {

        @Override
        public double value(double lamdaSignal, double lamdaIdler) {
            return functionPump.value(lamdaSignal, lamdaIdler) * functionPhaseMatch.value(lamdaSignal, lamdaIdler);
        }
    };

    private static final CorrelationFunction functionPhaseMatch = new CorrelationFunction() {

        @Override
        public double value(double lamdaSignal, double lamdaIdler) {
            double lengthOfCrystal = 1e-3;
            double lamdaPump = 1 / (1 / lamdaSignal + 1 / lamdaIdler);
            double kPump = Light.k(lamdaPump / 1e9, true);
            double kSignal = Light.k(lamdaSignal / 1e9, true);
            double kIdler = Light.k(lamdaIdler / 1e9, false);
            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / 10.025e-6);
            return Math.sin(arg) / arg;
        }
    };
    private static final CorrelationFunction functionPump = new CorrelationFunction() {

        @Override
        public double value(double arg1, double arg2) {
            double mu = 405;
            double sigma = 0.425;
            double lamdaPump = 1 / (1 / arg1 + 1 / arg2);
            return Math.exp(-(lamdaPump - mu) * (lamdaPump - mu) / 2 / sigma / sigma);
        }
    };

    public static void phaseMatch() throws IOException {
        double minOmigaS = 780;
        double maxOmigaS = 840;
        double minOmigaI = 780;
        double maxOmigaI = 840;
        int width = 1000;
        int height = 1000;

        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPhaseMatch, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "phaseMatch.png"));
    }

    public static void pump() throws IOException {
        double minOmigaS = 780;
        double maxOmigaS = 840;
        double minOmigaI = 780;
        double maxOmigaI = 840;
        int width = 1000;
        int height = 1000;

        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPump, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "pump.png"));
    }

    public static void firstTry() throws IOException {
        double minOmigaS = 0;
        double maxOmigaS = 100;
        double minOmigaI = 200;
        double maxOmigaI = 400;
        int width = 1000;
        int height = 900;
        CorrelationFunction function = new CorrelationFunction() {

            @Override
            public double value(double arg1, double arg2) {
                return Math.pow(arg1 * 5 - arg2, 2);
            }
        };

        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, function, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "correlation.png"));
    }
}

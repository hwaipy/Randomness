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

    private static final double minOmigaS = 1530;
    private static final double maxOmigaS = 1590;
    private static final double minOmigaI = 1530;
    private static final double maxOmigaI = 1590;
    private static final int width = 1000;
    private static final int height = 1000;

    public static void main(String[] args) throws IOException {
        pump();
        phaseMatch();
        join();
    }

    public static void join() throws IOException {
        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionJoin, width, height);
        correlationPloter.calculate();
        BufferedImage image = correlationPloter.createImage();
        Path path = PathsUtilities.getDataStoragyPath();
        ImageIO.write(image, "png", new File(path.toFile(), "join.png"));
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
            double lengthOfCrystal = 10e-3;
            double lamdaPump = 1 / (1 / lamdaSignal + 1 / lamdaIdler);
            double kPump = Light.k(lamdaPump / 1e9, true);
            double kSignal = Light.k(lamdaSignal / 1e9, true);
            double kIdler = Light.k(lamdaIdler / 1e9, false);
            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler + 2 * Math.PI / 46.2e-6);
            return Math.sin(arg) / arg;
        }
    };
    private static final CorrelationFunction functionPump = new CorrelationFunction() {

        @Override
        public double value(double arg1, double arg2) {
            double mu = 780;
            double sigma = 0.425;
            double lamdaPump = 1 / (1 / arg1 + 1 / arg2);
            return Math.exp(-(lamdaPump - mu) * (lamdaPump - mu) / 2 / sigma / sigma);
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

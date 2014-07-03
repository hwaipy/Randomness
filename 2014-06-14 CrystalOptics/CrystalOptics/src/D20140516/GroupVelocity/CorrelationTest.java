//package D20140516.GroupVelocity;
//
//import com.hwaipy.utilities.system.PathsUtilities;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.nio.file.Path;
//import javax.imageio.ImageIO;
//
///**
// *
// * @author Hwaipy
// */
//public class CorrelationTest {
//
//    private static final double minOmigaI = 793;
//    private static final double maxOmigaI = 795;
//    private static final double minOmigaS = 1611;
//    private static final double maxOmigaS = 1613;
//    private static final int width = 1000;
//    private static final int height = 1000;
//
//    public static void main(String[] args) throws IOException {
//        pump();
//        phaseMatch();
//        join();
//    }
//
//    public static void join() throws IOException {
//        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionJoin, width, height);
//        correlationPloter.calculate();
//        BufferedImage image = correlationPloter.createImage();
//        Path path = PathsUtilities.getDataStoragyPath();
//        ImageIO.write(image, "png", new File(path.toFile(), "join.png"));
////        double[] statistics = correlationPloter.statistics(false);
////        for (int i = 0; i < statistics.length; i++) {
////            System.out.println(i + "\t" + statistics[i]);
////        }
////        System.out.println(correlationPloter.totalIntensity());
////        System.out.println(correlationPloter.filtedIntensity(779.973, 780.027, 0.0148));
////        double maxFilterdIntensity = 0;
////        double maxS = 0;
////        double maxI = 0;
////        for (double s = 779.95; s < 779.99; s += 1. / 1000) {
////            System.out.println(s);
////            for (double i = 780.01; i < 780.05; i += 1. / 1000) {
////                double fi = correlationPloter.filtedIntensity(s, i, 0.0148);
////                if (fi > maxFilterdIntensity) {
////                    maxFilterdIntensity = fi;
////                    maxS = s;
////                    maxI = i;
////                }
////            }
////        }
////        System.out.println(maxS);
////        System.out.println(maxI);
////        System.out.println(maxFilterdIntensity);
//    }
//
//    private static final CorrelationFunction functionJoin = new CorrelationFunction() {
//
//        @Override
//        public double value(double lamdaSignal, double lamdaIdler) {
//            double result = functionPump.value(lamdaSignal, lamdaIdler) * functionPhaseMatch.value(lamdaSignal, lamdaIdler);
//            return result;
//        }
//    };
//
//    private static final CorrelationFunction functionPhaseMatch = new CorrelationFunction() {
//
//        @Override
//        public double value(double lamdaSignal, double lamdaIdler) {
//            double lengthOfCrystal = 30e-3;
//            double lamdaPump = 1 / (1 / lamdaSignal + 1 / lamdaIdler);
//            double kPump = Light.k(lamdaPump / 1e9, true);
//            double kSignal = Light.k(lamdaSignal / 1e9, true);
//            double kIdler = Light.k(lamdaIdler / 1e9, false);
//            double arg = lengthOfCrystal / 2 * (kPump - kSignal - kIdler - 2 * Math.PI / -27.4e-6);
//            double result = Math.sin(arg) / arg;
//            return result * result;
//        }
//    };
//    private static final CorrelationFunction functionPump = new CorrelationFunction() {
//
//        @Override
//        public double value(double arg1, double arg2) {
//            double mu = 532;
//            double sigma = 0.09;
//            double lamdaPump = 1 / (1 / arg1 + 1 / arg2);
//            double result = Math.exp(-(lamdaPump - mu) * (lamdaPump - mu) / 2 / sigma / sigma);
//            return result * result;
//        }
//    };
//
//    public static void phaseMatch() throws IOException {
//        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPhaseMatch, width, height);
//        correlationPloter.calculate();
//        BufferedImage image = correlationPloter.createImage();
//        Path path = PathsUtilities.getDataStoragyPath();
//        ImageIO.write(image, "png", new File(path.toFile(), "phaseMatch.png"));
//    }
//
//    public static void pump() throws IOException {
//        CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, functionPump, width, height);
//        correlationPloter.calculate();
//        BufferedImage image = correlationPloter.createImage();
//        Path path = PathsUtilities.getDataStoragyPath();
//        ImageIO.write(image, "png", new File(path.toFile(), "pump.png"));
//    }
//}

package D20140710.GHzPulseGeneration;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class GHzPulseGeneration {

    private static final Function[] functions = new Function[]{
        new Function() {

            @Override
            public double value(double x) {
                int xInt = (int) x;
                double xLocal = x - xInt;
                return (xLocal <= 0.6 && xLocal > 0.5) ? 1 : 0;
            }
        },
        new Function() {

            @Override
            public double value(double x) {
                int xInt = (int) x;
                Random random = new Random(xInt * 12000);
                double value = random.nextDouble();
                if (value > 0.5) {
                    return 1;
                }
                if (value > 0.2) {
                    return 0.3;
                }
                return 0;
            }
        },
        new Function() {

            @Override
            public double value(double x) {
                int xInt = (int) x;
                Random random = new Random(xInt * 120000);
                double value = random.nextDouble();
                if (value > 0.5) {
                    return 1;
                }
                return 0;
            }
        },
        new Function() {

            @Override
            public double value(double x) {
                int xInt = (int) x;
                Random random = new Random(xInt * 1200000);
                double value = random.nextDouble();
                if (value > 0.5) {
                    return 1;
                }
                return 0;
            }
        }};

    public static void main(String[] args) {
        double start = 0;
        double end = 10;
        double stepLength = 0.001;
        double inset = 0.3;
        StringBuilder sb = new StringBuilder();
        for (double x = start; x <= end; x += stepLength) {
            sb.append(x).append("\t");
            double baseLine = 0;
            for (Function function : functions) {
                double value = function.value(x) + baseLine;
                sb.append(value).append("\t");
                baseLine -= 1 + inset;
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append(System.lineSeparator());
        }
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(sb.toString()), null);
    }

    private interface Function {

        public double value(double x);
    }
}

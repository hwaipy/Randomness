package D20140729.PhaseLocking;

import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class PhaseLockingTest {

    private static final double visibilityOriginal = 0.9;

    public static void main(String[] args) {
        statisticsIndexed();
//        statistics();
//        System.out.println(fewPhotonsTest());
//        for (int i = 0; i < 1000; i++) {
//            accuracyTest();
//        }
    }

    public static final void statisticsIndexed() {
        for (int phiI = 0; phiI < 360; phiI++) {
            SortedList<Double> sortedResults = new SortedList<>();
            Contrast contrast = new Contrast();
            double phi = phiI / 180. * Math.PI;
            for (int i = 0; i < 1000; i++) {
                double result = fewPhotonsTest(phi);
                sortedResults.offer(result);
                contrast.offer(result);
            }
            System.out.println(phiI + "\t" + contrast.getAverageContrast());
//            System.out.println(contrast.getAverageContrast());
        }
    }

    public static final void statistics() {
        int[] results = new int[31];
        SortedList<Double> sortedResults = new SortedList<>();
        for (int i = 0; i < 1000; i++) {
            double result = fewPhotonsTest(29 / 180. * Math.PI);
            sortedResults.offer(result);
            int rI = (int) result;
            if (rI < 0) {
                rI = 0;
            }
            if (rI > 30) {
                rI = 30;
            }
            results[rI]++;
        }
        int sum = 0;
        for (int i = 0; i < results.length; i++) {
            sum += results[i];
            System.out.println(i + "\t" + sum);
        }
        System.out.println(sortedResults.get(100));
    }

    public static final double fewPhotonsTest() {
        return fewPhotonsTest(new Random().nextDouble());
    }

    public static final double fewPhotonsTest(double phi) {
        double overallMiu = 125;
        double singleMiu = overallMiu / 5;
        double p0 = (1 - Math.cos(phi)) / 2;
        double p1 = (1 - Math.cos(phi + Math.PI)) / 2;
        double p2 = (1 - Math.cos(phi + Math.PI / 2)) / 2;
        double p3 = (1 - Math.cos(phi - Math.PI / 2)) / 2;
        double fewP0 = getFewP(singleMiu, p0);
        double fewP1 = getFewP(singleMiu, p1);
        double fewP2 = getFewP(singleMiu, p2);
        double fewP3 = getFewP(singleMiu, p3);
        FourMeasurement fourMeasurement = new FourMeasurement(fewP0, fewP1, fewP2, fewP3);
        double phiCalculated = fourMeasurement.phi();
        double delta = phi - phiCalculated;
        double contrast = 0.5 - Math.cos(delta) / 2 * visibilityOriginal;
        double dB = -10 * Math.log10(contrast);
//        System.out.println(delta + "\t" + dB);
        return dB;
    }

    private static double getFewP(double miu, double accuracyP) {
        int photonNumber = getPossionVariable(miu);
//        int photonNumber = getStatisticPossionVariable(miu);
        int clicks = 0;
        Random random = new Random();
        for (int i = 0; i < photonNumber; i++) {
            if (random.nextDouble() < accuracyP) {
                clicks++;
            }
        }
        double eP = ((double) clicks) / photonNumber;
//        System.out.println("try get few p, miu=" + miu + ", n=" + photonNumber + ", p=" + accuracyP + ", esmatedP=" + eP);
        return eP;
    }

    public static final void accuracyTest() {
        double phi = new Random().nextDouble() * 2 * Math.PI;
        double p0 = (1 - Math.cos(phi)) / 2;
        double p1 = (1 - Math.cos(phi + Math.PI)) / 2;
        double p2 = (1 - Math.cos(phi + Math.PI / 2)) / 2;
        double p3 = (1 - Math.cos(phi - Math.PI / 2)) / 2;
        FourMeasurement fourMeasurement = new FourMeasurement(p0, p1, p2, p3);
        double phiCalculated = fourMeasurement.phi();
        double delta = phi - phiCalculated;
        if (Math.abs(delta) > 0.000001) {
            System.out.println(phi);
        }
    }

    private static int getStatisticPossionVariable(double miu) {
        int input = (int) (miu * 100);
        int output = 0;
        Random random = new Random();
        for (int i = 0; i < input; i++) {
            if (random.nextDouble() < 0.01) {
                output++;
            }
        }
        return output;
    }

    private static int getPossionVariable(double miu) {
        int n = 0;
        double p = Math.random();
        double cdf = getPossionProbability(n, miu);
        while (cdf < p) {
            n++;
            cdf += getPossionProbability(n, miu);
        }
        return n;
    }

    private static double getPossionProbability(int n, double miu) {
        double c = Math.exp(-miu);
        double sum = 1;
        for (int i = 1; i <= n; i++) {
            sum *= miu / i;
        }
        return sum * c;
    }
}

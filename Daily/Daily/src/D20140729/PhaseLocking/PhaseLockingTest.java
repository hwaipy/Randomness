package D20140729.PhaseLocking;

import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class PhaseLockingTest {

    private static final double visibilityOriginal = 1;

    public static void main(String[] args) {
        statisticsFlu();
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

    public static final void statisticsFlu() {
        for (int phiI = 100; phiI < 101; phiI++) {
            Contrast contrast = new Contrast();
            double phi = phiI / 180. * Math.PI;
            for (int i = 0; i < 1; i++) {
                double result = fluTest(phi);
                contrast.offer(result);
            }
            System.out.println(phiI + "\t" + contrast.getAverageContrast());
//            System.out.println(contrast.getAverageContrast());
        }
    }

    private static final double fluTest(double phi) {
        double fluDelta = 0.0;
        double fluOffset = 0.0;
        double fluPI2 = 0.8;
        double deltaPhi = Math.PI * fluPI2;
        double p0 = (1 - Math.cos(phi)) / 2;
        double p1 = (1 - Math.cos(phi + deltaPhi)) / 2;
        double p2 = (1 - Math.cos(phi + deltaPhi / 2)) / 2;
        double p3 = (1 - Math.cos(phi - deltaPhi / 2)) / 2;
        System.out.println(p0);
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        double fluP0 = flu(p0, fluDelta, fluOffset);
        double fluP1 = flu(p1, fluDelta, fluOffset);
        double fluP2 = flu(p2, fluDelta, fluOffset);
        double fluP3 = flu(p3, fluDelta, fluOffset);
        System.out.println(fluP0);
        System.out.println(fluP1);
        System.out.println(fluP2);
        System.out.println(fluP3);
        FourMeasurement fourMeasurement = new FourMeasurement(fluP0, fluP1, fluP2, fluP3);
        double phiCalculated = fourMeasurement.phi();
        System.out.println(phiCalculated / Math.PI * 180);
        double delta = phi - phiCalculated;
        double contrast = 0.5 - Math.cos(delta) / 2 * visibilityOriginal;
        double dB = -10 * Math.log10(contrast);
        return dB;
    }
    private static final Random r = new Random();

    private static double flu(double original, double flu, double offset) {
        double result = original + original * flu * (r.nextDouble() * 2 - 1) + offset;
        if (result < 0) {
            return 0;
        }
        if (result > 1) {
            return 1;
        }
        return result;
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

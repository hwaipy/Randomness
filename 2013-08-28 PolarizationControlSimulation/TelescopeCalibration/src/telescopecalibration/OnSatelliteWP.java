package telescopecalibration;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;
import simulationconsole.entanglement.HalfWavePlate;
import simulationconsole.entanglement.QuarterWavePlate;

/**
 *
 * @author hwaipy
 */
public class OnSatelliteWP {
//0\1\2\3\4\5 for MT HVDA/LR/RL, JF HVDA/LR/RL

    private static final int CODE = 5;
    private static final int threadCount = 8;

    private static final boolean isMT = new Boolean[]{true, true, true, false, false, false}[CODE];

    private static final double[] zeros = isMT ? new double[]{90.05, 146.58, 99.99} : new double[]{125.42, 174.72, 137.86};
//        double[] comp = isMT ? new double[]{80.1, 127.5, 61.4} : new double[]{115.3, -89.2, 138.2};//2016.10.2
    private static final double[] comp = isMT ? new double[]{78.968, 127.045, 60.765} : new double[]{111.716, -95.458, 136.858};//2017.3.1
    private static final double[] compLogic = new double[3];

    private final Matrix<Complex> fibreMatrix;

    public static final void main(String[] args) throws M1ProcessException, IOException {
//        new OnSatelliteWP().process();
//        distanceOptimize();
//        check();
        checkSecialAngles();
    }

    public OnSatelliteWP() {
        System.out.println("Initial State for HVDA measurement: ");
        for (int i = 0; i < 3; i++) {
            compLogic[i] = comp[i] - zeros[i];
            System.out.println(compLogic[i]);
            compLogic[i] = compLogic[i] / 180.0 * Math.PI;
        }
        System.out.println();
        QuarterWavePlate qwp1 = QuarterWavePlate.create(compLogic[0]);
        QuarterWavePlate qwp2 = QuarterWavePlate.create(compLogic[1]);
        HalfWavePlate hwp = HalfWavePlate.create(compLogic[2]);
        Matrix<Complex> compMatrix = hwp.getMatrix().times(qwp2.getMatrix()).times(qwp1.getMatrix());
        fibreMatrix = compMatrix.inverse();
    }

    public void process() throws M1ProcessException {
        double[] aoWP = calcWPForExtraQWP();

        LinkedBlockingQueue<String> outputQueue = new LinkedBlockingQueue<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String s = outputQueue.take();
                        System.out.println(s);
                        PrintWriter pw = new PrintWriter(new FileOutputStream(new String[]{"MT-HV.csv", "MT-LR.csv", "MT-RL.csv", "JF-HV.csv", "JF-LR.csv", "JF-RL.csv"}[CODE], true));
                        pw.println(s);
                        pw.close();
                    } catch (Exception ex) {
                        Logger.getLogger(OnSatelliteWP.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }).start();

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        for (int i = 0; i < 1000000; i++) {
            executorService.submit(() -> {
                double[] newWP = searchRandom(aoWP);
                String s = newWP[0] + ", " + newWP[1] + ", " + newWP[2];
                outputQueue.offer(s);
//                if (newWP != null) {
//                    double[] distances = new double[3];
//                    double distance = 0;
//                    for (int i1 = 0; i1 < distances.length; i1++) {
//                        distances[i1] = distance(newWP[i1], compLogic[i1]);
//                        distance += distances[i1];
//                    }
//                    double[] newWPDeg = new double[3];
//                    double[] newWPScale = new double[3];
//                    for (int i2 = 0; i2 < newWP.length; i2++) {
//                        newWPDeg[i2] = newWP[i2] / Math.PI * 180;
//                        newWPScale[i2] = newWPDeg[i2] + zeros[i2];
//                    }
//                    outputQueue.offer(distance + "\t" + Arrays.toString(distances) + "\t" + Arrays.toString(newWP) + "\t" + Arrays.toString(newWPDeg) + "\t" + Arrays.toString(newWPScale));
//                }
            });
        }
    }

    private double[] calcWPForExtraQWP() throws M1ProcessException {
        QuarterWavePlate extQWP = QuarterWavePlate.create(Math.PI / 4);
        QuarterWavePlate extQWP2 = QuarterWavePlate.create(-Math.PI / 4);
        double[] measurements = calculateMeasurements(
                new Matrix[]{
                    fibreMatrix, fibreMatrix.times(extQWP.getMatrix()), fibreMatrix.times(extQWP2.getMatrix()),
                    fibreMatrix, fibreMatrix.times(extQWP.getMatrix()), fibreMatrix.times(extQWP2.getMatrix())}[CODE]);
        return calculateAngleOfWP(measurements);
    }

    private double[] search(double[] target, double[] current) {
        AnnealingTest p = new AnnealingTest(0.99999, 4048, current, target);
        AnnealingTest.Result result = p.process();
        if (!result.success) {
            return null;
        }
        return result.angles;
    }

    private double[] searchRandom(double[] target) {
        Random random = new Random();
        double[] start = random.doubles(3, -Math.PI, Math.PI).toArray();
        return search(target, start);
    }

    private double[] calculateMeasurements(Matrix<Complex> U) {
        Matrix<Complex> mH = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}});
        Matrix<Complex> mD = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2)), Complex.ONE.divide(Math.sqrt(2))}});
        Matrix<Complex> phiHAfter = U.times(mH.transpose());
        Matrix<Complex> phiDAfter = U.times(mD.transpose());
        Matrix<Complex> MQ_0 = QuarterWavePlate.create(0).getMatrix();
        Matrix<Complex> MQ_N45 = QuarterWavePlate.create(-Math.PI / 4).getMatrix();
        Matrix<Complex> MH_0 = HalfWavePlate.create(0).getMatrix();
        Matrix<Complex> MH_22_5 = HalfWavePlate.create(Math.PI / 8).getMatrix();
        Matrix<Complex> MH_N22_5 = HalfWavePlate.create(-Math.PI / 8).getMatrix();
        Matrix<Complex> phiMHHV = MH_0.times(MQ_0).times(MQ_0).times(phiHAfter);
        Matrix<Complex> phiMHDA = MH_22_5.times(MQ_N45).times(MQ_N45).times(phiHAfter);
        Matrix<Complex> phiMHLR = MH_N22_5.times(MQ_N45).times(MQ_0).times(phiHAfter);
        Matrix<Complex> phiMDHV = MH_0.times(MQ_0).times(MQ_0).times(phiDAfter);
        Matrix<Complex> phiMDDA = MH_22_5.times(MQ_N45).times(MQ_N45).times(phiDAfter);
        Matrix<Complex> phiMDLR = MH_N22_5.times(MQ_N45).times(MQ_0).times(phiDAfter);
        double mHH = Math.pow(phiMHHV.get(0, 0).magnitude(), 2);
        double mHV = Math.pow(phiMHHV.get(1, 0).magnitude(), 2);
        double mHD = Math.pow(phiMHDA.get(0, 0).magnitude(), 2);
        double mHA = Math.pow(phiMHDA.get(1, 0).magnitude(), 2);
        double mHL = Math.pow(phiMHLR.get(0, 0).magnitude(), 2);
        double mHR = Math.pow(phiMHLR.get(1, 0).magnitude(), 2);
        double mDH = Math.pow(phiMDHV.get(0, 0).magnitude(), 2);
        double mDV = Math.pow(phiMDHV.get(1, 0).magnitude(), 2);
        double mDD = Math.pow(phiMDDA.get(0, 0).magnitude(), 2);
        double mDA = Math.pow(phiMDDA.get(1, 0).magnitude(), 2);
        double mDL = Math.pow(phiMDLR.get(0, 0).magnitude(), 2);
        double mDR = Math.pow(phiMDLR.get(1, 0).magnitude(), 2);
        return new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR};
    }

    private double[] calculateAngleOfWP(double[] v12) throws M1ProcessException {
        M1Process m1 = M1Process.calculate(v12);
        return m1.getResults();
    }

    private static double distance(double a, double b) {
        double d = Math.abs(a - b);
        while (d >= Math.PI * 2) {
            d -= Math.PI * 2;
        }
        if (d > Math.PI) {
            d = Math.PI * 2 - d;
        }
        return d;
    }

    private static void distanceOptimize() throws IOException {
        ArrayList<double[]> anglesA = loadAngles(new File("JF-HV.csv"));
        ArrayList<double[]> anglesB = loadAngles(new File("JF-LR.csv"));
        System.out.println(anglesA.size());
        System.out.println(anglesB.size());
        double bestD = 100000.00;
        double[] bestAnglesA = null;
        double[] bestAnglesB = null;
        for (double[] a : anglesA) {
            for (double[] b : anglesB) {
                double d = 0;
                for (int i = 0; i < 3; i++) {
                    d += distance(a[i], b[i]);
                }
                if (d < bestD) {
                    bestD = d;
                    bestAnglesA = a;
                    bestAnglesB = b;
                    System.out.println("New D: " + bestD);
                }
            }
        }
        System.out.println("Finished.");
        System.out.println(Arrays.toString(bestAnglesA));
        System.out.println(Arrays.toString(bestAnglesB));
    }

    private static ArrayList<double[]> loadAngles(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        ArrayList<double[]> as = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] split = line.split(", ");
            double[] angles = new double[3];
            for (int i = 0; i < 3; i++) {
                angles[i] = Double.parseDouble(split[i]);
            }
            as.add(angles);
        }
        return as;
    }

    private static void check() {
//        doCheck(new double[]{1.369395339052881, 1.5170031654456486, -0.545156331242947});
//        doCheck(new double[]{0.8273782152644237, 1.679461226888947, -0.6651430649253559});
        doCheck(new double[]{-0.30445490136842873, 1.4813341715564763, 3.1096544824514547});
//        doCheck(new double[]{-0.2518448229118361, 0.7888339722003247, -3.5288199377961487});
//        doCheck(new double[]{-3.3810112763396876, 1.5696801812009766, -1.5869081909139642});
//        doCheck(new double[]{2.836875620250683, 2.2617505517331575, -1.2408514256355236});
    }

    private static void doCheck(double[] newWP) {
        newWP[0] -= -0 * Math.PI / 180;
        newWP[1] -= (360) * Math.PI / 180;
        newWP[2] -= (-180) * Math.PI / 180;

        OnSatelliteWP onSatelliteWP = new OnSatelliteWP();
        Matrix<Complex> fm = onSatelliteWP.fibreMatrix;
        Matrix<Complex> finalMatrix = HalfWavePlate.create(newWP[2]).getMatrix()
                .times(QuarterWavePlate.create(newWP[1]).getMatrix())
                .times(QuarterWavePlate.create(newWP[0]).getMatrix())
                .times(fm);
        System.out.println(finalMatrix);
        Matrix<Complex> finalState = finalMatrix.times(ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.I.divide(Math.sqrt(2))}}));
        System.out.println(finalState);
        System.out.println();
        for (int i = 0; i < 3; i++) {
            newWP[i] = newWP[i] / Math.PI * 180;
        }
        System.out.println("Logical Angles: " + Arrays.toString(newWP));
        for (int i = 0; i < 3; i++) {
            newWP[i] += onSatelliteWP.zeros[i];
        }
        System.out.println("View Angles: " + Arrays.toString(newWP));
        for (int i = 0; i < 3; i++) {
            newWP[i] -= OnSatelliteWP.comp[i];
        }
        System.out.println("Relative Angles: " + Arrays.toString(newWP));
        System.out.println();
    }

    private static void checkSecialAngles() {
//        double[] angles = new double[]{168.511, 233.498, 68.755};
//        double[] angles = new double[]{137.455, 242.806, 61.880};
//        double[] angles = new double[]{107.976, -100.406, 136.030};
        double[] angles = new double[]{110.990, -140.083, 115.674};
        double[] newWP = new double[3];
        newWP[0] = (angles[0] - zeros[0]) * Math.PI / 180;
        newWP[1] = (angles[1] - zeros[1]) * Math.PI / 180;
        newWP[2] = (angles[2] - zeros[2]) * Math.PI / 180;

        OnSatelliteWP onSatelliteWP = new OnSatelliteWP();
        Matrix<Complex> fm = onSatelliteWP.fibreMatrix;
        Matrix<Complex> finalMatrix = HalfWavePlate.create(newWP[2]).getMatrix()
                .times(QuarterWavePlate.create(newWP[1]).getMatrix())
                .times(QuarterWavePlate.create(newWP[0]).getMatrix())
                .times(fm);
        System.out.println(finalMatrix);

        System.out.println();
        System.out.println("H:");
        Matrix<Complex> finalStateH = finalMatrix.times(ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE}, {Complex.ZERO}}));
        calcDet(finalStateH);
        System.out.println("------------------------------------------------------------------");
        System.out.println("D:");
        Matrix<Complex> finalStateD = finalMatrix.times(ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.ONE.divide(Math.sqrt(2))}}));
        calcDet(finalStateD);
        System.out.println("------------------------------------------------------------------");
        System.out.println("L:");
        Matrix<Complex> finalStateL = finalMatrix.times(ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.I.divide(Math.sqrt(2))}}));
        calcDet(finalStateL);
        System.out.println("------------------------------------------------------------------");
    }

    private static void calcDet(Matrix<Complex> state) {
        Matrix<Complex> H = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE}, {Complex.ZERO}});
        Matrix<Complex> V = ComplexMatrix.valueOf(new Complex[][]{{Complex.ZERO}, {Complex.ONE}});
        Matrix<Complex> D = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.ONE.divide(Math.sqrt(2))}});
        Matrix<Complex> A = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.ONE.divide(-Math.sqrt(2))}});
        Matrix<Complex> L = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.I.divide(Math.sqrt(2))}});
        Matrix<Complex> R = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE.divide(Math.sqrt(2))}, {Complex.I.divide(-Math.sqrt(2))}});

        System.out.println(Math.pow(H.transpose().times(state).get(0, 0).magnitude(), 2));
        System.out.println(Math.pow(V.transpose().times(state).get(0, 0).magnitude(), 2));
        System.out.println(Math.pow(D.transpose().times(state).get(0, 0).magnitude(), 2));
        System.out.println(Math.pow(A.transpose().times(state).get(0, 0).magnitude(), 2));
        System.out.println(Math.pow(L.transpose().times(state).get(0, 0).magnitude(), 2));
        System.out.println(Math.pow(R.transpose().times(state).get(0, 0).magnitude(), 2));
    }
}

package simulationconsole.entanglement;

import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) throws M1ProcessException {
        TwoPhotonState entanglementNoisy = new TwoPhotonState(0.08);
        TwoPhotonState entanglementPurity = new TwoPhotonState();

        System.out.println(entanglementNoisy.contrastHV());
        System.out.println(entanglementNoisy.contrastDA());

        Random random = new Random(1);
        SinglePartyOperator[] randomOperators = new SinglePartyOperator[]{
            HalfWavePlate.create(random.nextDouble() * Math.PI),
            QuarterWavePlate.create(random.nextDouble() * Math.PI),
            QuarterWavePlate.create(random.nextDouble() * Math.PI)
        };
        for (SinglePartyOperator operator : randomOperators) {
            entanglementNoisy = entanglementNoisy.evolution(operator, SinglePartyOperator.I);
            entanglementPurity = entanglementPurity.evolution(operator, SinglePartyOperator.I);
        }

        WavePlate qwp1 = QuarterWavePlate.create(0);
        WavePlate qwp2 = QuarterWavePlate.create(0);
        WavePlate hwp = HalfWavePlate.create(0);

        TwoPhotonState measurement1 = entanglementNoisy
                .evolution(SinglePartyOperator.I, qwp1)
                .evolution(SinglePartyOperator.I, qwp2)
                .evolution(SinglePartyOperator.I, hwp);
        double[][] coincidents1 = measurement1.coincidents();
        double cHH = coincidents1[1][0];
        double cHV = coincidents1[1][1];
        double cHD = coincidents1[1][2];
        double cHA = coincidents1[1][3];
        double cDH = coincidents1[3][0];
        double cDV = coincidents1[3][1];
        double cDD = coincidents1[3][2];
        double cDA = coincidents1[3][3];

        qwp2.increase(-Math.PI / 4);
        hwp.increase(-Math.PI / 8);

        TwoPhotonState measurement2 = entanglementNoisy
                .evolution(SinglePartyOperator.I, qwp1)
                .evolution(SinglePartyOperator.I, qwp2)
                .evolution(SinglePartyOperator.I, hwp);
        double[][] coincidents2 = measurement2.coincidents();
        double cHL = coincidents2[1][0];
        double cHR = coincidents2[1][1];
        double cDL = coincidents2[3][0];
        double cDR = coincidents2[3][1];

        M1Process m1Process = null;
        try {
            m1Process = M1Process.calculate(new double[]{cHH, cHV, cHD, cHA, cHL, cHR, cDH, cDV, cDD, cDA, cDL, cDR});
//            m1Process = M1Process.calculate(new double[]{3071, 1920, 2808, 2191, 85, 4914, 179, 4820, 3316, 1683, 2055, 2944});
        } catch (M1ProcessException ex) {
            ex.printStackTrace(System.out);
        }
        if (m1Process != null) {
            double[] result = m1Process.getResults();
            System.out.println(result[0] / Math.PI * 180);
            System.out.println(result[1] / Math.PI * 180);
            System.out.println(result[2] / Math.PI * 180);
            qwp1.setTheta(result[0]);
            qwp2.setTheta(result[1]);
            hwp.setTheta(result[2]);
            TwoPhotonState measurementFinal = entanglementPurity
                    .evolution(SinglePartyOperator.I, qwp1)
                    .evolution(SinglePartyOperator.I, qwp2)
                    .evolution(SinglePartyOperator.I, hwp);
            double cH = measurementFinal.contrastHV();
            double cD = measurementFinal.contrastDA();
            System.out.println(cH);
            System.out.println(cD);
        }
//        M1Process m1Process = M1Process.calculate(new double[]{
//               IHH = inputs[0];
//        IHV = inputs[1];
//        IHD = inputs[2];
//        IHA = inputs[3];
//        IHL = inputs[4];
//        IHR = inputs[5];
//        IDH = inputs[6];
//        IDV = inputs[7];
//        IDD = inputs[8];
//        IDA = inputs[9];
//        IDL = inputs[10];
//        IDR = inputs[11];
//    });
        //        entanglement.evolution(HalfWavePlate.create(45. / 180 * Math.PI), SinglePartyOperator.I);
        //        entanglement.evolution(QuarterWavePlate.create(45. / 180 * Math.PI), SinglePartyOperator.I);
        //        entanglement.evolution(QuarterWavePlate.create(45. / 180 * Math.PI), SinglePartyOperator.I);
        //        entanglement.show();
        //        System.out.println(entanglement.measurement(SinglePartyOperator.PROJECTION_H, SinglePartyOperator.PROJECTION_H));
    }
}

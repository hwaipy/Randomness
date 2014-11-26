package D20141123.Ryan_PRA_81_053805;

import com.hwaipy.measure.unit.Units;
import com.hwaipy.physics.crystaloptics.Medium;
import com.hwaipy.physics.crystaloptics.Mediums;
import com.hwaipy.physics.crystaloptics.MonochromaticWave;
import org.jscience.physics.amount.Amount;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) {
        MonochromaticWave signal = MonochromaticWave.byWaveLength(Amount.valueOf(810, Units.NANOMETRE));
        MonochromaticWave idler = MonochromaticWave.byWaveLength(Amount.valueOf(810, Units.NANOMETRE));
        Medium medium = Mediums.KTiOPO4;
        double length = 15e-3;
        double polingPeriod = 10.025e-6;
        int orderQPM = 1;
        double waistPump = 50e-6;
        double waistSignal = waistPump * 1;
        double waistIdler = waistSignal;
//        GaussianAmplitudeFunction pumpEnvelope = new GaussianAmplitudeFunction(405e-9, 1e-9);
//        SPDCState SPDCState = new SPDCState(signal, idler, medium, length, polingPeriod, orderQPM,
//                waistPump, waistSignal, waistIdler, pumpEnvelope);
//        SPDCState.value();
        SPDCProbability spdcProbability = new SPDCProbability(signal, idler, medium, length, waistPump, waistSignal, waistIdler);
        System.out.println(spdcProbability.value() * 0.238e16 / 1e6 + "M");
    }
}

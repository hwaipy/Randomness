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
        MonochromaticWave signal = MonochromaticWave.byWaveLength(Amount.valueOf(1500, Units.NANOMETRE));
        MonochromaticWave idler = MonochromaticWave.byWaveLength(Amount.valueOf(1500, Units.NANOMETRE));
        Medium medium = Mediums.KTiOPO4;
        double length = 15e-3;
        double polingPeriod = 45.577e-6;
        int orderQPM = 1;
        double waistPump = 100e-6;
        double waistSignal = waistPump * 0.2665;
        double waistIdler = waistSignal;
        GaussianAmplitudeFunction pumpEnvelope = new GaussianAmplitudeFunction(750e-9, 1e-9);
        SPDCState SPDCState = new SPDCState(signal, idler, medium, length, polingPeriod, orderQPM,
                waistPump, waistSignal, waistIdler, pumpEnvelope);
        System.out.println(SPDCState.value());
    }
}

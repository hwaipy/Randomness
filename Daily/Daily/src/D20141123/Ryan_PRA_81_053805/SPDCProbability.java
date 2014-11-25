package D20141123.Ryan_PRA_81_053805;

import com.hwaipy.measure.unit.Units;
import com.hwaipy.physics.crystaloptics.Axis;
import com.hwaipy.physics.crystaloptics.Medium;
import com.hwaipy.physics.crystaloptics.MonochromaticWave;
import javax.measure.quantity.Frequency;
import javax.measure.unit.SI;
import org.jscience.physics.amount.Amount;

/**
 *
 * @author Hwaipy
 */
public class SPDCProbability {

    private final MonochromaticWave pump;
    private final MonochromaticWave signal;
    private final MonochromaticWave idler;
    private final Medium medium;
    private final double lengthOfCrystal;
    private final double waistPump;
    private final double waistSignal;
    private final double waistIdler;
//    private final double polingPeriod;
//    private final double K;
//    private final int orderQPM;
//    private final double nPump;
//    private final double nSignal;
//    private final double nIdler;
    private final double kPump;
    private final double kSignal;
    private final double kIdler;
    private final double lamdaSignal;
    private final double lamdaIdler;
    private final double deltaK;
    private final double zetaPump;
    private final double zetaSignal;
    private final double zetaIdler;
    private final double zeta;
    private final double A;
    private final double B;

    public SPDCProbability(MonochromaticWave signal, MonochromaticWave idler,
            Medium medium, double length, double wP, double wS, double wI) {
        this.signal = signal;
        this.idler = idler;
        Amount<Frequency> pumpAngularFrequency = signal.getAngularFrequency().plus(idler.getAngularFrequency());
        pump = MonochromaticWave.byAngularFrequency(pumpAngularFrequency);
        this.medium = medium;
        this.lengthOfCrystal = length;
        waistPump = wP;
        waistSignal = wS;
        waistIdler = wI;

        kPump = pump.getWaveNumber(medium, Axis.Y).doubleValue(Units.RECIPROCALMETRE);
        kSignal = signal.getWaveNumber(medium, Axis.Y).doubleValue(Units.RECIPROCALMETRE);
        kIdler = idler.getWaveNumber(medium, Axis.Z).doubleValue(Units.RECIPROCALMETRE);
        deltaK = kPump - kSignal - kIdler;
        zetaPump = lengthOfCrystal / kPump / waistPump / waistPump;
        zetaSignal = lengthOfCrystal / kSignal / waistSignal / waistSignal;
        zetaIdler = lengthOfCrystal / kIdler / waistIdler / waistIdler;
        A = 1 + kSignal * zetaSignal / kPump / zetaPump + kIdler * zetaIdler / kPump / zetaPump;
        B = (1 - deltaK / kPump)
                * (1 + (kSignal + deltaK) * zetaPump / (kPump - deltaK) / zetaSignal
                + (kIdler + deltaK) * zetaPump / (kPump - deltaK) / zetaIdler);
        zeta = B / A * zetaSignal * zetaIdler / zetaPump;
        lamdaSignal = signal.getWaveLength().doubleValue(SI.METRE);
        lamdaIdler = idler.getWaveLength().doubleValue(SI.METRE);
    }

    public double value() {
        double Xeff = 7.6e-12 * 2;
        double value1 = 64 * Math.pow(Math.PI, 3) * Constants.H_BAR
                * Constants.C * Constants.EPSILON
                * medium.getIndex(signal, Axis.Y) * medium.getIndex(idler, Axis.Z)
                / Constants.EPSILON0 / medium.getIndex(pump, Axis.Y)
                / Math.abs(medium.getGroupIndex(signal, Axis.Y) - medium.getGroupIndex(idler, Axis.Z));
        double value2 = Xeff / lamdaSignal / lamdaIdler;
        double value3 = Math.atan(zeta) / A / B;
        return value1 * value2 * value2 * value3;
    }
}

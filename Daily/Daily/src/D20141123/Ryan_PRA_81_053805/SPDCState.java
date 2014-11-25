package D20141123.Ryan_PRA_81_053805;

import com.hwaipy.measure.unit.Units;
import com.hwaipy.physics.crystaloptics.Axis;
import com.hwaipy.physics.crystaloptics.Medium;
import com.hwaipy.physics.crystaloptics.MonochromaticWave;
import javax.measure.quantity.Frequency;
import javax.measure.unit.SI;
import org.jscience.mathematics.number.Complex;
import org.jscience.physics.amount.Amount;

/**
 *
 * @author Hwaipy
 */
public class SPDCState {

    private final MonochromaticWave pump;
    private final MonochromaticWave signal;
    private final MonochromaticWave idler;
    private final Medium medium;
    private final double lengthOfCrystal;
    private final double waistPump;
    private final double waistSignal;
    private final double waistIdler;
    private final double polingPeriod;
    private final double K;
    private final int orderQPM;
    private final double nPump;
    private final double nSignal;
    private final double nIdler;
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
    private final double C;
    private final double Phi;
    private final Function pumpEnvelope;

    public SPDCState(MonochromaticWave signal, MonochromaticWave idler,
            Medium medium, double length, double polingPeriod, int orderQPM,
            double wP, double wS, double wI, Function pumpEnvelope) {
        this.signal = signal;
        this.idler = idler;
        Amount<Frequency> pumpAngularFrequency = signal.getAngularFrequency().plus(idler.getAngularFrequency());
        pump = MonochromaticWave.byAngularFrequency(pumpAngularFrequency);
        this.medium = medium;
        this.lengthOfCrystal = length;
        this.polingPeriod = polingPeriod;
        this.K = 2 * Math.PI / polingPeriod;
        this.orderQPM = orderQPM;
        waistPump = wP;
        waistSignal = wS;
        waistIdler = wI;

        nPump = medium.getIndex(pump, Axis.Y);
        nSignal = medium.getIndex(signal, Axis.Y);
        nIdler = medium.getIndex(idler, Axis.Z);
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
        C = deltaK / kPump * zetaPump * zetaPump / zetaSignal / zetaIdler * A / B / B;
        zeta = B / A * zetaSignal * zetaIdler / zetaPump;
        System.out.println("Zeta = " + zeta);
        Phi = (deltaK + orderQPM * K) * lengthOfCrystal;
        System.out.println("Phi = " + (Phi / Math.PI) + "Pi");
        lamdaSignal = signal.getWaveLength().doubleValue(SI.METRE);
        lamdaIdler = idler.getWaveLength().doubleValue(SI.METRE);
        this.pumpEnvelope = pumpEnvelope;
    }

    public Complex value() {
        double Xeff = 1;
        double Np = 1;
        double value1 = Math.sqrt(8 * Math.PI * Math.PI * Constants.EPSILON
                * Constants.H_BAR * nSignal * nIdler * Np * lengthOfCrystal
                / Constants.EPSILON0 / nPump);
        double value2 = Xeff / lamdaSignal / lamdaIdler;
        double value3 = pumpEnvelope.value(pump.getWaveLength().doubleValue(SI.METRE))
                / Math.sqrt(A * B);
        ComplexIntegrater Fintegrater = new ComplexIntegrater(QPMFunction);
        Complex F = Fintegrater.integrate(-1, 1, 100000);
        System.out.println(F);
        return F.times(value1).times(value2).times(value3);
    }
    private final ComplexFunction QPMFunction = new ComplexFunction() {

        @Override
        public Complex value(double x) {
            Complex exp = Complex.valueOf(0, Phi * x / 2).exp();
            double sqrtZeta = Math.sqrt(zeta);
            Complex denominator = Complex.valueOf(1 - C * zeta * zeta * x * x, -zeta * x);
            return exp.times(sqrtZeta).divide(denominator);
        }
    };
}

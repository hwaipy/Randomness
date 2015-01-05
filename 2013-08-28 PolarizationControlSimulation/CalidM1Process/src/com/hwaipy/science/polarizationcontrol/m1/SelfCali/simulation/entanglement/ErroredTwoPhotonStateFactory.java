package com.hwaipy.science.polarizationcontrol.m1.SelfCali.simulation.entanglement;

import java.util.Random;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import simulationconsole.entanglement.TwoPhotonState;

/**
 *
 * @author Hwaipy
 */
public class ErroredTwoPhotonStateFactory {

    private final Random random = new Random();
    private final double maxDelta;
    private final double maxdelta;

    public ErroredTwoPhotonStateFactory(double maxDelta, double maxdelta) {
        this.maxDelta = maxDelta;
        this.maxdelta = maxdelta;
    }

    public TwoPhotonState generateRandomErroredTwoPhotonState() {
        double Delta = (random.nextDouble() * 2 - 1) * maxDelta;
        double delta = (random.nextDouble() * 2 - 1) * maxdelta;
        return generateErroredTwoPhotonState(maxDelta, maxdelta);
    }

    public static TwoPhotonState generateErroredTwoPhotonState(double Delta, double delta) {
        double A = 1 / Math.sqrt(2 + 2 * Delta + Delta * Delta);
        Complex beta = Complex.valueOf(Math.cos(delta), Math.sin(delta));
        beta = beta.times(1 + Delta).times(-A);
        Complex c1 = Complex.ZERO;
        Complex c2 = Complex.valueOf(A, 0);
        Complex c3 = beta;
        Complex c4 = Complex.ZERO;
        ComplexMatrix psy = ComplexMatrix.valueOf(new Complex[][]{{c1}, {c2}, {c3}, {c4}});
        ComplexMatrix psyDag = ComplexMatrix.valueOf(new Complex[][]{{c1.conjugate(), c2.conjugate(), c3.conjugate(), c4.conjugate()}});
        ComplexMatrix dm = psy.times(psyDag);
        return new TwoPhotonState(dm);
    }
}

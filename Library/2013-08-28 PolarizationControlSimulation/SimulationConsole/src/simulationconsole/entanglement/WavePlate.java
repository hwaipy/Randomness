package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public abstract class WavePlate extends SinglePartyOperator {

    private double theta = 0;

    protected WavePlate() {
        super(null);
    }

    public double getTheta() {
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
        setMatrix(calculateMatrix(theta));
    }

    public void increase(double delta) {
        theta += delta;
        setMatrix(calculateMatrix(theta));
    }

    protected abstract Matrix<Complex> calculateMatrix(double theta);
}

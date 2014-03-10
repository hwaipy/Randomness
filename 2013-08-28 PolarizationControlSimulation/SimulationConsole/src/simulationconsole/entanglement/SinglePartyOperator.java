package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class SinglePartyOperator {

    private static final Matrix<Complex> SINGLE_STATE_H = ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}}).transpose();
    private static final Matrix<Complex> SINGLE_STATE_V = ComplexMatrix.valueOf(new Complex[][]{{Complex.ZERO, Complex.ONE}}).transpose();
    private static final Matrix<Complex> SINGLE_STATE_D = ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(1 / Math.sqrt(2), 0), Complex.valueOf(1 / Math.sqrt(2), 0)}}).transpose();
    private static final Matrix<Complex> SINGLE_STATE_A = ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(1 / Math.sqrt(2), 0), Complex.valueOf(-1 / Math.sqrt(2), 0)}}).transpose();
    public static final SinglePartyOperator I = new SinglePartyOperator(ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.ONE}}));
    public static final SinglePartyOperator PROJECTION_H = new SinglePartyOperator(SINGLE_STATE_H.times(SINGLE_STATE_H.transpose()));
    public static final SinglePartyOperator PROJECTION_V = new SinglePartyOperator(SINGLE_STATE_V.times(SINGLE_STATE_V.transpose()));
    public static final SinglePartyOperator PROJECTION_D = new SinglePartyOperator(SINGLE_STATE_D.times(SINGLE_STATE_D.transpose()));
    public static final SinglePartyOperator PROJECTION_A = new SinglePartyOperator(SINGLE_STATE_A.times(SINGLE_STATE_A.transpose()));
    private Matrix<Complex> matrix;

    SinglePartyOperator(Matrix<Complex> matrix) {
        this.matrix = matrix;
    }

    protected void setMatrix(Matrix<Complex> m) {
        matrix = m;
    }

    public Matrix<Complex> getMatrix() {
        return matrix;
    }
}

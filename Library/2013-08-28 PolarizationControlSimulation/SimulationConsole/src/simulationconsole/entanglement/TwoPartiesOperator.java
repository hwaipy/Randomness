package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class TwoPartiesOperator {

    private final Matrix<Complex> matrix;

    TwoPartiesOperator(Matrix<Complex> m) {
        matrix = m;
    }

    TwoPartiesOperator(SinglePartyOperator o1, SinglePartyOperator o2) {
        matrix = o1.getMatrix().tensor(o2.getMatrix());
    }

    public Matrix<Complex> getMatrix() {
        return matrix;
    }
}

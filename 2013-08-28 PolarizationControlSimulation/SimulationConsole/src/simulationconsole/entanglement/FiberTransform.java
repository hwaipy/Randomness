package simulationconsole.entanglement;

import java.util.Random;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class FiberTransform extends SinglePartyOperator {

    private FiberTransform(Matrix<Complex> matrix) {
        super(matrix);
    }

    public static FiberTransform createRandomFiber(Random random) {
        HalfWavePlate hwp = HalfWavePlate.create(random.nextDouble() * Math.PI);
        QuarterWavePlate qwp2 = QuarterWavePlate.create(random.nextDouble() * Math.PI);
        QuarterWavePlate qwp1 = QuarterWavePlate.create(random.nextDouble() * Math.PI);
        Matrix<Complex> transform = hwp.getMatrix().times(qwp2.getMatrix()).times(qwp1.getMatrix());
        return new FiberTransform(transform);
//        return new FiberTransform(I.getMatrix());
    }

    public static FiberTransform createReverse(double qwp1, double qwp2, double hwp) {
        HalfWavePlate hWP = HalfWavePlate.create(hwp);
        QuarterWavePlate qWP2 = QuarterWavePlate.create(qwp2);
        QuarterWavePlate qWP1 = QuarterWavePlate.create(qwp1);
        Matrix<Complex> transform = hWP.getMatrix().times(qWP2.getMatrix()).times(qWP1.getMatrix());
        return new FiberTransform(transform);
    }
}

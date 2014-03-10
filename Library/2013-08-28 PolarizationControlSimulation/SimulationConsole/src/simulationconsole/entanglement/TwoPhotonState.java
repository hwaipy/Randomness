package simulationconsole.entanglement;

import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class TwoPhotonState {

    private final Matrix<Complex> densityMatrix;

    public TwoPhotonState() {
        ComplexMatrix psy = ComplexMatrix.valueOf(new Complex[][]{{Complex.ZERO, Complex.ONE, Complex.valueOf(-1, 0), Complex.ZERO}});
        densityMatrix = psy.transpose().times(psy).times(Complex.valueOf(0.5, 0));
    }

    public TwoPhotonState(double noise) {
        ComplexMatrix psy = ComplexMatrix.valueOf(new Complex[][]{{Complex.ZERO, Complex.ONE, Complex.valueOf(-1, 0), Complex.ZERO}});
        ComplexMatrix I = ComplexMatrix.valueOf(new Complex[][]{
            {Complex.ONE, Complex.ZERO, Complex.ZERO, Complex.ZERO},
            {Complex.ZERO, Complex.ONE, Complex.ZERO, Complex.ZERO},
            {Complex.ZERO, Complex.ZERO, Complex.ONE, Complex.ZERO},
            {Complex.ZERO, Complex.ZERO, Complex.ZERO, Complex.ONE}
        }).times(Complex.valueOf(0.25, 0));
        ComplexMatrix rouPsy = psy.transpose().times(psy).times(Complex.valueOf(0.5, 0));
        ComplexMatrix noisedRouPsy = rouPsy.times(Complex.valueOf(1 - noise, 0));
        ComplexMatrix noisedI = I.times(Complex.valueOf(noise, 0));
        densityMatrix = noisedRouPsy.plus(noisedI);
    }

    private TwoPhotonState(Matrix<Complex> m) {
        densityMatrix = m;
    }

    public void show() {
        System.out.println(densityMatrix);
    }

    public TwoPhotonState evolution(SinglePartyOperator o1, SinglePartyOperator o2) {
        return evolution(new TwoPartiesOperator(o1, o2));
    }

    public TwoPhotonState evolution(TwoPartiesOperator o) {
        Matrix<Complex> operation = o.getMatrix();
        Matrix<Complex> operationDag = dag(operation);
        return new TwoPhotonState(operation.times(densityMatrix).times(operationDag));
    }

    public double measurement(SinglePartyOperator o1, SinglePartyOperator o2) {
        return measurement(new TwoPartiesOperator(o1, o2));
    }

    public double measurement(TwoPartiesOperator o) {
        Matrix<Complex> measurement = o.getMatrix();
        Matrix<Complex> measurementDag = dag(measurement);
        Matrix<Complex> result = measurement.times(densityMatrix).times(measurementDag);
        int maxR = 0;
        int maxC = 0;
        double max = -1;
        for (int r = 0; r < measurement.getNumberOfRows(); r++) {
            for (int c = 0; c < measurement.getNumberOfColumns(); c++) {
                Complex complex = measurement.get(r, c);
                double value = complex.times(complex.conjugate()).getReal();
                if (value > max) {
                    max = value;
                    maxR = r;
                    maxC = c;
                }
            }
        }
        Complex complex = result.get(maxR, maxC);
        double resultValue = complex.times(complex.conjugate()).getReal();
        return Math.sqrt(resultValue / max);
    }

    public double[][] coincidents() {
        double[][] results = new double[4][4];
        SinglePartyOperator[] operators = new SinglePartyOperator[]{
            SinglePartyOperator.PROJECTION_H, SinglePartyOperator.PROJECTION_V,
            SinglePartyOperator.PROJECTION_D, SinglePartyOperator.PROJECTION_A
        };
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                results[i][j] = measurement(operators[i], operators[j]);
            }
        }
        return results;
    }

    public double contrastHV() {
        double[][] coincidents = coincidents();
        return (coincidents[0][1] + coincidents[1][0]) / (coincidents[0][0] + coincidents[1][1]);
    }

    public double contrastDA() {
        double[][] coincidents = coincidents();
        return (coincidents[2][3] + coincidents[3][2]) / (coincidents[2][2] + coincidents[3][3]);
    }

    private static Matrix<Complex> dag(Matrix<Complex> m) {
        Matrix<Complex> transpose = m.transpose();
        Complex[][] data = new Complex[transpose.getNumberOfRows()][transpose.getNumberOfColumns()];
        for (int r = 0; r < data.length; r++) {
            for (int c = 0; c < data[0].length; c++) {
                data[r][c] = transpose.get(r, c).conjugate();
            }
        }
        return ComplexMatrix.valueOf(data);
    }
}

package D20141110.EigenstateCalculate;

import java.util.Arrays;
import org.jscience.mathematics.number.Float64;
import org.jscience.mathematics.vector.Float64Matrix;
import org.netlib.lapack.DGEEV;
import org.netlib.util.intW;

/**
 *
 * @author Hwaipy
 */
public class EigenstateCalc {

    public static void main(String[] args) {
        Function potentialFunction = new ParabolicFunction(1);
        double xStart = -3;
        double xEnd = 3;
        int dimension = 400;
        double m = 1 / 4.;

        double dx = (xEnd - xStart) / (dimension - 1);
        //Define V
        double[][] Vvalue = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            double x = xStart + dx * i;
            Vvalue[i][i] = potentialFunction.valueOf(x);
        }
        Float64Matrix V = Float64Matrix.valueOf(Vvalue);

        //Define D
        double[][] Dvalue = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            Dvalue[i][i] = -2;
            if (i > 0) {
                Dvalue[i][i - 1] = 1;
            }
            if (i < dimension - 1) {
                Dvalue[i][i + 1] = 1;
            }
        }
        Float64Matrix D = Float64Matrix.valueOf(Dvalue);
        D = D.times(Float64.valueOf(1 / dx / dx));

        //Calc M
        Float64Matrix M = V.minus(D.times(Float64.valueOf(2)));
        double[][] Mvalue = new double[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                Mvalue[i][j] = M.get(i, j).doubleValue();
            }
        }

        //Test
        String JOBVL = "N";
        String JOBVR = "V";
        int N = dimension;
        double[][] A = Mvalue;
        int LDA = N;
        double[] WR = new double[N];
        double[] WI = new double[N];
        int LDVL = 1;
        double[][] VL = new double[LDVL][N];
        int LDVR = N;
        double[][] VR = new double[LDVR][N];
        int LWORK = 100 * N;
        double[] WORK = new double[LWORK];
        intW INFO = new intW(-1);
        long startTime = System.nanoTime();
        DGEEV.DGEEV(JOBVL, JOBVR, N, A, WR, WI, VL, VR, WORK, LWORK, INFO);
        long endTime = System.nanoTime();
        System.out.println((endTime - startTime) / 1000000000.);
        System.out.println(Arrays.toString(WR));
    }
}

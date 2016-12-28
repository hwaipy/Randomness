package telescopecalibration;

import java.util.ArrayList;
import org.jscience.mathematics.number.Complex;
import org.jscience.mathematics.vector.ComplexMatrix;
import org.jscience.mathematics.vector.Matrix;

/**
 *
 * @author Hwaipy
 */
public class RemoteCali {

  public static void main(String[] args) {
    String srcS = "83.32010112\n"
            + "83.11483393\n"
            + "82.89939033\n"
            + "82.67330472\n"
            + "82.4361044\n"
            + "82.18729943\n"
            + "81.92636593\n"
            + "81.65279152\n"
            + "81.36601655\n"
            + "81.0654812\n"
            + "80.75060205\n"
            + "80.42077683\n"
            + "80.07540082\n"
            + "79.71383447\n"
            + "79.3354364\n"
            + "78.93955588\n"
            + "78.52551176\n"
            + "78.09264109\n"
            + "77.64024829\n"
            + "77.16764585\n"
            + "76.67414022\n"
            + "76.15903802\n"
            + "75.62164881\n"
            + "75.06129112\n"
            + "74.47729781\n"
            + "73.86900735\n"
            + "73.23578858\n"
            + "72.57703195\n"
            + "71.89215864\n"
            + "71.18062777\n"
            + "70.4419395\n"
            + "69.67564437\n"
            + "68.88134774\n"
            + "68.05871424\n"
            + "67.20747579\n"
            + "66.32744365\n"
            + "65.41850454\n"
            + "64.48063313\n"
            + "63.513893\n"
            + "62.51845241\n"
            + "61.49456913\n"
            + "60.4426029\n"
            + "59.36303848\n"
            + "58.25644855\n"
            + "57.12352534\n"
            + "55.96507024\n"
            + "54.78197933\n"
            + "53.57526592\n"
            + "52.34603293\n"
            + "51.0954752\n"
            + "49.82488556\n"
            + "48.53562706\n"
            + "47.2291314\n"
            + "45.90690456\n"
            + "44.57048832\n"
            + "43.22146715\n"
            + "41.86146599\n"
            + "40.49212612\n"
            + "39.11507974\n"
            + "37.73199249\n"
            + "36.34446513\n"
            + "34.95413215\n"
            + "33.56255181\n"
            + "32.17127122\n"
            + "30.78176739\n"
            + "29.39547703\n"
            + "28.01376949\n"
            + "26.63796634\n"
            + "25.269299\n"
            + "23.90893259\n"
            + "22.55797001\n"
            + "21.21740823\n"
            + "19.88822918\n"
            + "18.57126971\n"
            + "17.26734019\n"
            + "15.977163\n"
            + "14.70138885\n"
            + "13.44058641\n"
            + "12.19530273\n"
            + "10.96594929\n"
            + "9.752956702\n"
            + "8.556638537\n"
            + "7.377278958\n"
            + "6.215107778\n"
            + "5.070311406\n"
            + "3.943024021\n"
            + "2.833334436\n"
            + "1.741303546\n"
            + "0.666965531\n"
            + "-0.389708102\n"
            + "-1.4287455\n"
            + "-2.450219865\n"
            + "-3.454216086\n"
            + "-4.44085766\n"
            + "-5.410278443\n"
            + "-6.362615442\n"
            + "-7.298050739\n"
            + "-8.216769821\n"
            + "-9.118958219\n"
            + "-10.00481591\n"
            + "-10.87457136\n"
            + "-11.72844565\n"
            + "-12.56665956\n"
            + "-13.38946252\n"
            + "-14.19708358\n"
            + "-14.98976607\n"
            + "-15.76776835\n"
            + "-16.53132368\n"
            + "-17.28070352\n"
            + "-18.0161386\n"
            + "-18.73789434\n"
            + "-19.44621525\n"
            + "-20.14135143\n"
            + "-20.82355225\n"
            + "-21.49306707\n"
            + "-22.15014152\n"
            + "-22.79501511\n"
            + "-23.42792294\n"
            + "-24.04911918\n"
            + "-24.65882348\n"
            + "-25.25727034\n"
            + "-25.84469546\n"
            + "-26.42130339\n"
            + "-26.98733948\n"
            + "-27.54300938\n"
            + "-28.08851968\n"
            + "-28.62409011\n"
            + "-29.14992986";

    ArrayList<Double> zitais = new ArrayList<>();
    for (String src : srcS.split("\n")) {
      zitais.add(Double.parseDouble(src));
    }
    for (double zitai : zitais) {
      double state = 0;
      double phase = 145 / 180. * Math.PI;
      double rotate = zitai / 180. * Math.PI;
      Matrix<Complex> output = transform(getLinearPolState(state),
              rotate(rotate),
              phase(phase),
              rotate(-rotate)
      );
      double h = (contrast(output, state + 90 / 180.0 * Math.PI));
      double v = (contrast(output, state));
      double d = (contrast(output, state - 45 / 180.0 * Math.PI));
      double a = (contrast(output, state + 45 / 180.0 * Math.PI));
      System.out.println(h + "\t" + v + "\t" + d + "\t" + a);
    }
  }

  private static double contrast(Matrix<Complex> state, double measureTheta) {
    double m = measure(state, measureTheta);
    return m / (1 - m);
  }

  private static double measure(Matrix<Complex> state, double measureTheta) {
    if (state.getNumberOfRows() != 2 || state.getNumberOfColumns() != 1) {
      throw new RuntimeException();
    }
    Complex r = getLinearPolState(measureTheta).transpose().times(state).get(0, 0);
    return Math.pow(r.magnitude(), 2);
  }

  private static Matrix<Complex> getLinearPolState(double theta) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(Math.cos(theta), 0)}, {Complex.valueOf(Math.sin(theta), 0)}});
  }

  private static Matrix<Complex> phase(double p) {
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.ONE, Complex.ZERO}, {Complex.ZERO, Complex.valueOf(Math.cos(p), Math.sin(p))}});
  }

  private static Matrix<Complex> rotate(double t) {
    t *= 1;
    return ComplexMatrix.valueOf(new Complex[][]{{Complex.valueOf(Math.cos(t), 0), Complex.valueOf(Math.sin(t), 0)}, {Complex.valueOf(-Math.sin(t), 0), Complex.valueOf(Math.cos(t), 0)}});
  }

  private static Matrix<Complex> transform(Matrix<Complex> input, Matrix<Complex>... Us) {
    Matrix<Complex> state = input;
    for (Matrix<Complex> U : Us) {
      state = U.times(state);
    }
    return state;
  }
}

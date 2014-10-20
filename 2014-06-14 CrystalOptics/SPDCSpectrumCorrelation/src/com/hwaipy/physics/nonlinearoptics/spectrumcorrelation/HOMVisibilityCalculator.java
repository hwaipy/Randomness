package com.hwaipy.physics.nonlinearoptics.spectrumcorrelation;

/**
 *
 * @author Hwaipy
 */
public class HOMVisibilityCalculator {

    private final double minArg1;
    private final double maxArg1;
    private final double minArg2;
    private final double maxArg2;
    private final double[][] correlations;
    private final CorrelationFunction function;
    private final int dimension;

    public HOMVisibilityCalculator(double minArg1, double maxArg1, double minArg2, double maxArg2,
            CorrelationFunction function, int dimension) {
        this.minArg1 = minArg1;
        this.maxArg1 = maxArg1;
        this.minArg2 = minArg2;
        this.maxArg2 = maxArg2;
        correlations = new double[dimension][dimension];
        this.function = function;
        this.dimension = dimension;
    }

    public double calculate() {
        //result第一维是arg2，第二维是arg1
        double stepOfArg1 = (maxArg1 - minArg1) / (dimension - 1);
        double stepOfArg2 = (maxArg2 - minArg2) / (dimension - 1);
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                double arg1 = minArg1 + x * stepOfArg1;
                double arg2 = minArg2 + y * stepOfArg2;
                double result = function.value(arg1, arg2);
                correlations[y][x] = result;
            }
        }
        double squreSum = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                double value = correlations[j][i];
                squreSum += value * value;
            }
        }
        double c = 1 / Math.sqrt(squreSum);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                correlations[j][i] *= c;
            }
        }

        double A = 0;
        double E = 0;
        for (int i1 = 0; i1 < dimension; i1++) {
            for (int j1 = 0; j1 < dimension; j1++) {
                for (int i2 = 0; i2 < dimension; i2++) {
                    for (int j2 = 0; j2 < dimension; j2++) {
                        A += Math.pow(correlations[i2][i1] * correlations[j2][j1], 2);
                        E += (correlations[i2][i1] * correlations[j2][j1] * correlations[i2][j1] * correlations[j2][i1]);
                    }
                }
            }
        }
        return E / A;
    }
}

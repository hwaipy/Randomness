package D20150303.FourFoldSimulation;

import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class FourFoldSimulation {

  private static final Random random = new Random();

  private static int process(ProcessMode mode, double p, double etaT, double etaS, int dark, long frequancy, int time) {
    long count = frequancy * time;
    double darkRate = dark / frequancy;
    Fork fork = new Fork(p, Statistic.THERMAL);
    int coincidence = 0;
    for (int i = 0; i < count; i++) {
      boolean isCoincidence = false;
      switch (mode) {
        case MATCH:
          int result = shoot(fork, etaT, etaS, darkRate);
          isCoincidence = (result == 15);
          break;
        case OFFSET:
          int result1 = shoot(fork, etaT, etaS, darkRate);
          int result2 = shoot(fork, etaT, etaS, darkRate);
          result1 &= 0b1100;
          result2 &= 0b0011;
          isCoincidence = ((result1 == 0b1100) && (result2 == 0b0011));
          break;
        default:
          throw new RuntimeException();
      }
      if (isCoincidence) {
        coincidence++;
      }
    }
    return coincidence;
  }

  private static int shoot(Fork fork, double etaT, double etaS, double darkRate) {
    int fork1 = fork.nextFork();
    int fork2 = fork.nextFork();
    int detectorT1 = random.nextDouble() < darkRate ? 1 : 0;
    int detectorT2 = random.nextDouble() < darkRate ? 1 : 0;
    int detectorS1 = random.nextDouble() < darkRate ? 1 : 0;
    int detectorS2 = random.nextDouble() < darkRate ? 1 : 0;
    if (fork1 != 0) {
      detectorT1 += attenuation(fork1, etaT);
      int S1 = attenuation(fork1, etaS);
      int[] bs1 = beamSpliter(S1);
      detectorS1 += bs1[0];
      detectorS2 += bs1[1];
    }
    if (fork2 != 0) {
      detectorT2 += attenuation(fork2, etaT);
      int S2 = attenuation(fork2, etaS);
      int[] bs2 = beamSpliter(S2);
      detectorS1 += bs2[0];
      detectorS2 += bs2[1];
    }
    int result = 0;
    if (detectorT1 > 0) {
      result |= 0b1000;
    }
    if (detectorS1 > 0) {
      result |= 0b0100;
    }
    if (detectorS2 > 0) {
      result |= 0b0010;
    }
    if (detectorT2 > 0) {
      result |= 0b0001;
    }
    return result;
  }

  private static int[] beamSpliter(int fork) {
    int port1 = 0;
    for (int i = 0; i < fork; i++) {
      if (random.nextDouble() < 0.5) {
        port1++;
      }
    }
    return new int[]{port1, fork - port1};
  }

  private static int attenuation(int fork, double eta) {
    int result = 0;
    for (int i = 0; i < fork; i++) {
      if (random.nextDouble() < eta) {
        result++;
      }
    }
    return result;
  }

  private static class Fork {

    private final double[] range = new double[3];

    private Fork(double p, Statistic statistic) {
      double p3 = p * p * p * statistic.getFrac(3);
      double p2 = p * p * statistic.getFrac(2);
      double p1 = p;
      range[0] = 1 - p1 - p2 - p3;
      range[1] = range[0] + p1;
      range[2] = range[1] + p2;
    }

    private int nextFork() {
      double rd = random.nextDouble();
      if (rd < range[0]) {
        return 0;
      }
      else if (rd < range[1]) {
        return 1;
      }
      else if (rd < range[2]) {
        return 2;
      }
      else {
        return 3;
      }
    }

  }

//  private static class Random {
//
//    java.util.Random random = new java.util.Random();
//    private static final int LENGTH = 3571;
//    private static final double seed = Math.PI - 3;
//    private final double[] values = new double[LENGTH];
//    private int position = 0;
//
//    private Random() {
//      for (int i = 0; i < values.length; i++) {
//        values[i] = random.nextDouble();
//      }
//    }
//
//    private double nextDouble() {
//      if (position >= LENGTH) {
//        position = 0;
//        for (int i = 0; i < LENGTH; i++) {
//          values[i] += seed;
//          if (values[i] >= 1) {
//            values[i] -= 1;
//          }
//        }
//        values[random.nextInt(LENGTH)] = random.nextDouble();
//      }
//      double value = values[position];
//      position++;
//      return value;
//    }
//
//  }
  private enum ProcessMode {

    MATCH, OFFSET;
  }

  private enum Statistic {

    POISSON {
              @Override
              double getFrac(int fork) {
                if (fork == 2) {
                  return 0.5;
                }
                else if (fork == 3) {
                  return 1. / 6;
                }
                else {
                  throw new RuntimeException();
                }
              }

            },
    THERMAL {
              @Override
              double getFrac(int fork) {
                return 1;
              }

            };

    abstract double getFrac(int fork);

  }

  public static void main(String[] args) {
    double p = 0.01;
    double etaT = 0.1;
    double etaS = 1;
    int dark = 20000000;
    long frequancy = 7600000;
    int time = 10;

    long startTime = System.nanoTime();
    int coincidence1 = process(ProcessMode.MATCH, p, etaT, etaS, dark, frequancy, time);
    System.out.println(coincidence1);
    int coincidence2 = process(ProcessMode.OFFSET, p, etaT, etaS, dark, frequancy, time);
    System.out.println(coincidence2);
    System.out.println(coincidence1 / (double) coincidence2);
    long endTime = System.nanoTime();
    System.out.println(((endTime - startTime) / 1000000000.) + "s");
  }

  private static void testAttenuation() {
    int result = 0;
    for (int i = 0; i < 1000000; i++) {
      result += attenuation(5, 0.1);
    }
    System.out.println(result / 1000000.);//About 0.5
  }

  private static void testFork() {
    Fork poissonFork = new Fork(0.01, Statistic.POISSON);
    Fork thermalFork = new Fork(0.01, Statistic.THERMAL);
    int[] result = new int[4];
    for (int i = 0; i < 100000000; i++) {
      int shoot = poissonFork.nextFork();
      result[shoot]++;
    }
    System.out.println(Arrays.toString(result));//About 894833,100000,5000,167
    result = new int[4];
    for (int i = 0; i < 100000000; i++) {
      int shoot = thermalFork.nextFork();
      result[shoot]++;
    }
    System.out.println(Arrays.toString(result));//About 889000,100000,10000,1000
  }

}

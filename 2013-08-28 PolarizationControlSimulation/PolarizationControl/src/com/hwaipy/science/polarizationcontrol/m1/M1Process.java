package com.hwaipy.science.polarizationcontrol.m1;

import Jama.Matrix;
import com.hwaipy.science.polarizationcontrol.device.QuarterWavePlate;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author HwaipyLab
 */
public class M1Process {

  private final double IHH;
  private final double IHV;
  private final double IHD;
  private final double IHA;
  private final double IHL;
  private final double IHR;
  private final double IDH;
  private final double IDV;
  private final double IDD;
  private final double IDA;
  private final double IDL;
  private final double IDR;
  private double normalizeParaSH;
  private double normalizeParaSR;
  private double[] results;

  private M1Process(double[] inputs) throws M1ProcessException {
    if (inputs.length != 12) {
      throw new IllegalArgumentException();
    }
    IHH = inputs[0];
    IHV = inputs[1];
    IHD = inputs[2];
    IHA = inputs[3];
    IHL = inputs[4];
    IHR = inputs[5];
    IDH = inputs[6];
    IDV = inputs[7];
    IDD = inputs[8];
    IDA = inputs[9];
    IDL = inputs[10];
    IDR = inputs[11];
    calculate();
  }

  private M1Process(double[] inputs, double[] effction) throws M1ProcessException {
    if (inputs.length != 12 || effction.length != 4) {
      throw new IllegalArgumentException();
    }
    IHH = inputs[0] / effction[3];
    IHV = inputs[1] / effction[2];
    IHD = inputs[2] / effction[0];
    IHA = inputs[3] / effction[1];
    IHL = inputs[4] / effction[3];
    IHR = inputs[5] / effction[2];
    IDH = inputs[6] / effction[3];
    IDV = inputs[7] / effction[2];
    IDD = inputs[8] / effction[0];
    IDA = inputs[9] / effction[1];
    IDL = inputs[10] / effction[3];
    IDR = inputs[11] / effction[2];
    System.out.println(IHH + ", " + IHV + ", " + IHD + ", " + IHA + ", " + IHL + ", " + IHR + ", " + IDH + ", " + IDV + ", " + IDD + ", " + IDA + ", " + IDL + ", " + IDR);
    System.out.println(IHH + IHV - IHD);
    System.out.println(IDH + IDV - IDD);
    calculate();
  }

  public static M1Process calculate(double[] inputs) throws M1ProcessException {
    return new M1Process(inputs);
  }

  public static M1Process calculate(double[] inputs, double[] efficiences) throws M1ProcessException {
    return new M1Process(inputs, efficiences);
  }

  private void calculate() throws M1ProcessException {
    double[] sh = new double[]{(IHH - IHV) / (IHH + IHV), (IHD - IHA) / (IHD + IHA), (IHR - IHL) / (IHR + IHL)};
    double[] sd = new double[]{(IDH - IDV) / (IDH + IDV), (IDD - IDA) / (IDD + IDA), (IDR - IDL) / (IDR + IDL)};
    normalizeParaSH = normalize(sh);
    normalizeParaSR = normalize(sd);
//        System.out.println(Arrays.toString(sh));
//        System.out.println(Arrays.toString(sd));

    double[] sr = new double[]{sh[1] * sd[2] - sh[2] * sd[1], sh[2] * sd[0] - sh[0] * sd[2], sh[0] * sd[1] - sh[1] * sd[0]};
//        System.out.println(Arrays.toString(sr));

    double m = Math.sqrt(Math.pow(sr[0], 2) + Math.pow(sr[1], 2) + Math.pow(sr[2], 2));
    Matrix SH = new Matrix(new double[][]{{1, sh[0], sh[1], sh[2]}}).transpose();
    Matrix SR = new Matrix(new double[][]{{1, sr[0] / m, sr[1] / m, sr[2] / m}}).transpose();
    double theta1 = 0.5 * Math.atan(SR.get(2, 0) / SR.get(1, 0));

    QuarterWavePlate qwp1 = new QuarterWavePlate(theta1);
    Matrix SR1 = qwp1.getMatrix().times(SR);
    Matrix SH1 = qwp1.getMatrix().times(SH);

//        System.out.println("SR:");
//        System.out.println(SR.get(1, 0));
//        System.out.println(SR.get(2, 0));
//        System.out.println(SR.get(3, 0));
//        System.out.println("SR1:");
//        System.out.println(SR1.get(1, 0));
//        System.out.println(SR1.get(2, 0));
//        System.out.println("SH1");
//        System.out.println(SH1.get(1, 0));
//        System.out.println(SH1.get(2, 0));
//        System.out.println(SH1.get(3, 0));
    double a2 = 0.5 * Math.atan(SH1.get(2, 0) / SH1.get(1, 0));
    double a3 = 0.5 * Math.asin(SH1.get(3, 0));
//        System.out.println("a2=arctan " + SH1.get(2, 0) + "/" + SH1.get(1, 0));
//        System.out.println("a2=" + a2);
//        System.out.println("a3=" + a3);
    results = new double[3];
    results[0] = theta1;

//        System.out.println(SH1.get(1, 0));
//        System.out.println(SH1.get(2, 0));
//        System.out.println(SH1.get(3, 0));
//        System.out.println(SR1.get(1, 0));
//        System.out.println(SR1.get(2, 0));
    int flag = 0;
    if (SH1.get(1, 0) > 0) {
      flag |= 1 << 4;
    }
    if (SH1.get(2, 0) > 0) {
      flag |= 1 << 3;
    }
    if (SH1.get(3, 0) > 0) {
      flag |= 1 << 2;
    }
    if (SR1.get(1, 0) > 0) {
      flag |= 1 << 1;
    }
    if (SR1.get(2, 0) > 0) {
      flag |= 1;
    }
//        System.out.println(flag);
    switch (flag) {
      case 23:
      case 19:
      case 29:
      case 25:
        //A
        results[1] = a2;
        results[2] = (a2 + a3) / 2;
        break;
      case 15:
        //B
        results[1] = a2;
        results[2] = (a2 - a3) / 2 + Math.PI / 4;
        break;
      case 11:
      case 5:
      case 1:
//                System.out.println(123);
        //C
        results[1] = a2;
        results[2] = (a2 - a3) / 2;
        results[2] = results[2] > 0 ? results[2] - Math.PI / 4 : results[2] + Math.PI / 4;
        break;
      case 20:
      case 16:
      case 30:
      case 26:
        //D
        results[1] = a2 + Math.PI / 2;
        results[2] = (a2 - a3) / 2;
        break;
      case 6:
        //E
        results[1] = a2 + Math.PI / 2;
        results[2] = (a2 + a3) / 2 - Math.PI / 4;
        break;
      case 8:
        //F
        results[1] = a2 + Math.PI / 2;
        results[2] = (a2 + a3) / 2 + Math.PI / 4;
        break;
      case 12:
      case 2:
        //G
        results[1] = a2 + Math.PI / 2;
        results[2] = (a2 + a3) / 2;
        results[2] = results[2] > 0 ? results[2] - Math.PI / 4 : results[2] + Math.PI / 4;
        break;
      default:
        //Z
        throw new M1ProcessException();
    }
  }

  private double trim(double theta) {
    double t = theta;
    while (t < Math.PI / 2) {
      t += Math.PI;
    }
    while (t > Math.PI / 2) {
      t -= Math.PI;
    }
    return t;
  }

  private double normalize(double[] data) {
    if (data.length != 3) {
      throw new IllegalArgumentException();
    }
    double c = 0;
    for (double d : data) {
      c += d * d;
    }
    c = Math.sqrt(c);
    for (int i = 0; i < 3; i++) {
      data[i] /= c;
    }
    return c;
  }

  public double getNormalizeParaSH() {
    return normalizeParaSH;
  }

  public double getNormalizeParaSR() {
    return normalizeParaSR;
  }

  public double getTheta1() {
    return results[0];
  }

  public double getTheta2() {
    return results[1];
  }

  public double getTheta3() {
    return results[2];
  }

  public double[] getResults() {
    return Arrays.copyOf(results, 3);
  }

  public static void main(String[] args) throws M1ProcessException, IOException {
    //        M1Process m1p = new M1Process(new double[]{0.36, 1.63, 0.208, 1.78, 1.063, 0.918, 0.501, 0.0718, 0.0995, 0.475, 0.265, 0.301});
    //        M1Process m1p = new M1Process(new double[]{242, 1908, 364.5, 1780, 987, 1171, 264, 30.17, 236, 56.4, 156.5, 138.3});
    //        M1Process m1p = new M1Process(new double[]{252.5, 1954, 370.2, 1834, 1003, 1205, 548.5, 129.0, 82.45, 593.5, 255.0, 424.0});
    //        M1Process m1p = new M1Process(new double[]{267.2, 2054, 385.9, 1923, 1070, 1250, 588.5, 136.0, 89.65, 636.5, 274.5, 450.0});
    //        M1Process m1p = new M1Process(new double[]{265.3, 2051, 1878, 420, 1025, 1269, 577.2, 132.6, 624.6, 79.85, 289.9, 415.1});
    //        M1Process m1p = new M1Process(new double[]{1783, 1240, 32.77, 3033, 1503, 1534, 37.02, 565.7, 229.5, 367.2, 425.4, 174.3});
    //        M1Process m1p = new M1Process(new double[]{1840, 1228, 18, 3030.51, 1517, 1548, 36.02, 561.1, 231.82, 372.32, 420.0, 174.6});
    //        M1Process m1p = new M1Process(new double[]{2232, 876.5, 2994, 114.5, 1493, 1615, 579.3, 37.53, 189.3, 427.53, 408.7, 205.0});
    //        M1Process m1p = new M1Process(new double[]{352.8, 268.7, 1098, 14.14, 249.2, 414.2, 129.7, 5.201, 103.1, 161.3, 93.3, 65.09},
    //                new double[]{674.5 / 1494, 796.5 / 1768, 91.08 / 302.8, 756.6 / 3275});
    //        M1Process m1p = new M1Process(new double[]{1527.1, 893.3, 2389.0, 31.39, 1078.7, 1377.0, 561.4, 17.29, 220.7, 358.0, 403.9, 216.4});//+ bad
    //        M1Process m1p = new M1Process(new double[]{1527.1, 893.3, 2432.0, -11.61, 1078.7, 1377.0, 561.4, 17.29, 228.4, 350.3, 403.9, 216.4});//- bad
    //        M1Process m1p = new M1Process(new double[]{321.0, 368.5, 1235, 7.055, 308.1, 394.5, 154.3, 3.182, 148.7, 157.3, 62.30, 125.1},
    //                new double[]{674.5 / 1494, 796.5 / 1768, 91.08 / 302.8, 756.6 / 3275});
    //        M1Process m1p = new M1Process(new double[]{1519.3, 1225.1, 2728.7, 15.7, 1333.6, 1311.5, 667.9, 10.6, 329.3, 349.2, 269.7, 415.9});//+ bad
    //        M1Process m1p = new M1Process(new double[]{1519.3, 1225.1, 2735.5, 8.9, 1333.6, 1311.5, 667.9, 10.6, 329.4, 349.1, 269.7, 415.9});//- bad
    //        M1Process m1p = new M1Process(new double[]{1236, 659.5, 1855, 42.68, 947, 938.6, 1002, 30.52, 371.7, 653.2, 448.2, 579.3});
    //        M1Process m1p = new M1Process(new double[]{692.3, 368.8, 1028, 23.80, 534.4, 543.3, 1263, 38.63, 474.8, 823.4, 587.9, 762.5});
    //        M1Process m1p = new M1Process(new double[]{43.84, 17.38, 93.1, 1.278, 26.47, 25.32, 1250, 35.31, 454.6, 819.3, 363.0, 476.7});
    //        M1Process m1p = new M1Process(new double[]{958.5, 555.8, 1473, 24.58, 775.2, 800.7, 104.9, 2.528, 41.03, 66.77, 46.77, 60.31});
    //        M1Process m1p = new M1Process(new double[]{950.7, 564.9, 1463, 23.35, 804.0, 801.7, 290.4, 6.218, 114.6, 182.9, 130.7, 170.3});
    //        M1Process m1p = new M1Process(new double[]{1392, 791.3, 406.2, 1778, 325.2, 1855, 4412, 109.4, 2419, 2101, 2998, 1533});
    //        M1Process m1p = new M1Process(new double[]{1374, 776.5, 394.3, 1757, 330.7, 1830, 4417, 111.8, 2422, 2127, 3039, 1522});
    //        M1Process m1p = new M1Process(new double[]{475.6, 551.6, 1027, 2.326, 473.5, 519.9, 3440, 44.55, 1886, 1593, 2178, 1292});
    //        M1Process m1p = new M1Process(new double[]{634.4, 347.3, 34.7, 950.6, 625.2, 360.7, 128.5, 3218, 1203, 2139, 2069, 1244});
    //        M1Process m1p = new M1Process(new double[]{11.57, 1030, 414.4, 626.7, 462.3, 585.1, 1300, 2010, 3184, 129.4, 2082, 1267});
//        Process process = Runtime.getRuntime().exec("java -jar /Users/Hwaipy/Documents/Dropbox/Code/2013-08-28\\ PolarizationControlSimulation/SimulationConsole/dist/SimulationConsole.jar");
//        PrintWriter print = new PrintWriter(process.getOutputStream());
//        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        print.println("read\r\n");
//        print.flush();
//        System.out.println(reader.readLine());

//        M1Process m1p = new M1Process(new double[]{0.7349, 0.2651, 0.9069, 0.0931, 0.6710, 0.3290, 0.0670, 0.9330, 0.7500, 0.2500, 0.5000, 0.5000});
//        M1Process m1p = new M1Process(new double[]{0.6178, 0.3822, 0.8862, 0.1138, 0.7948, 0.2052, 0.0325, 0.9675, 0.5074, 0.4926, 0.6771, 0.3229});
//        M1Process m1p = new M1Process(new double[]{3823, 1176, 1469, 3530, 3530, 1469, 391, 4608, 2093, 2906, 2906, 2093});
//        M1Process m1p = new M1Process(new double[]{1244, 3755, 2320, 2679, 2679, 2320, 4644, 355, 2085, 2914, 2914, 2085});
//        M1Process m1p = new M1Process(new double[]{3014, 1985, 891, 4108, 656, 4343, 2295, 2704, 594, 4405, 4105, 894});
//        M1Process m1p = new M1Process(new double[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
//        M1Process m1p = new M1Process(new double[]{650, 641, 23, 1304, 540.7, 798.5, 2051, 41.52, 1184, 932.6, 824.6, 1294});
//        M1Process m1p = new M1Process(new double[]{8.999, 1086, 450.7, 645.2, 481.6, 619.6, 1370, 2050, 3300, 118, 2124, 1314});
    M1Process m1p = new M1Process(new double[]{190.0, 40.3, 178.0, 31.5, 118.0, 70.0, 44.5, 295, 240, 67, 223, 115});
    System.out.println(m1p.getTheta1() / Math.PI * 180);
    System.out.println(m1p.getTheta2() / Math.PI * 180);
    System.out.println(m1p.getTheta3() / Math.PI * 180);

//        FiberTransform ft = FiberTransform.createReverse(30. / 180 * Math.PI + Math.PI / 2, 20. / 180 * Math.PI + Math.PI / 2, 10. / 180 * Math.PI + Math.PI / 2);
//        WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
//        WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
//        WavePlate hwp = new WavePlate(Math.PI, 0);
//
//        Polarization measurementH1 = Polarization.H.transform(ft)
//                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mHH = measurementH1.getH();
//        double mHV = measurementH1.getV();
//        double mHD = measurementH1.getD();
//        double mHA = measurementH1.getA();
//        Polarization measurementD1 = Polarization.D.transform(ft)
//                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mDH = measurementD1.getH();
//        double mDV = measurementD1.getV();
//        double mDD = measurementD1.getD();
//        double mDA = measurementD1.getA();
//        qwp2.increase(-Math.PI / 4);
//        hwp.increase(-Math.PI / 8);
//        Polarization measurementH2 = Polarization.H.transform(ft)
//                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mHL = measurementH2.getH();
//        double mHR = measurementH2.getV();
//        Polarization measurementD2 = Polarization.D.transform(ft)
//                .transform(qwp1).transform(qwp2).transform(hwp);
//        double mDL = measurementD2.getH();
//        double mDR = measurementD2.getV();
//
//        System.out.println(Arrays.toString(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR}));
//
//        M1Process m1Process = null;
//        double cH = 0;
//        double cD = 0;
//        try {
//            m1Process = M1Process.calculate(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR});
//        } catch (M1ProcessException ex) {
//        }
//        if (m1Process != null) {
//            double[] result = m1Process.getResults();
//            qwp1.setTheta(result[0]);
//            qwp2.setTheta(result[1]);
//            hwp.setTheta(result[2]);
//            Polarization resultH = Polarization.H.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
//            Polarization resultD = Polarization.D.transform(ft).transform(qwp1).transform(qwp2).transform(hwp);
//            cH = resultH.getH() / resultH.getV();
//            cD = resultD.getD() / resultD.getA();
//        }
  }
}

package com.hwaipy.science.polarizationcontrol.m1;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.text.DecimalFormat;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class CalidM1Process {

    private Random random = new Random(1);
    private FiberTransform ft = FiberTransform.createRandomFiber(random);
    private WavePlate[] wavePlates = new WavePlate[]{new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI, 0)};
    private double[] stocksH;

    public CalidM1Process() {
        stocksH = readStocksH();
        DecimalFormat format = new DecimalFormat("0.0000");
        for (double power : stocksH) {
            System.out.print(format.format(power) + ", ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
//        singlePhoit();
        traversal();
    }

    public static void singlePhoit() {
        CalidM1Process calidM1Process = new CalidM1Process();
        DecimalFormat format = new DecimalFormat("0.0000");
        double[] powers = calidM1Process.readPower();
        for (double power : powers) {
            System.out.print(format.format(power) + ", ");
        }
        System.out.println();

        calidM1Process.wavePlates[0].setTheta(+Math.PI / 4);
        calidM1Process.wavePlates[1].setTheta(+Math.PI / 4);
        calidM1Process.wavePlates[2].setTheta(-Math.PI / 8);
        powers = calidM1Process.readPower();
        for (double power : powers) {
            System.out.print(format.format(power) + ", ");
        }
        System.out.println();
    }

    public static void traversal() {
        CalidM1Process calidM1Process = new CalidM1Process();

        double start = -90.0 / 180 * Math.PI;
        double end = 90.0 / 180 * Math.PI;
        double stepLength = 0.5 / 180 * Math.PI;
        for (double qwp1A = start; qwp1A <= end; qwp1A += stepLength) {
            for (double qwp2A = start; qwp2A <= end; qwp2A += stepLength) {
                for (double hwp = start; hwp <= end; hwp += stepLength) {
                    calidM1Process.wavePlates[0].setTheta(qwp1A);
                    calidM1Process.wavePlates[1].setTheta(qwp2A);
                    calidM1Process.wavePlates[2].setTheta(hwp);
                    double[] powers = calidM1Process.readPowerH();
                    String fits = calidM1Process.fitStocks(powers);
                    if (fits != null) {
                        DecimalFormat format = new DecimalFormat("0.0");
                        System.out.println(""
                                + format.format(calidM1Process.wavePlates[0].getTheta() / Math.PI * 180) + "\t"
                                + format.format(calidM1Process.wavePlates[1].getTheta() / Math.PI * 180) + "\t"
                                + format.format(calidM1Process.wavePlates[2].getTheta() / Math.PI * 180) + "\t"
                                + fits
                        );
                    }
                }
            }
        }
//        powers = calidM1Process.readPower();
//        for (double power : powers) {
//            System.out.print(format.format(power) + ", ");
//        }
//        System.out.println();
    }

    private double[] readPower() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        Polarization measurementD = Polarization.D.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{
            measurementH.getH(), measurementH.getV(), measurementH.getD(), measurementH.getA(),
            measurementD.getH(), measurementD.getV(), measurementD.getD(), measurementD.getA()};
    }

    private double[] readPowerH() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{measurementH.getH(), measurementH.getV(), measurementH.getD(), measurementH.getA()};
    }

    private double[] readStocksH() {
        Polarization measurementH = Polarization.H.transform(ft)
                .transform(wavePlates[0]).transform(wavePlates[1]).transform(wavePlates[2]);
        return new double[]{
            measurementH.getH(), measurementH.getV(), measurementH.getD(), measurementH.getA(),
            measurementH.getL(), measurementH.getR()};
    }

    private String fitStocks(double[] powers) {
        StringBuilder sb = new StringBuilder();
        for (double power : powers) {
            char c = fitStock(power);
            if (c == '?') {
                return null;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    private char fitStock(double power) {
        if (Math.abs(power - stocksH[0]) < 0.000001) {
            return 'H';
        }
        if (Math.abs(power - stocksH[1]) < 0.000001) {
            return 'V';
        }
        if (Math.abs(power - stocksH[2]) < 0.000001) {
            return 'D';
        }
        if (Math.abs(power - stocksH[3]) < 0.000001) {
            return 'A';
        }
        if (Math.abs(power - stocksH[4]) < 0.000001) {
            return 'L';
        }
        if (Math.abs(power - stocksH[5]) < 0.000001) {
            return 'R';
        }
        return '?';
    }
}

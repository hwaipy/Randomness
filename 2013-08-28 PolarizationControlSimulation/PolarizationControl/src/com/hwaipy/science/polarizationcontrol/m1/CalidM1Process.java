package com.hwaipy.science.polarizationcontrol.m1;

import com.hwaipy.science.polarizationcontrol.device.FiberTransform;
import com.hwaipy.science.polarizationcontrol.device.HalfWavePlate;
import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Hwaipy
 */
public class CalidM1Process {

    private Random random = new Random(1);
    private FiberTransform ft = FiberTransform.createRandomFiber(random);
    private WavePlate[] wavePlates = new WavePlate[]{new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI / 2, 0), new WavePlate(Math.PI, 0)};

    public static void main(String[] args) {
        CalidM1Process calidM1Process = new CalidM1Process();
        DecimalFormat format = new DecimalFormat("0.0000");
        double[] powers = calidM1Process.readPower();
        for (double power : powers) {
            System.out.print(format.format(power) + ", ");
        }
        System.out.println();

        calidM1Process.wavePlates[1].setTheta(+Math.PI / 4);
        calidM1Process.wavePlates[2].setTheta(-Math.PI / 8);
        powers = calidM1Process.readPower();
        for (double power : powers) {
            System.out.print(format.format(power) + ", ");
        }
        System.out.println();
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
}

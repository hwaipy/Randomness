package D20140710.FFT;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author Hwaipy
 */
public class FFTAnalysis {

    public static void main(String[] args) throws FileNotFoundException, IOException {
//        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/科学/_2014-06-19 DPS QKD/2014-07-23 干涉稳定性测试/Jul-22-2014_16-22-57.csv");
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/科学/_2014-06-19 DPS QKD/2014-07-23 干涉稳定性测试/Jul-23-2014_08-07-38.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        reader.readLine();
        ArrayList<Double> samples = new ArrayList<>();
        while (true) {
            String line = reader.readLine();
            if (line == null || line.length() == 0) {
                break;
            }
            String[] split = line.split("\t");
            double value = Double.parseDouble(split[1]);
            samples.add(value);
        }
        int N = 65536;
        FFTColumbia fftColumbia = new FFTColumbia(N);
        double[] re = new double[N];
        double[] im = new double[N];
        for (int i = 0; i < N; i++) {
            re[i] = samples.get(i);
            im[i] = 0;
        }
        fftColumbia.fft(re, im);

        for (int i = 0; i < re.length; i++) {
            System.out.println((1. / N * 10 * (i + 1)) + "\t" + re[re.length - i - 1]);
        }
    }
}

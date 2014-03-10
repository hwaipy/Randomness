package D20131026.PM320E_DataParse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Hwaipy
 */
public class DataParse {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-10-26 不稳相干涉曲线补测/PM320E_log.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String currentTime = "";
        int powerCount = 0;
        double powerSum = 0;
        int valueCount = 0;
        double valueSum = 0;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] split = line.split("\t");
            String time = split[1];
            String power = split[2];
            if (!time.equals(currentTime)) {
                double value = powerSum / powerCount;
                powerCount = 0;
                powerSum = 0;
                currentTime = time;
                valueCount++;
                valueSum += value;
                if (valueCount == 15) {
                    double l = valueSum / valueCount;
                    double b = 6.3e-5;
                    System.out.println(1 - 2 * l / b);
                    valueCount = 0;
                    valueSum = 0;
                }
            }
            if (power.equals("Inf")) {
            } else {
                powerCount++;
                powerSum += Double.valueOf(power);
            }
        }
    }
}

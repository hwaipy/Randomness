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
public class DataListToFunctionList {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/Users/Hwaipy/Documents/Dropbox/LabWork/实验/2013-10-26 不稳相干涉曲线补测/Data_V1.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        int time = 0;
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            String[] split = line.split(";");
            time += 25;
            String t = (time / 100) + "." + (time % 100);
            System.out.println("A[x_] := " + split[0] + " /; x == " + t + ";");
            System.out.println("B[x_] := " + split[1] + " /; x == " + t + ";");
        }
    }
}

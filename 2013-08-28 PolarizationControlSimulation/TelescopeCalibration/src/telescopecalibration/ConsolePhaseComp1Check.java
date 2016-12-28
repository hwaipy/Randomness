package telescopecalibration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Hwaipy
 */
public class ConsolePhaseComp1Check {

  public static void main(String[] args) throws IOException {
    args = new String[]{"/Users/Hwaipy/Documents/Dropbox/LabWork/工程/2016-07-21 丽江站建设/8.每日实验数据/20161207/偏振补偿/手动/补偿安排.csv"};
    File file = new File(args[0]);
    LinkedList<double[]> angles = loadAngles(file);
    for (int i = 0; i < angles.size(); i++) {
      double[] angle = angles.get(i);
      double rH = angle[0];
      double rV = angle[1];
      double rotate = angle[2];
      double phase = angle[3];
      boolean mir = angle[3] > 0.5;
      M1Simulation s = new M1Simulation();
      System.out.print(i + "\t");
      s.calculate(rH, rV, phase / 180 * Math.PI, rotate / 180 * Math.PI, mir);
      System.out.println();
    }
  }

  private static LinkedList<double[]> loadAngles(File file) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    LinkedList<double[]> angles = new LinkedList<>();
    reader.readLine();
    while (true) {
      String line = reader.readLine();
      if (line == null || line.length() == 0) {
        break;
      }
      String[] split = line.split(",");
      double[] ags = new double[4];
      for (int i = 0; i < ags.length; i++) {
        ags[i] = Double.parseDouble(split[i]);
      }
      angles.add(ags);
    }
    return angles;
  }
}

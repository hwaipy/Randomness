package qrng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import static qrng.Parameters.dir;

/**
 * pycharm
 *
 * @author Hwaipy
 */
public class Stati {

  public static void main(String[] args) throws Exception {
    File[] files = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return name.toLowerCase().endsWith(".sli.bin");
      }
    });
    for (File file : files) {
      staticticErrorRate(file.getAbsolutePath());
    }
  }

  private static void staticticErrorRate(String dataFilePath) throws Exception {
    File file = new File(dataFilePath);
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
    String dataS = reader.readLine();
    System.out.print(dataS.length() + "\t");
    int period = 1000;
    double[] totals = new double[period];
    double[] errors = new double[period];
    int dphoton = 0;
    for (int i = 0; i < dataS.length(); i++) {
      char c = dataS.charAt(i);
      int index = i % totals.length;
      switch (c) {
        case '1':
          totals[index] += 1;
          errors[index] += 1;
          break;
        case '2':
          totals[index] += 1;
          break;
        case '3':
          dphoton++;
          break;
      }
    }

    for (double total : totals) {
      System.out.print(total + "\t");
    }
    for (double total : errors) {
      System.out.print(total + "\t");
    }
    System.out.println();

//    ArrayList<Double> errorList = new ArrayList<>();
//    for (int i = 0; i < period; i++) {
//      double er = errors[i] / totals[i];
//      System.out.println(i + "\t" + er);
//      errorList.add(er);
//    }
//    errorList.sort(null);
//    System.out.println("---");
//    System.out.println(errorList.get(0));
//    System.out.println(errorList.get(errorList.size() - 1));
//    int errorsForCheck = 0;
//    int totalsForCheck = 0;
//    int errorsForRNG = 0;
//    int totalsForRNG = 0;
//    for (int i = 0; i < totals.length; i++) {
//      double er = errors[i] / totals[i];
//      if (er > 0.8) {
//        errorsForCheck += errors[i];
//        totalsForCheck += totals[i];
//      } else {
//        errorsForRNG += errors[i];
//        totalsForRNG += totals[i];
//      }
//    }
//    System.out.println("::::\t" + errorsForCheck + "\t" + totalsForCheck + "\t" + errorsForRNG + "\t" + totalsForRNG + "\t" + dphoton);
  }
}

package telescopecalibration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Hwaipy
 */
public class TeleAll {

  public static void main(String[] args) throws IOException {
    PrintWriter pw = new PrintWriter("out.csv");
//    for (int rH = 0; rH < 360; rH++) {
//      for (int rV = 0; rV < 90; rV++) {
    double rH = 32.750001;
    double rV = 0.0001;
    M1Simulation.M1SimulationResult result = new M1Simulation().calculate(rH, rV, 0, 0, false);
    double[] angle = result.angles;
//    double a1 = angle[0] / Math.PI * 180 - 15.1;
//    double a2 = angle[1] / Math.PI * 180 + 51.3;
//    double a3 = angle[2] / Math.PI * 180 - 15.3;

    double a1 = angle[0] / Math.PI * 180 - 16.8;
    double a2 = angle[1] / Math.PI * 180 + 52.8;
    double a3 = angle[2] / Math.PI * 180 - 15.6;
//    pw.printf("%d,%d,%.3f,%.3f,%.3f\r\n", rH, rV, a1, a2, a3);
    System.out.println(a1 + "\t" + a2 + "\t" + a3);
//      }
//      System.out.println(rH);
//    }
//    pw.close();
  }

  private static LinkedList<double[]> loadAngles(File file) throws IOException {
    InputStream is = new FileInputStream(file);
    HSSFWorkbook workbook = new HSSFWorkbook(is);
    HSSFSheet sheet = workbook.getSheetAt(0);
    LinkedList<double[]> angles = new LinkedList<>();
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      HSSFRow row = sheet.getRow(i);
      double A = row.getCell(0).getNumericCellValue();
      double E = row.getCell(1).getNumericCellValue();
      double AS = row.getCell(2).getNumericCellValue() - (row.getCell(4).getNumericCellValue() - row.getCell(3).getNumericCellValue());
      double phase = row.getCell(8).getNumericCellValue();
      double HWP = row.getCell(9).getNumericCellValue();
      angles.add(new double[]{A, E, AS, phase, HWP});
    }
    return angles;
  }
}

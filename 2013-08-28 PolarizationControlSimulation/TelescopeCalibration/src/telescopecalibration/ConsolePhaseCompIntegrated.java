package telescopecalibration;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.imageio.ImageIO;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author Hwaipy
 */
public class ConsolePhaseCompIntegrated {

  public static void main(String[] args) throws IOException {
    String dateS = LocalDateTime.now().minusHours(8 + 24).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    args = new String[]{"/Users/Hwaipy/Dropbox/LabWork/工程/2016-07-21 丽江站建设/8.每日实验数据/" + dateS + "/偏振补偿/偏振补偿.xls"};
//    args = new String[]{"../../../实验计划/" + dateS + "/偏振补偿/偏振补偿.xls"};
    File file = new File(args[0]);
    LinkedList<double[]> angles = loadAngles(file);
    ArrayList<double[]> results = new ArrayList<>();
    angles.stream().forEach((angle) -> {
      double rH = angle[0];
      double rV = angle[1];
      double rotate = angle[2];
      double phase = angle[3];
      boolean tomoQWP = angle[5] > 0.5;
      M1Simulation s = new M1Simulation();
      M1Simulation.M1SimulationResult result = s.calculate(rH, rV, phase / 180 * Math.PI, rotate / 180 * Math.PI, false, tomoQWP);
      results.add(result.getAngles());
    });
    for (int i = 1; i < results.size(); i++) {
      double[] angleLast = results.get(i - 1);
      double[] angleCurrent = results.get(i);
      AnnealingTest p = new AnnealingTest(0.9999, 4048, angleLast, angleCurrent);
      p.output = true;
      AnnealingTest.Result result = p.process();
      double[] newAngles = result.angles;
      if (!result.success) {
        System.out.println("Failed at " + i);
        System.out.println(Arrays.toString(angleLast));
        System.out.println(Arrays.toString(angleCurrent));
      }
      results.set(i, newAngles);
    }
    for (int i = 0; i < results.size(); i++) {
      double HWP = angles.get(i)[4] / 180.0 * Math.PI;
      results.get(i)[2] += HWP;
    }
    File outputFile = new File(file.getAbsolutePath() + ".csv");
    ArrayList<ArrayList<Double>> data = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      data.add(new ArrayList<>());
    }
    try (PrintWriter printWriter = new PrintWriter(outputFile)) {
      for (int i = 1; i < results.size(); i++) {
        double[] angle = results.get(i);
        double a1 = angle[0] / Math.PI * 180 - 14.471;
        double a2 = angle[1] / Math.PI * 180 + 51.315;
        double a3 = angle[2] / Math.PI * 180 - 15.865;
        printWriter.printf("%d,%.3f,%.3f,%.3f\r\n", i, a1, a2, a3);
        data.get(0).add((double) i);
        data.get(1).add(a1);
        data.get(2).add(a2);
        data.get(3).add(a3);
        data.get(4).add(angles.get(i)[0]);
        data.get(5).add(angles.get(i)[1]);
      }
    }
    Chart chart = new ChartBuilder().width(1024).height(800).chartType(StyleManager.ChartType.Line).title("望远镜/小转台角度曲线").xAxisTitle("Time (s)").yAxisTitle("Angles").build();
    Series s4 = chart.addSeries("望远镜方位", data.get(0), data.get(4));
    Series s5 = chart.addSeries("望远镜俯仰", data.get(0), data.get(5));
    Series s1 = chart.addSeries("QWP1", data.get(0), data.get(1));
    Series s2 = chart.addSeries("QWP2", data.get(0), data.get(2));
    Series s3 = chart.addSeries("HWP", data.get(0), data.get(3));
    chart.getStyleManager().setMarkerSize(0);
    BufferedImage image = new BufferedImage(1024, 800, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = image.createGraphics();
    chart.paint(g2);
    g2.dispose();
    ImageIO.write(image, "png", new File(file.getAbsolutePath() + ".png"));
  }

  private static LinkedList<double[]> loadAngles(File file) throws IOException {
    InputStream is = new FileInputStream(file);
    HSSFWorkbook workbook = new HSSFWorkbook(is);
    HSSFSheet sheet = workbook.getSheetAt(0);
    LinkedList<double[]> angles = new LinkedList<>();
    boolean hasTomoQWP = false;
    try {
      hasTomoQWP = sheet.getRow(0).getCell(10).getStringCellValue().equals("TomoQWP");
    } catch (Exception e) {
    }
    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
      HSSFRow row = sheet.getRow(i);
      double A = row.getCell(0).getNumericCellValue();
      double E = row.getCell(1).getNumericCellValue();
      double AS = row.getCell(2).getNumericCellValue() - (row.getCell(4).getNumericCellValue() - row.getCell(3).getNumericCellValue());
      double phase = row.getCell(8).getNumericCellValue();
      double HWP = row.getCell(9).getNumericCellValue();
      double tomoQWP = hasTomoQWP ? row.getCell(10).getNumericCellValue() : 0;
      angles.add(new double[]{A, E, AS, phase, HWP, tomoQWP});
    }
    return angles;
  }
}

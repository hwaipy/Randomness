package telescopecalibration;

import com.xeiam.xchart.Chart;
import com.xeiam.xchart.ChartBuilder;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.StyleManager;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class ConsolePhaseComp {

  public static void main(String[] args) throws IOException {
    args = new String[]{"/Users/Hwaipy/Documents/Dropbox/LabWork/工程/2016-07-21 丽江站建设/8.每日实验数据/20161126/偏振补偿/补偿安排.csv"};
    File file = new File(args[0]);
    LinkedList<double[]> angles = loadAngles(file);
    ArrayList<double[]> results = new ArrayList<>();
    angles.stream().forEach((angle) -> {
      double rH = angle[0];
      double rV = angle[1];
      double rotate = angle[2];
      double phase = angle[3];
      boolean mir = angle[3] > 0.5;
      M1Simulation s = new M1Simulation();
//      System.out.println(rH + "\t" + rV + "\t" + rotate + "\t" + phase + "\t" + mir);
      M1Simulation.M1SimulationResult result = s.calculate(rH, rV, phase / 180 * Math.PI, rotate / 180 * Math.PI, mir);
      results.add(result.getAngles());
    });
    for (int i = 1; i < results.size(); i++) {
      double[] angleLast = results.get(i - 1);
      double[] angleCurrent = results.get(i);
      AnnealingTest p = new AnnealingTest(0.9999, 4048, angleLast, angleCurrent);
      p.output = true;
      AnnealingTest.Result result = p.process();
//      double[] newAngles = result.success ? result.angles : Arrays.copyOf(angleLast, angleLast.length);
      double[] newAngles = result.angles;
      if (!result.success) {
        System.out.println("Failed at " + i);
        System.out.println(Arrays.toString(angleLast));
        System.out.println(Arrays.toString(angleCurrent));
      }
      results.set(i, newAngles);
    }
    File outputFile = new File(file.getAbsolutePath() + "ttt m.csv");
    ArrayList<ArrayList<Double>> data = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      data.add(new ArrayList<>());
    }
    try (PrintWriter printWriter = new PrintWriter(outputFile)) {
      for (int i = 0; i < results.size(); i++) {
        double[] angle = results.get(i);
        double a1 = angle[0] / Math.PI * 180 - 15.1;
        double a2 = angle[1] / Math.PI * 180 + 51.3;
        double a3 = angle[2] / Math.PI * 180 - 15.3;
        printWriter.printf("%d,%.3f,%.3f,%.3f\r\n", i + 1, a1, a2, a3);
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
    ImageIO.write(image, "png", new File(file.getAbsolutePath() + "ttt m.png"));
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

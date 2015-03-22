package com.hwaipy.physics.nonlinearoptics.spectrumcorrelation;

import com.hwaipy.physics.crystaloptics.Mediums;
import com.hwaipy.utilities.system.PathsUtilities;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.imageio.ImageIO;

/**
 *
 * @author Hwaipy
 */
public class CorrelationTest {

  private static final double minOmigaS = 779.0;
  private static final double maxOmigaS = 781.0;
  private static final double minOmigaI = 779.0;
  private static final double maxOmigaI = 781.0;
  private static final int width = 200;
  private static final int height = 200;
  private static final Filter bandPass780_3 = new BandPassFilter(780, 3, 0);
  private static final Filter bandPass780_1 = new BandPassFilter(780, 1, 0);
  private static final Filter gaussian390_02 = new GaussianFilter(390, 0.2 / 2.35);
  private static final Filter gaussian390_0015 = new GaussianFilter(390, 0.015 / 2.35);
  private static final Filter gaussian780_0030 = new GaussianFilter(780, 0.030 / 2.35);
  private static final Filter fpEtalon390_0015 = new FabryPerotCaviry(0.855, 0.2535 / 1000);
  private static final Filter fpEtalon780_0030 = new FabryPerotCaviry(0.855, 0.507 / 1000);

  public static void main(String[] args) throws IOException {
    CorrelationFunction functionPump = new PumpFunction(390, 1.6);
    functionPump.filterPump(gaussian390_02);
    functionPump.filterPump(fpEtalon390_0015);
//    plot(functionPump, "pump");
    CorrelationFunction functionPhaseMatch = new QuasiPhaseMatchFunction(Mediums.KTiOPO4, 10, 7.9482);
//    plot(functionPhaseMatch, "phaseMatch");
    CorrelationFunction functionJoin = new JointFunction(functionPhaseMatch, functionPump);
    plot(functionJoin, "join");
//        functionJoin.filterSignal(bandPass780_1);
//        functionJoin.filterSignal(bandPass780_3);
//        functionJoin.filterIdle(bandPass780_3);
//        functionJoin.filterSignal(fpEtalon780_0030);
//    functionJoin.filterIdle(fpEtalon780_0030);
    functionJoin.filterSignal(fpEtalon780_0030);
//    functionJoin.filterSignal(gaussian780_0030);
//    functionJoin.filterIdle(gaussian780_0030);
//    functionJoin.filterSignal(new BandPassFilter(780, 0.20, 0));
//    functionJoin.filterIdle(new BandPassFilter(780, 0.20, 0));
    plot(functionJoin, "filtered");
//    purity(functionJoin);
//
    HOM(functionJoin);
  }

  private static void plot(CorrelationFunction function, String name) throws IOException {
    CorrelationPloter correlationPloter = new CorrelationPloter(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, function, width, height);
    correlationPloter.calculate();
    BufferedImage image = correlationPloter.createImage();
    Path path = PathsUtilities.getDataStoragyPath();
    ImageIO.write(image, "png", new File(path.toFile(), name + ".png"));
//    double[] statistics1 = correlationPloter.statistics(true);
//    double[] statistics2 = correlationPloter.statistics(false);
//    for (int i = 0; i < statistics1.length; i++) {
//      System.out.println(statistics1[i] + "\t" + statistics2[i]);
//    }
  }

  private static void purity(CorrelationFunction function) throws IOException {
    PurityCalculator purityCalculator = new PurityCalculator(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, function, width, height);
    double purity = purityCalculator.calculate(true);
    System.out.println("Purity is " + purity);
    System.out.println("g2 = " + (1 + purity));
  }

  private static void HOM(CorrelationFunction function) throws IOException {
    HOMVisibilityCalculator homCalculator = new HOMVisibilityCalculator(minOmigaS, maxOmigaS, minOmigaI, maxOmigaI, function, width);
    double visibility = homCalculator.calculate();
    System.out.println("HOM Visibility is " + visibility);
  }

}

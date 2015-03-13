package D20140729.OAMDrawer;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class Test {

    public static void main(String[] args) throws IOException {
        FunctionDrawer drawer = new FunctionDrawer();
        LGModeFunction gaussian = new LGModeFunction(0, 0, 1, 1);
        Function tiltedGaussian = new XTilt(gaussian, 0.1);
        LGModeFunction oam = new LGModeFunction(0, 0, 1, 1);
        drawer.draw(new Intensity(oam), 1000, 1000, -3, 3, -3, 3, new File("re_or.png"));

        Sum sum = new Sum(tiltedGaussian, oam);
        Intensity intensity = new Intensity(sum);
        drawer.draw(intensity, 1000, 1000, -3, 3, -3, 3, new File("re_dt.png"));

        Product rebuild = new Product(gaussian, intensity);
        Function tiltedRebuild = new XTilt(rebuild, 0.1);
        drawer.draw(new Intensity(tiltedRebuild), 1000, 1000, -3, 3, -3, 3, new File("re_rb.png"));
    }
}

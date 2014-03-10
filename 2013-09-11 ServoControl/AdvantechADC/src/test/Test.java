package test;

import com.hwaipy.device.advantech.AdvantechADC;
import com.hwaipy.device.advantech.Device;

/**
 *
 * @author Hwaipy Lab
 */
public class Test {

    public static void main(String[] args) throws Exception {
        AdvantechADC advantechADC = new AdvantechADC();
        advantechADC.loadDevice();
//        advantechADC.show();
        Device device = advantechADC.getDevice(0);
        device.open();
//        device.setAIConfiguration(16, 0);
//        device.setAIConfiguration(16, 1);
//        long start = System.nanoTime();
//        for (int i = 0; i < 10000; i++) {
//            device.readAIVoltage(0);
//            device.readAIVoltage(1);
//        }
//        long end = System.nanoTime();
//        System.out.println((end - start) / 1000000000.);
        boolean state = false;
        while (true) {
            device.writeDOBit(0, 1, state);
            state = !state;
            Thread.sleep(3000);
        }
    }
}

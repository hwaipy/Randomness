package udiapplication.picoharp300application;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoharp300device.PicoHarp300Device;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class PicoHarp300Debugger {

    public static void main(String[] args) throws FileNotFoundException, IOException, PicoQuantException {
        PicoHarp300Device device = new PicoHarp300Device(0);
        device.open();
        device.initialize(Mode.T2);


        device.close();
    }
}

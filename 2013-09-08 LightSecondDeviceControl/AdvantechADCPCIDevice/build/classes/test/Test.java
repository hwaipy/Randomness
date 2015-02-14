package test;

import com.hwaipy.unifieddeviceinterface.adc.advantech.AdvantechADCDevice;
import com.hwaipy.unifieddeviceinterface.adc.advantech.AdvantechADCDeviceManager;
import com.hwaipy.unifieddeviceinterface.adc.advantech.AdvantechADCException;
import java.util.List;

/**
 *
 * @author ustc
 */
public class Test {

    public static void main(String[] args) throws AdvantechADCException {
        AdvantechADCDeviceManager deviceManager = AdvantechADCDeviceManager.getManager();
        List<AdvantechADCDevice> deviceList = deviceManager.getDeviceList();
        for (AdvantechADCDevice device : deviceList) {
            System.out.println(device.getDeviceIndex() + ", " + device.getDeviceName() + ", " + device.getNumberOfSubDevices());
        }
        AdvantechADCDevice device0 = deviceList.get(0);
        AdvantechADCDevice device1 = deviceList.get(1);
        device0.open();
        device0.dioWriteBit(0, 0, true);
        device0.close();
    }
}

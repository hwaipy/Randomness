package com.hwaipy.device.advantech.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hwaipy Lab
 */
public class DeviceFeatureStructure extends Structure {

    public byte[] szDriverVer = new byte[8];    // device driver version
    public byte[] szDriverName = new byte[16];  // device driver name
    public int dwBoardID;         // board ID
    public short usMaxAIDiffChl;    // Max. number of differential channel
    public short usMaxAISiglChl;    // Max. number of single-end channel
    public short usMaxAOChl;        // Max. number of D/A channel
    public short usMaxDOChl;        // Max. number of digital out channel
    public short usMaxDIChl;        // Max. number of digital input channel
    public short usDIOPort;         // specifies if programmable or not
    public short usMaxTimerChl;     // Max. number of Counter/Timer channel
    public short usMaxAlarmChl;     // Max number of  alram channel
    public short usNumADBit;        // number of bits for A/D converter
    public short usNumADByte;       // A/D channel width in bytes.
    public short usNumDABit;        // number of bits for D/A converter.
    public short usNumDAByte;       // D/A channel width in bytes.
    public short usNumGain;        // Max. number of gain code
    public GainListStructure[] glGainList = new GainListStructure[16];    // Gain listing
    public int[] dwPermutation = new int[4];  // Permutation

    @Override
    protected List getFieldOrder() {
        String[] fieldOrder = new String[]{"szDriverVer", "szDriverName", "dwBoardID",
            "usMaxAIDiffChl", "usMaxAISiglChl", "usMaxAOChl", "usMaxDOChl", "usMaxDIChl",
            "usDIOPort", "usMaxTimerChl", "usMaxAlarmChl", "usNumADBit", "usNumADByte",
            "usNumDABit", "usNumDAByte", "usNumGain", "glGainList", "dwPermutation"};
        return Arrays.asList(fieldOrder);
    }

    public static class ByReference extends DeviceFeatureStructure implements Structure.ByReference {
    }

    public static class ByValue extends DeviceFeatureStructure implements Structure.ByValue {
    }
}

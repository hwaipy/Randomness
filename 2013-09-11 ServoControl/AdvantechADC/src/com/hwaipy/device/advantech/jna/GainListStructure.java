package com.hwaipy.device.advantech.jna;

import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Hwaipy Lab
 */
public class GainListStructure extends Structure {

    public short usGainCde;
    public float fMaxGainVal;
    public float fMinGainVal;
    public byte[] szGainStr = new byte[16];

    @Override
    protected List getFieldOrder() {
        String[] fieldOrder = new String[]{"usGainCde", "fMaxGainVal", "fMinGainVal", "szGainStr"};
        return Arrays.asList(fieldOrder);
    }

    public static class ByReference extends GainListStructure implements Structure.ByReference {
    }
}
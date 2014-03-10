package com.hwaipy.device.advantech;

import com.hwaipy.device.advantech.jna.DeviceFeatureStructure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 * @author Hwaipy Lab
 */
public class DeviceFeature {

    private final String driverVersion;    // device driver version
    private final String driverName;  // device driver name
    private final int boardID;         // board ID
    private final int maxAIDifferentialChannel;    // Max. number of differential channel
    private final int maxAISingleEndChannel;    // Max. number of single-end channel
    private final int maxAOChannel;        // Max. number of D/A channel
    private final int maxDOChannel;        // Max. number of digital out channel
    private final int maxDIChannel;        // Max. number of digital input channel
    private final int DIOPort;         // specifies if programmable or not
    private final int maxTimerChannel;     // Max. number of Counter/Timer channel
    private final int maxAlarmChannel;     // Max number of  alram channel
    private final int numberOfADBits;        // number of bits for A/D converter
    private final int numberOfADBytes;       // A/D channel width in bytes.
    private final int numberOfDABits;        // number of bits for D/A converter.
    private final int numberOfDABytes;       // D/A channel width in bytes.
    private final int numberOfGain;        // Max. number of gain code
    private final ArrayList<MeasurementGain> measurementGains;    // Gain listing
    private final int[] permutations;  // Permutation

    DeviceFeature(DeviceFeatureStructure dfs) {
        driverVersion = Util.createStringFromBytesArray(dfs.szDriverVer);
        driverName = Util.createStringFromBytesArray(dfs.szDriverName);
        boardID = dfs.dwBoardID;
        maxAIDifferentialChannel = dfs.usMaxAIDiffChl;
        maxAISingleEndChannel = dfs.usMaxAISiglChl;
        maxAOChannel = dfs.usMaxAOChl;
        maxDOChannel = dfs.usMaxAOChl;
        maxDIChannel = dfs.usMaxDIChl;
        DIOPort = dfs.usDIOPort;
        maxTimerChannel = dfs.usMaxTimerChl;
        maxAlarmChannel = dfs.usMaxAlarmChl;
        numberOfADBits = dfs.usNumADBit;
        numberOfADBytes = dfs.usNumADByte;
        numberOfDABits = dfs.usNumDABit;
        numberOfDABytes = dfs.usNumDAByte;
        numberOfGain = dfs.usNumGain;
        measurementGains = new ArrayList<>(MeasurementGain.createMeasurementGains(dfs.glGainList));
        permutations = Arrays.copyOf(dfs.dwPermutation, dfs.dwPermutation.length);
    }

    public String getDriverVersion() {
        return driverVersion;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getBoardID() {
        return boardID;
    }

    public int getMaxAIDifferentialChannel() {
        return maxAIDifferentialChannel;
    }

    public int getMaxAISingleEndChannel() {
        return maxAISingleEndChannel;
    }

    public int getMaxAOChannel() {
        return maxAOChannel;
    }

    public int getMaxDOChannel() {
        return maxDOChannel;
    }

    public int getMaxDIChannel() {
        return maxDIChannel;
    }

    public int getDIOPort() {
        return DIOPort;
    }

    public int getMaxTimerChannel() {
        return maxTimerChannel;
    }

    public int getMaxAlarmChannel() {
        return maxAlarmChannel;
    }

    public int getNumberOfADBits() {
        return numberOfADBits;
    }

    public int getNumberOfADBytes() {
        return numberOfADBytes;
    }

    public int getNumberOfDABits() {
        return numberOfDABits;
    }

    public int getNumberOfDABytes() {
        return numberOfDABytes;
    }

    public int getNumberOfGain() {
        return numberOfGain;
    }

    public Collection<MeasurementGain> getMeasurementGains() {
        return Collections.unmodifiableList(measurementGains);
    }

    public int[] getPermutations() {
        return permutations;
    }
}

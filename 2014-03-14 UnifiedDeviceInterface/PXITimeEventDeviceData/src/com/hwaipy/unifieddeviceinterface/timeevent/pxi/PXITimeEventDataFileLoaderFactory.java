package com.hwaipy.unifieddeviceinterface.timeevent.pxi;

import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataFileLoader;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataFileLoaderFactory;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public class PXITimeEventDataFileLoaderFactory implements TimeEventDataFileLoaderFactory {

    @Override
    public TimeEventDataFileLoader newTimeEventDataFileLoader(File dataFile) throws IOException {
        return new PXITimeEventDataFileLoader(dataFile);
    }
}

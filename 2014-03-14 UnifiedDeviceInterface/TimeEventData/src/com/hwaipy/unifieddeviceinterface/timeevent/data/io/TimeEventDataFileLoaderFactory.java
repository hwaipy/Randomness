package com.hwaipy.unifieddeviceinterface.timeevent.data.io;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventDataFileLoaderFactory {

    public TimeEventDataFileLoader newTimeEventDataFileLoader(File dataFile) throws IOException;
}

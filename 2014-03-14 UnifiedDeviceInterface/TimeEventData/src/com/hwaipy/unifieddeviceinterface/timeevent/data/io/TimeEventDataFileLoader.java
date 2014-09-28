package com.hwaipy.unifieddeviceinterface.timeevent.data.io;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventDataFileLoader {

    //TODO 愚蠢 应该可以动态调整
    public int getChannelCount();

    public TimeEventData loadNext() throws IOException;

    public void complete(TimeEventClusterData cluster);

    public TimeEventSerializer getSerializer();
}

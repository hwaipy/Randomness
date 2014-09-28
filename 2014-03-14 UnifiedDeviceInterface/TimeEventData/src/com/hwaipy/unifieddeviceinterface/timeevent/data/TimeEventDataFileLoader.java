package com.hwaipy.unifieddeviceinterface.timeevent.data;

import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventSerializer;
import java.io.IOException;

/**
 *
 * @author Hwaipy
 */
public interface TimeEventDataFileLoader {

    public int getChannelCount();

    public TimeEventData loadNext() throws IOException;

    public void complete(TimeEventClusterData cluster);

    public TimeEventSerializer getSerializer();
}

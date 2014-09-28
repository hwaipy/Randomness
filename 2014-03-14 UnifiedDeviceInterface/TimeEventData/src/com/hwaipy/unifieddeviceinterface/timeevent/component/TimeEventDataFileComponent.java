package com.hwaipy.unifieddeviceinterface.timeevent.component;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataComponent;
import com.hwaipy.unifieddeviceInterface.DataType;
import com.hwaipy.unifieddeviceInterface.components.AbstractDataComponent;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataManager;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Hwaipy
 */
public class TimeEventDataFileComponent extends AbstractDataComponent implements Component, DataComponent {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    @Override
    public void startComponent() {
    }

    @Override
    public void stopComponent() {
    }

    @Override
    public ComponentInformation getInformation() {
        return null;
    }

    @Override
    public Collection<DataType> dependent() {
        return Arrays.asList(new DataType("TimeEventDataFile", FileData.class));
    }

    @Override
    public Collection<DataType> export() {
        return Arrays.asList(new DataType("TimeEventDataFile", TimeEventClusterData.class));
    }

    @Override
    public void dataUpdate(Data data) {
        if (data instanceof FileData) {
            EXECUTOR_SERVICE.submit(() -> {
                TimeEventDataManager.loadTimeEventClusterData(null);
            });
        } else {
            throw new IllegalArgumentException("FileData only.");
        }
    }
}

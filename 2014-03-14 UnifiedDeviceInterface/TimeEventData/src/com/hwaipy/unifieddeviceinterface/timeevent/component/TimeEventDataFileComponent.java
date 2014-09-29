package com.hwaipy.unifieddeviceinterface.timeevent.component;

import com.hwaipy.unifieddeviceInterface.Component;
import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataComponent;
import com.hwaipy.unifieddeviceInterface.DataType;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.AbstractDataComponent;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataManager;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hwaipy
 */
public class TimeEventDataFileComponent extends AbstractDataComponent implements Component, DataComponent {

    private static final BlockingQueue<Runnable> tasks = new LinkedBlockingQueue<>(20);

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
            tasks.offer((Runnable) () -> {
                try {
                    TimeEventClusterData cluster = TimeEventDataManager.loadTimeEventClusterData(((FileData) data).getFile(), "PXI");
                    DataUpdateEvent event = new DataUpdateEvent(TimeEventDataFileComponent.this, cluster);
                    fireDataUpdateEvent(event);
                } catch (IOException ex) {
                    //异常处理
                    Logger.getLogger(TimeEventDataFileComponent.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } else {
            throw new IllegalArgumentException("FileData only.");
        }
    }

    static {
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    Runnable runnable = tasks.take();
                    runnable.run();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, "TimeEventDataFileComponentThread");
        thread.setDaemon(true);
        thread.start();
    }
}

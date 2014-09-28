package com.hwaipy.unifieddeviceInterface.components;

import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import java.io.File;

/**
 *
 * @author Hwaipy
 */
public class FileSelectionComponent extends AbstractDataComponent {

    private File file = null;

    @Override
    public void startComponent() {
    }

    @Override
    public void stopComponent() {
    }

    @Override
    public ComponentInformation getInformation() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void select(File file) {
        this.file = file;
        fireDataUpdateEvent(new DataUpdateEvent(this, new FileData(file)));
    }
}

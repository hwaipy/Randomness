package com.hwaipy.unifieddeviceInterface.components;

import com.hwaipy.unifieddeviceInterface.ComponentInformation;
import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataType;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;

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

    @Override
    public Collection<DataType> export() {
        return Arrays.asList(new DataType("FileData", FileData.class));
    }

    @Override
    public void dataUpdate(Data data) {
    }
}

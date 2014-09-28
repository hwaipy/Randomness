package com.hwaipy.unifieddeviceInterface.data;

import com.hwaipy.unifieddeviceInterface.Data;
import java.io.File;

/**
 *
 * @author Hwaipy
 */
public class FileData implements Data {

    private final File file;

    public FileData(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}

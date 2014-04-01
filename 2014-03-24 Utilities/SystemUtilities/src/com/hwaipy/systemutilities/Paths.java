package com.hwaipy.systemutilities;

import java.nio.file.Path;

/**
 *
 * @author Hwaipy
 */
public class Paths {

    private static final String KEY_PATH_DATA_STORAGY = "KeyPathDataStorage";
    private static final String DEF_PATH_DATA_STORAGY = ".";

    public static Path getDataStoragyPath() {
        String directory = Properties.getProperty(KEY_PATH_DATA_STORAGY, DEF_PATH_DATA_STORAGY);
        Path path = java.nio.file.Paths.get(directory);
        return path;
    }
}

package com.hwaipy.systemutilities;

import java.nio.file.Path;

/**
 * 提供系统默认路径。数据从{@link com.hwaipy.systemutilities.Properties Properties}获取。
 *
 * @see com.hwaipy.systemutilities.Properties
 * @author Hwaipy
 */
public class Paths {

    private static final String KEY_PATH_DATA_STORAGY = "KeyPathDataStorage";
    private static final String DEF_PATH_DATA_STORAGY = ".";

    /**
     *
     * @return
     */
    public static Path getDataStoragyPath() {
        String directory = Properties.getProperty(KEY_PATH_DATA_STORAGY, DEF_PATH_DATA_STORAGY);
        Path path = java.nio.file.Paths.get(directory);
        return path;
    }
}

package com.hwaipy.systemutilities;

import java.nio.file.Path;

/**
 *
 * @author Hwaipy
 */
public class Paths {

    private static final String KEY_PATH_DATA_STORAGY = "KeyPathDataStorage";
    private static final String DEF_PATH_DATA_STORAGY = "KeyPathDataStorage";

//    public static Path getDataStoragyPath() {
//
//        Path p = java.nio.file.Paths.get("");
//    }
    public static void main(String[] args) {
        Path p = java.nio.file.Paths.get(".");
        System.out.println(p.toAbsolutePath().toFile().getAbsolutePath());
    }

}

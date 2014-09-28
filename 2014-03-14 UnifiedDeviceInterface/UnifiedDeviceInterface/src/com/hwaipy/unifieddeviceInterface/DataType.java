package com.hwaipy.unifieddeviceInterface;

/**
 *
 * @author Hwaipy
 */
public class DataType {

    private final String name;
    private final Class<? extends Data> dataClasss;

    public DataType(String name, Class<? extends Data> dataClasss) {
        this.name = name;
        this.dataClasss = dataClasss;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Data> getDataClasss() {
        return dataClasss;
    }
}

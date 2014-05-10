package com.hwaipy.apple.iso.backup;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public interface Filter<T> {

    public boolean isValid(T t);
}

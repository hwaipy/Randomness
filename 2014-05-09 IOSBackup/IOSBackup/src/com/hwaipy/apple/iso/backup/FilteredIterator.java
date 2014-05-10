package com.hwaipy.apple.iso.backup;

import java.util.Iterator;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public class FilteredIterator<T> implements Iterator<T> {

    private final Iterator<T> originalIterator;
    private final Filter<T> filter;
    private T next;

    public FilteredIterator(Iterator<T> originalIterator, Filter<T> filter) {
        this.originalIterator = originalIterator;
        this.filter = filter;
        loadNext();
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public T next() {
        T result = next;
        loadNext();
        return result;
    }

    private void loadNext() {
        while (originalIterator.hasNext()) {
            T t = originalIterator.next();
            if (filter.isValid(t)) {
                next = t;
                return;
            }
        }
        next = null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

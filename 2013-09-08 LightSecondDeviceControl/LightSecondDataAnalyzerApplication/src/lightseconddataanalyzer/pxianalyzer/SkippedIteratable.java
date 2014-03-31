package lightseconddataanalyzer.pxianalyzer;

import java.util.Iterator;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public class SkippedIteratable<T> implements Iterable<T> {

    private final Iterable<T> iterable;
    private final int skip;

    public SkippedIteratable(Iterable<T> iterable, int skip) {
        this.iterable = iterable;
        this.skip = skip;
    }

    @Override
    public Iterator<T> iterator() {
        return new SkippedIterator();
    }

    private class SkippedIterator implements Iterator<T> {

        private final Iterator<T> iterator = iterable.iterator();
        private T next = null;

        public SkippedIterator() {
            if (iterator.hasNext()) {
                next = iterator.next();
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public T next() {
            T value = next;
            findNext();
            return value;
        }

        private void findNext() {
            int remaining = skip;
            while (iterator.hasNext() && remaining > 0) {
                iterator.next();
                remaining--;
            }
            if (iterator.hasNext()) {
                next = iterator.next();
            } else {
                next = null;
            }
        }
    }

}

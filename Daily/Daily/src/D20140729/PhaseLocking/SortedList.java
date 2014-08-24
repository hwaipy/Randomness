package D20140729.PhaseLocking;

import java.util.LinkedList;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public class SortedList<T extends Comparable<T>> {

    private final LinkedList<T> list = new LinkedList<>();

    public SortedList() {
    }

    public void offer(T t) {
        for (int i = 0; i < list.size(); i++) {
            T original = list.get(i);
            if (t.compareTo(original) < 0) {
                list.add(i, t);
                return;
            }
        }
        list.add(t);
    }

    public T get(int index) {
        return list.get(index);
    }
}

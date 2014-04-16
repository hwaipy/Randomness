package lightseconddataanalyzer.synctest;

import java.util.TreeMap;

/**
 *
 * @author Hwaipy
 */
public class DistributionStat {

    private final TreeMap<Integer, Integer> map = new TreeMap<>();

    public void increase(int value) {
        Integer count = map.get(value);
        if (count == null) {
            map.put(value, 1);
        } else {
            map.put(value, count + 1);
        }
    }

    public void show() {
        for (Integer key : map.keySet()) {
            System.out.println(key + "\t" + map.get(key));
        }
    }
}

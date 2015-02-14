package lightseconddataanalyzer.synctest;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.Coincidence;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class TimeOrderedCoincidenceList {

    private final ArrayList<Coincidence> coincidencesList = new ArrayList<>();

    public TimeOrderedCoincidenceList(Iterable<Coincidence> coincidences1, Iterable<Coincidence> coincidences2) {
        Iterator<Coincidence> iterator1 = coincidences1.iterator();
        Iterator<Coincidence> iterator2 = coincidences2.iterator();
        Coincidence coincidence1 = null;
        Coincidence coincidence2 = null;
        if (iterator1.hasNext()) {
            coincidence1 = iterator1.next();
        }
        if (iterator2.hasNext()) {
            coincidence2 = iterator2.next();
        }
        while (coincidence1 != null || coincidence2 != null) {
            if (coincidence2 == null) {
                coincidencesList.add(coincidence1);
                coincidence1 = null;
            } else if (coincidence1 == null) {
                coincidencesList.add(coincidence2);
                coincidence2 = null;
            } else {
                long time1 = coincidence1.getEvent1().getTime();
                long time2 = coincidence2.getEvent1().getTime();
                if (time1 < time2) {
                    coincidencesList.add(coincidence1);
                    coincidence1 = null;
                } else if (time1 > time2) {
                    coincidencesList.add(coincidence2);
                    coincidence2 = null;
                } else {
                    System.out.println("equals");
                    time1 = coincidence1.getEvent2().getTime();
                    time2 = coincidence2.getEvent2().getTime();
                    if (time1 < time2) {
                        coincidencesList.add(coincidence1);
                        coincidence1 = null;
                    } else {
                        coincidencesList.add(coincidence2);
                        coincidence2 = null;
                    }
                }
            }
            coincidence1 = iterator1.hasNext() ? iterator1.next() : null;
            coincidence2 = iterator2.hasNext() ? iterator2.next() : null;
        }
    }

    public int size() {
        return coincidencesList.size();
    }

    public Coincidence get(int index) {
        return coincidencesList.get(index);
    }
}

package lightseconddataanalyzer.fourfold;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.Coincidence;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.process.CoincidenceMatcher;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Hwaipy
 */
public class CoincidenceEventList implements TimeEventList {

    private final CoincidenceMatcher matcher;
    private final ArrayList<TimeEvent> events;

    public CoincidenceEventList(CoincidenceMatcher matcher, int order) {
        this.matcher = matcher;
        events = new ArrayList<>(matcher.count());
        Iterator<Coincidence> iterator = matcher.iterator();
        while (iterator.hasNext()) {
            Coincidence coincidence = iterator.next();
            TimeEvent event = order == 1 ? coincidence.getEvent1() : coincidence.getEvent2();
            events.add(event);
        }
    }

    @Override
    public int size() {
        return events.size();
    }

    @Override
    public TimeEvent get(int index) {
        return events.get(index);
    }

    @Override
    public void set(TimeEvent event, int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<TimeEvent> iterator() {
        return events.iterator();
    }
}

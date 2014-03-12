package counterapplication;

import counterapplication.counterdatasource.CounterDataSource;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public class DefaultCounter implements Counter {

    private final CounterDataSource dataSource;

    private final EventListenerList eventListenerList = new EventListenerList();
    private final ActionListener listener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Collection<TimeEvent> data = dataSource.getData();
            System.out.println("New data incoming: " + data.size());
        }
    };
    private int coincidenceGate = 1000;
    private int scanStepLength = 100;

    public DefaultCounter(CounterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void addCounterListener(CounterListener listener) {
        eventListenerList.add(CounterListener.class, listener);
    }

    @Override
    public void removeCounterListener(CounterListener listener) {
        eventListenerList.remove(CounterListener.class, listener);
    }

    @Override
    public void start() {
        dataSource.setAtionListener(listener);
        dataSource.start();
    }

    @Override
    public void stop() {
        dataSource.setAtionListener(null);
        dataSource.stop();
    }

    @Override
    public void setCoincidenceGata(int gate) {
        coincidenceGate = gate;
    }

    @Override
    public void setScanStepLength(int stepLength) {
        scanStepLength = stepLength;
    }
}

package counterapplication.counterdatasource;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 *
 * @author Hwaipy
 */
public interface CounterDataSource {

    public void start();

    public void stop();

    public void setAtionListener(ActionListener listener);

    public Collection<TimeEvent> getData();
}

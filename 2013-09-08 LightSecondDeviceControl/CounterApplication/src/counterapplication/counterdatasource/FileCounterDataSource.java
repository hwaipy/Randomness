package counterapplication.counterdatasource;

import com.hwaipy.unifieddeviceinterface.DeviceException;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.TimeEvent;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.data.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data.HydraHarp400T2Loader;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.timeeventcontainer.TimeEventSegment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import javax.swing.Timer;

/**
 *
 * @author Hwaipy
 */
public class FileCounterDataSource implements CounterDataSource {

    private final File file;
    private final TimeEventSegment segment;
    private final Timer timer = new Timer(100, new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    });
    private ActionListener listener;
    private long startTime;

    public FileCounterDataSource(File file) throws IOException, DeviceException {
        this.file = file;
        HydraHarp400T2Loader loader = new HydraHarp400T2Loader(file);
        segment = TimeEventDataManager.loadTimeEventSegment(loader);
    }

    @Override
    public void start() {
        startTime = System.nanoTime();
        timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public void setAtionListener(ActionListener listener) {
        this.listener = listener;
    }

    @Override
    public Collection<TimeEvent> getData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

package com.hwaipy.unifieddeviceinterface.virtualinstrument;

import com.hwaipy.unifieddeviceInterface.DataInstrument;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.DataUpdateListener;
import com.hwaipy.unifieddeviceInterface.Instrument;
import com.hwaipy.unifieddeviceInterface.InstrumentInformation;
import com.hwaipy.unifieddeviceInterface.RunnableInstrument;
import com.hwaipy.utilities.system.WeakReferenceMapUtilities;
import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

/**
 *
 * @author Hwaipy
 */
public class VirtualCounterInstrument implements Instrument, DataInstrument, RunnableInstrument {

    private final InstrumentInformation information = new InstrumentInformation();
    private Timer timer;
    private int timerIndex = 0;
    private long integrateTime = 1000;
    private int minimalCount = 1000;
    private int maximalCount = 100000;
    private final int channel;

    public VirtualCounterInstrument(int channel) {
        this.channel = channel;
    }

    @Override
    public void openInstrument() {
    }

    @Override
    public void closeInstrument() {
    }

    @Override
    public InstrumentInformation getInformation() {
        return information;
    }

    @Override
    public void startInstrument() {
        timer = new Timer("VirtualCounterInstrumentTimer-" + timerIndex, true);
        timerIndex++;
        timer.scheduleAtFixedRate(new TimerTask() {
            private Random random = new Random();

            @Override
            public void run() {
                int[] counts = new int[channel];
                for (int i = 0; i < channel; i++) {
                    double nd = random.nextDouble();
                    counts[channel] = (int) (nd * (maximalCount - minimalCount) + minimalCount);
                }
                SwingUtilities.invokeLater(() -> {
                    fireDataUpdateEvent(counts);
                });
            }
        }, integrateTime, integrateTime);
    }

    @Override
    public void stopInstrument() {
        timer.cancel();
    }

    private void fireDataUpdateEvent(int[] counts) {
        DataUpdateEvent dataUpdateEvent = new DataUpdateEvent(this, counts);
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            Arrays.stream(eventListenerList.getListeners(DataUpdateListener.class))
                    .forEach(listener -> listener.dataUpdated(dataUpdateEvent));
        }
    }
}

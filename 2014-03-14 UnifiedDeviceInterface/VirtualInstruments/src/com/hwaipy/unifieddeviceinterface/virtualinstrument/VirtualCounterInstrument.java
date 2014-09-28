package com.hwaipy.unifieddeviceinterface.virtualinstrument;

import com.hwaipy.unifieddeviceInterface.DataInstrument;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.DataUpdateListener;
import com.hwaipy.unifieddeviceInterface.Instrument;
import com.hwaipy.unifieddeviceInterface.InstrumentInformation;
import com.hwaipy.unifieddeviceInterface.RunnableInstrument;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
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
    private boolean running = false;

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

    public int getChannelCount() {
        return channel;
    }

    public void setIntegrateTime(long integrateTime) {
        synchronized (this) {
            this.integrateTime = integrateTime;
            if (running) {
                stopInstrument();
                startInstrument();
            }
        }
    }

    @Override
    public void startInstrument() {
        synchronized (this) {
            timer = new Timer("VirtualCounterInstrumentTimer-" + timerIndex, true);
            timerIndex++;
            timer.scheduleAtFixedRate(new TimerTask() {
                private Random random = new Random();

                @Override
                public void run() {
                    long[] counts = new long[channel];
                    for (int i = 0; i < channel; i++) {
                        double nd = random.nextDouble();
                        counts[i] = (long) (nd * (maximalCount - minimalCount) + minimalCount);
                    }
                    SwingUtilities.invokeLater(() -> {
                        fireDataUpdateEvent(counts);
                    });
                }
            }, integrateTime, integrateTime);
            running = true;
        }
    }

    @Override
    public void stopInstrument() {
        synchronized (this) {
            timer.cancel();
            running = false;
        }
    }

    private void fireDataUpdateEvent(long[] counts) {
        DataUpdateEvent dataUpdateEvent = new DataUpdateEvent(this, new CounterData(counts));
        EventListenerList eventListenerList = (EventListenerList) WeakReferenceMapUtilities.get(this, EventListenerList.class);
        if (eventListenerList != null) {
            Arrays.stream(eventListenerList.getListeners(DataUpdateListener.class))
                    .forEach(listener -> listener.dataUpdated(dataUpdateEvent));
        }
    }
}

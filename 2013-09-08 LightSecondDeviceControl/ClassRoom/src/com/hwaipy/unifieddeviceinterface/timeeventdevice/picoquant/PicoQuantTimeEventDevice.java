package com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant;

import com.hwaipy.unifieddeviceinterface.datadispatch.DataDispatcher;
import com.hwaipy.unifieddeviceinterface.datadispatch.DataIncomeListener;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.AbstractTimeEventDevice;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @author Hwaipy
 */
public abstract class PicoQuantTimeEventDevice extends AbstractTimeEventDevice {

    private final int deviceIndex;
    private final String deviceName;
    private final DataDispatcher<ByteBuffer> dataDispatcher;
    private boolean opened = false;
    private boolean measurementing = false;

    protected PicoQuantTimeEventDevice(int deviceIndex, String deviceName) {
        this.deviceIndex = deviceIndex;
        this.deviceName = deviceName;
        dataDispatcher = new DataDispatcher<>();
        measurementExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "PicoQuant Measurement Thread - "
                        + PicoQuantTimeEventDevice.this.deviceName);
                thread.setDaemon(true);
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        });
    }

    @Override
    public void addDataIncomeListener(DataIncomeListener<ByteBuffer> listener) {
        dataDispatcher.addDataIncomeListener(listener);
    }

    @Override
    public void removeDataIncomeListener(DataIncomeListener<ByteBuffer> listener) {
        dataDispatcher.removeDataIncomeListener(listener);
    }

    public abstract void initialize(Mode mode) throws PicoQuantException;

    public abstract String getLibraryVersion();

    public abstract String getSerialNumber() throws PicoQuantException;

    public abstract String getHardwareInfo() throws PicoQuantException;

    public abstract int getNumOfInputChannels() throws PicoQuantException;

    public abstract void setInputCFD(int channel, int zeroCross, int level) throws PicoQuantException;

    public abstract void setInputCFD(int zeroCross, int level) throws PicoQuantException;

    protected abstract void calibrate() throws PicoQuantException;

    protected abstract void startMeasurement(int acquisitionTime) throws PicoQuantException;

    protected abstract void stopMeasurement() throws PicoQuantException;

    protected abstract ByteBuffer readFIFO() throws PicoQuantException;

    protected int getDeviceIndex() {
        return deviceIndex;
    }

    protected boolean isOpened() {
        return opened;
    }

    protected void setOpened(boolean opened) {
        this.opened = opened;
    }

    protected boolean isMeasurementing() {
        return measurementing;
    }
    private AtomicReference<CountDownLatch> stopLatch = new AtomicReference<>(null);
    private final ExecutorService measurementExecutor;
    private boolean stopSignal = false;

    public void start(final int acquisitionTime) {
        if (measurementing) {
            throw new IllegalStateException();
        }
        stopSignal = false;
        measurementing = true;
        measurementExecutor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    calibrate();
                    startMeasurement(acquisitionTime);
                    while (!stopSignal) {
                        ByteBuffer buffer = readFIFO();
                        if (buffer == null) {
                            break;
                        }
                        dataDispatcher.fireDataIncomeEvents(buffer);
                    }
                    stopMeasurement();
                    measurementing = false;
                    CountDownLatch latch = stopLatch.get();
                    if (latch != null) {
                        latch.countDown();
                    }
                } catch (Exception ex) {
                    //TODO Handle this exception
                    throw new RuntimeException();
                }
            }
        });
    }

    public void stopLater() {
        stop();
    }

    public void stopAndWait() throws InterruptedException {
        if (!measurementing) {
            return;
        }
        stopLatch.set(new CountDownLatch(1));
        stop();
        CountDownLatch latch = stopLatch.get();
        if (latch != null) {
            latch.await();
        }
    }

    private void stop() {
        stopSignal = true;
    }
}

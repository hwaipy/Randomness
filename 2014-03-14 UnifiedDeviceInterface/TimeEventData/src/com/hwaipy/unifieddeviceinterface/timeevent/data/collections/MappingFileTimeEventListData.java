package com.hwaipy.unifieddeviceinterface.timeevent.data.collections;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventSerializer;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Hwaipy
 */
public class MappingFileTimeEventListData implements TimeEventListData, SetableTimeEventListData {

    private static final int BUFFER_SIZE = 1024000;
    private static final int UNIT_SIZE = 8;
    private static final ExecutorService EXECUTOR = Executors.newSingleThreadExecutor();
    private boolean complete = false;
    private final File file;
    private final FileChannel channel;
    private int count = 0;
    private ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
    private MappedByteBuffer map;
    private final TimeEventSerializer serializer = new TimeEventSerializer();
    private final boolean disposed = false;

    public MappingFileTimeEventListData(File file) throws IOException {
        this.file = file;
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.setLength(0);
        channel = raf.getChannel();
    }

    @Override
    public int size() {
        if (!complete || disposed) {
            throw new RuntimeException();
        }
        return count;
    }

    @Override
    public TimeEventData get(int index) {
        if (!complete || disposed) {
            throw new RuntimeException();
        }
        if (index >= count) {
            throw new IndexOutOfBoundsException(index + ">=" + count);
        }
        try {
            map.position(index * UNIT_SIZE);
            return read();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void set(TimeEventData event, int index) {
        if (!complete || disposed) {
            throw new RuntimeException();
        }
        if (index >= count) {
            throw new IndexOutOfBoundsException(index + ">=" + count);
        }
        try {
            map.position(index * UNIT_SIZE);
            write(serializer.serialize(event));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Iterator<TimeEventData> iterator() {
        if (!complete || disposed) {
            throw new RuntimeException();
        }
        return new Iterator<TimeEventData>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < count;
            }

            @Override
            public TimeEventData next() {
                TimeEventData e = get(index);
                index++;
                return e;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
    }

    public void push(long storage) throws IOException {
        if (complete || disposed) {
            throw new RuntimeException();
        }
        write(storage);
        count++;
    }

    public void complete() throws IOException {
        if (complete || disposed) {
            throw new RuntimeException();
        }
        complete = true;
        flush(true);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            EXECUTOR.submit((Runnable) () -> {
                countDownLatch.countDown();
            });
            countDownLatch.await();
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
        map = channel.map(FileChannel.MapMode.READ_WRITE, 0, channel.position());
    }

    private TimeEventData read() throws IOException {
        if (!complete || disposed) {
            throw new RuntimeException();
        }
        return serializer.deserialize(map.getLong());
    }

    private void write(long storage) throws IOException {
        if (complete || disposed) {
            map.putLong(storage);
        } else {
            buffer.putLong(storage);
            flush(false);
        }
    }

    private void flush(final boolean force) throws IOException {
        if (force || !buffer.hasRemaining()) {
            final ByteBuffer writeBuffer = buffer;
            buffer = ByteBuffer.allocate(1024000);
            EXECUTOR.submit((Runnable) () -> {
                try {
                    writeBuffer.limit(writeBuffer.position());
                    writeBuffer.position(0);
                    while (writeBuffer.hasRemaining()) {
                        channel.write(writeBuffer);
                    }
                    if (force) {
                        channel.force(false);
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}

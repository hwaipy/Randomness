package com.hwaipy.unifieddeviceinterface.timeeventdevice.hydraharp400data;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author Hwaipy
 */
class MappingReader {

    private final FileChannel channel;
    private final MappedByteBuffer buffer;

    public MappingReader(FileChannel channel) throws IOException {
        this.channel = channel;
        channel.position(0);
        buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        buffer.order(ByteOrder.LITTLE_ENDIAN);
    }

    public String getStringByASCII(int length) {
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        int end = 0;
        while (true) {
            if (bytes[end] == 0) {
                break;
            }
            end++;
        }
        return new String(bytes, 0, end);
    }

    public byte get() {
        return buffer.get();
    }

    public int getInt() {
        return buffer.getInt();
    }

    public long getLong() {
        return buffer.getLong();
    }

    public double getDouble() {
        getInt();
        getInt();
        return 0;
    }

    public void skip(int length) {
        buffer.position(buffer.position() + length);
    }

    public int remaining() {
        return buffer.remaining();
    }

    public boolean hasNextInt() {
        return buffer.remaining() >= 4;
    }

    public int position() {
        return buffer.position();
    }
}

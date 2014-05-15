package com.hwaipy.apple.iso.backup.mbdb;

import com.hwaipy.apple.iso.backup.old.Filter;
import com.hwaipy.apple.iso.backup.old.FilteredIterator;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 解析MBDB文件。MBDB文件用于存储file records.
 * 其包含的Domain和Location字段相连后，通过SHA1得到相应备份文件的名称。
 * @author Hwaipy
 */
public class MBDBParser implements Iterable<MBDBEntry> {

    private final Path path;
    private ArrayList<MBDBEntry> entries;

    private MBDBParser(Path path) {
        this.path = path;
    }

    @Override
    public Iterator<MBDBEntry> iterator() {
        return entries.iterator();
    }

    public Iterator<MBDBEntry> iterator(Filter<MBDBEntry> filter) {
        return new FilteredIterator<>(iterator(), filter);
    }

    private void parse() throws IOException, MBDBParseException {
        FileChannel channel = FileChannel.open(path, StandardOpenOption.READ);
        long fileSize = channel.size();
        ByteBuffer buffer;
        if (fileSize < 10 * 1024 * 1024) {
            buffer = ByteBuffer.allocate((int) fileSize);
            channel.read(buffer);
            buffer.rewind();
        } else {
            buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
        }
        checkHead(buffer);
        LinkedList<MBDBEntry> entriesList = new LinkedList<>();
        while (buffer.hasRemaining()) {
            MBDBEntry entry = readUnit(buffer);
            entriesList.add(entry);
        }
        entries = new ArrayList<>(entriesList.size());
        entries.addAll(entriesList);
    }

    private void checkHead(ByteBuffer buffer) throws MBDBParseException {
        String head = getString(buffer, 4);
        if (!"mbdb".equals(head) || buffer.get() != 5 || buffer.get() != 0) {
            throw new MBDBParseException();
        }
    }

    private MBDBEntry readUnit(ByteBuffer buffer) throws MBDBParseException {
        MBDBEntry entry = new MBDBEntry();
        entry.setDomain(getString(buffer));
        entry.setPath(getString(buffer));
        entry.setLinkTarget(getString(buffer));
        entry.setDataHash(getHash(buffer));
        entry.setUnknown1(getString(buffer));
        entry.setMode(getUnsignedShort(buffer));
        entry.setUnknown2(getUnsignedInteger(buffer));
        entry.setUnknown3(getUnsignedInteger(buffer));
        entry.setUserID(getUnsignedInteger(buffer));
        entry.setGroupID(getUnsignedInteger(buffer));
        entry.setTime1(getUnsignedInteger(buffer));
        entry.setTime2(getUnsignedInteger(buffer));
        entry.setTime3(getUnsignedInteger(buffer));
        entry.setFileLength(getLong(buffer));
        entry.setFlag(getUnsignedByte(buffer));
        int propertyCount = getUnsignedByte(buffer);
        for (int i = 0; i < propertyCount; i++) {
            entry.addProperties(getString(buffer), getString(buffer));
        }
        return entry;
    }

    private String getString(ByteBuffer buffer, int length) throws MBDBParseException {
        if (buffer.remaining() < length) {
            throw new MBDBParseException();
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return new String(bytes);
    }

    private String getString(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 2) {
            throw new MBDBParseException();
        }
        int length = buffer.getShort();
        if (length == -1) {
            return "";
        }
        return getString(buffer, length);
    }

    private int getUnsignedByte(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 1) {
            throw new MBDBParseException();
        }
        int b = buffer.get();
        if (b < 0) {
            b += 256;
        }
        return b;
    }

    private int getUnsignedShort(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 2) {
            throw new MBDBParseException();
        }
        int b = buffer.getShort();
        if (b < 0) {
            b += 65536;
        }
        return b;
    }

    private long getUnsignedInteger(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 4) {
            throw new MBDBParseException();
        }
        long b = buffer.getInt();
        if (b < 0) {
            b += 4294967296l;
        }
        return b;
    }

    private long getLong(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 8) {
            throw new MBDBParseException();
        }
        return buffer.getLong();
    }

    private String getHash(ByteBuffer buffer) throws MBDBParseException {
        if (buffer.remaining() < 2) {
            throw new MBDBParseException();
        }
        int length = buffer.getShort();
        if (length == -1) {
            return "";
        }
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytesToString(bytes);
    }

    private String bytesToString(byte[] data) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] temp = new char[data.length * 2];
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            temp[i * 2] = hexDigits[b >>> 4 & 0x0f];
            temp[i * 2 + 1] = hexDigits[b & 0x0f];
        }
        return new String(temp);
    }

    public static MBDBParser parse(Path path) throws IOException, MBDBParseException {
        MBDBParser parser = new MBDBParser(path);
        parser.parse();
        return parser;
    }
}

package com.hwaipy.unifieddeviceinterface.timeevent.data.io;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventDataFileLoader;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.DefaultTimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.MappingFileTimeEventListData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Hwaipy
 */
public class TimeEventDataManager {

    private static final String MappingFilesRoot = "TimeEventMappingFiles";
    private static final String suffix = "TimeEventMapping";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("YYYY.MM.dd HH.mm.ss.SSS");

    public static TimeEventClusterData loadTimeEventClusterData(TimeEventDataFileLoader loader) throws IOException {
        final File folder = createMappingFileFolder();
        TimeEventSerializer serializer = loader.getSerializer();
        MappingFileTimeEventListData[] mappingLists = createMappingLists(folder, loader.getChannelCount());
        while (true) {
            TimeEventData timeEvent = loader.loadNext();
            if (timeEvent == TimeEventData.ERROR_EVENT) {
                continue;
            }
            if (timeEvent == null) {
                break;
            }
//            long v = timeEvent.getTime() * 16 + timeEvent.getChannel();
//            raf.writeLong(v);;
            mappingLists[timeEvent.getChannel()].push(serializer.serialize(timeEvent));
        }
        for (MappingFileTimeEventListData list : mappingLists) {
            list.complete();
        }
        DefaultTimeEventClusterData cluster = new DefaultTimeEventClusterData(mappingLists);
        loader.complete(segment);
        return segment;
    }

    private static MappingFileTimeEventList[] createMappingLists(File folder, int channelCount) throws IOException {
        MappingFileTimeEventList[] mappingLists = new MappingFileTimeEventList[channelCount];
        for (int i = 0; i < channelCount; i++) {
            File file = new File(folder, i + "." + suffix);
            mappingLists[i] = new MappingFileTimeEventList(file);
        }
        clearOnExit(folder);
        return mappingLists;
    }

    //TODO 文件夹名有冲突隐患
    private static File createMappingFileFolder() {
        File file = new File(MappingFilesRoot, getUniquePrefix());
        file.mkdirs();
        return file;
    }

    private static String getUniquePrefix() {
        DateTime time = new DateTime(System.currentTimeMillis());
        return time.toString(dateTimeFormatter);
    }

    private static void clearOnExit(final File folder) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                clearMappingFiles(folder);
            }
        }));
    }

    private static void clearMappingFiles(File folder) {
        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                file.delete();
            }
            folder.delete();
        }
    }
}

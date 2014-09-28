package com.hwaipy.unifieddeviceinterface.timeevent.data.io;

import com.hwaipy.unifieddeviceinterface.timeevent.data.TimeEventData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.DefaultTimeEventClusterData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.MappingFileTimeEventListData;
import com.hwaipy.unifieddeviceinterface.timeevent.data.collections.TimeEventClusterData;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class TimeEventDataManager {

    private static final String MappingFilesRoot = "TimeEventMappingFiles";
    private static final String suffix = "TimeEventMapping";
    private static final Map<String, TimeEventDataFileLoaderFactory> loaderFactoryMap = new HashMap<>();

    public static TimeEventClusterData loadTimeEventClusterData(File timeEventDataFile, String protocol) throws IOException {
        TimeEventDataFileLoaderFactory loaderFactory = loaderFactoryMap.get(protocol);
        TimeEventDataFileLoader loader = loaderFactory.newTimeEventDataFileLoader(timeEventDataFile);
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
            mappingLists[timeEvent.getChannel()].push(serializer.serialize(timeEvent));
        }
        for (MappingFileTimeEventListData list : mappingLists) {
            list.complete();
        }
        DefaultTimeEventClusterData cluster = new DefaultTimeEventClusterData(mappingLists);
        loader.complete(cluster);
        return cluster;
    }

    public static void registerLoaderFactory(String protocol, TimeEventDataFileLoaderFactory factory) {
        loaderFactoryMap.put(protocol, factory);
    }

    private static MappingFileTimeEventListData[] createMappingLists(File folder, int channelCount) throws IOException {
        MappingFileTimeEventListData[] mappingLists = new MappingFileTimeEventListData[channelCount];
        for (int i = 0; i < channelCount; i++) {
            File file = new File(folder, i + "." + suffix);
            mappingLists[i] = new MappingFileTimeEventListData(file);
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
        return Instant.now().toString();
    }

    private static void clearOnExit(final File folder) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            clearMappingFiles(folder);
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

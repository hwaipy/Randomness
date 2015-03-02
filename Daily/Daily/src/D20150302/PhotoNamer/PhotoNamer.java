package D20150302.PhotoNamer;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Hwaipy
 */
public class PhotoNamer {

    private static final File dir = new File("/Users/Hwaipy/Desktop/benamed");

    public static void main(String[] args) {
        File[] files = dir.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".gif");
            }
        });
        for (File file : files) {
            Instant instant = Instant.ofEpochMilli(file.lastModified());
            LocalDateTime dateTime = java.time.LocalDateTime.ofInstant(instant, ZoneId.of(ZoneId.SHORT_IDS.get("CTT")));
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH.mm.ss");
            String originalName = file.getName();
            for (int offset = 0; offset < 10000; offset++) {
                LocalDateTime time = dateTime.plusSeconds(offset);
                String timeString = time.format(timeFormatter);
                String newName = timeString + originalName.substring(originalName.length() - 4).toLowerCase();
                File newFile = new File(dir, newName);
                if (!newFile.exists()) {
                    file.renameTo(newFile);
                    if (offset != 0) {
                        System.out.println("Offset " + offset + " for " + newFile.getName());
                    }
                    break;
                } else {
                    continue;
                }
            }
        }
    }
}

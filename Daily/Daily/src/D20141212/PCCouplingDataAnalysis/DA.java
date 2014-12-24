package D20141212.PCCouplingDataAnalysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Hwaipy
 */
public class DA {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        File file = new File("/Users/Hwaipy/Desktop/9.fc,non+fc,PC,PBS,T,colli+random.txt");
        TreeMap<Long, LinkedList<Double>> resultMap = new TreeMap<>();
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            Instant start = null;
            while (true) {
                String line = raf.readLine();
                if (line == null) {
                    break;
                }
                String[] split = line.split("\t");
                if (split.length != 4) {
                    continue;
                }
                String timeString = split[0].replaceAll(",", "T") + "Z";
//                System.out.println(timeString);
                Instant instant = java.time.Instant.parse(timeString);
                if (start == null) {
                    start = instant;
                }
                Duration pastTime = Duration.between(start, instant);
                long second = pastTime.getSeconds();
                String resultString = split[3];
                double result = Double.parseDouble(resultString);
                if (!resultMap.containsKey(second)) {
                    resultMap.put(second, new LinkedList<Double>());
                }
                LinkedList<Double> list = resultMap.get(second);
                list.add(result);
            }
        }
        try (PrintWriter writer = new PrintWriter(new File(file.getAbsolutePath() + ".da"))) {
            for (Map.Entry<Long, LinkedList<Double>> entry : resultMap.entrySet()) {
                long second = entry.getKey();
                LinkedList<Double> resultList = entry.getValue();
                int resultCount = resultList.size();
                double resultSum = 0;
                for (Double result : resultList) {
                    resultSum += result;
                }
                double result = resultSum / resultCount;
                writer.println(second + "\t" + result);
            }
        }
    }
}

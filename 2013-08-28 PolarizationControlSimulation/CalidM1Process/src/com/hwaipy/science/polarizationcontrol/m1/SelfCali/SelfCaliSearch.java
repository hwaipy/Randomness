package com.hwaipy.science.polarizationcontrol.m1.SelfCali;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 *
 * @author Hwaipy
 */
public class SelfCaliSearch {

    public static void main(String[] args) throws Exception {
//        overSearch();
        singleSearch();
    }

    public static void overSearch() throws Exception {
        loadNoteList();
        File file = new File("keyOrdersAll.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        LinkedList<String[]> keyOrders = new LinkedList<>();
        while (true) {
            String line = raf.readLine();
            if (line == null) {
                break;
            }
            String[] items = line.split("\t");
            if (items.length != 6) {
                throw new RuntimeException();
            }
            keyOrders.add(items);
        }
        double shortestDistance = Double.MAX_VALUE;
        String[] bestOrder = null;
        for (Note start : noteList) {
//            System.out.println(start);
            for (String[] keyOrder : keyOrders) {
                if (keyOrder[0].equals(start.key)) {
                    double distance = searchRoute(start, keyOrder, false);
                    if (distance < 181) {
                        System.out.println(Arrays.toString(keyOrder) + start);
                    }
                    if (distance < shortestDistance) {
                        shortestDistance = distance;
                        bestOrder = keyOrder;
                    }
                }
            }
        }
        System.out.println(Arrays.toString(bestOrder));
    }

    public static void singleSearch() throws Exception {
        loadNoteList();
        File file = new File("keyOrders.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        LinkedList<String[]> keyOrders = new LinkedList<>();
        while (true) {
            String line = raf.readLine();
            if (line == null) {
                break;
            }
            String[] items = line.split("\t");
            if (items.length != 6) {
                throw new RuntimeException();
            }
            keyOrders.add(items);
        }
        String[] keyOrder = new String[]{"LRVH", "HVLR", "ADHV", "RLAD", "DARL", "VHDA"};
        double distance = searchRoute(new Note(45, 0, 0, "LRVH"), keyOrder, true);
        System.out.println(distance);
    }

    public static void main22(String[] args) throws Exception {
        loadNoteList();
        File file = new File("keyOrdersAll.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        LinkedList<String[]> keyOrders = new LinkedList<>();
        while (true) {
            String line = raf.readLine();
            if (line == null) {
                break;
            }
            String[] items = line.split("\t");
            if (items.length != 6) {
                throw new RuntimeException();
            }
            keyOrders.add(items);
        }
        double shortestDistance = Double.MAX_VALUE;
        String[] bestOrder = null;
        int lineIndex = 0;
        System.out.println(keyOrders.size());
        for (String[] keyOrder : keyOrders) {
            lineIndex++;
            if (lineIndex % 1000 == 0) {
                System.out.println(lineIndex / (double) keyOrders.size());
            }
            double distance = searchRoute(keyOrder, false);
            if (distance < 180.1) {
                System.out.println(Arrays.toString(keyOrder) + distance);
            }
            if (distance < shortestDistance) {
                shortestDistance = distance;
                bestOrder = keyOrder;
            }
        }
        System.out.println(Arrays.toString(bestOrder));
    }

    private static double searchRoute(String[] keyOrder, boolean printTrace) {
        Note note = new Note(0, 0, 0, keyOrder[0]);
        if (printTrace) {
            System.out.println(note);
        }
        double distance = 0;
        for (int i = 1; i < keyOrder.length; i++) {
            String keyNext = keyOrder[i];
            Note noteNext = nearest(note, keyNext);
            distance += note.distance(noteNext);
            note = noteNext;
            if (printTrace) {
                System.out.println(note);
            }
        }
        return distance;
    }

    private static double searchRoute(Note start, String[] keyOrder, boolean printTrace) {
        double distance = 0;
        Note note = start;
        if (printTrace) {
            System.out.println(note);
        }
        for (int i = 1; i < keyOrder.length; i++) {
            String keyNext = keyOrder[i];
            Note noteNext = nearest(note, keyNext);
            distance += note.distance(noteNext);
            note = noteNext;
            if (printTrace) {
                System.out.println(note);
            }
        }
        return distance;
    }

    public static void main11(String[] args) throws Exception {
        loadNoteList();
//        Note noteStart = new Note(0, 0, 0, "HVDA");
//        Note note = noteStart;
//        while (true) {
//            System.out.println(note.key + "\t" + note.theta1 + "\t" + note.theta2 + "\t" + note.theta3);
//            removeRelated(note.key);
//            note = stepNext(note);
//            if (note == null) {
//                break;
//            }
//        }
//        Note[] nearest = nearest(noteStart);
//        for (Note nearest1 : nearest) {
//            System.out.println(nearest1);
//        }
//        
        String[] keys = new String[]{"HVDA", "HVAD", "HVLR", "HVRL", "VHDA", "VHAD", "VHLR", "VHRL", "DAHV", "DAVH", "DALR", "DARL", "ADHV", "ADVH", "ADLR", "ADRL", "LRHV", "LRVH", "LRDA", "LRAD", "RLHV", "RLVH", "RLDA", "RLAD"};
        LinkedList<String[]> keyOrderList = new LinkedList<>();
        for (int i1 = 0; i1 < keys.length; i1++) {
            System.out.println(i1);
            for (int i2 = 0; i2 < keys.length; i2++) {
                for (int i3 = 0; i3 < keys.length; i3++) {
                    for (int i4 = 0; i4 < keys.length; i4++) {
                        for (int i5 = 0; i5 < keys.length; i5++) {
                            for (int i6 = 0; i6 < keys.length; i6++) {
                                String[] keyOrder = new String[]{keys[i1], keys[i2], keys[i3], keys[i4], keys[i5], keys[i6]};
//                                if (valid(keyOrder) && keyOrder[0].equals("VHRL")) {
                                if (valid(keyOrder)) {
                                    keyOrderList.add(keyOrder);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (String[] keyOrder : keyOrderList) {
            for (String key : keyOrder) {
                System.out.print(key + "\t");
            }
            System.out.println();
        }
    }
    private static final LinkedList<Note> noteList = new LinkedList<>();

    private static void loadNoteList() throws Exception {
        File file = new File("AngleAndKey.txt");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        while (true) {
            String line = raf.readLine();
            if (line == null) {
                break;
            }
            String[] items = line.split("\t");
            if (items.length != 4) {
                throw new RuntimeException();
            }
            double theta1 = Double.parseDouble(items[0]);
            double theta2 = Double.parseDouble(items[1]);
            double theta3 = Double.parseDouble(items[2]);
            String key = items[3];
            noteList.add(new Note(theta1, theta2, theta3, key));
        }
    }

    private static boolean valid(String[] keyOrder) {
        if (keyOrder.length != 6) {
            throw new RuntimeException();
        }
        for (int i = 0; i < 4; i++) {
            Set<Character> charSet = new HashSet<>();
            for (int j = 0; j < 6; j++) {
                charSet.add(keyOrder[j].charAt(i));
            }
            if (charSet.size() < 6) {
                return false;
            }
        }
        return true;
    }

    private static Note[] nearest(Note from) {
        ArrayList<Note> nearestList = new ArrayList<>();
        LinkedList<Note> list = new LinkedList<>(noteList);
        while (!list.isEmpty()) {
            Note nearest = stepNext(from);
            if (nearest == null) {
                break;
            }
            nearestList.add(nearest);
            removeAll(nearest.key);
        }
        return nearestList.toArray(new Note[nearestList.size()]);
    }

    private static Note nearest(Note from, String key) {
        LinkedList<Note> list = new LinkedList<>(noteList);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).key.equals(key)) {
                list.remove(i);
                i--;
            }
        }
        Note nearestNote = null;
        double distance = Double.MAX_VALUE;
        for (Note note : list) {
            double d = from.distance(note);
            if (d < distance) {
                distance = d;
                nearestNote = note;
            }
        }
        return nearestNote;
    }

    private static Note stepNext(Note from) {
        Note next = null;
        double currentDistance = Double.MAX_VALUE;
        for (Note note : noteList) {
            double distance = from.distance(note);
            if (distance < currentDistance) {
                currentDistance = distance;
                next = note;
            }
        }
        return next;
    }

    private static void removeAll(String key) {
        for (int i = 0; i < noteList.size(); i++) {
            if (key.equals(noteList.get(i).key)) {
                noteList.remove(i);
                i--;
            }
        }
    }

    private static void removeRelated(String key) {
        for (int i = 0; i < noteList.size(); i++) {
            boolean related = false;
            for (int j = 0; j < key.length(); j++) {
                if (key.charAt(j) == noteList.get(i).key.charAt(j)) {
                    related = true;
                    break;
                }
            }
            if (related) {
                noteList.remove(i);
                i--;
            }
        }
    }

    private static class Note {

        private final double theta1;
        private final double theta2;
        private final double theta3;
        private final String key;

        private Note(double theta1, double theta2, double theta3, String key) {
            this.theta1 = theta1;
            this.theta2 = theta2;
            this.theta3 = theta3;
            this.key = key;
        }

        private double distance(Note target) {
            double distance1 = Math.abs(theta1 - target.theta1);
            double distance2 = Math.abs(theta2 - target.theta2);
            double distance3 = Math.abs(theta3 - target.theta3);
            distance1 = distance1 > 90 ? 180 - distance1 : distance1;
            distance2 = distance2 > 90 ? 180 - distance2 : distance2;
            distance3 = distance3 > 90 ? 180 - distance3 : distance3;
            return distance1 + distance2 + distance3;
        }

        @Override
        public String toString() {
            return (key + "\t" + theta1 + "\t" + theta2 + "\t" + theta3);
        }
    }
}

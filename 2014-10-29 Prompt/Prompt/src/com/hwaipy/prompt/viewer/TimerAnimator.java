package com.hwaipy.prompt.viewer;

/**
 *
 * @author Hwaipy
 */
public class TimerAnimator {

    private boolean running = false;
    private long startTime;

    public TimerAnimator() {
    }

    public void start(long time) {
        running = true;
        startTime = time;
    }

    public void reset() {
        running = false;
    }

    public String updateTime(long time) {
        if (!running) {
            return "Standby";
        }
        long t = time - startTime;
        long timeInSecond = t / 1000000000;
        long minute = timeInSecond / 60;
        long second = timeInSecond % 60;
        String minuteS;
        if (minute < 10) {
            minuteS = "0" + minute;
        } else {
            minuteS = "" + minute;
        }
        String secondS;
        if (second < 10) {
            secondS = "0" + second;
        } else {
            secondS = "" + second;
        }
        return minuteS + ":" + secondS;
    }

//    public static void main(String[] args) {
//        TimerAnimator timerAnimator = new TimerAnimator();
//        timerAnimator.start(System.nanoTime());
//        System.out.println(timerAnimator.updateTime(System.nanoTime() + 2000000000l));
//    }
}

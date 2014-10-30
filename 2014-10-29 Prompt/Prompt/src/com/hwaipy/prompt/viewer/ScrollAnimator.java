package com.hwaipy.prompt.viewer;

/**
 *
 * @author Hwaipy
 */
public class ScrollAnimator {

    private final double acceleration;
    private double currentPosition;
    private double lastTime;
    private double targetPosition;
    private double velocity = 0;

    public ScrollAnimator(double currentPosition, double acceleration, double currentTime) {
        this.currentPosition = currentPosition;
        this.acceleration = acceleration;
        lastTime = currentTime;
        targetPosition = currentPosition;
    }

    public void setTargetPosition(double targetPosition) {
        this.targetPosition = targetPosition;
    }

    public double updatePosition(double time) {
        double v0 = velocity;
        double x0 = currentPosition;
        double xt = targetPosition;
        double D = xt - x0;
//        System.out.println("D=" + D);
        boolean positive = D > 0;
        double a = positive ? acceleration : -acceleration;
        double v0_a = v0 / a;
//        System.out.println("v0=" + v0);
        double T2 = Math.sqrt(v0_a * v0_a / 2 + D / a);
        double T1 = T2 - v0_a;
        double T = time - lastTime;
//        System.out.println("T1=" + T1);
//        System.out.println("T2=" + T2);
        double actualT1;
        double actualT2;
        if (T <= T1) {
            actualT1 = T;
            actualT2 = 0;
        } else {
            actualT1 = T1;
            actualT2 = T - T1;
            if (actualT2 > T2) {
                actualT2 = T2;
            }
        }
        double v1 = v0 + a * actualT1;
        double v2 = v1 - a * actualT2;
//        System.out.println("v1=" + v1);
//        System.out.println("v2=" + v2);
        double dx1 = (v0 + v1) * actualT1 / 2;
        double dx2 = (v1 + v2) * actualT2 / 2;
        velocity = v2;
        currentPosition += dx1 + dx2;
        lastTime = time;
//        System.out.println(currentPosition);
        return currentPosition;
    }

//    public static void main(String[] args) {
//        Animator animator = new Animator(0, 5, 0);
//        animator.setTargetPosition(100);
//        for (double time = 0; time < 100; time += 0.1) {
//            System.out.println(time + "\t" + animator.updatePosition(time));
//        }
//    }
}

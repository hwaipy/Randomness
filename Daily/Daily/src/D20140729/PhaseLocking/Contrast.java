package D20140729.PhaseLocking;

/**
 *
 * @author Hwaipy
 */
public class Contrast {

    private int count = 0;
    private double sumOfFidelity = 0;

    public Contrast() {
    }

    public void offer(double contrast) {
        sumOfFidelity += 1 / (contrast + 1);
        count++;
    }

    public double getAverageContrast() {
        double averageFidelity = sumOfFidelity / count;
        return 1 / averageFidelity - 1;
    }
}

package counterapplication;

/**
 *
 * @author Hwaipy
 */
public class CounterResultPackage {

    private final int c1;
    private final int c2;
    private final int c3;
    private final int c4;
    private final int c12;
    private final int c13;
    private final int c14;
    private final int c23;
    private final int c24;
    private final int c34;

    public CounterResultPackage(int c1, int c2, int c3, int c4,
            int c12, int c13, int c14, int c23, int c24, int c34) {
        this.c1 = c1;
        this.c2 = c2;
        this.c3 = c3;
        this.c4 = c4;
        this.c12 = c12;
        this.c13 = c13;
        this.c14 = c14;
        this.c23 = c23;
        this.c24 = c24;
        this.c34 = c34;
    }

    public int getSingleChannelCount(int channel) {
        switch (channel) {
            case 1:
                return c1;
            case 2:
                return c2;
            case 3:
                return c3;
            case 4:
                return c4;
            default:
                throw new RuntimeException();
        }
    }

    public int getTwoFoldCoincidence(int channel1, int channel2) {
        int code = channel1 * 10 + channel2;
        switch (code) {
            case 12:
                return c12;
            case 13:
                return c13;
            case 14:
                return c14;
            case 23:
                return c23;
            case 24:
                return c24;
            case 34:
                return c34;
            default:
                throw new RuntimeException();
        }
    }
}

package counterapplication;

/**
 *
 * @author Hwaipy
 */
public interface Counter {

    public void start();

    public void stop();

    public void addCounterListener(CounterListener listener);

    public void removeCounterListener(CounterListener listener);

    public void setCoincidenceGata(int gate);

    public void setScanStepLength(int stepLength);
}

package D20141016.GroupVelocityWithFilter;

import java.util.ArrayList;

/**
 *
 * @author Hwaipy
 */
public abstract class CorrelationFunction {

    private final ArrayList<Filter> pumpFilters = new ArrayList<>();
    private final ArrayList<Filter> signalFilters = new ArrayList<>();
    private final ArrayList<Filter> idleFilters = new ArrayList<>();

    public void filterPump(Filter filter) {
        pumpFilters.add(filter);
    }

    public void filterSignal(Filter filter) {
        signalFilters.add(filter);
    }

    public void filterIdle(Filter filter) {
        idleFilters.add(filter);
    }

    public double value(double signalLamda, double idleLamda) {
        double result = correlationValue(signalLamda, idleLamda);
        double pumpLamda = 1 / (1 / signalLamda + 1 / idleLamda);
        for (Filter pumpFilter : pumpFilters) {
            result *= Math.sqrt(pumpFilter.transmittance(pumpLamda));
        }
        for (Filter signalFilter : signalFilters) {
            result *= Math.sqrt(signalFilter.transmittance(signalLamda));
        }
        for (Filter idleFilter : idleFilters) {
            result *= Math.sqrt(idleFilter.transmittance(idleLamda));
        }
        return result;
    }

    protected abstract double correlationValue(double signalLamda, double idleLamda);
}

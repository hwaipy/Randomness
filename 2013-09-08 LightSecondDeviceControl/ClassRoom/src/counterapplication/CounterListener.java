package counterapplication;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface CounterListener extends EventListener {

    public void resultUpdated(CounterResultPackage resultPackage);
}

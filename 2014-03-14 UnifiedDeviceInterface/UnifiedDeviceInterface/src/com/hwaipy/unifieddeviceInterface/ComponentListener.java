package com.hwaipy.unifieddeviceInterface;

import java.util.EventListener;

/**
 *
 * @author Hwaipy
 */
public interface ComponentListener extends EventListener {

    public default void componentStarting(ComponentEvent event) {
    }

    public default void componentStarted(ComponentEvent event) {
    }

    public default void componentStoping(ComponentEvent event) {
    }

    public default void componentStoped(ComponentEvent event) {
    }
}

package com.hwaipy.unifieddeviceInterface;

import java.util.EventObject;

/**
 *
 * @author Hwaipy
 */
public class ComponentEvent extends EventObject {

    public ComponentEvent(Component source) {
        super(source);
    }
}

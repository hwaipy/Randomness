package com.hwaipy.unifieduserinterface;

import com.hwaipy.unifieddeviceInterface.Component;
import javax.swing.JPanel;

/**
 *
 * @author Hwaipy
 * @param <T>
 */
public class ComponentPanel<T extends Component> extends JPanel {

    private final T component;

    public ComponentPanel(T component) {
        this.component = component;
    }

    protected Component getComponent() {
        return component;
    }
}

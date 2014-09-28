package com.hwaipy.unifieduserinterface.componentpanels;

import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.DataUpdateListener;
import com.hwaipy.unifieddeviceInterface.components.CounterComponent;
import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageConstraint;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageLayoutManager;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Hwaipy
 */
public class CounterComponentPanel extends ComponentPanel<CounterComponent> {

    private JLabel title = new JLabel("Title");
    private JFormattedTextField count = new JFormattedTextField();

    public CounterComponentPanel(CounterComponent component) {
        super(component);
        setLayout(new PercentageLayoutManager());
        count.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));
        add(title, new PercentageConstraint(0, 0, 1, 0.3));
        add(count, new PercentageConstraint(0, 0.3, 1, 0.7));
        component.addDataUpdateListener((DataUpdateEvent event) -> {

        });
    }
}

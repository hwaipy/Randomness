package com.hwaipy.unifieduserinterface.componentpanels;

import com.hwaipy.unifieddeviceInterface.Data;
import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.CounterComponent;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageConstraint;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageLayoutManager;
import java.text.NumberFormat;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Hwaipy
 */
public class SingleChannelCounterComponentPanel extends ComponentPanel<CounterComponent> {

    private JLabel title = new JLabel("Title");
    private JFormattedTextField count = new JFormattedTextField();

    public SingleChannelCounterComponentPanel(CounterComponent component, int channel) {
        super(component);
        setLayout(new PercentageLayoutManager());
        count.setFormatterFactory(new DefaultFormatterFactory(new NumberFormatter(NumberFormat.getIntegerInstance())));
        count.setValue(0);
        count.setHorizontalAlignment(JTextField.TRAILING);
        title.setHorizontalTextPosition(JTextField.CENTER);
        add(title, new PercentageConstraint(0, 0, 1, 0.3));
        add(count, new PercentageConstraint(0, 0.3, 1, 0.7));
        count.setEditable(false);
        component.addDataUpdateListener((DataUpdateEvent event) -> {
            Data data = event.getData();
            if (data instanceof CounterData) {
                CounterData counterData = (CounterData) data;
                count.setValue(counterData.getCount(channel));
            } else {
                throw new IllegalArgumentException("CounterData only.");
            }
        });
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }
}

package uditest;

import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.FileSelectionComponent;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import com.hwaipy.unifieddeviceinterface.timeevent.component.RecursionCoincidenceMatcherComponent;
import com.hwaipy.unifieddeviceinterface.timeevent.component.TimeEventClusterDataCounterComponent;
import com.hwaipy.unifieddeviceinterface.timeevent.component.TimeEventDataFileComponent;
import com.hwaipy.unifieddeviceinterface.timeevent.data.io.TimeEventDataManager;
import com.hwaipy.unifieddeviceinterface.timeevent.pxi.PXITimeEventDataFileLoaderFactory;
import com.hwaipy.unifieddeviceinterface.virtualinstrument.VirtualCounterComponent;
import com.hwaipy.unifieddeviceinterface.virtualinstrument.VirtualCounterInstrument;
import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.componentpanels.FileSelectionComponentPanel;
import com.hwaipy.unifieduserinterface.componentpanels.SingleChannelCounterComponentPanel;
import com.hwaipy.unifieduserinterface.layoutmanager.GridConstraint;
import com.hwaipy.unifieduserinterface.main.MainPanel;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 *
 * @author Hwaipy
 */
public class UDITest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        JFrame jFrame = new JFrame("UDI Test");
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel mainPanel = new MainPanel();
        jFrame.setContentPane(mainPanel);

        VirtualCounterInstrument virtualCounterInstrument = new VirtualCounterInstrument(8);
        VirtualCounterComponent virtualCounterComponent = new VirtualCounterComponent(virtualCounterInstrument);
        virtualCounterComponent.addDataUpdateListener((DataUpdateEvent event) -> {
            CounterData data = (CounterData) event.getData();
            int channel = data.getChannelCount();
            for (int i = 0; i < channel; i++) {
                System.out.print(data.getCount(i) + "\t");
            }
            System.out.println();
        });
//        virtualCounterInstrument.open();
//        virtualCounterComponent.start();

        mainPanel.getGridPanel().setGridDimension(10, 10);

        TimeEventDataManager.registerLoaderFactory("PXI", new PXITimeEventDataFileLoaderFactory());

        //Component定义
        FileSelectionComponent fileSelectionComponent1 = new FileSelectionComponent();
        TimeEventDataFileComponent timeEventDataFileComponent1 = new TimeEventDataFileComponent();
        TimeEventClusterDataCounterComponent timeEventClusterDataCounterComponent1 = new TimeEventClusterDataCounterComponent();
        FileSelectionComponent fileSelectionComponent2 = new FileSelectionComponent();
        TimeEventDataFileComponent timeEventDataFileComponent2 = new TimeEventDataFileComponent();
        TimeEventClusterDataCounterComponent timeEventClusterDataCounterComponent2 = new TimeEventClusterDataCounterComponent();
        RecursionCoincidenceMatcherComponent gpsRecursionCoincidenceMatcherComponent = new RecursionCoincidenceMatcherComponent(0, 0);

        //Component连接
        fileSelectionComponent1.addDataUpdateListener((DataUpdateEvent event) -> {
            if (event.getData() instanceof FileData) {
                timeEventDataFileComponent1.dataUpdate(event.getData());
            }
        });
        timeEventDataFileComponent1.addDataUpdateListener((DataUpdateEvent event) -> {
            timeEventClusterDataCounterComponent1.dataUpdate(event.getData());
            gpsRecursionCoincidenceMatcherComponent.dataUpdate(event.getData(), 0);
        });
        timeEventClusterDataCounterComponent1.addDataUpdateListener((DataUpdateEvent event) -> {
        });
        fileSelectionComponent2.addDataUpdateListener((DataUpdateEvent event) -> {
            if (event.getData() instanceof FileData) {
                timeEventDataFileComponent2.dataUpdate(event.getData());
            }
        });
        timeEventDataFileComponent2.addDataUpdateListener((DataUpdateEvent event) -> {
            timeEventClusterDataCounterComponent2.dataUpdate(event.getData());
            gpsRecursionCoincidenceMatcherComponent.dataUpdate(event.getData(), 1);
        });
        timeEventClusterDataCounterComponent2.addDataUpdateListener((DataUpdateEvent event) -> {
        });

        //ComponentPanel定义
        ComponentPanel fileSelectionComponentPanel1 = new FileSelectionComponentPanel(fileSelectionComponent1);
        mainPanel.getGridPanel().addComponentPanel(fileSelectionComponentPanel1, new GridConstraint(0, 0, 2, 1));
        for (int i = 0; i < 8; i++) {
            SingleChannelCounterComponentPanel singleChannelCounterComponentPanel = new SingleChannelCounterComponentPanel(timeEventClusterDataCounterComponent1, i);
            singleChannelCounterComponentPanel.setTitle("Channel " + i);
            mainPanel.getGridPanel().addComponentPanel(singleChannelCounterComponentPanel, new GridConstraint(0, i + 1, 2, 1));
        }
        ComponentPanel fileSelectionComponentPanel2 = new FileSelectionComponentPanel(fileSelectionComponent2);
        mainPanel.getGridPanel().addComponentPanel(fileSelectionComponentPanel2, new GridConstraint(2, 0, 2, 1));
        for (int i = 0; i < 8; i++) {
            SingleChannelCounterComponentPanel singleChannelCounterComponentPanel = new SingleChannelCounterComponentPanel(timeEventClusterDataCounterComponent2, i);
            singleChannelCounterComponentPanel.setTitle("Channel " + i);
            mainPanel.getGridPanel().addComponentPanel(singleChannelCounterComponentPanel, new GridConstraint(2, i + 1, 2, 1));
        }

        jFrame.pack();
        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        jFrame.setVisible(true);
    }
}

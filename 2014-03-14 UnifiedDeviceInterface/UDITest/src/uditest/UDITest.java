package uditest;

import com.hwaipy.unifieddeviceInterface.DataUpdateEvent;
import com.hwaipy.unifieddeviceInterface.components.FileSelectionComponent;
import com.hwaipy.unifieddeviceInterface.data.CounterData;
import com.hwaipy.unifieddeviceInterface.data.FileData;
import com.hwaipy.unifieddeviceinterface.timeevent.component.TimeEventDataFileComponent;
import com.hwaipy.unifieddeviceinterface.virtualinstrument.VirtualCounterComponent;
import com.hwaipy.unifieddeviceinterface.virtualinstrument.VirtualCounterInstrument;
import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.componentpanels.FileSelectionComponentPanel;
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

        FileSelectionComponent fileSelectionComponent = new FileSelectionComponent();
        TimeEventDataFileComponent timeEventDataFileComponent = new TimeEventDataFileComponent();
        fileSelectionComponent.addDataUpdateListener((DataUpdateEvent event) -> {
            System.out.println(((FileData) event.getData()).getFile());
        });

        ComponentPanel testComponentPanel = new FileSelectionComponentPanel(fileSelectionComponent);
        mainPanel.getGridPanel().addComponentPanel(testComponentPanel, new GridConstraint(0, 0, 2, 1));

        jFrame.pack();
        Dimension frameSize = jFrame.getSize();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        jFrame.setVisible(true);
    }

    private void test() {
    }
}

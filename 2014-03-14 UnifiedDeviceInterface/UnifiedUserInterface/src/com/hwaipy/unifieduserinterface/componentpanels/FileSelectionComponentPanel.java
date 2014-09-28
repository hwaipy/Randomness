package com.hwaipy.unifieduserinterface.componentpanels;

import com.hwaipy.unifieddeviceInterface.components.FileSelectionComponent;
import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageConstraint;
import com.hwaipy.unifieduserinterface.layoutmanager.PercentageLayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

/**
 *
 * @author Hwaipy
 */
public class FileSelectionComponentPanel extends ComponentPanel<FileSelectionComponent> {

    private JLabel title = new JLabel("Select a file");
    private JFileChooser fileChooser = new JFileChooser(new File("/Users/Hwaipy/Desktop/DAT/"));

    public FileSelectionComponentPanel(FileSelectionComponent component) {
        super(component);
        setLayout(new PercentageLayoutManager());
        add(title, new PercentageConstraint(0, 0, 1, 1));
        title.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int option = fileChooser.showOpenDialog(FileSelectionComponentPanel.this);
                    if (JFileChooser.APPROVE_OPTION == option) {
                        File selectedFile = fileChooser.getSelectedFile();
                        component.select(selectedFile);
                    }
                }
            }
        });
    }
}

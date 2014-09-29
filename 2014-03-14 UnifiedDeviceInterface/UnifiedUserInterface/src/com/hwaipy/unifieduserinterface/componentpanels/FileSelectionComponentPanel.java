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
import javax.swing.JTextField;

/**
 *
 * @author Hwaipy
 */
public class FileSelectionComponentPanel extends ComponentPanel<FileSelectionComponent> {

    private JLabel title = new JLabel("Double click to select a file");
    private JLabel fileNameLabel = new JLabel("");
    private JFileChooser fileChooser = new JFileChooser(new File("/Users/Hwaipy/Desktop/DAT/"));
    private File file = null;

    public FileSelectionComponentPanel(FileSelectionComponent component) {
        super(component);
        setLayout(new PercentageLayoutManager());
        title.setHorizontalAlignment(JTextField.CENTER);
        add(title, new PercentageConstraint(0, 0, 1, 0.4));
        fileNameLabel.setHorizontalAlignment(JTextField.CENTER);
        add(fileNameLabel, new PercentageConstraint(0, 0.4, 1, 0.6));
        title.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int option = fileChooser.showOpenDialog(FileSelectionComponentPanel.this);
                    if (JFileChooser.APPROVE_OPTION == option) {
                        file = fileChooser.getSelectedFile();
                        fileNameLabel.setText(file.getName());
                        component.select(file);
                    }
                }
            }
        });
    }
}

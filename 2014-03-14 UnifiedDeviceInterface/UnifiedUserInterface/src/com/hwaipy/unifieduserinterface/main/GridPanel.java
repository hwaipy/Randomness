package com.hwaipy.unifieduserinterface.main;

import com.hwaipy.unifieduserinterface.ComponentPanel;
import com.hwaipy.unifieduserinterface.layoutmanager.GridConstraint;
import com.hwaipy.unifieduserinterface.layoutmanager.GridLayoutManager;
import java.awt.Color;
import javax.swing.JPanel;

/**
 *
 * @author Hwaipy
 */
public class GridPanel extends JPanel {

    public GridPanel() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager();
        setLayout(gridLayoutManager);
        setBackground(Color.YELLOW);
    }

    public void setGridDimension(int newColumn, int newRow) {
        ((GridLayoutManager) getLayout()).setGridDimension(newColumn, newRow);
    }

    public void addComponentPanel(ComponentPanel componentPanel, GridConstraint gridConstraint) {
        //test code
        add(componentPanel, gridConstraint);
    }
}

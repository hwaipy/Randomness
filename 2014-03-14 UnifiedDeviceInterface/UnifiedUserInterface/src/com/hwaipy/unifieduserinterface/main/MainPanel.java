package com.hwaipy.unifieduserinterface.main;

import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarConstraintBody;
import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarConstraintHead;
import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarLayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Hwaipy
 */
public class MainPanel extends JPanel {

    private final HeadBarPanel headBarPanel = new HeadBarPanel();
    private final GridPanel gridPanel = new GridPanel();

    public MainPanel() {
        setLayout(new HeadBarLayoutManager());
        add(headBarPanel, new HeadBarConstraintHead(50));
        add(gridPanel, new HeadBarConstraintBody());
    }

    public HeadBarPanel getHeadBarPanel() {
        return headBarPanel;
    }

    public GridPanel getGridPanel() {
        return gridPanel;
    }
}

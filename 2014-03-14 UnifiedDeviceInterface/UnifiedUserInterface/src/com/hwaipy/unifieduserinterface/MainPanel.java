package com.hwaipy.unifieduserinterface;

import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarConstraintBody;
import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarConstraintHead;
import com.hwaipy.unifieduserinterface.layoutmanager.HeadBarLayoutManager;
import javax.swing.JPanel;

/**
 *
 * @author Hwaipy
 */
public class MainPanel extends JPanel {

    public MainPanel() {
        setLayout(new HeadBarLayoutManager());
        add(new HeadbarPanel(), new HeadBarConstraintHead(50));
        add(new GridPanel(), new HeadBarConstraintBody());
    }
}

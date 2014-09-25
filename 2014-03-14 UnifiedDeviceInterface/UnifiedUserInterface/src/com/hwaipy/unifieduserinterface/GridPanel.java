package com.hwaipy.unifieduserinterface;

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
        JPanel jPanel1 = new JPanel();
        JPanel jPanel2 = new JPanel();
        JPanel jPanel3 = new JPanel();
        jPanel1.setBackground(Color.CYAN);
        jPanel2.setBackground(Color.GRAY);
        jPanel3.setBackground(Color.ORANGE);
        add(jPanel1, new GridConstraint(0, 0, 2, 1));
        add(jPanel2, new GridConstraint(0, 1, 1, 1));
        add(jPanel3, new GridConstraint(2, 1, 1, 1));
    }
}

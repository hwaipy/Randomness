package com.hwaipy.unifieduserinterface.layoutmanager;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;

/**
 *
 * @author Hwaipy
 */
public class HeadBarLayoutManager implements LayoutManager2 {

    private Component head = null;
    private Component body = null;
    private int headheight = 0;

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof HeadBarConstraintHead) {
            head = comp;
            headheight = ((HeadBarConstraintHead) constraints).getHeight();
        } else if (constraints instanceof HeadBarConstraintBody) {
            body = comp;
        } else {
            throw new IllegalArgumentException("Constraints must be instance of HeadBarConstraintHead or HeadBarConstraintBody.");
        }
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    @Override
    public void invalidateLayout(Container target) {
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(800, 600);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public void layoutContainer(Container parent) {
        Dimension containerSize = parent.getSize();
        Insets insets = parent.getInsets();
        head.setBounds(insets.left, insets.top, containerSize.width - insets.left - insets.right, headheight);
        body.setBounds(insets.left, insets.top + headheight, containerSize.width - insets.left - insets.right, containerSize.height - headheight - insets.top - insets.bottom);
    }
}

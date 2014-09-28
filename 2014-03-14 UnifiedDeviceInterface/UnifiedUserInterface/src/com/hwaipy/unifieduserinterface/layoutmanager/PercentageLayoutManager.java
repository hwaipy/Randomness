package com.hwaipy.unifieduserinterface.layoutmanager;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hwaipy
 */
public class PercentageLayoutManager implements LayoutManager2 {

    private Map<Component, PercentageConstraint> componentMap = new HashMap<>();

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof PercentageConstraint) {
            PercentageConstraint percentageConstraint = (PercentageConstraint) constraints;
            componentMap.put(comp, percentageConstraint);
        } else {
            throw new IllegalArgumentException("Constraint must be instance of GridConstraint.");
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(0, 0);
    }

    @Override
    public void layoutContainer(Container parent) {
        Insets insets = parent.getInsets();
        Dimension containerSize = parent.getSize();
        int xStart = insets.left;
        int width = containerSize.width - insets.left - insets.right;
        int yStart = insets.top;
        int height = containerSize.height - insets.top - insets.bottom;
        componentMap.entrySet().forEach(entry -> {
            Component component = entry.getKey();
            PercentageConstraint constraint = entry.getValue();
            double xDouble = xStart + width * constraint.getX();
            double yDouble = yStart + height * constraint.getY();
            double widthDouble = width * constraint.getWidth();
            double heightDouble = height * constraint.getHeight();
            component.setBounds((int) xDouble, (int) yDouble, (int) widthDouble, (int) heightDouble);
        });
    }
}

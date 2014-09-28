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
public class GridLayoutManager implements LayoutManager2 {

    private int column = 3;
    private int row = 2;
    private int inset = 5;
    private boolean[][] states = new boolean[row][column];
    private Map<Component, GridConstraint> componentMap = new HashMap<>();

    public void setInset(int inset) {
        this.inset = inset;
    }

    public void setGridDimension(int newColumn, int newRow) {
        boolean[][] newStates = new boolean[newRow][newColumn];
        for (int x = 0; x < Math.max(column, newColumn); x++) {
            for (int y = 0; y < Math.max(row, newRow); y++) {
                if (x < column && y < row) {
                    //in old region
                    if (x < newColumn && y < newRow) {
                        //in new region
                        newStates[y][x] = states[y][x];
                    } else {
                        //out of new region
                        if (states[y][x] == true) {
                            throw new IllegalStateException("Old region has component.");
                        }
                    }
                } else {
                    //out of old region
                    if (x < newColumn && y < newRow) {
                        //in new region
                        newStates[y][x] = false;
                    } else {
                        //out of new region
                    }
                }
            }
        }
        states = newStates;
        this.column = newColumn;
        this.row = newRow;
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof GridConstraint) {
            GridConstraint gridConstraint = (GridConstraint) constraints;
            componentMap.put(comp, gridConstraint);
            if (gridConstraint.getX() + gridConstraint.getWidth() > column || gridConstraint.getY() + gridConstraint.getHeight() > row) {
                throw new IllegalArgumentException("Out of grid region.");
            }
            for (int x = gridConstraint.getX(); x < gridConstraint.getX() + gridConstraint.getWidth(); x++) {
                for (int y = gridConstraint.getY(); y < gridConstraint.getY() + gridConstraint.getHeight(); y++) {
                    if (states[y][x]) {
                        throw new IllegalStateException("Region overlaped.");
                    }
                    states[y][x] = true;
                }
            }
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
        int yStart = insets.top;
        double w = (containerSize.width - inset) / (double) column - inset;
        double h = (containerSize.height - inset) / (double) row - inset;
        componentMap.entrySet().forEach(entry -> {
            Component component = entry.getKey();
            GridConstraint constraint = entry.getValue();
            double xDouble = xStart + inset + (w + inset) * constraint.getX();
            double yDouble = yStart + inset + (h + inset) * constraint.getY();
            double widthDouble = (w + inset) * constraint.getWidth() - inset;
            double heightDouble = (h + inset) * constraint.getHeight() - inset;
            component.setBounds((int) xDouble, (int) yDouble, (int) widthDouble, (int) heightDouble);
        });
    }
}

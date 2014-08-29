package com.hwaipy.figurerenderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Hwaipy
 */
public abstract class Renderer {

    private AffineTransform affineTransform = new AffineTransform();

    public abstract void render(Graphics2D graphics2D);

    public void setAffineTransform(AffineTransform affineTransform) {
        this.affineTransform = affineTransform;
    }

    public AffineTransform getAffineTransform() {
        return affineTransform;
    }
}

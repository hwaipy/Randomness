package com.hwaipy.figurerenderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

/**
 *
 * @author Hwaipy
 */
public class Canvas {

    private final int width;
    private final int height;
    private LinkedList<Renderer> renderers = new LinkedList<>();

    public Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void appandRenderer(Renderer renderer) {
        renderers.addLast(renderer);
    }

    public void insertRenderer(int index, Renderer renderer) {
        renderers.add(index, renderer);
    }

    public BufferedImage renderAsBitmat() {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = image.createGraphics();
        render(graphics2D);
        graphics2D.dispose();
        return image;
    }

    public void renderAsEPS(OutputStream outputStream) throws IOException {
        EPSDocumentGraphics2D graphics2D = new EPSDocumentGraphics2D(false);
        graphics2D.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());
        graphics2D.setupDocument(outputStream, width, height);

        render(graphics2D);
        graphics2D.finish();
    }

    private void render(Graphics2D graphics2D) {
        AffineTransform initialTransform = graphics2D.getTransform();
        renderers.stream().map((renderer) -> {
            graphics2D.setTransform(initialTransform);
            graphics2D.transform(renderer.getAffineTransform());
            return renderer;
        }).forEach((renderer) -> {
            renderer.render(graphics2D);
        });
    }
}

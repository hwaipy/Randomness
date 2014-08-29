package com.hwaipy.figurerenderer.tryout;

import com.hwaipy.figurerenderer.Canvas;
import com.hwaipy.figurerenderer.Renderer;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.apache.xmlgraphics.image.loader.impl.imageio.ImageIOUtil;

/**
 *
 * @author Hwaipy
 */
public class Tryout {

    public static void main(String[] args) throws IOException {
        tryOne();
    }

    public static void tryOne() throws IOException {
        Canvas canvas = new Canvas(500, 300);
        canvas.appandRenderer(new Renderer1());
        canvas.appandRenderer(new Renderer2());
        BufferedImage bitmat = canvas.renderAsBitmat();
        ImageIO.write(bitmat, "png", new File("renderertest.png"));
        FileOutputStream fileOutputStream = new FileOutputStream("renderertest.eps");
        canvas.renderAsEPS(fileOutputStream);
        fileOutputStream.close();
    }

    private static class Renderer1 extends Renderer {

        public Renderer1() {
        }

        @Override
        public void render(Graphics2D graphics2D) {
            graphics2D.setColor(Color.RED);
            graphics2D.fillRect(0, 0, 300, 300);
        }
    };

    private static class Renderer2 extends Renderer {

        public Renderer2() {
        }

        @Override
        public void render(Graphics2D graphics2D) {
            graphics2D.setColor(Color.GREEN);
            graphics2D.fillRect(100, 50, 50, 50);
        }
    };
}

package D20140128.ApacheXMLGraphicsTest;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import org.apache.xmlgraphics.java2d.ps.EPSDocumentGraphics2D;

/**
 *
 * @author Hwaipy
 */
public class FineXMLGraphicsTry {

    public static void generateEPSusingJava2D(File outputFile) throws IOException {
        OutputStream out = new java.io.FileOutputStream(outputFile);
        out = new java.io.BufferedOutputStream(out);
        try {
            //Instantiate the EPSDocumentGraphics2D instance
            EPSDocumentGraphics2D g2d = new EPSDocumentGraphics2D(false);
            g2d.setGraphicContext(new org.apache.xmlgraphics.java2d.GraphicContext());

            //Set up the document size
            g2d.setupDocument(out, 25400, 25400); //400pt x 200pt

            //Paint a bounding box
            g2d.drawRect(0, 0, 400, 200);

            //A few rectangles rotated and with different color
            Graphics2D copy = (Graphics2D) g2d.create();
            int c = 12;
            for (int i = 0; i < c; i++) {
                float f = ((i + 1) / (float) c);
                Color col = new Color(0.0f, 1 - f, 0.0f);
                copy.setColor(col);
                copy.fillRect(70, 90, 50, 50);
                copy.rotate(-2 * Math.PI / (double) c, 70, 90);
            }
            copy.dispose();

            //Some text
            g2d.rotate(-0.25);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("sans-serif", Font.PLAIN, 36));
            g2d.drawString("Hello world!", 140, 140);
            g2d.setColor(Color.RED.darker());
            g2d.setFont(new Font("serif", Font.PLAIN, 36));
            g2d.drawString("Hello world!", 140, 180);

            //Cleanup
            g2d.finish();
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    /**
     * Command-line interface
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            generateEPSusingJava2D(new File(new File("."), "eps-example1.eps"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}

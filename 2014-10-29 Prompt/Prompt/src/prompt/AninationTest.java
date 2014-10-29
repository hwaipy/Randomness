package D20141029.Prompt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author Hwaipy
 */
public class AninationTest {

    private static int position = 0;

    public static void main(String[] args) {
        final JFrame frame = new JFrame();
        frame.pack();
        frame.setVisible(true);
        frame.setSize(1000, 500);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //通过调用GraphicsEnvironment的getDefaultScreenDevice方法获得当前的屏幕设备了  
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // 全屏设置  
        gd.setFullScreenWindow(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {
                String text = "This is an animation";
//                g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Color.RED);
                g.drawString(text, 200, 200 + position);
                g.fillRect(200 + position, 200, 200, 200);
            }
        });
        Timer animationTimer = new Timer(10, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                position++;
                frame.repaint();
            }
        });
        animationTimer.setInitialDelay(1000);
        animationTimer.setRepeats(true);
        animationTimer.start();
    }
}

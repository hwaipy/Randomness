package prompt;

import com.hwaipy.prompt.Presentation;
import com.hwaipy.prompt.viewer.PresentationViewer;
import com.hwaipy.prompt.viewer.PresentationViewerContent;
import java.awt.AWTEvent;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

/**
 *
 * @author Hwaipy
 */
public class PromptTest {

    public static void main(String[] args) throws IOException {
        File file = new File("content.cnt");
        Presentation presentation = Presentation.load(file);
        PresentationViewerContent viewerContent = new PresentationViewerContent(presentation);

        final PresentationViewer viewer = new PresentationViewer(viewerContent);

        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        gd.setFullScreenWindow(mainFrame);
        mainFrame.setContentPane(viewer);
//        JFrame没有双缓冲

        Toolkit.getDefaultToolkit().addAWTEventListener((AWTEvent event) -> {
            if (event instanceof KeyEvent) {
                KeyEvent keyEvent = (KeyEvent) event;
                int id = keyEvent.getID();
                char keyChar = keyEvent.getKeyChar();
                int keyCode = keyEvent.getKeyCode();
                int modifiers = keyEvent.getModifiers();
                //Debug: Command+D
                if ((id == KeyEvent.KEY_PRESSED) && ((keyChar == 'd') || (keyChar == 'D'))
                        && (modifiers == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
                    viewer.setViewLine(3);
                }
                //up
                if ((id == KeyEvent.KEY_PRESSED) && (keyCode == KeyEvent.VK_UP)) {
                    viewer.increaseViewLine(false);
                }
                //down
                if ((id == KeyEvent.KEY_PRESSED) && (keyCode == KeyEvent.VK_DOWN)) {
                    viewer.increaseViewLine(true);
                }
                //Timer start
                if ((id == KeyEvent.KEY_PRESSED) && ((keyChar == 't') || (keyChar == 'T'))
                        && (modifiers == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
                    viewer.timerStart();
                }
                //Timer reset
                if ((id == KeyEvent.KEY_PRESSED) && ((keyChar == 'r') || (keyChar == 'R'))
                        && (modifiers == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
                    viewer.timerReset();
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }
}

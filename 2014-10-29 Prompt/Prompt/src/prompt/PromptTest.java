package prompt;

import com.hwaipy.prompt.Presentation;
import com.hwaipy.prompt.viewer.PresentationViewer;
import com.hwaipy.prompt.viewer.PresentationViewerContent;
import java.awt.AWTEvent;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
//        JFrame没有双缓冲
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        mainFrame.setSize(screenSize);
//        mainFrame.setVisible(true);
        mainFrame.setContentPane(viewer);

        //Register keys
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
//                    viewer.increaseViewLine(false);
                    indexOfView--;
                    if (indexOfView < 0) {
                        indexOfView = 0;
                    }
                    sendCommand("view:" + indexOfView);
                }
                //down
                if ((id == KeyEvent.KEY_PRESSED) && (keyCode == KeyEvent.VK_DOWN)) {
//                    viewer.increaseViewLine(true);
                    indexOfView++;
                    sendCommand("view:" + indexOfView);
                }
                //Timer start
                if ((id == KeyEvent.KEY_PRESSED) && ((keyChar == 't') || (keyChar == 'T'))
                        && (modifiers == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
//                    viewer.timerStart();
                    sendCommand("Timer:Start");
                }
                //Timer reset
                if ((id == KeyEvent.KEY_PRESSED) && ((keyChar == 'r') || (keyChar == 'R'))
                        && (modifiers == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())) {
//                    viewer.timerReset();
                    sendCommand("Timer:Reset");
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);

        String broadCastMessage = "heart click";
        //Open Broadcast Server
        new Thread(() -> {
            try {
                DatagramSocket server = new DatagramSocket(20483);
                DatagramPacket receive = new DatagramPacket(new byte[1024], 1024);
                while (true) {
                    try {
                        server.receive(receive);
                        byte[] recvByte = Arrays.copyOfRange(receive.getData(), 0,
                                receive.getLength());
                        String message = new String(recvByte);
                        if (message.equals(broadCastMessage)) {
                            InetAddress address = receive.getAddress();
                            boolean exist = false;
                            //Check socket existing
                            for (Socket s : socketMap.keySet()) {
                                if (s.getInetAddress().equals(address)) {
                                    exist = true;
                                }
                            }
                            //Open Socket
                            if (!exist) {
                                Socket socket = new Socket(address, 20483);
                                OutputStream out = socket.getOutputStream();
                                socketMap.put(socket, out);
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (SocketException ex) {
                Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        ).start();

        //Open Broadcast Sender
        new Thread(
                () -> {
                    try {
                        byte[] msg = broadCastMessage.getBytes();
                        InetAddress inetAddr = InetAddress.getByName("255.255.255.255");
                        try (DatagramSocket client = new DatagramSocket()) {
                            DatagramPacket sendPack = new DatagramPacket(msg, msg.length, inetAddr, 20483);
                            while (true) {
                                client.send(sendPack);
                                Thread.sleep(1000);
                            }
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (SocketException | UnknownHostException ex) {
                        Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        ).start();

        //Open TCP Server
        new Thread(
                () -> {
                    try {
                        ServerSocket serverSocket = new ServerSocket(20483);
                        while (true) {
                            final Socket socket = serverSocket.accept();
                            new Thread(() -> {
                                try {
                                    InputStream inputStream = socket.getInputStream();
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                                    while (true) {
                                        String line = reader.readLine();
                                        if (line == null) {
                                            break;
                                        }
                                        if (line.length() > 0) {
                                            if (line.startsWith("view:")) {
                                                String indexString = line.substring(5);
                                                int index = Integer.parseInt(indexString);
                                                indexOfView = index;
                                                viewer.setViewLine(index);
                                            } else if (line.equals("Timer:Start")) {
                                                viewer.timerStart();
                                            } else if (line.equals("Timer:Reset")) {
                                                viewer.timerReset();
                                            }
                                        }
                                    }
                                } catch (IOException ex) {
                                    Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }).start();
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PromptTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
        ).start();
    }
    private static final ConcurrentHashMap<Socket, OutputStream> socketMap = new ConcurrentHashMap<>();
    private static int indexOfView = 0;

    private static void sendCommand(String cmd) {
//        System.out.println(socketMap.size());
        for (Map.Entry<Socket, OutputStream> entry : socketMap.entrySet()) {
            OutputStream output = entry.getValue();
            try {
                output.write((cmd + System.lineSeparator()).getBytes());
            } catch (IOException ex) {
                Socket key = entry.getKey();
                socketMap.remove(key);
            }
        }
    }
}

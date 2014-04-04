package udiapplication.picoharp300application;

import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoharp300device.PicoHarp300Device;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.Mode;
import com.hwaipy.unifieddeviceinterface.timeeventdevice.picoquant.PicoQuantException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Hwaipy
 */
public class PicoHarp300DebuggerFrame extends javax.swing.JFrame {

    private final PicoHarp300Device device = new PicoHarp300Device(0);
    private final CounterParser counterParser = new CounterParser();

    /**
     * Creates new form PicoHarp300DebuggerFrame
     */
    public PicoHarp300DebuggerFrame() throws PicoQuantException {
        initComponents();
        device.open();
        device.initialize(Mode.T2);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    device.close();
                } catch (PicoQuantException ex) {
                    Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        Executors.newSingleThreadExecutor().submit(runner);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabelC1 = new javax.swing.JLabel();
        jLabelCc = new javax.swing.JLabel();
        jLabelC2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabelC1.setFont(new java.awt.Font("宋体", 0, 48)); // NOI18N
        jLabelC1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelC1.setText("0");
        jLabelC1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelCc.setFont(new java.awt.Font("宋体", 0, 48)); // NOI18N
        jLabelCc.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelCc.setText("0");
        jLabelCc.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabelC2.setFont(new java.awt.Font("宋体", 0, 48)); // NOI18N
        jLabelC2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelC2.setText("0");
        jLabelC2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabelC1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelC2, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jLabelCc, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelC1)
                    .addComponent(jLabelCc)
                    .addComponent(jLabelC2))
                .addContainerGap(304, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private final Runnable runner = new Runnable() {
        @Override
        public void run() {
            device.start(1000000);
            System.out.println("Run");
            while (true) {
//                try {
//                    ByteBuffer buffer = device.takeBuffer();
//                    counterParser.offer(buffer);
//                    if (counterParser.hasNewView()) {
//                        jLabelC1.setText("" + counterParser.getC1());
//                        jLabelC2.setText("" + counterParser.getC2());
//                        jLabelCc.setText("" + counterParser.getCc());
//                    }
//                } catch (InterruptedException | IOException ex) {
//                    Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(Level.SEVERE, null, ex);
//                    break;
//                }
            }
        }
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PicoHarp300DebuggerFrame().setVisible(true);
                } catch (PicoQuantException ex) {
                    Logger.getLogger(PicoHarp300DebuggerFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabelC1;
    private javax.swing.JLabel jLabelC2;
    private javax.swing.JLabel jLabelCc;
    // End of variables declaration//GEN-END:variables
}

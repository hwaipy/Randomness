package telescopecalibration.testp;

import com.hwaipy.science.polarizationcontrol.device.Polarization;
import com.hwaipy.science.polarizationcontrol.device.WavePlate;
import com.hwaipy.science.polarizationcontrol.m1.M1Process;
import com.hwaipy.science.polarizationcontrol.m1.M1ProcessException;
import java.awt.Color;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import telescopecalibration.TelescopeTransform;

/**
 *
 * @author Hwaipy
 */
public class TM extends javax.swing.JFrame {

  /**
   * Creates new form TM
   */
  public TM() {
    initComponents();
    updateWPS();
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    buttonGroup1 = new javax.swing.ButtonGroup();
    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jTextFieldE = new javax.swing.JTextField();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    jTextFieldA = new javax.swing.JTextField();
    jTextFieldP1 = new javax.swing.JTextField();
    jTextFieldP2 = new javax.swing.JTextField();
    jTextFieldP3 = new javax.swing.JTextField();
    jTextFieldPhase = new javax.swing.JTextField();
    jTextFieldRotate = new javax.swing.JTextField();
    jLabel6 = new javax.swing.JLabel();
    jLabel7 = new javax.swing.JLabel();
    jPanel2 = new javax.swing.JPanel();
    jTextFieldAddress1 = new javax.swing.JTextField();
    jTextFieldAddress2 = new javax.swing.JTextField();
    jButton1 = new javax.swing.JButton();
    jPanel3 = new javax.swing.JPanel();
    jTextFieldHWPEXT = new javax.swing.JTextField();
    jLabel8 = new javax.swing.JLabel();
    jPanel4 = new javax.swing.JPanel();
    jLabelWP1 = new javax.swing.JLabel();
    jLabelWP2 = new javax.swing.JLabel();
    jLabelWP3 = new javax.swing.JLabel();
    jPanel5 = new javax.swing.JPanel();
    jTextFieldZeroWP1 = new javax.swing.JTextField();
    jTextFieldZeroWP2 = new javax.swing.JTextField();
    jTextFieldZeroWP3 = new javax.swing.JTextField();

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

    jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabel1.setText("A");

    jTextFieldE.setText("0");
    jTextFieldE.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldEFocusLost(evt);
      }
    });
    jTextFieldE.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldEKeyTyped(evt);
      }
    });

    jLabel2.setText("E");

    jLabel3.setText("Phase 1");

    jLabel4.setText("Phase 2");

    jLabel5.setText("Phase 3");

    jTextFieldA.setText("32.75");
    jTextFieldA.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldAFocusLost(evt);
      }
    });
    jTextFieldA.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldAKeyTyped(evt);
      }
    });

    jTextFieldP1.setText("0.28");
    jTextFieldP1.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldP1FocusLost(evt);
      }
    });
    jTextFieldP1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldP1KeyTyped(evt);
      }
    });

    jTextFieldP2.setText("-1.33");
    jTextFieldP2.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldP2FocusLost(evt);
      }
    });
    jTextFieldP2.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldP2KeyTyped(evt);
      }
    });

    jTextFieldP3.setText("0.48");
    jTextFieldP3.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldP3FocusLost(evt);
      }
    });
    jTextFieldP3.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldP3KeyTyped(evt);
      }
    });

    jTextFieldPhase.setText("0");
    jTextFieldPhase.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldPhaseFocusLost(evt);
      }
    });
    jTextFieldPhase.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldPhaseKeyTyped(evt);
      }
    });

    jTextFieldRotate.setText("0");
    jTextFieldRotate.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldRotateFocusLost(evt);
      }
    });
    jTextFieldRotate.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldRotateKeyTyped(evt);
      }
    });

    jLabel6.setText("Rotate");

    jLabel7.setText("Phase");

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabel3)
          .addComponent(jLabel4)
          .addComponent(jLabel5)
          .addComponent(jLabel6)
          .addComponent(jLabel7)
          .addGroup(jPanel1Layout.createSequentialGroup()
            .addGap(19, 19, 19)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jLabel1)
              .addComponent(jLabel2))))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTextFieldRotate, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldP2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldP1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldP3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldA, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldE, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldPhase, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(12, Short.MAX_VALUE))
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel1Layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldA, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel1))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldE, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel2))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel3))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel4))
        .addGap(3, 3, 3)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel5))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldPhase, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel7))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(jTextFieldRotate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabel6))
        .addContainerGap())
    );

    jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTextFieldAddress1.setText("169.254.80.1");

    jTextFieldAddress2.setText("169.254.223.0");

    jButton1.setText("Connect");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
    jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jTextFieldAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTextFieldAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jButton1)
        .addContainerGap(18, Short.MAX_VALUE))
    );
    jPanel2Layout.setVerticalGroup(
      jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
            .addComponent(jTextFieldAddress1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jTextFieldAddress2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
    );

    jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jTextFieldHWPEXT.setText("0");
    jTextFieldHWPEXT.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldHWPEXTFocusLost(evt);
      }
    });
    jTextFieldHWPEXT.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldHWPEXTKeyTyped(evt);
      }
    });

    jLabel8.setText("HWP额外");

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(27, 27, 27)
            .addComponent(jTextFieldHWPEXT, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
          .addGroup(jPanel3Layout.createSequentialGroup()
            .addGap(37, 37, 37)
            .addComponent(jLabel8)))
        .addContainerGap(32, Short.MAX_VALUE))
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jLabel8)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTextFieldHWPEXT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addGap(117, 117, 117))
    );

    jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

    jLabelWP1.setText("0.00°");

    jLabelWP2.setText("0.00°");

    jLabelWP3.setText("0.00°");

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addGap(26, 26, 26)
        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jLabelWP3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelWP2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jLabelWP1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jLabelWP1)
        .addGap(18, 18, 18)
        .addComponent(jLabelWP2)
        .addGap(18, 18, 18)
        .addComponent(jLabelWP3)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("波片零位"));

    jTextFieldZeroWP1.setText("-15.1");
    jTextFieldZeroWP1.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldZeroWP1FocusLost(evt);
      }
    });
    jTextFieldZeroWP1.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldZeroWP1KeyTyped(evt);
      }
    });

    jTextFieldZeroWP2.setText("51.3");
    jTextFieldZeroWP2.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldZeroWP2FocusLost(evt);
      }
    });
    jTextFieldZeroWP2.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldZeroWP2KeyTyped(evt);
      }
    });

    jTextFieldZeroWP3.setText("-15.3");
    jTextFieldZeroWP3.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(java.awt.event.FocusEvent evt) {
        jTextFieldZeroWP3FocusLost(evt);
      }
    });
    jTextFieldZeroWP3.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyTyped(java.awt.event.KeyEvent evt) {
        jTextFieldZeroWP3KeyTyped(evt);
      }
    });

    javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
    jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addGap(17, 17, 17)
        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addComponent(jTextFieldZeroWP3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldZeroWP2, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(jTextFieldZeroWP1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    jPanel5Layout.setVerticalGroup(
      jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout.createSequentialGroup()
        .addComponent(jTextFieldZeroWP1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jTextFieldZeroWP2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(jTextFieldZeroWP3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap())
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
              .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addGap(0, 0, Short.MAX_VALUE)))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
          .addGroup(layout.createSequentialGroup()
            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
          .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
          .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        .addContainerGap(7, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    jButton1.setEnabled(false);
    jTextFieldAddress1.setEnabled(false);
    jTextFieldAddress2.setEnabled(false);
    es.submit(() -> {
      try {
        transp = new Transp(jTextFieldAddress1.getText(), jTextFieldAddress2.getText());
      } catch (IOException ex) {
      }
      SwingUtilities.invokeLater(() -> {
        if (transp == null) {
          jButton1.setText("Failed");
          jButton1.setForeground(Color.RED);
        } else {
          jButton1.setText("Connected");
          jButton1.setForeground(Color.GREEN);
        }
      });
    });
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jTextFieldAFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldAFocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldAFocusLost

  private void jTextFieldAKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldAKeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldAKeyTyped

  private void jTextFieldEFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldEFocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldEFocusLost

  private void jTextFieldEKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldEKeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldEKeyTyped

  private void jTextFieldP1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldP1FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldP1FocusLost

  private void jTextFieldP1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldP1KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldP1KeyTyped

  private void jTextFieldP2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldP2FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldP2FocusLost

  private void jTextFieldP2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldP2KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldP2KeyTyped

  private void jTextFieldP3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldP3FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldP3FocusLost

  private void jTextFieldP3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldP3KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldP3KeyTyped

  private void jTextFieldZeroWP1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP1FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldZeroWP1FocusLost

  private void jTextFieldZeroWP1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP1KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldZeroWP1KeyTyped

  private void jTextFieldZeroWP2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP2FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldZeroWP2FocusLost

  private void jTextFieldZeroWP2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP2KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldZeroWP2KeyTyped

  private void jTextFieldZeroWP3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP3FocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldZeroWP3FocusLost

  private void jTextFieldZeroWP3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldZeroWP3KeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldZeroWP3KeyTyped

  private void jTextFieldPhaseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldPhaseFocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldPhaseFocusLost

  private void jTextFieldPhaseKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldPhaseKeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldPhaseKeyTyped

  private void jTextFieldRotateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldRotateFocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldRotateFocusLost

  private void jTextFieldRotateKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldRotateKeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldRotateKeyTyped

  private void jTextFieldHWPEXTFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldHWPEXTFocusLost
    updateWPS();
  }//GEN-LAST:event_jTextFieldHWPEXTFocusLost

  private void jTextFieldHWPEXTKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldHWPEXTKeyTyped
    if (evt.getKeyChar() == '\n') {
      updateWPS();
    }
  }//GEN-LAST:event_jTextFieldHWPEXTKeyTyped

  /**
   * @param args the command line arguments
   * @throws java.lang.Exception
   */
  public static void main(String args[]) throws Exception {
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    java.awt.EventQueue.invokeLater(() -> {
      new TM().setVisible(true);
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JLabel jLabel8;
  private javax.swing.JLabel jLabelWP1;
  private javax.swing.JLabel jLabelWP2;
  private javax.swing.JLabel jLabelWP3;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JPanel jPanel5;
  private javax.swing.JTextField jTextFieldA;
  private javax.swing.JTextField jTextFieldAddress1;
  private javax.swing.JTextField jTextFieldAddress2;
  private javax.swing.JTextField jTextFieldE;
  private javax.swing.JTextField jTextFieldHWPEXT;
  private javax.swing.JTextField jTextFieldP1;
  private javax.swing.JTextField jTextFieldP2;
  private javax.swing.JTextField jTextFieldP3;
  private javax.swing.JTextField jTextFieldPhase;
  private javax.swing.JTextField jTextFieldRotate;
  private javax.swing.JTextField jTextFieldZeroWP1;
  private javax.swing.JTextField jTextFieldZeroWP2;
  private javax.swing.JTextField jTextFieldZeroWP3;
  // End of variables declaration//GEN-END:variables
  private Transp transp;
  private final ExecutorService es = Executors.newSingleThreadExecutor();

  private void updateWPS() {
    try {
      double A = Double.parseDouble(jTextFieldA.getText());
      double E = Double.parseDouble(jTextFieldE.getText());
      double pA = Double.parseDouble(jTextFieldP1.getText());
      double pB = Double.parseDouble(jTextFieldP2.getText());
      double pC = Double.parseDouble(jTextFieldP3.getText());
      double phase = Double.parseDouble(jTextFieldPhase.getText());
      double rotate = Double.parseDouble(jTextFieldRotate.getText());
      double[] wpZenos = new double[]{Double.parseDouble(jTextFieldZeroWP1.getText()),
        Double.parseDouble(jTextFieldZeroWP2.getText()), Double.parseDouble(jTextFieldZeroWP3.getText())};
      double hwpExt = Double.parseDouble(jTextFieldHWPEXT.getText());

      double rH = -(A - 32.75 + 0.00001);
      double rV = -(E + 0.00001);
      TelescopeTransform tt = TelescopeTransform.create(pA, pB, pC, (rV / 180. * Math.PI), (rH / 180. * Math.PI), phase / 180. * Math.PI, rotate / 180. * Math.PI, false);
      WavePlate qwp1 = new WavePlate(Math.PI / 2, 0);
      WavePlate qwp2 = new WavePlate(Math.PI / 2, 0);
      WavePlate hwp = new WavePlate(Math.PI, 0);
      Polarization measurementH1 = Polarization.H.transform(tt)
              .transform(qwp1).transform(qwp2).transform(hwp);
      double mHH = measurementH1.getH();
      double mHV = measurementH1.getV();
      double mHD = measurementH1.getD();
      double mHA = measurementH1.getA();
      Polarization measurementD1 = Polarization.D.transform(tt)
              .transform(qwp1).transform(qwp2).transform(hwp);
      double mDH = measurementD1.getH();
      double mDV = measurementD1.getV();
      double mDD = measurementD1.getD();
      double mDA = measurementD1.getA();
      qwp2.increase(-Math.PI / 4);
      hwp.increase(-Math.PI / 8);
      Polarization measurementH2 = Polarization.H.transform(tt)
              .transform(qwp1).transform(qwp2).transform(hwp);
      double mHL = measurementH2.getH();
      double mHR = measurementH2.getV();
      Polarization measurementD2 = Polarization.D.transform(tt)
              .transform(qwp1).transform(qwp2).transform(hwp);
      double mDL = measurementD2.getH();
      double mDR = measurementD2.getV();
      M1Process m1Process = null;
      try {
        m1Process = M1Process.calculate(new double[]{mHH, mHV, mHD, mHA, mHL, mHR, mDH, mDV, mDD, mDA, mDL, mDR});
      } catch (M1ProcessException ex) {
      }
      if (m1Process != null) {
        double[] result = m1Process.getResults();
        for (int i = 0; i < 3; i++) {
          result[i] *= (180.0 / Math.PI);
          result[i] += wpZenos[i];
        }
        result[2] += hwpExt;
        NumberFormat format = NumberFormat.getNumberInstance();
        jLabelWP1.setText(format.format(result[0]));
        jLabelWP2.setText(format.format(result[1]));
        jLabelWP3.setText(format.format(result[2]));
      }
    } catch (Exception e) {
    }
  }
}
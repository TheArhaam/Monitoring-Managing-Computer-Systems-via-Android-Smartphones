/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MyPack;

import LibPack.MyImage;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

/**
 *
 * @author shree
 */
public class MonitorWindow extends javax.swing.JFrame {

    /**
     * Creates new form MonitorWindow
     */
    Timer timer;
    TakeSnap timerTask;
    boolean flag = true;
    MyImage desktopImage, commandFromAndroid;
    int PC_Screen_X, PC_Screen_Y, Phone_Screen_X, Phone_Screen_Y;
    String myServerIdId = "";

    boolean serverLive = false;

    public MonitorWindow() {
        initComponents();
        setLocationRelativeTo(null);
        desktopImage = new MyImage();
        Dimension sd = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        //System.out.println("Width: " + sd.width + "   Height: " + sd.height);
        Phone_Screen_X = 1920;
        Phone_Screen_Y = 1080;
        PC_Screen_X = sd.width;
        PC_Screen_Y = sd.height;
    }

    void call_Check_Connection_Servlet() {
        int resp = -1;
        try {
            // System.out.println("CAlling Servlet");
            String urlstr = "http://" + txtServerIp.getText().trim() + ":8084/WebServer/CheckConnectionStatusServlet";

            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object
            out.writeObject(1);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            resp = (Integer) in.readObject();
            in.close();
            if (resp == 1) {
                String password = null;
                password = JOptionPane.showInputDialog(this, "Enter Server Password");
                if (password != null) {
                    call_Check_Password_Servlet(password);
                }
                //serverLive = true;
            } else {
                JOptionPane.showMessageDialog(this, "It Looks like server is not Active please try restarting the Server or check server IP");

            }
        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            e.printStackTrace();
        }
    }

    void call_Check_Password_Servlet(String password) {
        int resp = -1;
        try {
            // System.out.println("CAlling Servlet");
            String urlstr = "http://" + txtServerIp.getText().trim() + ":8084/WebServer/CheckPasswordServlet";

            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object
            out.writeObject(password);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            resp = (Integer) in.readObject();
            in.close();
            if (resp == 1) {
                call_PcID_Servlet();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Server Password");
            }
        } catch (Exception e) {
//            System.out.println("Error: " + e);
//            e.printStackTrace();
        }
    }

    void call_Servlet() {
        try {
            // System.out.println("CAlling Servlet");
            String urlstr = "http://" + txtServerIp.getText().trim() + ":8084/WebServer/ReadSnapServlet";

            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object
            out.writeObject(desktopImage);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            commandFromAndroid = (MyImage) in.readObject();
            in.close();

            flag = true;
            Phone_Screen_X = commandFromAndroid.mobileWidth;
            Phone_Screen_Y = commandFromAndroid.mobileHeight;

            if (commandFromAndroid.clickCmd == -1) {
                //Do nothing no command from android
            } else if (commandFromAndroid.clickCmd == 1) {
                move_Mouse(commandFromAndroid.x, commandFromAndroid.y);
            } else if (commandFromAndroid.clickCmd == 2) {
                //Left Clikc
                execute_Click_Command("1");
            } else if (commandFromAndroid.clickCmd == 3) {
                //Right Click
                execute_Click_Command("2");
            } else if (commandFromAndroid.clickCmd == 4) {
                //Double Click
                execute_Click_Command("3");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    void call_PcID_Servlet() {
        try {
            // System.out.println("CAlling Servlet");
            String urlstr = "http://" + txtServerIp.getText().trim() + ":8084/WebServer/PcActiveStatusServlet";

            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String id = txtPcID.getText().trim();
            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object
            out.writeObject(id);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            int resp = (Integer) in.readObject();
            in.close();

            //System.out.println("Command: " + resp);
            if (resp == -1) {
                JOptionPane.showMessageDialog(this, "Problem in Activating Server please restart again");
                lblActiveStatus.setText("Active Status: OFF");
                lblActiveStatus.setBackground(Color.RED);
            } else if (resp == 1) {
                lblActiveStatus.setText("Active Status: ON");
                lblActiveStatus.setBackground(Color.green);
                myServerIdId = txtPcID.getText().trim();
                timer = new Timer();
                timerTask = new TakeSnap();
                timer.schedule(timerTask, 10, 100);
            } else if (resp == 2) {
                lblActiveStatus.setText("Active Status: OFF");
                lblActiveStatus.setBackground(Color.RED);
                JOptionPane.showMessageDialog(this, "Pc ID Alfready Present");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    void call_DeletePcID_Servlet() {
        try {
            // System.out.println("CAlling Servlet");
            String urlstr = "http://" + txtServerIp.getText().trim() + ":8084/WebServer/DeactivePcIDServlet";

            URL url = new URL(urlstr);
            URLConnection connection = url.openConnection();

            connection.setDoOutput(true);
            connection.setDoInput(true);

            // don't use a cached version of URL connection
            connection.setUseCaches(false);
            connection.setDefaultUseCaches(false);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // specify the content type that binary data is sent
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            ObjectOutputStream out = new ObjectOutputStream(connection.getOutputStream());
            // send and serialize the object
            out.writeObject(myServerIdId);
            out.close();

            // define a new ObjectInputStream on the input stream
            ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
            // receive and deserialize the object, note the cast
            int resp = (Integer) in.readObject();
            in.close();

            //System.out.println("Command: " + resp);
            if (resp == -1) {
                JOptionPane.showMessageDialog(this, "Problem in De-Activating Server please deactive again");
            } else {
                lblActiveStatus.setText("Active Status: OFF");
                lblActiveStatus.setBackground(Color.RED);
                timer.cancel();
                timerTask.cancel();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }

    void execute_Click_Command(String cmd) {
        try {
            if (cmd.equals("1")) {
                System.out.println("Command 1");
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(1000);
            } else if (cmd.equals("2")) {
                System.out.println("Command 2");
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON3_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON3_MASK);
                Thread.sleep(1000);
            } else if (cmd.equals("3")) {
                System.out.println("Command 3");
                Robot robot = new Robot();
                robot.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(20);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                Thread.sleep(10);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
        }
    }

    void move_Mouse(float x, float y) {
        int Phone_Touched_X, Phone_Touched_Y;
        Phone_Touched_X = (int) x;
        Phone_Touched_Y = (int) y;
        try {
            int Position_X = ((PC_Screen_X * Phone_Touched_X) / Phone_Screen_X);
            int Position_Y = ((PC_Screen_Y * Phone_Touched_Y) / Phone_Screen_Y);
            //System.out.println("PosX: " + Position_X + "  POSy: " + Position_Y);
            Robot robot = new Robot();
            robot.mouseMove(Position_X, Position_Y);
            Thread.sleep(1000);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class TakeSnap extends TimerTask {

        @Override
        public void run() {
            try {
                // System.out.println("Flag: "+flag);
                if (flag) {
                    flag = false;
                    Robot robot = new Robot();
                    String format = "jpg";
                    String fileName = "D:\\ProjectData\\DesktopSnap\\FullScreenshot." + format;
                    desktopImage.pcID = txtPcID.getText().trim();
                    Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
                    String pp = System.getProperty("user.dir") + "\\src\\ImagePack\\mouse.png";
                    BufferedImage bmp = robot.createScreenCapture(screenRect);
                    Image cursor = ImageIO.read(new File(pp));
                    int x = MouseInfo.getPointerInfo().getLocation().x;
                    int y = MouseInfo.getPointerInfo().getLocation().y;

                    Graphics2D graphics2D = bmp.createGraphics();
                    graphics2D.drawImage(cursor, x, y, 16, 16, null); // cursor.gif is 16x16 size.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bmp, "jpg", baos);
                    desktopImage.imageArray = baos.toByteArray();
                    baos.flush();
                    baos.close();
                    // System.out.println("Byte arrya: "+desktopImage.mybyteArray.length);
                    call_Servlet();
                    //ImageIO.write(screenFullImage, format, new File(fileName));

                    //System.out.println("A full screenshot saved!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        txtPcID = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtServerIp = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblActiveStatus = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 5));

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Activate");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(51, 51, 51));
        jButton2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("De-Activate");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        txtPcID.setText("pc123");

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Your System ID");

        jButton3.setBackground(new java.awt.Color(255, 102, 102));
        jButton3.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("Exit");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 255));
        jLabel2.setText("Server IP address");

        txtServerIp.setText("192.168.1.103");

        jPanel2.setBackground(new java.awt.Color(102, 102, 102));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Control Panel");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblActiveStatus.setBackground(new java.awt.Color(255, 51, 51));
        lblActiveStatus.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        lblActiveStatus.setForeground(new java.awt.Color(255, 255, 255));
        lblActiveStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblActiveStatus.setText("Active Status: OFF");
        lblActiveStatus.setOpaque(true);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtPcID, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(txtServerIp, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblActiveStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtServerIp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPcID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblActiveStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton2)
                        .addComponent(jButton3)))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:

        try {
            call_Check_Connection_Servlet();
        } catch (Exception e) {
        }
        try {

        } catch (Exception ex) {
            System.err.println(ex);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        call_DeletePcID_Servlet();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(MonitorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MonitorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MonitorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MonitorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MonitorWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblActiveStatus;
    private javax.swing.JTextField txtPcID;
    private javax.swing.JTextField txtServerIp;
    // End of variables declaration//GEN-END:variables
}

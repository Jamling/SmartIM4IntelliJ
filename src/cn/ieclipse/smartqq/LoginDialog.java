package cn.ieclipse.smartqq;

import com.scienjus.smartqq.callback.LoginCallback;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel lTip;
    private JPanel pContent;
    private JLabel lqrcode;
    private JTextArea textArea1;

    public LoginDialog(SmartQQWindow window) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);


        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        new Thread() {
            @Override
            public void run() {
                window.getClient().login(new LoginCallback() {
                    @Override
                    public void onQrcode(final String s) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //Image image = ImageIO.read(new File(s));
                                    ImageIcon icon = new ImageIcon(s);
                                    icon.getImage().flush();
                                    lqrcode.setIcon(icon);
                                    pack();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }

                    @Override
                    public void onLogin(boolean ok, final Exception e) {
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (ok) {
                                    setVisible(false);
                                } else {
                                    textArea1.setText(e.getMessage());
                                    pack();
                                }
                            }
                        });
                    }
                });
            }
        }.start();


        String txt = "";
        if (window.getClient().isLogin()) {
            txt = "logined";
        }
        lTip.setToolTipText(txt);
    }

    private void onOK() {
// add your code here
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
//        LoginDialog dialog = new LoginDialog();
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
    }
}

package cn.ieclipse.smartim.dialogs;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.LoginCallback;
import cn.ieclipse.smartim.callback.impl.DefaultLoginCallback;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import java.awt.event.*;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPanel pContent;
    private JLabel lqrcode;
    private JTextArea errorText;
    private JBScrollPane errorPane;
    private int result;
    private SmartClient client;

    public LoginDialog(SmartClient client) {
        this.client = client;
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
            @Override public void run() {
                doLogin();
            }
        }.start();

        String txt = "";
        if (client.isLogin()) {
            txt = "已登录，点击确定重新加载联系人列表";
        } else {
            txt = "使用手机QQ扫描上面的二维码进行登录";
        }
        errorText.setText(txt);
    }

    private void onOK() {
        // add your code here
        result = 1;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    protected void doLogin() {
        if (client == null) {
            return;
        }
        client.setLoginCallback(new DefaultLoginCallback());
        client.login();

        LoginCallback loginCallback = new LoginCallback() {
            @Override public void onQrcode(final String s) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
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

            @Override public void onLogin(final boolean ok, final Exception e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override public void run() {
                        if (ok) {
                            setVisible(false);
                        } else {
                            errorText.setText(e.getMessage());
                            pack();
                        }
                    }
                });
            }
        };
    }

    public static void main(String[] args) {
        //        LoginDialog dialog = new LoginDialog();
        //        dialog.pack();
        //        dialog.setVisible(true);
        //        System.exit(0);
    }
}

package cn.ieclipse.smartim.console;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/14.
 */
public class ChatInputPane {
    private JPanel panel;
    private JButton btnSend;
    private JTextPane textPane;

    public JTextPane getTextPane() {
        return textPane;
    }

    public JButton getBtnSend() {
        return btnSend;
    }

    public JPanel getPanel() {
        return panel;
    }
}

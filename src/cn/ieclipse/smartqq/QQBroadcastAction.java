package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.actions.BroadcastAction;

import javax.swing.*;

public class QQBroadcastAction extends BroadcastAction {

    public QQBroadcastAction(SmartQQPanel panel) {
        super(panel);
    }

    @Override protected void openDialog() {
        QQBroadcastDialog dialog = new QQBroadcastDialog(imPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}

package cn.ieclipse.wechat;

import cn.ieclipse.smartim.actions.BroadcastAction;

import javax.swing.*;

public class WXBroadcastAction extends BroadcastAction {
    
    public WXBroadcastAction(WechatPanel panel) {
        super(panel);
    }
    
    @Override
    protected void openDialog() {
        WXBroadcastDialog dialog = new WXBroadcastDialog((WechatPanel) imPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
}

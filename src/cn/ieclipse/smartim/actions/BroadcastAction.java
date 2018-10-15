package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SmartIcons;

import javax.swing.*;

/**
 * Created by Jamling on 2018/02/23.
 */
public class BroadcastAction extends IMPanelAction {
    
    public BroadcastAction(IMPanel panel) {
        super(panel, "群发", "消息群发", SmartIcons.broadcast);
    }
    
    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        final SmartClient client = imPanel.getClient();
        if (client.isLogin()) {
            openDialog();
        }
        else {
            // fix #52
            JOptionPane.showMessageDialog(null, "已断开连接，请重新登录成功后再试");
        }
    }
    
    protected void openDialog() {
    
    }
    
    public static String groupMacro = "{group}";
    public static String memberMacro = "{member}";
}

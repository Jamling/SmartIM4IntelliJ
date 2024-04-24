package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/11/1.
 */
public class DisconnectAction extends IMPanelAction {
    public DisconnectAction(IMPanel panel) {
        super(panel, "退出登录", "断开连接", SmartIcons.close);
    }

    @Override public void actionPerformed(AnActionEvent anActionEvent) {
        imPanel.close();
    }
}

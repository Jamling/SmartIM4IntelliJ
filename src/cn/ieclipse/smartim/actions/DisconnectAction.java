package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/11/1.
 */
public class DisconnectAction extends AbstractAction {
    public DisconnectAction(IMPanel panel) {
        super("Close", "Close the connection", SmartIcons.close, panel);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        imPanel.close();
    }
}

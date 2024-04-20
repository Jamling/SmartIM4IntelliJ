package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.impl.DefaultLoginCallback;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.MessageDialogBuilder;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/11.
 */
public class LoginAction extends IMPanelAction {

    public LoginAction(IMPanel panel) {
        super(panel, "Login", "Click to login", SmartIcons.signin);
    }

    @Override public void actionPerformed(AnActionEvent anActionEvent) {
        SmartClient client = imPanel.getClient();
        boolean ok = true;
        if (client.isLogin()) {
            ok = MessageDialogBuilder.yesNo("", "您已处于登录状态，确定要重新登录吗？").ask(getEventProject(anActionEvent));
        }
        if (ok) {
            client.setLoginCallback(new MyLoginCallback(client, imPanel));
            new Thread(client::login).start();
        } else {
            imPanel.initContacts();
        }
    }

    public static class MyLoginCallback extends DefaultLoginCallback {
        private final SmartClient client;
        private final IMPanel panel;

        private MyLoginCallback(SmartClient client, IMPanel panel) {
            this.client = client;
            this.panel = panel;
        }

        @Override protected void onLoginFinish(boolean success, Exception e) {
            super.onLoginFinish(success, e);
            panel.initContacts();
        }
    }
}

package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.impl.DefaultLoginCallback;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.ui.MessageDialogBuilder;
import com.intellij.openapi.wm.ToolWindow;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/11.
 */
public class LoginAction extends AnAction implements DumbAware {

    private IMPanel smartPanel;
    private ToolWindow toolWindow;


    public LoginAction(IMPanel panel, ToolWindow toolWindow) {
        super("Login", "Click to login", SmartIcons.signin);
        this.smartPanel = panel;
        this.toolWindow = toolWindow;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        SmartClient client = smartPanel.getClient();
        boolean ok = true;
        if (client.isLogin()) {
            ok = MessageDialogBuilder.yesNo("", "您已处于登录状态，确定要重新登录吗？").show() == 0;
        }
        if (ok) {
            client.setLoginCallback(new MyLoginCallback(client, smartPanel));
            new Thread() {
                @Override
                public void run() {
                    client.login();
                }
            }.start();
        } else {
            smartPanel.initContacts();
        }
//
//        LoginDialog dialog = new LoginDialog(client);
//        dialog.setSize(200, 240);
//        dialog.setLocationRelativeTo(null);
//        dialog.pack();
//        dialog.setVisible(true);
//        LOG.info("login : " + client.isLogin());
//        if (client.isLogin()) {
//            new LoadThread(client).start();
//        } else {
//            //fillTestData(client);
//        }
    }

//    private void fillTestData(SmartClient client) {
//        client.categories = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            Category c = new Category();
//            c.setName("Cate " + i);
//            client.categories.add(c);
//            for (int j = 0; j < 5; j++) {
//                Friend f = new Friend();
//                f.setMarkname("friend " + j);
//                c.addFriend(f);
//            }
//        }
//        client.groups = new ArrayList<>();
//        Group g = new Group();
//        g.setName("Group 0");
//        client.groups.add(g);
//        smartPanel.updateContact();
//    }

    public static class MyLoginCallback extends DefaultLoginCallback {
        private SmartClient client;
        private IMPanel panel;

        private MyLoginCallback(SmartClient client, IMPanel panel) {
            this.client = client;
            this.panel = panel;
        }

        @Override
        protected void onLoginFinish(boolean success, Exception e) {
            super.onLoginFinish(success, e);
            panel.initContacts();
        }
    }
}

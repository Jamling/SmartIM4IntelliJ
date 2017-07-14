package cn.ieclipse.smartqq.actions;

import cn.ieclipse.smartqq.LOG;
import cn.ieclipse.smartqq.LoginDialog;
import cn.ieclipse.smartqq.SmartPanel;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.wm.ToolWindow;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import icons.SmartIcons;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by Jamling on 2017/7/11.
 */
public class LoginAction extends AnAction implements DumbAware {

    private SmartPanel smartPanel;
    private ToolWindow toolWindow;


    public LoginAction(SmartPanel panel, ToolWindow toolWindow) {
        super("Login", "Click to login", SmartIcons.signin);
        this.smartPanel = panel;
        this.toolWindow = toolWindow;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        SmartClient client = smartPanel.getClient();
        if (client.isLogin()) {
            new LoadThread(client).start();
            return;
        }
        LoginDialog dialog = new LoginDialog(client);
        dialog.setSize(200, 240);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        LOG.info("login : " + client.isLogin());
        if (client.isLogin()) {
            new LoadThread(client).start();
        } else {
            //fillTestData(client);
        }
    }

    private void fillTestData(SmartClient client) {
        client.categories = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Category c = new Category();
            c.setName("Cate " + i);
            client.categories.add(c);
            for (int j = 0; j < 5; j++) {
                Friend f = new Friend();
                f.setMarkname("friend " + j);
                c.addFriend(f);
            }
        }
        client.groups = new ArrayList<>();
        Group g = new Group();
        g.setName("Group 0");
        client.groups.add(g);
        smartPanel.updateContact();
    }

    private class LoadThread extends Thread {
        private SmartClient client;

        LoadThread(SmartClient client) {
            this.client = client;
        }

        @Override
        public void run() {
            LOG.info("load init data");
            try {
                client.reload();
            } catch (Exception e) {
                LOG.info("load init data failed", e);
            }
            LOG.info("load init data finished");
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        LOG.info("update contact UI");
                        smartPanel.updateContact();
                    } catch (Exception e) {
                        LOG.error("update contact UI failed : " + e.getMessage());
                        LOG.sendNotification("更新界面失败", e.getMessage());
                    }
                    LOG.info("start listen pull message");
                    client.start();
                }
            });
        }
    }
}

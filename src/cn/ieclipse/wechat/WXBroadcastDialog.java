package cn.ieclipse.wechat;

import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.dialogs.BroadcastDialog;
import cn.ieclipse.smartim.model.IMessage;
import cn.ieclipse.smartim.views.ContactTreeMode;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.treeStructure.Tree;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.WechatMessage;

import javax.swing.*;
import java.util.List;

public class WXBroadcastDialog extends BroadcastDialog {
    private JTree recentTree;
    private JTree friendTree;
    private JTree groupTree;
    private JTree discussTree;
    private WXContactTreeNode root1, root2, root3, root4;
    private ContactTreeMode recentModel;
    private ContactTreeMode friendModel;
    private ContactTreeMode groupModel;
    private ContactTreeMode discussModel;

    public WXBroadcastDialog(IMPanel panel) {
        super(panel);
    }

    @Override protected void initTab(JTabbedPane host) {
        // recentTree = new JTree();
        friendTree = new Tree();
        groupTree = new JTree();
        discussTree = new JTree();

        // JScrollPane scrollPane1 = new JScrollPane(recentTree);
        // tabHost.addTab("最近", null, scrollPane1, null);

        JScrollPane scrollPane2 = new JBScrollPane(friendTree);
        tabHost.addTab("好友", null, scrollPane2, null);

        JScrollPane scrollPane3 = new JBScrollPane(groupTree);
        tabHost.addTab("群组", null, scrollPane3, null);

        JScrollPane scrollPane4 = new JBScrollPane(discussTree);
        tabHost.addTab("讨论组", null, scrollPane4, null);

        initTrees(recentTree, friendTree, groupTree, discussTree);
        // root1 = new WXContactTreeNode(false, "recent", imPanel);
        root2 = new WXContactTreeNode(false, "friend", imPanel);
        root3 = new WXContactTreeNode(false, "group", imPanel);
        root4 = new WXContactTreeNode(false, "discuss", imPanel);
        root2.update();
        root3.update();
        root4.update();

        // recentModel = new ContactTreeMode(root1);
        friendModel = new ContactTreeMode(root2);
        groupModel = new ContactTreeMode(root3);
        discussModel = new ContactTreeMode(root4);

        // recentTree.setModel(recentModel);
        friendTree.setModel(friendModel);
        groupTree.setModel(groupModel);
        discussTree.setModel(discussModel);

    }

    protected void sendInternal(String text, List<Object> targets) {
        WechatClient client = (WechatClient)imPanel.getClient();
        int ret = 0;
        if (targets != null) {
            for (Object obj : targets) {
                if (obj != null && obj instanceof Contact) {
                    Contact target = (Contact)obj;
                    try {
                        IMessage m = createMessage(text, target, client);
                        client.sendMessage(m, target);
                        ret++;
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    protected IMessage createMessage(String text, Contact target, WechatClient client) {
        IMessage m = null;
        if (!client.isClose()) {
            String msg = target.isGroup() ? text.replace(BroadcastAction.groupMacro, target.getName()) : text;
            m = client.createMessage(WechatMessage.MSGTYPE_TEXT, msg, target);
        }
        return m;
    }
}

package cn.ieclipse.wechat;

import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.handler.MessageInterceptor;
import cn.ieclipse.smartim.views.ContactTreeMode;
import cn.ieclipse.smartim.views.IMContactView;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.ui.treeStructure.Tree;
import io.github.biezhi.wechat.api.WechatClient;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/11.
 */
public class WXContactView extends IMContactView {
    private JPanel panel;
    private JBTabbedPane tabHost;

    private Tree recentTree;
    private Tree friendTree;
    private Tree groupTree;
    private Tree discussTree;

    private WXContactTreeNode root1, root2, root3, root4;

    private ContactTreeMode recentModel;
    private ContactTreeMode friendModel;
    private ContactTreeMode groupModel;
    private ContactTreeMode discussModel;

    private MessageInterceptor interceptor;
    public WXContactView(WechatPanel imPanel) {
        super(imPanel);

        receiveCallback = new WXReceiveCallback(imPanel);
        sendCallback = new IMSendCallback(imPanel);
        robotCallback = new WXRobotCallback(imPanel);
        modificationCallback = new WXModificationCallback(imPanel);
        interceptor = new WXMessageInterceptor();

        root1 = new WXContactTreeNode(false, "recent", imPanel);
        root2 = new WXContactTreeNode(false, "friend", imPanel);
        root3 = new WXContactTreeNode(false, "group", imPanel);
        root4 = new WXContactTreeNode(false, "public", imPanel);

        tabHost.removeTabAt(2);
        initTrees(recentTree, friendTree, groupTree, discussTree);
        init();
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public WechatPanel getImPanel() {
        return (WechatPanel) super.getImPanel();
    }

    public void init() {
        recentModel = new ContactTreeMode(root1);
        friendModel = new ContactTreeMode(root2);
        //groupModel = new ContactTreeMode(root3);
        discussModel = new ContactTreeMode(root4);

        recentTree.setModel(recentModel);
        friendTree.setModel(friendModel);
        //groupTree.setModel(groupModel);
        discussTree.setModel(discussModel);
    }

    @Override
    protected void initTree(Tree tree) {
        super.initTree(tree);
    }

    @Override
    protected WechatClient getClient() {
        return getImPanel().getClient();
    }

    @Override
    protected void doLoadContacts() {
        WechatClient client = getClient();
        if (client.isLogin()) {
            try {
                client.init();
                notifyLoadContacts(true);

                client.setReceiveCallback(receiveCallback);
                client.addReceiveCallback(robotCallback);
                client.setSendCallback(sendCallback);
                client.setModificationCallbacdk(modificationCallback);
                client.addMessageInterceptor(interceptor);
                client.start();
            } catch (Exception e) {
                LOG.error("微信初始化失败", e);
            }
        } else {
            notifyLoadContacts(false);
        }
    }

    @Override
    protected void onLoadContacts(boolean success) {
        root1.update();
        root2.update();
        //root3.update();
        root4.update();
        if (success) {
            init();
        }
        updateTrees(recentTree, friendTree, groupTree, discussTree);
    }

    @Override
    protected void doUpdateContacts(int index) {
        if (index == 0) {
            root1.update();
            updateTrees(recentTree);
        }
    }
}

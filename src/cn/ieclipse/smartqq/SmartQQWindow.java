package cn.ieclipse.smartqq;

import com.alibaba.fastjson.JSONObject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.scienjus.smartqq.callback.MessageCallback2;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.*;
import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by Jamling on 2017/6/30.
 */
public class SmartQQWindow implements ToolWindowFactory {
    private JPanel mPanel;
    private JTextField tfContactSearch;
    private JTabbedPane tabbedContact;
    private JButton tbLogin;
    private JButton tbSetting;
    private JPanel mContactPanel;
    private JTree tFriend;
    private JList tRecent;
    private JTabbedPane tabbedChat;
    private JButton tbTest;
    private JScrollPane pGroup;
    private JScrollPane pDiscuss;
    private JList tGroup;
    private JList tDiscuss;
    private JScrollPane pRecent;
    private JScrollPane pFriend;
    private JButton tbClose;
    private JButton tbShow;
    private JSplitPane mSplitPanel;

    //
    private SmartQQWindow window;
    private SmartClient client;
    public static Logger LOG = Logger.getLogger("SmartQQ");

    public SmartClient getClient() {
        if (client == null || client.isClose()) {
            client = new SmartClient();
        }

        Project p = ProjectManager.getInstance().getDefaultProject();
        Project[] ps = ProjectManager.getInstance().getOpenProjects();
        if (ps != null) {
            for (Project t : ps) {
                if (!t.isDefault()) {
                    p = t;
                }
            }
        }

        File dir = new File(p.getBasePath(), Project.DIRECTORY_STORE_FOLDER);
        client.setQrPath(new File(dir, "qrcode.png").getAbsolutePath());
        return client;
    }

    public SmartQQWindow() {
        tbLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JSONObject json = new JSONObject();
                json.put("code", 0);
                System.out.println(json.toJSONString());

                LoginDialog dialog = new LoginDialog(SmartQQWindow.this);
                dialog.setSize(200, 240);
                dialog.setLocationRelativeTo(null);
                dialog.pack();
                dialog.setVisible(true);
                LOG.info("login : " + getClient().isLogin());
                if (getClient().isLogin()) {
                    new Thread() {
                        @Override
                        public void run() {
                            LOG.info("load init data");
                            try {
                                getClient().reload();
                            } catch (Exception e) {
                                LOG.info("load init data failed", e);
                            }
                            LOG.info("load init data finished");
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        LOG.info("update contact UI");
                                        updateContact();
                                    } catch (Exception e) {
                                        LOG.error("update contact UI failed : " + e.getMessage());
                                    }

                                    getClient().setCallback(callback);
                                    LOG.info("start listen pull message");
                                    getClient().start();
                                }
                            });

                        }
                    }.start();
                }
            }
        });
        tbTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Friend f = new Friend();
                f.setMarkname("f " + System.currentTimeMillis());
                openChat(f);
            }
        });
        tbClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getClient().close();
                int count = tabbedChat.getTabCount();
                for (int i = 0; i < count; i++) {
                    tabbedChat.remove(0);
                }
            }
        });
        tbShow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean v = mContactPanel.isVisible();
                if (v) {
                    mSplitDividerLocation = mSplitPanel.getDividerLocation();
                } else {
                    mSplitPanel.setDividerLocation(mSplitDividerLocation);
                }
                mContactPanel.setVisible(!v);
            }
        });
    }

    private Dimension mContactPanelSize;
    private int mSplitDividerLocation = 0;

    private void updateContact() {
        //tFriend.setShowsRootHandles(true);
        tFriend.setRootVisible(false);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("root");

        List<Category> categories = getClient().getFriendListWithCategory();
        if (categories != null) {
            for (Category c : categories) {
                DefaultMutableTreeNode cn = new DefaultMutableTreeNode(c);
                root.add(cn);
                for (Friend f : c.getFriends()) {
                    DefaultMutableTreeNode fn = new DefaultMutableTreeNode(f);
                    cn.add(fn);
                }
            }
        }

        tFriend.setModel(new DefaultTreeModel(root));
        tFriend.expandRow(0);

        if (tRecent.getCellRenderer() instanceof FriendListCellRenderer) {
            ((FriendListCellRenderer) tRecent.getCellRenderer()).setClient(getClient());
        }
        tRecent.setListData(getClient().getRecentList().toArray());

        tGroup.setListData(getClient().getGroupList().toArray());

        tDiscuss.setListData(getClient().getDiscussList().toArray());
    }

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(mPanel, "Control", false);
        toolWindow.getContentManager().addContent(content);

        tFriend.addMouseListener(treeClick);
        tFriend.setCellRenderer(new FriendTreeCellRenderer());

        tRecent.addMouseListener(listClick);
        tRecent.setCellRenderer(new FriendListCellRenderer());

        tGroup.addMouseListener(listClick);
        tGroup.setCellRenderer(new FriendListCellRenderer());

        tDiscuss.addMouseListener(listClick);
        tDiscuss.setCellRenderer(new FriendListCellRenderer());

        tFriend.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("请先登录")));
    }

    private TreeSelectionListener treeSelectionListener = new TreeSelectionListener() {
        @Override
        public void valueChanged(TreeSelectionEvent e) {

        }
    };

    private MouseAdapter treeClick = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JTree tree = (JTree) e.getSource();
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selRow != -1 && e.getClickCount() == 2 && selPath != null) {
                Object selectedNode = selPath.getLastPathComponent();
                System.out.println(selectedNode);
                openChat(((DefaultMutableTreeNode) selectedNode).getUserObject());
            }
        }
    };

    private MouseAdapter listClick = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if (e.getClickCount() == 2) {
                JList tree = (JList) e.getSource();
                Object selectedNode = tree.getSelectedValue();
                if (selectedNode instanceof Recent) {
                    Recent r = (Recent) selectedNode;
                    if (r.getType() == 0) {
                        Friend f = client.getFriend(r.getUin());
                        openChat(f);
                    } else if (r.getType() == 1) {
                        Group g = client.getGroup(r.getUin());
                        openChat(g);
                    } else if (r.getType() == 2) {
                        Discuss d = client.getDiscuss(r.getUin());
                        openChat(d);
                    }
                    return;
                }
                openChat(selectedNode);
            }
        }
    };

    MessageCallback2 callback = new MessageCallback2() {

        @Override
        public void onReceiveMessage(DefaultMessage message, MessageFrom from) {
            Object target = null;
            if (message instanceof Message) {
                target = getClient().getFriend(message.getUserId());
            } else if (message instanceof GroupMessage) {
                target = getClient().getGroup(((GroupMessage) message).getGroupId());
            } else if (message instanceof DiscussMessage) {
                target = getClient().getDiscuss(((DiscussMessage) message).getDiscussId());
            }

            ChatConsole console =
                    findConsole(SmartClient.getName(target), false);
            Recent r = getClient().getRecent(0, message.getUserId());
            if (console != null) {
                String time = new SimpleDateFormat("HH:mm:ss")
                        .format(message.getTime());
                console.write(String.format("%s %s: %s", time,
                        from.getName(), message.getContent()));
            }
        }

        @Override
        public void onReceiveError(Throwable throwable) {
            LOG.error("receive message failed : " + throwable);
        }
    };

    private void openChat(Object obj) {
        String name = SmartClient.getName(obj);
        int count = tabbedChat.getTabCount();
        for (int i = 0; i < count; i++) {
            if (tabbedChat.getTitleAt(i).equals(name)) {
                tabbedChat.setSelectedIndex(i);
                return;
            }
        }
        ChatConsole console = new ChatConsole(getClient(), obj);
        tabbedChat.addTab(name, console.getPanel());
        console.initUI(tabbedChat);
        consoles.put(name, console);
        tabbedChat.setSelectedIndex(count);
    }

    private Map<String, ChatConsole> consoles = new HashMap<>();

    public ChatConsole findConsole(String name, boolean add) {
        return consoles.get(name);
    }

    private void createUIComponents() {
        tabbedChat = new ClosableTabHost();
    }
}

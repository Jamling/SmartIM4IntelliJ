package cn.ieclipse.smartqq;

import cn.ieclipse.smartqq.actions.HideContactAction;
import cn.ieclipse.smartqq.actions.LoginAction;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBTabbedPane;
import com.scienjus.smartqq.callback.MessageCallback2;
import com.scienjus.smartqq.callback.SendMessageCallback;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.*;
import icons.SmartIcons;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SmartPanel extends SimpleToolWindowPanel {

    ToolWindow toolWindow;
    JBTabbedPane tabbedChat;
    ContactPanel left;
    JBSplitter splitter;

    Project project;
    SmartClient client;

    public static SmartPanel getInstance(@NotNull Project project) {
        return project.getComponent(SmartPanel.class);
    }

    public SmartPanel(boolean vertical) {
        super(vertical);
        initUI();
    }

    public SmartPanel(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        initUI();
    }

    public SmartPanel(Project project, ToolWindow toolWindow) {
        super(false, true);
        this.project = project;
        this.toolWindow = toolWindow;
        initUI();
    }

    private void initUI() {
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new LoginAction(this, toolWindow));
        group.add(new HideContactAction(this));
        //group.add(new TestAction(this));
        AnAction action = new DumbAwareAction("Close", "Close the connection", SmartIcons.close) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                close();
            }
        };
        group.add(action);

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("SmartQQ", group, false);
        // toolbar.getComponent().addFocusListener(createFocusListener());
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());

        left = new ContactPanel();
        left.setCallback(new ContactPanel.Callback() {
            @Override
            public void open(Object o) {
                openChat(o);
            }
        });
        left.init();

        tabbedChat = new ClosableTabHost();

        splitter = new JBSplitter(false);
        splitter.setSplitterProportionKey("main.splitter.key");
        splitter.setFirstComponent(left.getPanel());
        splitter.setSecondComponent(tabbedChat);
        splitter.setProportion(0.3f);
        setContent(splitter);
    }


    public boolean isLeftHidden() {
        boolean v = left.getPanel().isVisible();
        return !v;
    }

    public void setLeftHide(boolean select) {
        left.getPanel().setVisible(!select);
    }

    public SmartClient getClient() {
        if (client == null || client.isClose()) {
            client = new SmartClient();
            client.setQrPath(getQrpath());
            client.setCallback(callback);
            client.setSendMessageCallback(sendMessageCallback);
        }
        return client;
    }

    public void updateContact() {
        left.update(client);
    }

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

            String name = SmartClient.getName(target);
            ChatConsole console = findConsole(name, false);
            Recent r = getClient().getRecent(0, message.getUserId());
            if (console != null) {
                int i = tabbedChat.indexOfComponent(console);
                if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
                    ((ClosableTabHost) tabbedChat).bling(i, name);
                }
                String time = new SimpleDateFormat("HH:mm:ss")
                        .format(message.getTime());
                console.write(String.format("%s %s: %s", time,
                        from.getName(), message.getContent()));
            }
        }

        @Override
        public void onReceiveError(Throwable throwable) {
            if (throwable == null) {
                // return;
            }
            LOG.error("receive message failed : " + throwable);
            LOG.sendNotification("错误", throwable.getMessage());
        }
    };

    private SendMessageCallback sendMessageCallback = new SendMessageCallback() {
        @Override
        public void onSendResult(int i, long l, String s, boolean b, Throwable throwable) {
            if (!b) {
                LOG.error("send message failed : " + throwable);
                LOG.sendNotification("发送失败", String.format("内容：%s(%s)", s, throwable.getMessage()));
            }
        }
    };

    private Map<String, ChatConsole> consoles = new HashMap<>();

    public ChatConsole findConsole(String name, boolean add) {
        return consoles.get(name);
    }

    private void randBling(){
        int size = tabbedChat.getComponentCount();
        int i = new Random().nextInt(size);
        String name = "Random";
        if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
            ((ClosableTabHost) tabbedChat).bling(i, name);
        }
    }

    public void openChat(Object obj) {
        System.out.println("open chat for " + obj);
        if (obj instanceof Category) {
            return;
        }
        if (client.isClose()) {
            LOG.sendNotification("错误", "连接已断开，请重新登录");
            return;
        }
        String name = SmartClient.getName(obj);
        int count = tabbedChat.getTabCount();
        for (int i = 0; i < count; i++) {
            if (tabbedChat.getTitleAt(i).equals(name)) {
                tabbedChat.setSelectedIndex(i);
                return;
            }
        }
        ChatConsole console = new ChatConsole(client, obj);
        tabbedChat.addTab(name, console);
        consoles.put(name, console);
        tabbedChat.setSelectedIndex(count);
    }

    public String getQrpath() {
        Project p = this.project;
        if (p == null) {
            p = ProjectManager.getInstance().getDefaultProject();
            Project[] ps = ProjectManager.getInstance().getOpenProjects();
            if (ps != null) {
                for (Project t : ps) {
                    if (!t.isDefault()) {
                        p = t;
                    }
                }
            }
        }
        File dir = new File(p.getBasePath(), Project.DIRECTORY_STORE_FOLDER);
        return new File(dir, "qrcode.png").getAbsolutePath();
    }

    public void close() {
        if (client != null) {
            client.close();
        }
        closeAllChat();
    }

    public void closeAllChat() {
        int count = tabbedChat.getTabCount();
        for (int i = 0; i < count; i++) {
            tabbedChat.remove(0);
        }
        consoles.clear();
    }
}

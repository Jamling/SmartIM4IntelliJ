package cn.ieclipse.smartim.views;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.AbstractAction;
import cn.ieclipse.smartim.actions.DisconnectAction;
import cn.ieclipse.smartim.actions.HideContactAction;
import cn.ieclipse.smartim.actions.LoginAction;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.console.ClosableTabHost;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.settings.SmartSettingsPanel;
import cn.ieclipse.smartqq.QQChatConsole;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBTabbedPane;
import icons.SmartIcons;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jamling on 2017/7/11.
 */
public abstract class IMPanel extends SimpleToolWindowPanel {

    protected ToolWindow toolWindow;
    protected JBTabbedPane tabbedChat;
    protected IMContactView left;
    protected JBSplitter splitter;

    protected Project project;

    public IMPanel(boolean vertical) {
        super(vertical);
        initUI();
    }

    public IMPanel(boolean vertical, boolean borderless) {
        super(vertical, borderless);
        initUI();
    }

    public IMPanel(Project project, ToolWindow toolWindow) {
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
        AnAction action = new DisconnectAction(this);
        group.add(action);

        group.add(new AbstractAction("Settings", "Settgins", AllIcons.General.Settings, this){
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                ShowSettingsUtil.getInstance().editConfigurable(project, new SmartSettingsPanel());
            }
        });

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("SmartQQ", group, false);
        // toolbar.getComponent().addFocusListener(createFocusListener());
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());

        left = createContactsUI();
        left.onLoadContacts(false);

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

    public abstract SmartClient getClient();

    public abstract IMContactView createContactsUI();

    public abstract IMChatConsole createConsoleUI(IContact contact);

    private Map<String, IMChatConsole> consoles = new HashMap<>();

    public IMChatConsole findConsole(IContact contact, boolean add) {
        return consoles.get(contact.getUin());
    }

    public void randBling() {
        int size = tabbedChat.getComponentCount();
        int i = new Random().nextInt(size);
        String name = "Random";
        if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
            ((ClosableTabHost) tabbedChat).bling(i, name);
        }
    }

    public void highlight(IMChatConsole console) {
        int i = tabbedChat.indexOfComponent(console);
        if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
            ((ClosableTabHost) tabbedChat).bling(i, console.getName());
        }
    }

    public IMChatConsole findConsoleById(String id, boolean show) {
        int count = tabbedChat.getTabCount();
        for (int i = 0; i < count; i++) {
            if (tabbedChat.getComponentAt(i) instanceof IMChatConsole) {
                IMChatConsole t = (IMChatConsole) tabbedChat.getComponentAt(i);
                if (id.equals(t.getUin())) {
                    if (show) {
                        tabbedChat.setSelectedIndex(i);
                    }
                    return t;
                }
            }
        }
        return null;
    }

    public void onDoubleClick(Object obj) {
        SmartClient client = getClient();
        if (client.isClose()) {
            LOG.sendNotification("错误", "连接已断开，请重新登录");
            return;
        }
        if (obj instanceof IContact) {
            openConsole((IContact) obj);
        }
    }


    public void openConsole(IContact contact) {
        IMChatConsole console = findConsoleById(contact.getUin(), true);
        if (console == null) {
            console = createConsoleUI(contact);
            console.setName(contact.getName());
            tabbedChat.addTab(contact.getName(), console);
            consoles.put(console.getUin(), console);
            tabbedChat.setSelectedComponent(console);
        }
    }

    public void close() {
        getClient().close();
        closeAllChat();
        left.onLoadContacts(false);
    }

    public void closeAllChat() {
        int count = tabbedChat.getTabCount();
        for (int i = 0; i < count; i++) {
            tabbedChat.remove(0);
        }
        consoles.clear();
    }

    public void initContacts() {
        left.initContacts();
    }

    public void notifyUpdateContacts(int index, boolean force) {
        left.notifyUpdateContacts(index, force);
    }
}

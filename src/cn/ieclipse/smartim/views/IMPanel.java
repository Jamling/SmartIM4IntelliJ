package cn.ieclipse.smartim.views;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.*;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.console.ClosableTabHost;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.components.JBTabbedPane;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jamling on 2017/7/11.
 */
public abstract class IMPanel extends SimpleToolWindowPanel implements ClosableTabHost.Callback {

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

    public Project getProject() {
        return project;
    }

    private void initUI() {
        DefaultActionGroup group = new DefaultActionGroup();
        group.add(new LoginAction(this));
        group.add(new HideContactAction(this));
        group.add(new DisconnectAction(this));
        createBroadcastAction(group);
        group.add(new SettingAction(this));
        //group.add(new MockConsoleAction(this));

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("SmartQQ", group, false);
        // toolbar.getComponent().addFocusListener(createFocusListener());
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());

        left = createContactsUI();
        left.onLoadContacts(false);

        tabbedChat = new ClosableTabHost(this);

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

    public abstract BroadcastAction createBroadcastAction(DefaultActionGroup group);

    private Map<String, IMChatConsole> consoles = new HashMap<>();

    public Map<String, IMChatConsole> getConsoles() {
        return consoles;
    }

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

    public boolean isCurrent(final IMChatConsole console) {
        if (console != null) {
            if (console == tabbedChat.getSelectedComponent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isCurrent(final IContact contact) {
        if (contact != null) {
            Object o = tabbedChat.getSelectedComponent();
            IMChatConsole console = (IMChatConsole) o;
            if (o != null && o instanceof IMChatConsole) {
                return contact.getUin().equals(console.getUin());
            }
        }
        return false;
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
        if (client.isClose() || !client.isLogin()) {
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
        console.clearUnread();
    }

    public void addConsole(IMChatConsole console) {
        tabbedChat.addTab(console.getName(), console);
        consoles.put(console.getUin(), console);
        tabbedChat.setSelectedComponent(console);
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

    @Override
    public void removeTabAt(int index) {
        IMChatConsole console = null;
        Component comp = tabbedChat.getComponentAt(index);
        if (comp instanceof IMChatConsole) {
            console = (IMChatConsole) comp;
            consoles.remove(console.getUin());
        }
        tabbedChat.removeTabAt(index);
    }

    public java.util.List<IMChatConsole> getConsoleList() {
        java.util.List<IMChatConsole> list = new ArrayList<>();
        if (tabbedChat != null) {
            int count = tabbedChat.getTabCount();
            for (int i = 0; i < count; i++) {
                if (tabbedChat.getComponentAt(i) instanceof IMChatConsole) {
                    IMChatConsole t = (IMChatConsole) tabbedChat.getComponentAt(i);
                    list.add(t);
                }
            }
        }
        return list;
    }

    public void initContacts() {
        left.initContacts();
    }

    public void notifyUpdateContacts(int index, boolean force) {
        left.notifyUpdateContacts(index, force);
    }
}

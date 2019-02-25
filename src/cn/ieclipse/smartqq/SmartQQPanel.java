package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.scienjus.smartqq.client.SmartQQClient;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SmartQQPanel extends IMPanel {


    public SmartQQPanel(Project project, ToolWindow toolWindow) {
        super(project, toolWindow);
        loadWelcome("qq");
    }

    @Override
    public SmartQQClient getClient() {
        return IMClientFactory.getInstance().getQQClient();
    }

    @Override
    public IMContactView createContactsUI() {
        return new QQContactView(this);
    }

    @Override
    public IMChatConsole createConsoleUI(IContact contact) {
        return new QQChatConsole(contact, this);
    }

    @Override
    public BroadcastAction createBroadcastAction(DefaultActionGroup group) {
        group.add(new QQBroadcastAction(this));
        return null;
    }


}

package cn.ieclipse.wechat;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import io.github.biezhi.wechat.api.WechatClient;

/**
 * Created by Jamling on 2017/11/1.
 */
public class WechatPanel extends IMPanel {
    public WechatPanel(Project project, ToolWindow toolWindow) {
        super(project, toolWindow);
        loadWelcome("wechat");
    }

    @Override public WechatClient getClient() {
        return IMClientFactory.getInstance().getWechatClient();
    }

    @Override public IMContactView createContactsUI() {
        return new WXContactView(this);
    }

    @Override public IMChatConsole createConsoleUI(IContact contact) {
        IMChatConsole console = new WXChatConsole(contact, this);
        console.setName(WXUtils.getPureName(contact.getName()));
        return console;
    }

    @Override public BroadcastAction createBroadcastAction(DefaultActionGroup group) {
        group.add(new WXBroadcastAction(this));
        return null;
    }

}

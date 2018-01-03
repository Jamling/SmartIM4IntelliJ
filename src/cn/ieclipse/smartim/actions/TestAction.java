package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.MockChatConsole;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.smartqq.SmartQQPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.scienjus.smartqq.model.Friend;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/12.
 */
public class TestAction extends DumbAwareAction {
    IMPanel panel;

    public TestAction(IMPanel panel) {
        super("Test", "Test", SmartIcons.test);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        AbstractContact contact = new AbstractContact() {
            @Override
            public String getName() {
                return "Test";
            }

            @Override
            public String getUin() {
                return "test";
            }
        };

        MockChatConsole console = new MockChatConsole(contact, panel);
        console.setName(contact.getName());
        panel.addConsole(console);
    }
}

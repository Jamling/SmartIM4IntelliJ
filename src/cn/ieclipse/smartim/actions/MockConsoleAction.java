package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.MockChatConsole;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/12.
 */
public class MockConsoleAction extends IMPanelAction {

    public MockConsoleAction(IMPanel panel) {
        super(panel, "Test", "Test", SmartIcons.test);
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

        MockChatConsole console = new MockChatConsole(contact, imPanel);
        console.setName(contact.getName());
        imPanel.addConsole(console);
    }
}

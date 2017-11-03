package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartqq.SmartQQPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.scienjus.smartqq.model.Friend;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/12.
 */
public class TestAction extends DumbAwareAction {
    SmartQQPanel panel;

    public TestAction(SmartQQPanel panel) {
        super("Test", "Test", SmartIcons.test);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        Friend f = new Friend();
        f.setMarkname("Test " + System.currentTimeMillis());
        panel.onDoubleClick(f);
    }
}

package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.ToggleActionButton;
import icons.SmartIcons;

public class ScrollLockAction extends ToggleActionButton {
    IMChatConsole console;

    public ScrollLockAction(IMChatConsole console) {
        super("禁止滚动", SmartIcons.lock);
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return !console.isScrollLock();
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        console.setScrollLock(!b);
    }
}

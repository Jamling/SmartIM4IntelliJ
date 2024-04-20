package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.ToggleActionButton;
import icons.SmartIcons;
import org.jetbrains.annotations.NotNull;

public class ScrollLockAction extends ToggleActionButton {
    IMChatConsole console;

    public ScrollLockAction(IMChatConsole console) {
        super("禁止滚动", SmartIcons.lock);
        this.console = console;
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }

    @Override public boolean isSelected(AnActionEvent anActionEvent) {
        return !console.isScrollLock();
    }

    @Override public void setSelected(AnActionEvent anActionEvent, boolean b) {
        console.setScrollLock(!b);
    }
}

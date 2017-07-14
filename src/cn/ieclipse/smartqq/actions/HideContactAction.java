package cn.ieclipse.smartqq.actions;

import cn.ieclipse.smartqq.SmartPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/12.
 */
public class HideContactAction extends ToggleAction {
    SmartPanel panel;

    public HideContactAction(SmartPanel panel) {
        super("Hide Conatct", "Hide Contact Tree", SmartIcons.hide);
        this.panel = panel;
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return panel.isLeftHidden();
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        panel.setLeftHide(b);
    }
}

package cn.ieclipse.smartim.actions;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SettingAction extends DumbAwareAction {

    public SettingAction() {
        super("Settings", "Preference & Settings", AllIcons.General.Settings);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

    }
}

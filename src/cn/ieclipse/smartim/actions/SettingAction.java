package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.settings.SmartSettingsPanel;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.options.ShowSettingsUtil;
import com.intellij.openapi.project.DumbAwareAction;
import icons.SmartIcons;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SettingAction extends DumbAwareAction {

    IMPanel imPanel;

    public SettingAction(IMPanel panel) {
        super("设置", "首选项及设置", SmartIcons.settings);
        this.imPanel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        ShowSettingsUtil.getInstance().editConfigurable(imPanel.getProject(), new SmartSettingsPanel());
    }
}

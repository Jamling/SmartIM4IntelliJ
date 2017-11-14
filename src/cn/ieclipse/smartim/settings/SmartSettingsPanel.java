package cn.ieclipse.smartim.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SmartSettingsPanel implements Configurable {
    private TextFieldWithBrowseButton send;
    private JCheckBox chkNotify;
    private JCheckBox chkUnread;
    private JCheckBox chkSendBtn;
    private JPanel panel;
    private JCheckBox chkGroupMsg;
    private SmartIMSettings settings;

    public SmartSettingsPanel() {
        settings = SmartIMSettings.getInstance();
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "SmartIM";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return chkNotify.isSelected() != settings.getState().NOTIFY_MSG
                || chkUnread.isSelected() != settings.getState().NOTIFY_UNREAD
                || chkSendBtn.isSelected() != settings.getState().SHOW_SEND
                || chkGroupMsg.isSelected() != settings.getState().NOTIFY_GROUP_MSG;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.getState().NOTIFY_MSG = chkNotify.isSelected();
        settings.getState().NOTIFY_UNREAD = chkUnread.isSelected();
        settings.getState().SHOW_SEND = chkSendBtn.isSelected();
        settings.getState().NOTIFY_GROUP_MSG = chkGroupMsg.isSelected();
        settings.loadState(settings.getState());
    }

    @Override
    public void reset() {
        chkNotify.setSelected(settings.getState().NOTIFY_MSG);
        chkGroupMsg.setSelected(settings.getState().NOTIFY_GROUP_MSG);
        chkSendBtn.setSelected(settings.getState().SHOW_SEND);
        chkUnread.setSelected(settings.getState().NOTIFY_UNREAD);
    }

    @Override
    public void disposeUIResources() {

    }
}

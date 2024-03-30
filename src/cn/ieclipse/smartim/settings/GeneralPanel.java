package cn.ieclipse.smartim.settings;

import cn.ieclipse.common.BareBonesBrowserLaunch;
import cn.ieclipse.smartim.common.RestUtils;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Jamling on 2017/7/11.
 */
public class GeneralPanel implements Configurable {
    private JComboBox comboSend;
    private JCheckBox chkNotify;
    private JCheckBox chkNotifyUnread;
    private JCheckBox chkSendBtn;
    private JPanel panel;
    private JCheckBox chkNotifyGroupMsg;
    private JCheckBox chkNotifyUnknown;
    private JCheckBox chkHideMyInput;
    private JLabel linkUpdate;
    private JLabel linkAbout;
    private JCheckBox chkHistory;
    private SmartIMSettings settings;

    public GeneralPanel(SmartIMSettings settings) {
        this.settings = settings;
        linkUpdate.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                RestUtils.checkUpdate();
            }
        });
        linkAbout.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                BareBonesBrowserLaunch.openURL(RestUtils.ABOUT_URL);
            }
        });
    }

    @Nls @Override public String getDisplayName() {
        return "SmartIM";
    }

    @Nullable @Override public String getHelpTopic() {
        return null;
    }

    @Nullable @Override public JComponent createComponent() {
        return panel;
    }

    @Override public boolean isModified() {
        return chkNotify.isSelected() != settings.getState().NOTIFY_MSG || chkNotifyUnread.isSelected() != settings
            .getState().NOTIFY_UNREAD || chkSendBtn.isSelected() != settings.getState().SHOW_SEND
            || chkNotifyGroupMsg.isSelected() != settings.getState().NOTIFY_GROUP_MSG
            || chkNotifyUnknown.isSelected() != settings.getState().NOTIFY_UNKNOWN
            || chkHideMyInput.isSelected() != settings.getState().HIDE_MY_INPUT || chkHistory.isSelected() != settings
            .getState().LOG_HISTORY || (!settings.getState().KEY_SEND.equals(comboSend.getSelectedItem().toString()));
    }

    @Override public void apply() throws ConfigurationException {
        settings.getState().NOTIFY_MSG = chkNotify.isSelected();
        settings.getState().NOTIFY_UNREAD = chkNotifyUnread.isSelected();
        settings.getState().SHOW_SEND = chkSendBtn.isSelected();
        settings.getState().NOTIFY_GROUP_MSG = chkNotifyGroupMsg.isSelected();
        settings.getState().NOTIFY_UNKNOWN = chkNotifyUnknown.isSelected();
        settings.getState().HIDE_MY_INPUT = chkHideMyInput.isSelected();
        settings.getState().LOG_HISTORY = chkHistory.isSelected();
        settings.getState().KEY_SEND = comboSend.getSelectedItem().toString();
    }

    @Override public void reset() {
        chkNotify.setSelected(settings.getState().NOTIFY_MSG);
        chkNotifyGroupMsg.setSelected(settings.getState().NOTIFY_GROUP_MSG);
        chkSendBtn.setSelected(settings.getState().SHOW_SEND);
        chkNotifyUnread.setSelected(settings.getState().NOTIFY_UNREAD);
        chkNotifyUnknown.setSelected(settings.getState().NOTIFY_UNKNOWN);
        chkHideMyInput.setSelected(settings.getState().HIDE_MY_INPUT);
        chkHistory.setSelected(settings.getState().LOG_HISTORY);
        comboSend.setSelectedItem(settings.getState().KEY_SEND);
    }

    @Override public void disposeUIResources() {

    }

    private void checkUpdate() {
        RestUtils.checkUpdate();
    }
}

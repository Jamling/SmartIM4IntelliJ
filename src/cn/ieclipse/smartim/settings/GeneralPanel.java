package cn.ieclipse.smartim.settings;

import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.common.RestUtils;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.ActionLink;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.util.Objects;

/**
 * Created by Jamling on 2017/7/11.
 */
public class GeneralPanel implements Configurable {
    private JComboBox<String> comboSend;
    private JCheckBox chkNotify;
    private JCheckBox chkNotifyUnread;
    private JCheckBox chkSendBtn;
    private JPanel panel;
    private JCheckBox chkNotifyGroupMsg;
    private JCheckBox chkNotifyUnknown;
    private JCheckBox chkHideMyInput;
    private ActionLink linkUpdate;
    private ActionLink linkAbout;
    private JCheckBox chkHistory;
    private JPanel sendGroup;
    private JPanel notifyGroup;
    private JPanel otherGroup;
    private JLabel version;
    private final SmartIMSettings settings;

    public GeneralPanel(SmartIMSettings settings) {
        this.settings = settings;
        //sendGroup.setBorder(IdeBorderFactory.createTitledBorder("发送"));
        notifyGroup.setBorder(IdeBorderFactory.createTitledBorder("通知"));
        linkAbout.setExternalLinkIcon();
        linkUpdate.addActionListener(event -> RestUtils.checkUpdate());
        linkAbout.addActionListener(event -> BrowserUtil.browse(RestUtils.ABOUT_URL));
        chkNotify.addItemListener(e -> {
            chkNotifyUnread.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            chkNotifyGroupMsg.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
            chkNotifyUnknown.setEnabled(e.getStateChange() == ItemEvent.SELECTED);
        });
        IMWindowFactory.getPlugin().ifPresent(pluginDescriptor -> version.setText("当前版本：" + pluginDescriptor.getVersion()));
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
        return chkNotify.isSelected() != Objects.requireNonNull(settings.getState()).NOTIFY_MSG || chkNotifyUnread.isSelected() != settings
            .getState().NOTIFY_UNREAD || chkSendBtn.isSelected() != settings.getState().SHOW_SEND
            || chkNotifyGroupMsg.isSelected() != settings.getState().NOTIFY_GROUP_MSG
            || chkNotifyUnknown.isSelected() != settings.getState().NOTIFY_UNKNOWN
            || chkHideMyInput.isSelected() != settings.getState().HIDE_MY_INPUT || chkHistory.isSelected() != settings
            .getState().LOG_HISTORY || (!settings.getState().KEY_SEND.equals(comboSend.getSelectedItem().toString()));
    }

    @Override public void apply() throws ConfigurationException {
        Objects.requireNonNull(settings.getState()).NOTIFY_MSG = chkNotify.isSelected();
        settings.getState().NOTIFY_UNREAD = chkNotifyUnread.isSelected();
        settings.getState().SHOW_SEND = chkSendBtn.isSelected();
        settings.getState().NOTIFY_GROUP_MSG = chkNotifyGroupMsg.isSelected();
        settings.getState().NOTIFY_UNKNOWN = chkNotifyUnknown.isSelected();
        settings.getState().HIDE_MY_INPUT = chkHideMyInput.isSelected();
        settings.getState().LOG_HISTORY = chkHistory.isSelected();
        settings.getState().KEY_SEND = comboSend.getSelectedItem().toString();
    }

    @Override public void reset() {
        chkNotify.setSelected(Objects.requireNonNull(settings.getState()).NOTIFY_MSG);
        chkNotifyGroupMsg.setSelected(settings.getState().NOTIFY_GROUP_MSG);
        chkSendBtn.setSelected(settings.getState().SHOW_SEND);
        chkNotifyUnread.setSelected(settings.getState().NOTIFY_UNREAD);
        chkNotifyUnknown.setSelected(settings.getState().NOTIFY_UNKNOWN);
        chkHideMyInput.setSelected(settings.getState().HIDE_MY_INPUT);
        chkHistory.setSelected(settings.getState().LOG_HISTORY);
        comboSend.setSelectedItem(settings.getState().KEY_SEND);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}

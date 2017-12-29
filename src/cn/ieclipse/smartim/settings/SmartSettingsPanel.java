package cn.ieclipse.smartim.settings;

import cn.ieclipse.smartim.common.LOG;
import com.google.gson.Gson;
import com.intellij.notification.Notifications;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SmartSettingsPanel implements Configurable {
    private TextFieldWithBrowseButton send;
    private JCheckBox chkNotify;
    private JCheckBox chkNotifyUnread;
    private JCheckBox chkSendBtn;
    private JPanel panel;
    private JCheckBox chkNotifyGroupMsg;
    private JCheckBox chkNotifyUnknown;
    private JCheckBox chkHideMyInput;
    private JLabel linkUpdate;
    private JLabel linkAbout;
    private SmartIMSettings settings;

    private String update_url = "http://api.ieclipse.cn/smartqq/index/notice?p=intellij";
    private String about_url = "http://api.ieclipse.cn/smartqq/index/about";

    public SmartSettingsPanel() {
        settings = SmartIMSettings.getInstance();
        linkUpdate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    okhttp3.Request.Builder builder = (new okhttp3.Request.Builder()).url(update_url).get();
                    Request request = builder.build();
                    Call call = new OkHttpClient().newCall(request);
                    Response response = call.execute();
                    String json = response.body().string();
                    LOG.info(json);
                    if (response.code() == 200) {
                        UpdateInfo info = new Gson().fromJson(json, UpdateInfo.class);
                        cn.ieclipse.smartim.common.Notifications.notify(info.latest, info.desc);
                    }
                } catch (Exception ex) {
                    LOG.error("检查SmartIM最新版本", ex);
                }
            }
        });
        linkAbout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cn.ieclipse.util.BareBonesBrowserLaunch.openURL(about_url);
            }
        });
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
                || chkNotifyUnread.isSelected() != settings.getState().NOTIFY_UNREAD
                || chkSendBtn.isSelected() != settings.getState().SHOW_SEND
                || chkNotifyGroupMsg.isSelected() != settings.getState().NOTIFY_GROUP_MSG
                || chkNotifyUnknown.isSelected() != settings.getState().NOTIFY_UNKNOWN
                || chkHideMyInput.isSelected() != settings.getState().HIDE_MY_INPUT
                ;
    }

    @Override
    public void apply() throws ConfigurationException {
        settings.getState().NOTIFY_MSG = chkNotify.isSelected();
        settings.getState().NOTIFY_UNREAD = chkNotifyUnread.isSelected();
        settings.getState().SHOW_SEND = chkSendBtn.isSelected();
        settings.getState().NOTIFY_GROUP_MSG = chkNotifyGroupMsg.isSelected();
        settings.getState().NOTIFY_UNKNOWN = chkNotifyUnknown.isSelected();
        settings.getState().HIDE_MY_INPUT = chkHideMyInput.isSelected();
        settings.loadState(settings.getState());
    }

    @Override
    public void reset() {
        chkNotify.setSelected(settings.getState().NOTIFY_MSG);
        chkNotifyGroupMsg.setSelected(settings.getState().NOTIFY_GROUP_MSG);
        chkSendBtn.setSelected(settings.getState().SHOW_SEND);
        chkNotifyUnread.setSelected(settings.getState().NOTIFY_UNREAD);
        chkNotifyUnknown.setSelected(settings.getState().NOTIFY_UNKNOWN);
        chkHideMyInput.setSelected(settings.getState().HIDE_MY_INPUT);
    }

    @Override
    public void disposeUIResources() {

    }
}

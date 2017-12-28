package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.console.ChatHistoryPane;
import cn.ieclipse.smartim.console.ChatInputPane;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.JBSplitter;
import com.scienjus.smartqq.QNUploader;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.handler.msg.DiscussMessageHandler;
import com.scienjus.smartqq.handler.msg.FriendMessageHandler;
import com.scienjus.smartqq.handler.msg.GroupMessageHandler;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.QQMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;

/**
 * Created by Jamling on 2017/7/1.
 */
public class QQChatConsole extends IMChatConsole {

    public QQChatConsole(IContact target, SmartQQPanel imPanel) {
        super(target, imPanel);
    }

    public void sendFile(final String file) {
        final File f = new File(file);
        new Thread() {
            public void run() {
                uploadLock = true;
                try {
                    QNUploader uploader = new QNUploader();
                    String ak = "";
                    String sk = "";
                    String bucket = "";
                    String domain = "";
                    String qq = getClient()
                            .getAccount().getAccount();
                    boolean enable = false;
                    boolean ts = false;
                    if (!enable) {
                        ak = "";
                        sk = "";
                    }
                    QNUploader.UploadInfo info = uploader.upload(qq, f, ak, sk, bucket,
                            null);
                    String url = info.getUrl(domain, ts);

                    String msg = String.format(
                            "来自SmartQQ的文件: %s (大小%s), 点击链接 %s 查看",
                            IMUtils.getName(file),
                            IMUtils.formatFileSize(info.fsize), url);
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            send(msg);
                        }
                    });
                } catch (Exception e) {
                    LOG.error("发送文件失败 : " + e);
                    LOG.sendNotification("发送文件失败", String.format("文件：%s(%s)", file, e.getMessage()));
                }
                uploadLock = false;
            }
        }.start();
    }

    @Override
    public SmartQQClient getClient() {
        return (SmartQQClient) super.getClient();
    }

    @Override
    public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        JsonObject obj = new JsonParser().parse(raw).getAsJsonObject();
        QQMessage m = null;
        if (obj.has("group_code")) {
            m = (QQMessage) new GroupMessageHandler().handle(obj);
        } else if (obj.has("did")) {
            m = (QQMessage) new DiscussMessageHandler().handle(obj);
        } else {
            m = (QQMessage) new FriendMessageHandler().handle(obj);
        }

        AbstractFrom from = getClient().parseFrom(m);
        String name = from == null ? "未知用户" : from.getName();
        String msg = IMUtils.formatHtmlMsg(m.getTime(), name, m.getContent());
        write(msg);
    }

    public void post(final String msg) {
        SmartQQClient client = getClient();
        if (this.contact != null) {
            QQMessage m = client.createMessage(msg, contact);
            client.sendMessage(m, this.contact);
        }
    }


    private void createUIComponents() {

    }

    public void write(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                insertDocument(msg);
            }
        });
    }

    @Override
    public boolean hideMyInput() {
        if (contact instanceof Friend) {
            return false;
        }
        return SmartIMSettings.getInstance().getState().HIDE_MY_INPUT;
    }
}

package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scienjus.smartqq.QNUploader;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.handler.msg.DiscussMessageHandler;
import com.scienjus.smartqq.handler.msg.FriendMessageHandler;
import com.scienjus.smartqq.handler.msg.GroupMessageHandler;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.QQMessage;

import java.io.File;

/**
 * Created by Jamling on 2017/7/1.
 */
public class QQChatConsole extends IMChatConsole {

    public QQChatConsole(IContact target, SmartQQPanel imPanel) {
        super(target, imPanel);
    }

    @Override public SmartQQClient getClient() {
        return (SmartQQClient)super.getClient();
    }

    @Override public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        JsonObject obj = new JsonParser().parse(raw).getAsJsonObject();
        QQMessage m = null;
        if (obj.has("group_code")) {
            m = (QQMessage)new GroupMessageHandler().handle(obj);
        } else if (obj.has("did")) {
            m = (QQMessage)new DiscussMessageHandler().handle(obj);
        } else {
            m = (QQMessage)new FriendMessageHandler().handle(obj);
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

    @Override public boolean hideMyInput() {
        if (contact instanceof Friend) {
            return false;
        }
        return SmartIMSettings.getInstance().getState().HIDE_MY_INPUT;
    }

    @Override protected void sendFileInternal(String file) throws Exception {
        final File f = new File(file);
        if (f.length() > (1 << 18)) {
            write(String.format("%s 上传中，请稍候……", f.getName()));
        }
        QNUploader uploader = new QNUploader();
        String ak = SmartIMSettings.getInstance().getState().QN_AK;
        String sk = SmartIMSettings.getInstance().getState().QN_SK;
        String bucket = SmartIMSettings.getInstance().getState().QN_BUCKET;
        String domain = SmartIMSettings.getInstance().getState().QN_DOMAIN;
        String qq = getClient().getAccount().getAccount();
        boolean enable = SmartIMSettings.getInstance().getState().QN_ENABLE;
        boolean ts = SmartIMSettings.getInstance().getState().QN_TS;
        if (!enable) {
            ak = "";
            sk = "";
        }
        QNUploader.UploadInfo info = uploader.upload(qq, f, ak, sk, bucket, null);
        String url = info.getUrl(domain, ts);

        final String msg = String
            .format("来自SmartQQ的文件: %s (大小%s), 点击链接 %s 查看", IMUtils.getName(file), IMUtils.formatFileSize(info.fsize),
                url);
        send(msg);
    }
}

package cn.ieclipse.wechat;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * Created by Jamling on 2017/11/1.
 */
public class WXChatConsole extends IMChatConsole {
    WechatClient client;
    WechatPanel imPanel;

    public WXChatConsole(IContact target, WechatPanel imPanel) {
        super(target, imPanel);
        this.imPanel = imPanel;
    }

    @Override
    public WechatClient getClient() {
        return (WechatClient) super.getClient();
    }

    @Override
    public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        WechatMessage m = (WechatMessage) getClient().handleMessage(raw);
        AbstractFrom from = getClient().getFrom(m);
        String name = from == null ? "未知用户" : from.getName();
        String msg = IMUtils.formatMsg(m.getTime(), name, m.getText());
        write(msg);
    }

    @Override
    public void post(String msg) {
        WechatClient client = getClient();
        if (client.isLogin() && contact != null) {
            WechatMessage m = client.createMessage(0, msg, contact);
            client.sendMessage(m, contact);
        } else {
            error("发送失败，客户端异常（可能已断开连接或找不到联系人）");
        }
    }

    @Override
    public void sendFile(String file) {
        error("暂不支持，敬请关注 https://github.com/Jamling/SmartIM 或 https://github.com/Jamling/SmartQQ4IntelliJ 最新动态");
    }
}

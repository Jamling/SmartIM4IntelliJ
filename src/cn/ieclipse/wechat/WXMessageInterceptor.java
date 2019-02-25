package cn.ieclipse.wechat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.handler.MessageInterceptor;
import cn.ieclipse.smartim.model.IMessage;
import io.github.biezhi.wechat.model.WechatMessage;

public class WXMessageInterceptor implements MessageInterceptor {
    Pattern code = Pattern.compile(IMUtils.CODE_REGEX);
    
    @Override
    public boolean handle(IMessage message) {
        if (message instanceof WechatMessage) {
            WechatMessage msg = (WechatMessage) message;
            if (msg.MsgType == WechatMessage.MSGTYPE_TEXT) {
                Matcher m = code.matcher(msg.getText());
                if (m.find()) {
                    String linkText = m.group().substring(6).trim();
                    int s = m.start() + 6;
                    int e = s + linkText.length();
                    StringBuilder sb = new StringBuilder(msg.getText());
                    sb.delete(s, e);
                    String url = String.format("<a href=\"code://%s\">%s</a>",
                            linkText, linkText);
                    sb.insert(s, url);
                    msg.text = sb.toString();
                }
            }
        }
        return false;
    }
    
}

/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.wechat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.util.StringUtils;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.UserFrom;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2018年2月8日
 *       
 */
public class WXUtils {
    public static char getContactChar(IContact contact) {
        if (contact instanceof Contact) {
            Contact c = (Contact) contact;
            char ch = 'F';
            if (c.isPublic()) {
                ch = 'P';
            }
            else if (c.isGroup()) {
                ch = 'G';
            }
            else if (c.is3rdApp() || c.isSpecial()) {
                ch = 'S';
            }
            return ch;
        }
        return 0;
    }
    
    public static String decodeEmoji(String src) {
        String regex = StringUtils.encodeXml("<span class=\"emoji[\\w\\s]*\"></span>");
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(src);
        
        List<String> groups = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        while (m.find()) {
            starts.add(m.start());
            ends.add(m.end());
            groups.add(m.group());
        }
        if (!starts.isEmpty()) {
            StringBuilder sb = new StringBuilder(src);
            int offset = 0;
            for (int i = 0; i < starts.size(); i++) {
                int s = starts.get(i);
                int e = ends.get(i);
                String g = groups.get(i);
                
                int pos = offset + s;
                sb.delete(pos, offset + e);
                String ng = g;
                ng = StringUtils.decodeXml(g);
                sb.insert(pos, ng);
                offset += ng.length() - g.length();
            }
            return sb.toString();
        }
        return src;
    }
    
    public static String getPureName(String name) {
        String regex = "<span class=\"emoji[\\w\\s]*\"></span>";
        return name.replaceAll(regex, "").trim();
    }
    
    // ------------>消息格式化
    // 1. 输入消息，即将发出去的消息
    // 1.1 发送的消息＝输入的消息
    // 1.2 显示的消息＝ html转义->处理超链接->处理换行和空格
    // 2. 接收的消息
    // 2.1 处理超链接
    // ------------>
    
    static String formatHtmlMsg(boolean my, long time, String name,
            String msg) {
        String t = new SimpleDateFormat("HH:mm:ss").format(time);
        String clz = my ? "my" : "sender";
        return String.format(IMUtils.DIV_ROW_FORMAT, clz, t, name, name, msg);
    }
    
    /**
     * 格式化发出去的消息，对于输入的消息 显示的消息＝ html转义->处理超链接->处理换行和空格
     * 
     * @param name
     *            发送者
     * @param msg
     *            原始输入内容
     * @param encode
     *            是否进行转义
     * @return 格式化的消息内容
     */
    public static String formatHtmlOutgoing(String name, String msg,
            boolean encode) {
        String content = msg;
        if (encode) {
            content = StringUtils.encodeXml(msg);
            content = content.replaceAll(" ", "&nbsp;");
            content = IMUtils.autoLink(content);
            content = content.replaceAll("\r?\n", "<br/>");
        }
        return WXUtils.formatHtmlMsg(true, System.currentTimeMillis(), name,
                content);
    }
    
    /**
     * 格式化收到的消息
     * 
     * @param m
     *            微信消息
     * @param from
     *            发送者
     * @return 格式化的html消息
     */
    public static String formatHtmlIncoming(WechatMessage m,
            AbstractFrom from) {
        String name = from.isOut() ? from.getTarget().getName()
                : from.getName();
        name = WXUtils.getPureName(name);
        String msg = null;
        String text = m.getText() == null ? "" : m.getText().toString();
        boolean my = from.isOut() ? true : false;
        if (m.MsgType != WechatMessage.MSGTYPE_TEXT) {
            if (m.MsgType == WechatMessage.MSGTYPE_APP
                    && m.AppMsgType == WechatMessage.APPMSGTYPE_ATTACH) {
                if (m.AppMsgInfo != null) {
                
                }
            }
        }
        else {
            if (from instanceof UserFrom) {
                Contact c = (Contact) from.getContact();
            }
        }
        msg = WXUtils.formatHtmlMsg(my, m.getTime(), name, text);
        msg = WXUtils.decodeEmoji(msg);
        return msg;
    }
}

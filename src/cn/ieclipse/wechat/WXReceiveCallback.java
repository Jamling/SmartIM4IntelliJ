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

import cn.ieclipse.smartim.IMReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.smartim.views.IMPanel;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.GroupFrom;
import io.github.biezhi.wechat.model.UserFrom;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月14日
 *       
 */
public class WXReceiveCallback extends IMReceiveCallback {
    
    public WXReceiveCallback(WechatPanel fContactView) {
        super(fContactView);
    }
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (from != null && from.getContact() != null) {
            boolean unknown = false;
            boolean notify = SmartIMSettings.getInstance()
                    .getState().NOTIFY_MSG;
            String uin = from.getContact().getUin();
            Contact contact = (Contact) from.getContact();
            contact.setLastMessage(message);
            if (from instanceof GroupFrom) {
                GroupFrom gf = (GroupFrom) from;
                unknown = gf.getMember() == null || gf.getMember().isUnknown();
                notify = SmartIMSettings.getInstance()
                        .getState().NOTIFY_GROUP_MSG;
            }
            else {
                unknown = from.getMember() == null;
            }
            handle(unknown, notify, message, from,
                    (AbstractContact) from.getContact());
        }
    }
    
    @Override
    protected String getNotifyContent(AbstractMessage message,
            AbstractFrom from) {
        CharSequence content = (from instanceof UserFrom) ? message.getText()
                : from.getName() + ":" + message.getText();
        return content.toString();
    }
    
    @Override
    protected String getMsgContent(AbstractMessage message, AbstractFrom from) {
        String name = from.getName();
        String msg = null;
        if (message instanceof WechatMessage) {
            WechatMessage m = (WechatMessage) message;
            String text = m.getText() == null ? null : m.getText().toString();
            boolean encodeHtml = true;
            if (m.MsgType != WechatMessage.MSGTYPE_TEXT) {
                encodeHtml = false;
            }
            else {
                if (from instanceof UserFrom) {
                    Contact c = (Contact) from.getContact();
                    encodeHtml = !c.isPublic();
                }
            }
            msg = IMUtils.formatHtmlMsg(false, encodeHtml, m.CreateTime, name,
                    text);
        }
        return msg;
    }
}

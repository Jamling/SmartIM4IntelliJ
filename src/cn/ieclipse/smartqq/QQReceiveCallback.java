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
package cn.ieclipse.smartqq;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.FriendFrom;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.QQContact;
import com.scienjus.smartqq.model.QQMessage;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.smartim.views.IMPanel;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月16日
 */
public class QQReceiveCallback extends IMReceiveCallback {
    
    public QQReceiveCallback(SmartQQPanel fContactView) {
        super(fContactView);
    }
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (from != null && from.getContact() != null) {
            boolean unknown = false;
            boolean notify = SmartIMSettings.getInstance()
                    .getState().NOTIFY_MSG;
            String uin = from.getContact().getUin();
            IContact qqContact = from.getContact();
            SmartQQClient client = (SmartQQClient) fContactView.getClient();
            if (from instanceof GroupFrom) {
                GroupFrom gf = (GroupFrom) from;
                unknown = (gf.getGroupUser() == null
                        || gf.getGroupUser().isUnknown());
                uin = gf.getGroup().getUin();
                notify = SmartIMSettings.getInstance()
                        .getState().NOTIFY_GROUP_MSG;
                qqContact = client.getGroup(gf.getGroup().getId());
            }
            else if (from instanceof DiscussFrom) {
                DiscussFrom gf = (DiscussFrom) from;
                unknown = (gf.getDiscussUser() == null
                        || gf.getDiscussUser().isUnknown());
                uin = gf.getDiscuss().getUin();
                notify = SmartIMSettings.getInstance()
                        .getState().NOTIFY_GROUP_MSG;
                qqContact = client.getDiscuss(gf.getDiscuss().getId());
            }
            handle(unknown, notify, message, from, (AbstractContact) qqContact);
        }
    }
    
    @Override
    protected String getMsgContent(AbstractMessage message, AbstractFrom from) {
        String name = from.getName();
        String msg = null;
        if (message instanceof QQMessage) {
            QQMessage m = (QQMessage) message;
            msg = IMUtils.formatHtmlMsg(m.getTime(), name, m.getContent());
        }
        return msg;
    }
    
    @Override
    protected String getNotifyContent(AbstractMessage message,
            AbstractFrom from) {
        CharSequence content = (from instanceof FriendFrom) ? message.getText()
                : from.getName() + ":" + message.getText();
        return content.toString();
    }
    
}

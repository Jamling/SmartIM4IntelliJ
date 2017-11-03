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

import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.common.Notifications;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.FriendFrom;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.QQContact;
import com.scienjus.smartqq.model.QQMessage;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月16日
 */
public class QQReceiveCallback implements ReceiveCallback {
    private SmartQQPanel fContactView;
    private QQChatConsole lastConsole;

    public QQReceiveCallback(SmartQQPanel fContactView) {
        this.fContactView = fContactView;
    }

    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (from != null && from.getContact() != null) {
            boolean unkown = false;
            boolean notify = true;
            String uin = from.getContact().getUin();
            QQContact qqContact = null;
            if (from instanceof GroupFrom) {
                GroupFrom gf = (GroupFrom) from;
                unkown = (gf.getGroupUser() == null
                        || gf.getGroupUser().isUnknown());
                uin = gf.getGroup().getUin();
                notify = true;
                qqContact = fContactView.getClient()
                        .getGroup(gf.getGroup().getId());
            } else if (from instanceof DiscussFrom) {
                DiscussFrom gf = (DiscussFrom) from;
                unkown = (gf.getDiscussUser() == null
                        || gf.getDiscussUser().isUnknown());
                uin = gf.getDiscuss().getUin();
                notify = true;
                qqContact = fContactView.getClient()
                        .getGroup(gf.getDiscuss().getId());
            }
            if (!unkown) {
                SmartQQClient client = IMClientFactory.getInstance()
                        .getQQClient();
                IMHistoryManager.getInstance().save(client, uin,
                        message.getRaw());
            }
            if (notify) {
                CharSequence content = (from instanceof FriendFrom)
                        ? message.getText()
                        : from.getName() + ":" + message.getText();
                Notifications.notify(fContactView, from.getContact(),
                        from.getContact().getName(), content);
            }
            if (qqContact != null) {
                qqContact.setLastMessage(message);
            }

            QQChatConsole console = (QQChatConsole) fContactView.findConsole(from.getContact(), false);
            if (console != null) {
                this.lastConsole = console;
                String name = from.getName();
                String msg = null;
                if (message instanceof QQMessage) {
                    QQMessage m = (QQMessage) message;
                    msg = IMUtils.formatMsg(m.getTime(), name, m.getContent());
                }
                console.write(msg);
                fContactView.highlight(console);
            } else {
                if (qqContact != null) {
                    qqContact.increaceUnRead();
                }
            }
            fContactView.notifyUpdateContacts(0, false);
        }
    }

    @Override
    public void onReceiveError(Throwable e) {
        if (e == null) {
            return;
        }
        if (lastConsole != null) {
            lastConsole.error(e);
        } else {
            LOG.error("SmartQQ 接收异常" + e);
            LOG.sendNotification("错误", e.getMessage());
        }
    }

}

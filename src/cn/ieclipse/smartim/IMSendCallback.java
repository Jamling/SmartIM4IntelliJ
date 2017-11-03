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
package cn.ieclipse.smartim;

import cn.ieclipse.smartim.callback.SendCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.views.IMPanel;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月16日
 */
public class IMSendCallback implements SendCallback {

    protected IMPanel imPanel;

    public IMSendCallback(IMPanel imPanel) {
        this.imPanel = imPanel;
    }

    @Override
    public void onSendResult(int type, String targetId, CharSequence msg,
                             boolean success, Throwable t) {
        if (success) {
            onSuccess(type, targetId, msg);
        } else {
            onFailure(type, targetId, msg, t);
        }
    }

    protected void onSuccess(int type, String targetId, CharSequence msg) {
        SmartClient client = getIMPanel().getClient();
        if (client instanceof AbstractSmartClient) {
            String name = client.getAccount().getName();
            IMHistoryManager.getInstance().save((AbstractSmartClient) client, targetId,
                    IMUtils.formatMsg(System.currentTimeMillis(), name, msg));
        }
    }

    protected void onFailure(int type, String targetId, CharSequence msg,
                             Throwable t) {
        String s = IMUtils.isEmpty(msg) ? ""
                : (msg.length() > 20 ? msg.toString().substring(0, 20) + "..."
                : msg.toString());
        IMChatConsole console = getIMPanel().findConsoleById(targetId,
                true);
        if (console != null) {
            console.error(String.format("%s 发送失败！", s));
        } else {
            LOG.error(String.format("发送到%s的信息（%s）", targetId, s), t);
            Notifications.notify("发送失败", String.format("%s 发送失败！", s));
        }
    }

    protected IMPanel getIMPanel() {
        return imPanel;
    }
}

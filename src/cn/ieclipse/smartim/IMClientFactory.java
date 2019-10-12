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

import com.scienjus.smartqq.client.SmartQQClient;
import io.github.biezhi.wechat.api.WechatClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月14日
 */
public class IMClientFactory {
    public static final String TYPE_QQ = "QQ";
    public static final String TYPE_WECHAT = "Wechat";
    private Map<String, SmartClient> clients = null;

    private static IMClientFactory instance = new IMClientFactory();

    private void checkClients() {
        if (clients == null) {
            clients = new HashMap<>();
        }
    }

    private SmartClient create(String type) {
        if (TYPE_QQ.equalsIgnoreCase(type)) {
            return new SmartQQClient();
        } else if (TYPE_WECHAT.equalsIgnoreCase(type)) {
            return new WechatClient();
        }
        throw new UnsupportedOperationException("No client type " + type);
    }

    public SmartClient getClient(String type) {
        checkClients();
        SmartClient client = clients.get(type);
        if (client == null || client.isClose()) {
            client = create(type);
            File dir = IMWindowFactory.getDefault().getWorkDir().getAbsoluteFile();
            client.setWorkDir(new File(dir, "SmartIM"));
            clients.put(type, client);
        }
        return client;
    }

    public SmartQQClient getQQClient() {
        return (SmartQQClient)getClient(TYPE_QQ);
    }

    public WechatClient getWechatClient() {
        return (WechatClient)getClient(TYPE_WECHAT);
    }

    public static IMClientFactory getInstance() {
        return instance;
    }
}

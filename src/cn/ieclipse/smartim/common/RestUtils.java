/*
 * Copyright 2014-2018 ieclipse.cn.
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
package cn.ieclipse.smartim.common;

import cn.ieclipse.util.XPathUtils;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.swing.text.html.StyleSheet;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2019年1月16日
 */
public class RestUtils {
    public final static String CSS_URL = "http://dl.ieclipse.cn/r/smartim.css";
    public final static String UPDATE_URL = "https://plugins.jetbrains.com/plugins/list?pluginId=9816";
    public final static String ABOUT_URL = "http://dl.ieclipse.cn/jws/about.html";

    public static String getWelcome(String im) {
        if (im.equals("qq")) {
            return "因腾讯业务调整，SmartQQ于2019年1月1日起停止服务。当前暂无替代的协议，暂无法提供脑出血，敬请谅解！";
        }

        final IdeaPluginDescriptor descriptor =
                PluginManager.getPlugin(PluginId.findId("cn.ieclipse.smartqq.intellij"));
        return String.format("欢迎使用SmartIM4IntelliJ (ver:%s)，为保障安全，请不要在公开场合讨论本插件以免被封", descriptor.getVersion());
    }

    public static void checkUpdate() {
        new Thread(() -> {
            try {
                okhttp3.Request.Builder builder = (new okhttp3.Request.Builder()).url(UPDATE_URL).get();
                Request request = builder.build();
                Call call = new OkHttpClient().newCall(request);
                Response response = call.execute();
                if (response.code() == 200) {
                    Document doc = XPathUtils.parse(response.body().byteStream());
                    Element node = XPathUtils.findElement(doc, "/plugin-repository/category/idea-plugin[1]");
                    String latest = XPathUtils.findElement(node, "version").getTextContent();
                    String desc = XPathUtils.findElement(node, "change-notes").getTextContent();
                    final IdeaPluginDescriptor descriptor =
                            PluginManager.getPlugin(PluginId.findId("cn.ieclipse.smartqq.intellij"));
                    SwingUtilities.invokeLater(() -> {
                        if (descriptor != null && descriptor.getVersion().equals(latest)) {
                            JOptionPane.showMessageDialog(null, "已是最新版本");
                            return;
                        }
                        Notifications.notify(latest, desc);
                        JOptionPane.showMessageDialog(null,
                                "发现新版本" + latest + "请在File->Settings->Plugins插件页中更新SmartIM");
                    });
                }
            } catch (Exception ex) {
                LOG.error("检查SmartIM最新版本", ex);
            }
        }).start();
    }

    public static void loadStyleAsync(final StyleSheet styleSheet) {
        // new Thread(() -> loadStyleSync(styleSheet)).start();
    }

    public static void loadStyleSync(final StyleSheet styleSheet) {
        try {
            styleSheet.importStyleSheet(new URL(CSS_URL));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }

}

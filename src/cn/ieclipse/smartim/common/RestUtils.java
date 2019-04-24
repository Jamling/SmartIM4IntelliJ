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

import com.google.gson.Gson;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.extensions.PluginId;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
    public final static String welcome_format = "http://api.ieclipse.cn/smartqq/index/welcome?p=%s&im=%s&v=%s";
    public final static String update_url = "http://api.ieclipse.cn/smartqq/index/notice?p=intellij";
    public final static String about_url = "http://api.ieclipse.cn/smartqq/index/about";

    public static String getWelcome(String im) {
        try {
            final IdeaPluginDescriptor descriptor = PluginManager.getPlugin(PluginId.findId("cn.ieclipse.smartqq.intellij"));
            Request.Builder builder = new Request.Builder()
                    .url(String.format(welcome_format, "intellij", im,
                            descriptor.getVersion()))
                    .get();
            Request request = builder.build();
            Call call = new OkHttpClient().newCall(request);
            Response response = call.execute();
            String json = response.body().string();
            if (response.code() == 200) {
                return json;
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void checkUpdate() {
        new Thread() {
            public void run() {
                try {
                    Request.Builder builder = (new Request.Builder())
                            .url(RestUtils.update_url).get();
                    Request request = builder.build();
                    Call call = new OkHttpClient().newCall(request);
                    Response response = call.execute();
                    String json = response.body().string();
                    LOG.info(json);
                    if (response.code() == 200) {
                        final UpdateInfo info = new Gson().fromJson(json,
                                UpdateInfo.class);
                        final IdeaPluginDescriptor descriptor = PluginManager.getPlugin(PluginId.findId("cn.ieclipse.smartqq.intellij"));
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                if (descriptor != null && descriptor.getVersion().equals(info.latest)) {
                                    JOptionPane.showMessageDialog(null, "已是最新版本");
                                    return;
                                }
                                cn.ieclipse.smartim.common.Notifications.notify(info.latest, info.desc);
                                JOptionPane.showMessageDialog(null, "发现新版本" + info.latest + "请在File->Settings->Plugins插件页中更新SmartQQ");
                            }

                            ;
                        });
                    }
                } catch (Exception ex) {
                    LOG.error("检查SmartIM最新版本", ex);
                }
            }

            ;
        }.start();
    }

    public static class UpdateInfo {
        public String latest;
        public String desc;
        public String link;
    }

    public static void loadStyleAsync(final StyleSheet styleSheet) {
//        new Thread() {
//            @Override
//            public void run() {
//                loadStyleSync(styleSheet);
//            }
//        }.start();
    }

    public static void loadStyleSync(final StyleSheet styleSheet) {
        try {
            styleSheet.importStyleSheet(
                    new URL("http://dl.ieclipse.cn/r/smartim.css"));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
    }
}

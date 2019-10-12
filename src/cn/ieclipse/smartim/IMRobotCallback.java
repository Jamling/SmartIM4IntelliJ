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

import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.robot.TuringRobot;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.util.EncodeUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月16日
 */
public abstract class IMRobotCallback implements ReceiveCallback, ModificationCallback {
    public static final String SEP = " ";
    protected TuringRobot turingRobot = new TuringRobot("Turing", 0, null);

    public static boolean isEnable() {
        return SmartIMSettings.getInstance().getState().ROBOT_ENABLE;
    }

    public static String getRobotName() {
        return SmartIMSettings.getInstance().getState().ROBOT_NAME;
    }

    /**
     * 对userId或groupId进行加密
     *
     * @param id userId
     * @return 加密后的md5字串
     */
    public static String encodeUid(String id) {
        return EncodeUtils.getMd5(id);
    }

    public static String getTuringApiKey() {
        String key = SmartIMSettings.getInstance().getState().ROBOT_KEY;
        if (key != null && !key.isEmpty()) {
            return key;
        }
        return null;
    }
}

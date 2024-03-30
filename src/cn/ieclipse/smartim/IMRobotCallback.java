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
import cn.ieclipse.smartim.callback.RobotCallback;
import cn.ieclipse.smartim.robot.IRobot;
import cn.ieclipse.smartim.robot.RobotFactory;
import cn.ieclipse.smartim.robot.TuringRobot;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.util.EncodeUtils;
import cn.ieclipse.util.EncryptUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年10月16日
 */
public abstract class IMRobotCallback implements RobotCallback, ModificationCallback {
    public static final String SEP = " ";
    @Override
    public boolean isEnable() {
        return SmartIMSettings.getInstance().getState().ROBOT_ENABLE;
    }

    @Override
    public String getRobotName() {
        return SmartIMSettings.getInstance().getState().ROBOT_NAME;
    }

    @Override
    public boolean isReplyAnyGroupMember() {
        return SmartIMSettings.getInstance().getState().ROBOT_GROUP_ANY;
    }

    @Override
    public boolean isReplyFriend() {
        return SmartIMSettings.getInstance().getState().ROBOT_FRIEND_ANY;
    }

    @Override
    public String emptyReply() {
        return SmartIMSettings.getInstance().getState().ROBOT_REPLY_EMPTY;
    }

    @Override
    public String getGroupWelcome() {
        return SmartIMSettings.getInstance().getState().ROBOT_GROUP_WELCOME;
    }

    @Override
    public IRobot getRobot() {
        return RobotFactory.getInstance().getRobot();
    }

    public void initRobot() {
        if (!isEnable()) {
            return;
        }

        int type = SmartIMSettings.getInstance().getState().ROBOT_TYPE;
        String key;
        String extra;
        if (type == RobotFactory.ROBOT_OPENAI) {
            key = SmartIMSettings.getInstance().getState().ROBOT_OPENAI_KEY;
            extra = SmartIMSettings.getInstance().getState().ROBOT_OPENAI_EXTRA;
        } else {
            key = SmartIMSettings.getInstance().getState().ROBOT_KEY;
            extra = SmartIMSettings.getInstance().getState().ROBOT_KEY;
        }
        RobotFactory.getInstance().changeSettings(type, key, extra);
    }

    public static String getTuringApiKey() {
        String key = SmartIMSettings.getInstance().getState().ROBOT_KEY;
        if (key != null && !key.isEmpty()) {
            return key;
        }
        return null;
    }
}

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

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupInfo;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2018年2月12日
 */
public class QQUtils {
    public static char getContactChar(IContact target) {
        char ch = 'F';
        if (target instanceof Group || target instanceof GroupInfo) {
            ch = 'G';
        } else if (target instanceof Discuss || target instanceof DiscussInfo) {
            ch = 'D';
        }
        return ch;
    }

    public static String decodeEmoji(String src) {
        return src;
        // String regex = "\\[\"face\",([1-9][0-9])\\]";
        // if (src != null) {
        // String n = src.replaceAll(regex,
        // "<span class=\"qqemoji qq_face qqface$1\" alt=\"QQ表情$1\"></span>");
        // return n;
        // }
        // else {
        // return "";
        // }
    }

    public static void main(String[] args) {
        String src =
            IMUtils.formatHtmlMsg(true, true, System.currentTimeMillis(), "Me", "1毛9[\"face\",0] [\"face\",71] 哈哈");
        System.out.println(src);
        System.out.println(decodeEmoji(src));
    }
}

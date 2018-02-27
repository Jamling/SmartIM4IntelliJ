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

import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.util.StringUtils;
import io.github.biezhi.wechat.model.Contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2018年2月8日
 *       
 */
public class WXUtils {
    public static char getContactChar(IContact contact) {
        if (contact instanceof Contact) {
            Contact c = (Contact) contact;
            char ch = 'F';
            if (c.isPublic()) {
                ch = 'P';
            }
            else if (c.isGroup()) {
                ch = 'G';
            }
            else if (c.is3rdApp() || c.isSpecial()) {
                ch = 'S';
            }
            return ch;
        }
        return 0;
    }
    
    public static String decodeEmoji(String src) {
        String regex = StringUtils.encodeXml("<span class=\"emoji.+</span>");
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(src);
        
        List<String> groups = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        while (m.find()) {
            starts.add(m.start());
            ends.add(m.end());
            groups.add(m.group());
        }
        if (!starts.isEmpty()) {
            StringBuilder sb = new StringBuilder(src);
            int offset = 0;
            for (int i = 0; i < starts.size(); i++) {
                int s = starts.get(i);
                int e = ends.get(i);
                String g = groups.get(i);
                
                int pos = offset + s;
                sb.delete(pos, offset + e);
                String ng = g;
                ng = StringUtils.decodeXml(g);
                sb.insert(pos, ng);
                offset += ng.length() - g.length();
            }
            return sb.toString();
        }
        return src;
    }
}

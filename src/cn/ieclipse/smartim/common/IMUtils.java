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
package cn.ieclipse.smartim.common;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月14日
 *       
 */
public class IMUtils {
    
    /**
     * Get file name from file path
     *
     * @param path
     *            file path
     *            
     * @return file name
     */
    public static String getName(String path) {
        File f = new File(path);
        String name = f.getName();
        return name;
    }
    
    public static String formatFileSize(long length) {
        if (length > (1 << 20)) {
            return length / (1 << 20) + "M";
        }
        else if (length > (1 << 10)) {
            return length / (1 << 10) + "K";
        }
        return length + "B";
    }
    
    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }
    
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }
    
    public static String formatMsg(long time, String name, CharSequence msg) {
        String s1 = new SimpleDateFormat("HH:mm:ss").format(time);
        return String.format("%s %s: %s\n", s1, name, msg);
    }
    
    public static boolean isMySendMsg(String raw) {
        return raw.matches("^\\d{2}:\\d{2}:\\d{2} [.\\s\\S]*");
    }
}

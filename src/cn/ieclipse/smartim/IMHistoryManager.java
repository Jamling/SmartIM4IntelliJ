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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.ieclipse.smartim.helper.FileStorage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月23日
 *       
 */
public class IMHistoryManager {
    private Map<String, FileStorage> stores = new HashMap<>();
    private static final int max = 30;
    private static final int size = 500;
    
    private static IMHistoryManager instance = new IMHistoryManager();
    
    public static IMHistoryManager getInstance() {
        return instance;
    }
    
    private FileStorage get(AbstractSmartClient client, String uin) {
        FileStorage fs = stores.get(uin);
        if (fs == null) {
            File f = new File(client.getWorkDir("history"), uin);
            fs = new FileStorage(size, f.getAbsolutePath());
            stores.put(uin, fs);
        }
        return fs;
    }
    
    public List<String> load(AbstractSmartClient client, String uin) {
        FileStorage fs = get(client, uin);
        return fs.getLast(max);
    }
    
    public boolean save(AbstractSmartClient client, String uin, String rawMsg) {
        FileStorage fs = get(client, uin);
        boolean ret = fs.append(rawMsg);
        ret = ret && fs.isPersistent() && fs.flush();
        return ret;
    }
}

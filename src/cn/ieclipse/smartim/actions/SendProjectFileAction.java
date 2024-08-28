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
package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.console.IMChatConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.vfs.VirtualFile;
import icons.SmartIcons;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年8月22日
 */
public class SendProjectFileAction extends SendFileAction {

    public SendProjectFileAction(IMChatConsole console) {
        super(console, "发送项目文件", "Send your project(workspace) file to " + console.getName(),
            SmartIcons.projectFile);
    }

    @Override public void actionPerformed(AnActionEvent e) {
        if (!console.enableUpload()) {
            console.error("文件发送中，请勿频繁操作");
            return;
        }
        FileChooserDescriptor descriptor = new FileChooserDescriptor(true, false, true, true, false, false);
        final VirtualFile virtualFile =
            FileChooser.chooseFile(descriptor, IMWindowFactory.getDefault().getProject(), null);
        if (virtualFile == null) {
            return;
        }
        String path = virtualFile.getCanonicalPath();
        if (path != null) {
            console.send(path);
        }
    }

}

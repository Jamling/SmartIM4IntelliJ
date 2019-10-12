package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.SmartIcons;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class SendFileAction extends IMChatAction {
    protected String dialogTitle;
    protected FileNameExtensionFilter filter_image =
        new FileNameExtensionFilter("图片文件", "jpg", "gif", "bmp", "jpeg", "png");
    protected FileFilter filter;

    public SendFileAction(IMChatConsole console) {
        super(console, "发送", "发送文件", SmartIcons.file);
        this.dialogTitle = "请选择要发送的文件";
    }

    public SendFileAction(IMChatConsole console, String text, String desc, Icon icon) {
        super(console, text, desc, icon);
    }

    @Override public void actionPerformed(AnActionEvent e) {
        if (!console.enableUpload()) {
            console.error("文件发送中，请勿频繁操作");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        if (dialogTitle != null) {
            chooser.setDialogTitle(dialogTitle);
        }
        if (filter != null) {
            chooser.setFileFilter(filter);
        }
        int code = chooser.showOpenDialog(new JLabel());
        if (code == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f != null) {
                console.sendFile(f.getAbsolutePath());
            }
        }
        return;
    }

}

package cn.ieclipse.smartim.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Jamling on 2018/1/2.
 */
public final class ActionUtils {
    private ActionUtils() {

    }

    public static VirtualFile getFile(AnActionEvent e) {
        return (CommonDataKeys.VIRTUAL_FILE.getData(e.getDataContext()));
    }

    public static Editor getEditor(AnActionEvent e) {
        return (CommonDataKeys.EDITOR.getData(e.getDataContext()));
    }
}

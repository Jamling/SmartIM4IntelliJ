package cn.ieclipse.smartim.idea;

import cn.ieclipse.smartim.IMWindowFactory;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.editor.ScrollType;
import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.TextEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

/**
 * Created by Jamling on 2018/1/2.
 */
public class EditorUtils {
    public static Editor openTextEditor(Project project, VirtualFile virtualFile) {
        Editor editor = null;
        FileEditor[] fileEditors = FileEditorManager.getInstance(project).openFile(virtualFile, true);
        for (FileEditor fileEditor : fileEditors) {
            if (fileEditor instanceof TextEditor) {
                TextEditor textEditor = (TextEditor) fileEditor;
                editor = textEditor.getEditor();
            }
        }
        return editor;
    }

    public static void setLine(Editor editor, int line, int column) {
        if (editor != null) {
            try {
                int startOffset = editor.logicalPositionToOffset(new LogicalPosition(line, column));
                editor.getCaretModel().moveToOffset(startOffset);
                editor.getScrollingModel().scrollToCaret(ScrollType.CENTER);
                editor.getSelectionModel().setSelection(startOffset, startOffset);
            } catch (Exception e) {
                // do nothing
            }
        }
    }

    public static int getEditorLine(Editor editor) {
        LogicalPosition pos = editor.getCaretModel().getLogicalPosition();
        return pos.line;
    }

    public static VirtualFile findByPath(Project project, String path) {
        return VFSUtils._getVirtualFile(project, path);
    }

    public static void openFile(String file, int line) {
        if (IMWindowFactory.getDefault() != null) {
            Project project = IMWindowFactory.getDefault().getProject();
            if (project != null) {
                VirtualFile vf = findByPath(project, file);
                if (vf != null) {
                    Editor editor = openTextEditor(project, vf);
                    if (editor != null) {
                        setLine(editor, line, 0);
                    }
                }
            }
        }
    }
}

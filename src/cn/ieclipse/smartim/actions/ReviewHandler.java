package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.dialogs.ReviewDialog;
import cn.ieclipse.smartim.idea.EditorUtils;
import cn.ieclipse.util.StringUtils;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jamling on 2018/1/2.
 */
public class ReviewHandler extends EditorActionHandler {
    @Override
    protected void doExecute(Editor editor, @Nullable Caret caret, DataContext dataContext) {
        super.doExecute(editor, caret, dataContext);

        VirtualFile vf = (CommonDataKeys.VIRTUAL_FILE.getData(dataContext));

        if (IMWindowFactory.getDefault() == null) {
            return;
        }
        Project project = editor.getProject();
        String path = vf.getPresentableUrl().substring(project.getPresentableUrl().length());
        if (path.length() > 0 && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (!StringUtils.isEmpty(path)) {
            String text = editor.getSelectionModel().getSelectedText();
            int line = EditorUtils.getEditorLine(editor);
            ReviewDialog dialog = new ReviewDialog();
            dialog.pack();
            dialog.setLocationRelativeTo(null);
            dialog.setData(path, line, text);
            dialog.setVisible(true);
            dialog.dispose();
        }
    }
}

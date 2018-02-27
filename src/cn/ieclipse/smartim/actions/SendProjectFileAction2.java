package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;
import com.intellij.ide.actions.GotoActionBase;
import com.intellij.ide.util.gotoByName.ChooseByNameModel;
import com.intellij.ide.util.gotoByName.ChooseByNamePopup;
import com.intellij.ide.util.gotoByName.GotoFileModel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import icons.SmartIcons;

import java.io.File;

/**
 * Created by Jamling on 2018/2/27.
 */
public class SendProjectFileAction2 extends GotoActionBase implements DumbAware {
    private IMChatConsole console;

    public SendProjectFileAction2(IMChatConsole console) {
        this.console = console;
        getTemplatePresentation().setIcon(SmartIcons.projectFile);
        getTemplatePresentation().setText("发送工程中的文件");
    }

    @Override
    protected void gotoActionPerformed(AnActionEvent anActionEvent) {
        Project project = anActionEvent.getData(CommonDataKeys.PROJECT);
        ChooseByNameModel model = new GotoFileModel(project);
        showNavigationPopup(anActionEvent, model, new Callback(), false);
    }

    private class Callback extends GotoActionCallback<String> {

        @Override
        public void elementChosen(ChooseByNamePopup chooseByNamePopup, Object o) {
            if (o != null && o instanceof PsiFile) {
                PsiFile file = (PsiFile) o;
                VirtualFile vf = file.getVirtualFile();
                if (vf != null) {
                    File f = new File(vf.getCanonicalPath());
                    if (f.exists()) {
                        console.sendFile(f.getAbsolutePath());
                    } else {
                        console.send(vf.getCanonicalPath());
                    }
                }
            }
        }
    }
}

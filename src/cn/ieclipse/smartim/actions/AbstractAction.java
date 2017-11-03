package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class AbstractAction extends DumbAwareAction {
    protected IMPanel imPanel;

    public AbstractAction(@Nullable String text, @Nullable String description, @Nullable Icon icon, IMPanel panel) {
        super(text, description, icon);
        this.imPanel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

    }
}

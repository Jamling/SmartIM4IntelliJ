package cn.ieclipse.smartqq.actions;

import cn.ieclipse.smartqq.SmartPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

/**
 * Created by Jamling on 2017/7/12.
 */
public class AbstractAction extends DumbAwareAction {
    SmartPanel panel;
    public AbstractAction(SmartPanel panel){
        super();
        this.panel = panel;
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {

    }
}

package cn.ieclipse.smartqq;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Jamling on 2017/7/11.
 */
public class SmartWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "Smart";

    private Project project;
    private SmartPanel panel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        this.project = project;
        toolWindow.setToHideOnEmptyContent(true);
        Content content = createContentPanel(project, toolWindow);
        toolWindow.getContentManager().addContent(content);
        ToolWindowManager manager = ToolWindowManager.getInstance(project);
        ((ToolWindowManagerEx) manager).addToolWindowManagerListener(new ToolWindowManagerListener() {
            @Override
            public void toolWindowRegistered(@NotNull String id) {
            }

            @Override
            public void stateChanged() {
                ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow(SmartWindowFactory.TOOL_WINDOW_ID);
                if (window != null) {
                    boolean visible = window.isVisible();
                    System.out.println("smartqq tool window " + window + " visible: " + visible);
                    if (visible && toolWindow.getContentManager().getContentCount() == 0) {
                        System.out.println("need init");
                        window.getContentManager().addContent(createContentPanel(project, window));
                    }
                }
            }
        });

        Disposer.register(project, new Disposable() {
            @Override
            public void dispose() {
                if (panel != null && panel.isEnabled()) {
                    panel.setEnabled(false);
                    panel = null;
                }
            }
        });
    }

    private Content createContentPanel(Project project, ToolWindow toolWindow) {
        System.out.println("project:" + project);
        panel = new SmartPanel(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        return content;
    }
}

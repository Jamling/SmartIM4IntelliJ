package cn.ieclipse.smartim;

import cn.ieclipse.smartqq.SmartQQPanel;
import cn.ieclipse.wechat.WechatPanel;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ex.ToolWindowManagerEx;
import com.intellij.openapi.wm.ex.ToolWindowManagerListener;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by Jamling on 2017/7/11.
 */
public class IMWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "SmartIM";

    private Project project;
    private SmartQQPanel panel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        instance = this;
        this.project = project;
        toolWindow.setToHideOnEmptyContent(true);
        createContents(project, toolWindow);
        ToolWindowManager manager = ToolWindowManager.getInstance(project);
        ((ToolWindowManagerEx) manager).addToolWindowManagerListener(new ToolWindowManagerListener() {
            @Override
            public void toolWindowRegistered(@NotNull String id) {
            }

            @Override
            public void stateChanged() {
                ToolWindow window = ToolWindowManager.getInstance(project).getToolWindow(IMWindowFactory.TOOL_WINDOW_ID);
                if (window != null) {
                    boolean visible = window.isVisible();
                    if (visible && toolWindow.getContentManager().getContentCount() == 0) {
                        System.out.println("need init");
                        createContents(project, window);
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

    private void createContents(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        SmartQQPanel qqPanel = new SmartQQPanel(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(qqPanel, "SmartQQ", false);
        toolWindow.getContentManager().addContent(content,0);

        WechatPanel wechatPanel = new WechatPanel(project, toolWindow);
        content = contentFactory.createContent(wechatPanel, "Wechat", false);
        toolWindow.getContentManager().addContent(content,1);
    }

    private Content createContentPanel(Project project, ToolWindow toolWindow) {
        System.out.println("project:" + project);
        panel = new SmartQQPanel(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        return content;
    }

    public File getWorkDir() {
        Project p = this.project;
        if (p == null) {
            p = ProjectManager.getInstance().getDefaultProject();
            Project[] ps = ProjectManager.getInstance().getOpenProjects();
            if (ps != null) {
                for (Project t : ps) {
                    if (!t.isDefault()) {
                        p = t;
                    }
                }
            }
        }
        File dir = new File(p.getBasePath(), Project.DIRECTORY_STORE_FOLDER);
        return dir;
    }

    private static IMWindowFactory instance;
    public static IMWindowFactory getDefault() {
        return instance;
    }
}

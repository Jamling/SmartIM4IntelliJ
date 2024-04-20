package cn.ieclipse.smartim;

import cn.ieclipse.smartqq.SmartQQPanel;
import cn.ieclipse.wechat.WechatPanel;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Optional;

/**
 * Created by Jamling on 2017/7/11.
 */
public class IMWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "SmartIM";

    private Project project;

    /**
     * 在未找到QQ新协议之前，不展示QQ
     */
    private final boolean enableQQ = false;

    @Override public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        instance = this;
        this.project = project;
        toolWindow.setToHideOnEmptyContent(true);
        createContents(project, toolWindow);
    }

    private void createContents(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        File dir = new File(getWorkDir(), "SmartIM");
        if (dir.exists()) {
            dir.mkdirs();
        }
        System.setProperty("log.home", dir.getAbsolutePath());
        ContentFactory contentFactory = ContentFactory.getInstance();
        Content content;

        WechatPanel wechatPanel = new WechatPanel(project, toolWindow);
        content = contentFactory.createContent(wechatPanel, "Wechat", false);
        toolWindow.getContentManager().addContent(content, 0);

        if (!enableQQ) {
            return;
        }
        SmartQQPanel qqPanel = new SmartQQPanel(project, toolWindow);
        content = contentFactory.createContent(qqPanel, "SmartQQ", false);
        toolWindow.getContentManager().addContent(content, 1);
    }

    public File getWorkDir() {
        Project p = this.project;
        if (p == null) {
            p = ProjectManager.getInstance().getDefaultProject();
            Project[] ps = ProjectManager.getInstance().getOpenProjects();
            for (Project t : ps) {
                if (!t.isDefault()) {
                    p = t;
                }
            }

        }
        return new File(p.getBasePath(), Project.DIRECTORY_STORE_FOLDER);
    }

    public Project getProject() {
        return project;
    }

    private static IMWindowFactory instance;

    public static IMWindowFactory getDefault() {
        return instance;
    }

    public static Optional<PluginDescriptor> getPlugin() {
        PluginId pluginId = PluginId.getId("cn.ieclipse.smartqq.intellij");
        return Optional.ofNullable(PluginManagerCore.getPlugin(pluginId));
    }
}

package cn.ieclipse.smartim.settings;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by Jamling on 2018/2/27.
 */
public class SmartSettingsPanel implements Configurable {
    private JPanel panel;
    private JBTabbedPane tabHost;
    private JBScrollPane scroll1;
    private JBScrollPane scroll2;
    private JBScrollPane scroll3;
    private SmartIMSettings settings;
    private GeneralPanel generalPanel;
    private RobotPanel robotPanel;
    private UploadPanel uploadPanel;

    public SmartSettingsPanel() {
        settings = SmartIMSettings.getInstance();
        generalPanel = new GeneralPanel(settings);
        scroll1.setViewportView(generalPanel.createComponent());

        robotPanel = new RobotPanel(settings);
        scroll2.setViewportView(robotPanel.createComponent());

        uploadPanel = new UploadPanel(settings);
        scroll3.setViewportView(uploadPanel);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "SmartIM";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        return panel;
    }

    @Override
    public boolean isModified() {
        return generalPanel.isModified() || robotPanel.isModified() || uploadPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        generalPanel.apply();
        robotPanel.apply();
        uploadPanel.apply();
        settings.loadState(settings.getState());
    }

    @Override
    public void reset() {
        generalPanel.reset();
        robotPanel.reset();
        uploadPanel.reset();
    }

    @Override
    public void disposeUIResources() {

    }
}

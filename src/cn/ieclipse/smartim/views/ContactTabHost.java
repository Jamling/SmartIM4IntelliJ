package cn.ieclipse.smartim.views;

import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTabHost extends JBTabbedPane {

    private Insets insets = JBUI.emptyInsets();

    public ContactTabHost() {
        setTabComponentInsets(insets);
    }
}

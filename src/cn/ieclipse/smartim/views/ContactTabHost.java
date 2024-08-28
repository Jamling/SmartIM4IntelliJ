package cn.ieclipse.smartim.views;

import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;

import java.awt.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTabHost extends JBTabbedPane {

    private final Insets insets = JBUI.emptyInsets();

    public ContactTabHost() {
        setTabComponentInsets(insets);
    }
}

package cn.ieclipse.smartim.views;

import com.intellij.ui.components.JBTabbedPane;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTabHost extends JBTabbedPane {

    private Insets insets = new Insets(0, 0, 0, 0);

    @NotNull @Override protected Insets getInsetsForTabComponent() {
        return insets;
    }
}

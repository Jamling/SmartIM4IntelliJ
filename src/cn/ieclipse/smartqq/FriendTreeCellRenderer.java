package cn.ieclipse.smartqq;

import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.Category;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/**
 * Created by Jamling on 2017/7/1.
 */
public class FriendTreeCellRenderer extends DefaultTreeCellRenderer {

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        Component component = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        if (value instanceof DefaultMutableTreeNode) {
            Object m = ((DefaultMutableTreeNode) value).getUserObject();
            JLabel label = (JLabel) component;
            label.setText(SmartClient.getName(m));
        }
        return component;
    }
}

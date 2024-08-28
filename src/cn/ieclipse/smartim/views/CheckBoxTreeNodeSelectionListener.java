package cn.ieclipse.smartim.views;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CheckBoxTreeNodeSelectionListener extends MouseAdapter {
    @Override public void mouseClicked(MouseEvent event) {
        JTree tree = (JTree)event.getSource();
        int x = event.getX();
        int y = event.getY();
        int row = tree.getRowForLocation(x, y);
        TreePath path = tree.getPathForRow(row);
        if (path != null) {
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)path.getLastPathComponent();
            if (selectedNode != null && selectedNode.getChildCount() > 0 && event.getClickCount() > 1) {
                // if (tree.isExpanded(path)) {
                // tree.collapsePath(path);
                // }
                // else {
                // tree.expandPath(path);
                // }
            } else if (selectedNode instanceof CheckBoxTreeNode node && event.getClickCount() == 1) {
                if (node != null) {
                    boolean isSelected = !node.isSelected();
                    node.setSelected(isSelected);
                    ((DefaultTreeModel)tree.getModel()).nodeStructureChanged(node);
                }
            }
        }
    }
}
package cn.ieclipse.smartim.views;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Jamling on 2017/11/1.
 */
public class IMContactDoubleClicker extends MouseAdapter {
    protected IMPanel imPanel;

    public IMContactDoubleClicker(IMPanel imPanel) {
        this.imPanel = imPanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JTree tree = (JTree) e.getSource();
        int selRow = tree.getRowForLocation(e.getX(), e.getY());
        TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        if (selRow != -1 && e.getClickCount() == 2 && selPath != null) {
            Object selectedNode = selPath.getLastPathComponent();
            if (imPanel != null) {
                imPanel.onDoubleClick(((DefaultMutableTreeNode) selectedNode).getUserObject());
            }
        }
    }
}

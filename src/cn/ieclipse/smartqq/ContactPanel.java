package cn.ieclipse.smartqq;

import com.intellij.ui.treeStructure.Tree;
import com.scienjus.smartqq.client.SmartClient;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by Jamling on 2017/7/11.
 */
public class ContactPanel {
    private JPanel panel;
    private ContactTabHost tabHost;
    private Tree recentTree;
    private Tree friendTree;
    private Tree groupTree;
    private Tree discussTree;

    private ContactTreeMode recentModel = new ContactTreeMode("recents");
    private ContactTreeMode friendModel = new ContactTreeMode("friends");
    private ContactTreeMode groupModel = new ContactTreeMode("groups");
    private ContactTreeMode discussModel = new ContactTreeMode("discusses");

    public JPanel getPanel() {
        return panel;
    }

    public void init() {
        recentTree.setModel(recentModel);
        friendTree.setModel(friendModel);
        groupTree.setModel(groupModel);
        discussTree.setModel(discussModel);
        init(null, recentTree, friendTree, groupTree, discussTree);
    }

    public void update(SmartClient client) {
        recentModel = new ContactTreeMode("recents");
        friendModel = new ContactTreeMode("friends");

        recentModel.update(client);
        friendModel.update(client);
        groupModel.update(client);
        discussModel.update(client);

        recentTree.setModel(recentModel);
        friendTree.setModel(friendModel);
        groupTree.setModel(groupModel);
        discussTree.setModel(discussModel);

        update(client, recentTree, friendTree, groupTree, discussTree);
    }

    private void update(SmartClient client, Tree... trees) {
        for (Tree tree : trees) {
            if (tree.getCellRenderer() instanceof ContactTreeCellRenderer) {
                ((ContactTreeCellRenderer) tree.getCellRenderer()).setClient(client);
            }
            tree.updateUI();
        }
    }

    private void init(SmartClient client, Tree... trees) {
        for (Tree tree : trees) {
            tree.setCellRenderer(new ContactTreeCellRenderer(null));
            tree.setRootVisible(false);
            tree.setAutoscrolls(true);
            tree.addMouseListener(treeClick);
        }
    }

    private MouseAdapter treeClick = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            JTree tree = (JTree) e.getSource();
            int selRow = tree.getRowForLocation(e.getX(), e.getY());
            TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
            if (selRow != -1 && e.getClickCount() == 2 && selPath != null) {
                Object selectedNode = selPath.getLastPathComponent();
                if (callback != null) {
                    callback.open(((DefaultMutableTreeNode) selectedNode).getUserObject());
                }
            }
        }
    };
    Callback callback;

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void open(Object o);
    }
}

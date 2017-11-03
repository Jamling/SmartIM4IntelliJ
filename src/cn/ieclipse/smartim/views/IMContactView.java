package cn.ieclipse.smartim.views;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.callback.SendCallback;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.tree.WideSelectionTreeUI;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/11.
 */
public abstract class IMContactView {

    protected IMPanel imPanel;

    protected ReceiveCallback receiveCallback;
    protected SendCallback sendCallback;
    protected ReceiveCallback robotCallback;
    protected ModificationCallback modificationCallback;

    public IMContactView(IMPanel imPanel) {
        this.imPanel = imPanel;
    }

    public abstract JPanel getPanel();

    public IMPanel getImPanel() {
        return imPanel;
    }

    protected void initTrees(Tree... trees) {
        for (Tree tree : trees) {
            if (tree != null) {
                initTree(tree);
            }
        }
    }

    protected void initTree(Tree tree) {
        tree.setCellRenderer(new ContactTreeCellRenderer());
        tree.setShowsRootHandles(false);
        tree.setRootVisible(false);
        tree.addMouseListener(new IMContactDoubleClicker(getImPanel()));
        tree.setUI(new WideSelectionTreeUI() {

        });
    }

    public void updateTrees(Tree... trees) {
        for (Tree tree : trees) {
            if (tree != null) {
                tree.updateUI();
            }
        }
    }

    public void initContacts() {
        new Thread() {
            public void run() {
                doLoadContacts();
            }
        }.start();
    }

    protected abstract SmartClient getClient();

    protected abstract void doLoadContacts();

    protected abstract void onLoadContacts(boolean success);

    protected void notifyLoadContacts(final boolean success) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                onLoadContacts(success);
            }
        });
    }

    public void notifyUpdateContacts(final int index, boolean force) {
        boolean notify = true;
        if (notify || force) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    doUpdateContacts(index);
                }
            });
        }
    }

    protected void doUpdateContacts(final int index) {

    }
}

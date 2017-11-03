package cn.ieclipse.smartim.views;

import javax.swing.tree.DefaultTreeModel;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTreeMode extends DefaultTreeModel {
    private ContactTreeNode root;

    public ContactTreeMode(ContactTreeNode root) {
        super(root);
        this.root = root;
    }

    @Override
    public ContactTreeNode getRoot() {
        return root;
    }

    public void updateChildren() {
        getRoot().update();
    }
}

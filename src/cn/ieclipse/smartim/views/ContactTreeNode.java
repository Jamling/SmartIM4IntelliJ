package cn.ieclipse.smartim.views;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Jamling on 2017/7/12.
 */
public abstract class ContactTreeNode extends DefaultMutableTreeNode {
    protected boolean check;
    protected String name;
    protected IMPanel imPanel;

    public ContactTreeNode(boolean check, String name, IMPanel imPanel) {
        super(new DefaultMutableTreeNode(name));
        this.name = name;
        this.check = check;
        this.imPanel = imPanel;
    }

    public void update() {
    }
}

package cn.ieclipse.smartim.views;

/**
 * Created by Jamling on 2017/7/12.
 */
public abstract class ContactTreeNode extends CheckBoxTreeNode {
    protected String name;
    protected IMPanel imPanel;

    public ContactTreeNode(Object userObject) {
        super(userObject);
    }

    public ContactTreeNode(boolean check, String name, IMPanel imPanel) {
        super(new CheckBoxTreeNode(name));
        this.name = name;
        this.imPanel = imPanel;
    }

    public void update() {
    }
}

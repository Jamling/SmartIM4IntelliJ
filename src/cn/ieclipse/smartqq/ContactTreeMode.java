package cn.ieclipse.smartqq;

import com.scienjus.smartqq.client.SmartClient;

import javax.swing.tree.DefaultTreeModel;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTreeMode extends DefaultTreeModel {

    public ContactTreeMode(String name) {
        super(new ContactTreeNode(name));
    }

    public void update(SmartClient client) {
        ((ContactTreeNode) root).update(client);
    }
}

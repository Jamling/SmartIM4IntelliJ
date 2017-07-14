package cn.ieclipse.smartqq;

import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.*;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

/**
 * Created by Jamling on 2017/7/12.
 */
public class ContactTreeNode extends DefaultMutableTreeNode {

    private SmartClient client;
    private String name;

    public ContactTreeNode(String name) {
        super(new DefaultMutableTreeNode(name));
        this.name = name;
    }

    public void update(SmartClient client) {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        root.removeAllChildren();
        if ("recents".equals(name)) {
            List<Recent> list = client.getRecentList();
            if (list != null) {
                for (Recent r : list) {
                    Object target = client.getRecentTarget(r);
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(target);
                    root.add(cn);
                }
            }
        } else if ("friends".equals(name)) {
            List<Category> categories = client.getFriendListWithCategory();
            if (categories != null) {
                for (Category c : categories) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(c);
                    root.add(cn);
                    for (Friend f : c.getFriends()) {
                        DefaultMutableTreeNode fn = new DefaultMutableTreeNode(f);
                        cn.add(fn);
                    }
                }
            }
        } else if ("groups".equals(name)) {
            List<Group> list = client.getGroupList();
            if (list != null) {
                for (Group r : list) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(r);
                    root.add(cn);
                }
            }
        } else if ("discusses".equals(name)) {
            List<Discuss> list = client.getDiscussList();
            if (list != null) {
                for (Discuss r : list) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(r);
                    root.add(cn);
                }
            }
        }
    }
}

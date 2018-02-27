package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.views.ContactTreeNode;
import cn.ieclipse.smartim.views.IMPanel;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.*;

import java.util.Collections;
import java.util.List;

/**
 * Created by Jamling on 2017/11/1.
 */
public class QQContactTreeNode extends ContactTreeNode {

    public QQContactTreeNode(Object userObject) {
        super(userObject);
    }

    public QQContactTreeNode(boolean check, String name, IMPanel imPanel) {
        super(check, name, imPanel);
    }

    public void update() {
        SmartQQClient client = (SmartQQClient) imPanel.getClient();
        QQContactTreeNode root = (QQContactTreeNode) getRoot();
        root.removeAllChildren();
        if ("recent".equals(name)) {
            List<QQContact> list = client.getRecents2();
            if (list != null) {
                synchronized(this) {
                    Collections.sort(list);
                }
                for (QQContact target : list) {
                    QQContactTreeNode cn = new QQContactTreeNode(target);
                    root.add(cn);
                }
            }
        } else if ("friend".equals(name)) {
            List<Category> categories = client.getFriendListWithCategory();
            if (categories != null) {
                for (Category c : categories) {
                    QQContactTreeNode cn = new QQContactTreeNode(c);
                    root.add(cn);
                    for (Friend f : c.getFriends()) {
                        QQContactTreeNode fn = new QQContactTreeNode(f);
                        cn.add(fn);
                    }
                }
            }
        } else if ("group".equals(name)) {
            List<Group> list = client.getGroupList();
            if (list != null) {
                for (Group r : list) {
                    QQContactTreeNode cn = new QQContactTreeNode(r);
                    root.add(cn);
                }
            }
        } else if ("discuss".equals(name)) {
            List<Discuss> list = client.getDiscussList();
            if (list != null) {
                for (Discuss r : list) {
                    QQContactTreeNode cn = new QQContactTreeNode(r);
                    root.add(cn);
                }
            }
        }
    }
}

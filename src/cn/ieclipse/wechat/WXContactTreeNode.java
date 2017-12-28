package cn.ieclipse.wechat;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.VirtualCategory;
import cn.ieclipse.smartim.views.ContactTreeNode;
import cn.ieclipse.smartim.views.IMPanel;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Jamling on 2017/11/1.
 */
public class WXContactTreeNode extends ContactTreeNode {
    public WXContactTreeNode(boolean check, String name, IMPanel imPanel) {
        super(check, name, imPanel);
    }

    @Override
    public void update() {
        WechatClient client = (WechatClient) imPanel.getClient();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getRoot();
        root.removeAllChildren();
        if ("recent".equals(name)) {
            List<Contact> list = client.getRecentList();
            if (list != null) {
                synchronized (this) {
                    Collections.sort(list);
                }
                for (Contact target : list) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(
                            target);
                    root.add(cn);
                }
            }
        } else if ("friend".equals(name)) {
            List<VirtualCategory<Contact>> categories = getContactGroup(
                    client.getMemberList());
            if (categories != null) {
                categories.add(0, new VirtualCategory<>("groups", client.getGroupList()));
                for (VirtualCategory<Contact> c : categories) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(c);
                    root.add(cn);
                    if (c.list != null) {
                        for (Contact f : c.list) {
                            DefaultMutableTreeNode fn = new DefaultMutableTreeNode(
                                    f);
                            cn.add(fn);
                        }
                    }
                }
            }
        } else if ("group".equals(name)) {
            List<Contact> list = client.getGroupList();
            if (list != null) {
                for (Contact r : list) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(r);
                    root.add(cn);
                }
            }
        } else if ("public".equals(name)) {
            List<Contact> list = client.getPublicUsersList();
            if (list != null) {
                for (Contact r : list) {
                    DefaultMutableTreeNode cn = new DefaultMutableTreeNode(r);
                    root.add(cn);
                }
            }
        }
    }

    public List<VirtualCategory<Contact>> getContactGroup(List<Contact> list) {
        List<VirtualCategory<Contact>> cates = new ArrayList<>();
        if (!IMUtils.isEmpty(list)) {
            List<Contact> unA = new ArrayList<>();
            TreeMap<String, List<Contact>> maps = new TreeMap<>();
            for (Contact c : list) {
                String py = c.getPYInitial();
                char A = IMUtils.isEmpty(py) ? '#' : py.charAt(0);
                if (A >= 'A' && A <= 'Z' || A >= 'a' && A <= 'z') {
                    String a = String.valueOf(A).toUpperCase();
                    List<Contact> values = maps.get(a);
                    if (values == null) {
                        values = new ArrayList<>();
                        maps.put(a, values);
                    }
                    values.add(c);
                } else {
                    unA.add(c);
                }
            }
            for (String n : maps.keySet()) {
                cates.add(new VirtualCategory<>(n, maps.get(n)));
            }
            if (!IMUtils.isEmpty(unA)) {
                cates.add(new VirtualCategory<>("#", unA));
            }
        }
        return cates;
    }
}

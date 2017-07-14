package cn.ieclipse.smartqq;

import com.intellij.ui.ColoredTreeCellRenderer;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.Recent;
import icons.SmartIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Created by Jamling on 2017/7/13.
 */
public class ContactTreeCellRenderer extends ColoredTreeCellRenderer {

    SmartClient client;

    public ContactTreeCellRenderer(SmartClient client) {
        this.client = client;
    }

    public void setClient(SmartClient client) {
        this.client = client;
    }

    @Override
    public void customizeCellRenderer(@NotNull JTree jTree, Object o, boolean b, boolean b1, boolean b2, int i, boolean b3) {
        Object obj = o instanceof DefaultMutableTreeNode ? ((DefaultMutableTreeNode) o).getUserObject() : o;
        setIcon(getDisplayIcon(obj));
        String name = (getDisplayName(obj));
        append(name == null ? "" : name);
    }

    public Icon getDisplayIcon(Object obj) {
        if (obj instanceof Recent) {
            Recent r = (Recent) obj;
            if (r.getType() == 0) {
                return SmartIcons.friend;
            } else if (r.getType() == 1) {
                return SmartIcons.group;
            } else if (r.getType() == 2) {
                return SmartIcons.discuss;
            }
        } else if (obj instanceof Group) {
            return SmartIcons.group;
        } else if (obj instanceof Discuss) {
            return SmartIcons.discuss;
        }
        return SmartIcons.friend;
    }

    public String getDisplayName(Object obj) {
        if (obj instanceof Recent && this.client != null && !this.client.isClose()) {
            Recent r = (Recent) obj;
            if (r.getType() == 0) {
                Friend f = client.getFriend(r.getUin());
                if (f != null) {
                    return client.getName(f);
                }
            } else if (r.getType() == 1) {
                Group g = client.getGroup(r.getUin());
                if (g != null) {
                    return g.getName();
                }
            } else if (r.getType() == 2) {
                Discuss d = client.getDiscuss(r.getUin());
                if (d != null) {
                    return d.getName();
                }
            }
        }
        return SmartClient.getName(obj);
    }
}

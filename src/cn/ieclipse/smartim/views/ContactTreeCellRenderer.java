package cn.ieclipse.smartim.views;

import cn.ieclipse.smartim.model.IContact;
import com.intellij.ui.ColoredTreeCellRenderer;
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

    public ContactTreeCellRenderer() {
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
        if (obj == null) {
            return null;
        }
        if (obj instanceof IContact) {
            return ((IContact) obj).getName();
        }
        return obj.toString();
    }
}

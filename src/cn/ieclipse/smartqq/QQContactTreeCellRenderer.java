package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.views.ContactTreeCellRenderer;
import cn.ieclipse.wechat.WXUtils;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.Recent;
import icons.SmartIcons;
import io.github.biezhi.wechat.model.Contact;

import javax.swing.*;

/**
 * Created by Jamling on 2019/2/27.
 */
public class QQContactTreeCellRenderer extends ContactTreeCellRenderer {

    @Override
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
        return super.getDisplayIcon(obj);
    }

    @Override
    public String getDisplayName(Object obj) {
        if (obj != null && obj instanceof Contact) {
            return WXUtils.getPureName(((Contact) obj).getName());
        }
        return super.getDisplayName(obj);
    }
}

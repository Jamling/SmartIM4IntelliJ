package cn.ieclipse.wechat;

import cn.ieclipse.smartim.views.ContactTreeCellRenderer;
import icons.SmartIcons;
import io.github.biezhi.wechat.model.Contact;

import javax.swing.*;

/**
 * Created by Jamling on 2019/2/27.
 */
public class WXContactTreeCellRenderer extends ContactTreeCellRenderer {

    @Override
    public Icon getDisplayIcon(Object obj) {
        if (obj != null && obj instanceof Contact) {
            if (((Contact) obj).isGroup()) {
                return SmartIcons.group;
            }
            else if (((Contact) obj).is3rdApp()) {
                return SmartIcons.app3rd;
            }
            else if (((Contact) obj).isPublic()) {
                return SmartIcons.subscriber;
            }
            else if (((Contact) obj).isSpecial()) {
                return SmartIcons.wechat;
            }
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

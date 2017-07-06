package cn.ieclipse.smartqq;

import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jamling on 2017/7/3.
 */
public class FriendListCellRenderer extends DefaultListCellRenderer {

    SmartClient client;

    public FriendListCellRenderer(SmartClient client) {
        super();
        this.client = client;
    }

    public FriendListCellRenderer() {
        super();
    }

    public void setClient(SmartClient client) {
        this.client = client;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object obj, int index, boolean isSelected, boolean cellHasFocus) {
        Component component = super.getListCellRendererComponent(list, obj, index, isSelected, cellHasFocus);
        JLabel label = (JLabel) component;
        label.setText(getDisplayName(obj));
        return component;
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

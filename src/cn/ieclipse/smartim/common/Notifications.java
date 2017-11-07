package cn.ieclipse.smartim.common;

import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.ui.popup.Balloon;

/**
 * Created by Jamling on 2017/10/31.
 */
public class Notifications {
    public static void notify(final String title, final CharSequence text) {
        com.intellij.notification.Notifications.Bus.register("SmartIM", NotificationDisplayType.BALLOON);
        Notification n = new Notification("SmartIM", title, text.toString(), NotificationType.INFORMATION);
        com.intellij.notification.Notifications.Bus.notify(n);
    }

    public static void notify(final IMPanel contactView,
                              final IContact target, final String title,
                              final CharSequence text) {
        com.intellij.notification.Notifications.Bus.register("SmartIM", NotificationDisplayType.BALLOON);
        Notification n = new Notification("SmartIM", title, text.toString(), NotificationType.INFORMATION);
        com.intellij.notification.Notifications.Bus.notify(n);
    }
}

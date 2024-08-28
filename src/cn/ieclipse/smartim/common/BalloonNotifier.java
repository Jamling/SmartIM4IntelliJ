package cn.ieclipse.smartim.common;

import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.util.StringUtils;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.ProjectManager;

/**
 * Created by Jamling on 2017/10/31.
 */
public class BalloonNotifier {
    private static final NotificationGroup NOTIFICATION_GROUP =
            NotificationGroupManager.getInstance().getNotificationGroup(IMWindowFactory.TOOL_WINDOW_ID);
    public static void notify(final String title, final CharSequence text) {
        String content = StringUtils.isEmpty(text) ? "" : text.toString();
        Notification n = NOTIFICATION_GROUP.createNotification(title, content, NotificationType.INFORMATION);
        n.notify(ProjectManager.getInstance().getDefaultProject());
    }

    public static void notify(final IMPanel contactView, final IContact target, final String title,
        final CharSequence text) {
        notify(title, text);
    }

    public static void error(String error) {
        Notification n = NOTIFICATION_GROUP.createNotification(IMWindowFactory.TOOL_WINDOW_ID, error, NotificationType.ERROR);
        n.notify(ProjectManager.getInstance().getDefaultProject());
    }
}

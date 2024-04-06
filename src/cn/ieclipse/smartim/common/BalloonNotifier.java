package cn.ieclipse.smartim.common;

import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.util.StringUtils;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.ProjectManager;

/**
 * Created by Jamling on 2017/10/31.
 */
public class BalloonNotifier {
    private static final NotificationGroup NOTIFICATION_GROUP =
            new NotificationGroup("SmartIM",
                    NotificationDisplayType.BALLOON, true);
    public static void notify(final String title, final CharSequence text) {
        NOTIFICATION_GROUP.createNotification(title, StringUtils.isEmpty(text) ? "" : text.toString(), NotificationType.INFORMATION, null)
                .notify(ProjectManager.getInstance().getDefaultProject());
    }

    public static void notify(final IMPanel contactView, final IContact target, final String title,
        final CharSequence text) {
        notify(title, text);
    }
}

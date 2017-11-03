package cn.ieclipse.smartim.common;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

/**
 * Created by Jamling on 2017/7/13.
 */
public class LOG {
    public static Logger LOG = Logger.getLogger("SmartQQ");

    public static void trace(Object message) {
        LOG.trace(message);
    }

    public static void trace(Object message, Throwable t) {
        LOG.trace(message, t);
    }

    public static boolean isTraceEnabled() {
        return LOG.isTraceEnabled();
    }

    public static void debug(Object message) {
        LOG.debug(message);
    }

    public static void debug(Object message, Throwable t) {
        LOG.debug(message, t);
    }

    public static void error(Object message) {
        LOG.error(message);
    }

    public static void error(Object message, Throwable t) {
        LOG.error(message, t);
    }

    public static void fatal(Object message) {
        LOG.fatal(message);
    }

    public static void fatal(Object message, Throwable t) {
        LOG.fatal(message, t);
    }

    public static void info(Object message) {
        LOG.info(message);
    }

    public static void info(Object message, Throwable t) {
        LOG.info(message, t);
    }

    public static boolean isDebugEnabled() {
        return LOG.isDebugEnabled();
    }

    public static Level getLevel() {
        return LOG.getLevel();
    }

    public static boolean isInfoEnabled() {
        return LOG.isInfoEnabled();
    }

    public static void log(Priority priority, Object message, Throwable t) {
        LOG.log(priority, message, t);
    }

    public static void log(Priority priority, Object message) {
        LOG.log(priority, message);
    }

    public static void log(String callerFQCN, Priority level, Object message, Throwable t) {
        LOG.log(callerFQCN, level, message, t);
    }

    public static void warn(Object message) {
        LOG.warn(message);
    }

    public static void warn(Object message, Throwable t) {
        LOG.warn(message, t);
    }

    public static void sendNotification(String title, String content) {
        Notification n = new Notification("smart", title, content, NotificationType.WARNING);
        Notifications.Bus.notify(n);
    }
}

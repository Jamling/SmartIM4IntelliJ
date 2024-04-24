package cn.ieclipse.smartim.common;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by Jamling on 2017/7/13.
 */
public class LOG {
    public static Logger LOG = Logger.getInstance("SmartIM");

    public static void trace(String message) {
        LOG.trace(message);
    }

    public static void trace(Object message, Throwable t) {
        LOG.trace(t);
    }

    public static boolean isTraceEnabled() {
        return LOG.isTraceEnabled();
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void debug(String message, Throwable t) {
        LOG.debug(message, t);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void error(String message, Throwable t) {
        LOG.error(message, t);
    }

    public static void fatal(String message) {
        LOG.error(message);
    }

    public static void fatal(String message, Throwable t) {
        LOG.error(message, t);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void info(String message, Throwable t) {
        LOG.info(message, t);
    }

    public static boolean isDebugEnabled() {
        return LOG.isDebugEnabled();
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void warn(String message, Throwable t) {
        LOG.warn(message, t);
    }

    public static void sendNotification(String title, String content) {
        BalloonNotifier.notify(title, content);
    }
}

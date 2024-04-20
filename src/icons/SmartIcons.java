package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class SmartIcons {
    private static Icon getIcon(String path) {
        return IconLoader.findIcon(path, SmartIcons.class);
    }
    public static Icon signin = getIcon("/icons/sign-in.svg");
    public static Icon signout = getIcon("/icons/sign-out.png");
    public static Icon test = getIcon("/icons/gitlab.png");
    public static Icon close = getIcon("/icons/close.svg");
    public static Icon show = getIcon("/icons/eye.svg");
    public static Icon hide = getIcon("/icons/eye-slash.svg");
    public static Icon broadcast = getIcon("/icons/broadcast.svg");
    public static Icon settings = AllIcons.General.Settings;

    public static Icon group = getIcon("/icons/user-circle.svg");
    public static Icon friend = getIcon("/icons/user.svg");
    public static Icon discuss = getIcon("/icons/user-o.svg");
    public static Icon subscriber = getIcon("/icons/subscriber.svg");
    public static Icon app3rd = getIcon("/icons/app.svg");
    public static Icon wechat = getIcon("/icons/wechat.svg");

    public static Icon file = AllIcons.FileTypes.Any_type;//getIcon("/icons/File.png");
    public static Icon projectFile = getIcon("/icons/folder.svg");
    public static Icon image = getIcon("/icons/image.svg");
    public static Icon face = getIcon("/icons/face.png");
    public static Icon lock = AllIcons.RunConfigurations.Scroll_down;
    public static Icon clear = getIcon("/icons/clear_co.png");

    public static void main(String[] args) {
        System.out.println(group);
    }
}

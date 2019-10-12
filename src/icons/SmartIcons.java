package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class SmartIcons {
    public static Icon signin = IconLoader.getIcon("/icons/sign-in.svg");
    public static Icon signout = IconLoader.getIcon("/icons/sign-out.png");
    public static Icon test = IconLoader.getIcon("/icons/gitlab.png");
    public static Icon close = IconLoader.getIcon("/icons/close.svg");
    public static Icon show = IconLoader.getIcon("/icons/eye.svg");
    public static Icon hide = IconLoader.getIcon("/icons/eye-slash.svg");
    public static Icon broadcast = IconLoader.getIcon("/icons/broadcast.svg");
    public static Icon settings = AllIcons.General.Settings;

    public static Icon group = IconLoader.getIcon("/icons/user-circle.svg");
    public static Icon friend = IconLoader.getIcon("/icons/user.svg");
    public static Icon discuss = IconLoader.getIcon("/icons/user-o.svg");
    public static Icon subscriber = IconLoader.getIcon("/icons/subscriber.svg");
    public static Icon app3rd = IconLoader.getIcon("/icons/app.svg");
    public static Icon wechat = IconLoader.getIcon("/icons/wechat.svg");

    public static Icon file = AllIcons.FileTypes.Any_type;//IconLoader.getIcon("/icons/File.png");
    public static Icon projectFile = IconLoader.getIcon("/icons/folder.svg");
    public static Icon image = IconLoader.getIcon("/icons/image.svg");
    public static Icon face = IconLoader.getIcon("/icons/face.png");
    public static Icon lock = AllIcons.RunConfigurations.Scroll_down;
    public static Icon clear = IconLoader.getIcon("/icons/clear_co.png");

    public static void main(String[] args) {
        System.out.println(group);
    }
}

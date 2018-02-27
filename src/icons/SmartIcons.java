package icons;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Created by Jamling on 2017/7/12.
 */
public class SmartIcons {
    public static Icon signin = IconLoader.getIcon("/icons/sign-in.png");
    public static Icon signout = IconLoader.getIcon("/icons/sign-out.png");
    public static Icon test = IconLoader.getIcon("/icons/gitlab.png");
    public static Icon close = IconLoader.getIcon("/icons/close.png");
    public static Icon show = IconLoader.getIcon("/icons/eye.png");
    public static Icon hide = IconLoader.getIcon("/icons/eye-slash.png");
    public static Icon broadcast = IconLoader.getIcon("/icons/broadcast.png");
    public static Icon settings = AllIcons.General.Settings;


    public static Icon group = IconLoader.getIcon("/icons/user-circle.png");
    public static Icon friend = IconLoader.getIcon("/icons/user.png");
    public static Icon discuss = IconLoader.getIcon("/icons/user-o.png");

    public static Icon file = AllIcons.FileTypes.Any_type;//IconLoader.getIcon("/icons/File.png");
    public static Icon projectFile = AllIcons.Toolbar.Folders;//IconLoader.getIcon("/icons/File.png");
    public static Icon image = IconLoader.getIcon("/icons/image.png");
    public static Icon face = IconLoader.getIcon("/icons/face.png");
    public static Icon lock = AllIcons.RunConfigurations.Scroll_down;
    public static Icon clear = IconLoader.getIcon("/icons/clear_co.png");

    public static void main(String[] args) {
        System.out.println(group);
    }
}

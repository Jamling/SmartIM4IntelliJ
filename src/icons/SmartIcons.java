package icons;

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


    public static Icon group = IconLoader.getIcon("/icons/user-circle.png");
    public static Icon friend = IconLoader.getIcon("/icons/user.png");
    public static Icon discuss = IconLoader.getIcon("/icons/user-o.png");

    public static void main(String[] args) {
        System.out.println(group);
    }
}

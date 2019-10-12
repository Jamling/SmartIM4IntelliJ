package cn.ieclipse.smartim.settings;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Jamling on 2018/2/24.
 */
public class RobotPanel {
    private JCheckBox chkRobot;
    private JTextField textRobotName;
    private JComboBox comboRobot;
    private JTextField textApiKey;
    private JTextField textWelcome;
    private JCheckBox chkGroupAny;
    private JCheckBox chkFriendAny;
    private JTextField textReplyEmpty;
    private JPanel panel;
    private SmartIMSettings settings;

    public RobotPanel(SmartIMSettings settings) {
        this.settings = settings;
    }

    public Component createComponent() {
        return panel;
    }

    public boolean isModified() {
        return settings.getState().ROBOT_TYPE != comboRobot.getSelectedIndex()
            || settings.getState().ROBOT_ENABLE != chkRobot.isSelected()
            || settings.getState().ROBOT_FRIEND_ANY != chkFriendAny.isSelected()
            || settings.getState().ROBOT_GROUP_ANY != chkGroupAny.isSelected() || !settings.getState().ROBOT_KEY
            .equals(textApiKey.getText().trim()) || !settings.getState().ROBOT_REPLY_EMPTY
            .equals(textReplyEmpty.getText().trim()) || !settings.getState().ROBOT_NAME
            .equals(textRobotName.getText().trim()) || !settings.getState().ROBOT_GROUP_WELCOME
            .equals(textWelcome.getText().trim());
    }

    public void reset() {
        int idx = settings.getState().ROBOT_TYPE;
        if (idx >= 0 && idx < comboRobot.getItemCount()) {
            comboRobot.setSelectedIndex(idx);
        }
        chkRobot.setSelected(settings.getState().ROBOT_ENABLE);
        chkFriendAny.setSelected(settings.getState().ROBOT_FRIEND_ANY);
        chkGroupAny.setSelected(settings.getState().ROBOT_GROUP_ANY);
        textApiKey.setText(settings.getState().ROBOT_KEY);
        textReplyEmpty.setText(settings.getState().ROBOT_REPLY_EMPTY);
        textRobotName.setText(settings.getState().ROBOT_NAME);
        textWelcome.setText(settings.getState().ROBOT_GROUP_WELCOME);
    }

    public void apply() {
        settings.getState().ROBOT_TYPE = comboRobot.getSelectedIndex();
        settings.getState().ROBOT_ENABLE = chkRobot.isSelected();
        settings.getState().ROBOT_FRIEND_ANY = chkFriendAny.isSelected();
        settings.getState().ROBOT_GROUP_ANY = chkGroupAny.isSelected();
        settings.getState().ROBOT_KEY = textApiKey.getText().trim();
        settings.getState().ROBOT_REPLY_EMPTY = textReplyEmpty.getText().trim();
        settings.getState().ROBOT_NAME = textRobotName.getText().trim();
        settings.getState().ROBOT_GROUP_WELCOME = textWelcome.getText().trim();
    }
}

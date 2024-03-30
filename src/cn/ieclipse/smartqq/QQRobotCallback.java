package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.IFrom;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.robot.TuringRobot.TuringRequestV2Builder;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.util.StringUtils;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.*;

import java.util.Map;

public class QQRobotCallback extends IMRobotCallback {

    private QQChatConsole console;
    private SmartQQPanel fContactView;

    public QQRobotCallback(SmartQQPanel fContactView) {
        this.fContactView = fContactView;
    }

    @Override public void onContactChanged(IContact contact) {
        if (!isEnable()) {
            return;
        }
        try {
            console = (QQChatConsole)fContactView.findConsoleById(contact.getUin(), false);
            // TODO
        } catch (Exception e) {
            LOG.error("机器人回应异常", e);
        }
    }

    @Override public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (!isEnable()) {
            return;
        }
        try {
            console = (QQChatConsole)fContactView.findConsoleById(from.getContact().getUin(), false);
            answer(from, (QQMessage)message);
        } catch (Exception e) {
            LOG.error("机器人回应异常", e);
        }
    }

    @Override public void onReceiveError(Throwable e) {
        // TODO Auto-generated method stub

    }

    public void answer(AbstractFrom from, QQMessage m) {
        String robotName = getRobotName();
        SmartQQClient client = fContactView.getClient();
        // auto reply friend
        if (from instanceof FriendFrom) {
            if (SmartIMSettings.getInstance().getState().ROBOT_FRIEND_ANY) {
                String reply = getReply(m.getContent(), (QQContact)from.getContact(), null);
                if (reply != null) {
                    String input = robotName + reply;
                    if (console == null) {
                        client.sendMessageToFriend(m.getUserId(), input);
                    } else {
                        console.send(input);
                    }
                }
            }
            return;
        } else if (from instanceof GroupFrom) {
            GroupFrom gf = (GroupFrom)from;
            String gName = gf.getGroup().getName();
            // QQContact qqContact = client.getGroup(gf.getGroup().getId());
            if (from.isNewbie()) {
                String welcome = SmartIMSettings.getInstance().getState().ROBOT_GROUP_WELCOME;
                if (welcome != null && !welcome.isEmpty()) {
                    GroupInfo info = gf.getGroup();
                    GroupUser gu = gf.getGroupUser();
                    String input = welcome;
                    if (gu != null) {
                        input = input.replace("{user}", gu.getName());
                    }
                    if (info != null) {
                        input = input.replace("{memo}", info.getMemo());
                    }
                    if (console != null) {
                        console.send(robotName + input);
                    } else {
                        client.sendMessageToGroup(gf.getGroup().getId(), robotName + input);
                    }
                }
            }

            // @
            if (atMe(from, m)) {
                String msg = m.getContent(true).trim();
                String reply = getReply(msg, from.getMember(), gName);
                if (reply != null) {
                    String input = robotName + "@" + from.getMember().getName() + SEP + reply;
                    if (console != null) {
                        console.send(input);
                    } else {
                        if (m instanceof GroupMessage) {
                            client.sendMessageToGroup(((GroupMessage)m).getGroupId(), input);
                        } else if (m instanceof DiscussMessage) {
                            client.sendMessageToDiscuss(((DiscussMessage)m).getDiscussId(), input);
                        }
                    }
                }
                return;
            }
            // replay any
            if (SmartIMSettings.getInstance().getState().ROBOT_GROUP_ANY) {
                if (from.isNewbie() || isMySend(m.getUserId())) {
                    return;
                }
                if (console == null) {
                    return;
                }

                if (from instanceof FriendFrom) {
                    return;
                } else if (from instanceof GroupFrom) {
                    if (((GroupFrom)from).getGroupUser().isUnknown()) {
                        return;
                    }
                } else if (from instanceof DiscussFrom) {
                    if (((DiscussFrom)from).getDiscussUser().isUnknown()) {
                        return;
                    }
                }

                String reply = getReply(m.getContent(), from.getMember(), gName);
                if (reply != null) {
                    String input = robotName + "@" + from.getName() + SEP + reply;
                    if (console != null) {
                        console.send(input);
                    }
                }
            }
        } // end group

    }

    private Map<String, Object> getParams(String text, IContact contact, String groupId) {
        String key = getTuringApiKey();
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        TuringRequestV2Builder builder = new TuringRequestV2Builder(key);
        builder.setText(text);
        String uid = encodeUid(contact.getName());
        String uname = contact.getName();
        String gid = groupId == null ? null : encodeUid(groupId);
        builder.setUserInfo(uid, uname, gid);
        // builder.setLocation(contact, contact.Province, null);
        return builder.build();
    }

    private String getReply(String text, IContact contact, String groupId) {
        if (StringUtils.isEmpty(text)) {
            String reply = SmartIMSettings.getInstance().getState().ROBOT_REPLY_EMPTY;
            if (!StringUtils.isEmpty(reply)) {
                return reply;
            }
            return null;
        }
        try {
            return getRobot().getRobotAnswer(text, contact, groupId);
        } catch (Exception e) {
            LOG.error("机器人回复失败", e);
        }
        return null;
    }

    private boolean atMe(IFrom from, QQMessage m) {
        if (m.getAts() != null) {
            if (m instanceof GroupMessage) {
                GroupInfo info = ((GroupFrom)from).getGroup();
                UserInfo me = getAccount();
                long uin = Long.parseLong(me.getUin());
                GroupUser me2 = info.getGroupUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            } else if (m instanceof DiscussMessage) {
                DiscussInfo info = ((DiscussFrom)from).getDiscuss();
                UserInfo me = getAccount();
                long uin = Long.parseLong(me.getUin());
                DiscussUser me2 = info.getDiscussUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            }
        }
        return false;
    }

    private UserInfo getAccount() {
        UserInfo me = getClient().getAccount();
        return me;
    }

    private SmartQQClient getClient() {
        return fContactView.getClient();
    }

    private boolean isMySend(long uin) {
        UserInfo me = getAccount();
        if (me != null && me.getUin().equals(String.valueOf(uin))) {
            return true;
        }
        return false;
    }
}

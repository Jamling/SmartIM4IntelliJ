package cn.ieclipse.wechat;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.util.StringUtils;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.*;

public class WXRobotCallback extends IMRobotCallback {

    private WXChatConsole console;
    private WechatClient client;
    private WechatPanel fContactView;

    public WXRobotCallback(WechatPanel fContactView) {
        this.fContactView = fContactView;
        initRobot();
    }

    @Override public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (!isEnable()) {
            return;
        }
        if (from.isOut()) {
            return;
        }
        client = getClient();
        if (!client.isLogin()) {
            return;
        }
        try {
            WechatMessage m = (WechatMessage)message;
            if (Const.API_SPECIAL_USER.contains(m.FromUserName)) {
                return;
            }
            if (m.MsgType == WechatMessage.MSGTYPE_TEXT) {
                console = (WXChatConsole)fContactView.findConsoleById(from.getContact().getUin(), false);
                answer(from, m);
            } else if (m.MsgType == WechatMessage.MSGTYPE_SYS) {
                if (!StringUtils.isEmpty(m.Content) && from instanceof GroupFrom) {
                    String c = m.Content;
                    if (c.contains("加入了群聊")) {
                        String n = c.replaceFirst(".*\"(.+)\".*", "$1");
                        n = WXUtils.getPureName(n);
                        GroupFrom gf = (GroupFrom)from;
                        String robotName = getRobotName();
                        String welcome = getGroupWelcome();
                        if (welcome != null && !welcome.isEmpty() && gf != null) {
                            String input = welcome;
                            input = input.replace("{memo}", "");
                            input = input.replace("{user}", "@" + n + " ");
                            if (console != null) {
                                console.send(robotName + input);
                            } else {
                                send(from, robotName + input);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("机器人回应异常", e);
        }
    }

    @Override public void onContactChanged(IContact contact) {
        // TODO Auto-generated method stub

    }

    @Override public void onReceiveError(Throwable e) {
        // TODO Auto-generated method stub

    }

    public void answer(AbstractFrom from, WechatMessage m) {
        String robotName = getRobotName();
        // auto reply friend
        if (from instanceof UserFrom) {
            if (isReplyFriend()) {
                Contact contact = (Contact)from.getContact();
                if (contact.isSpecial() || contact.is3rdApp()) {
                    return;
                }
                String reply = getReply(m.getText().toString(), contact, null);
                if (reply != null) {
                    String input = robotName + reply;
                    send(from, input);
                }
            }
        } else if (from instanceof GroupFrom) {
            GroupFrom gf = (GroupFrom)from;
            // newbie
            if (from.isNewbie()) {
                String welcome = getGroupWelcome();
                if (welcome != null && !welcome.isEmpty() && gf != null) {
                    String input = welcome;
                    if (gf.getMember() != null) {
                        input = input.replace("{memo}", "");
                        input = input.replace("{user}", WXUtils.getPureName(gf.getMember().getName()));
                    }
                    input = robotName + input;
                    send(from, input);
                }
            } // end newbie
            // @
            String name = getMyGroupName(from, m);
            String text = m.getText().toString();
            if (text.contains("@" + name)) {
                text = text.replace("@" + name, "");
                String reply = getReply(text, gf.getMember(), gf.getName());
                if (reply != null) {
                    String input = robotName + "@" + from.getMember().getName() + SEP + reply;
                    send(from, input);
                }
                return;
            } // end @
            // replay any
            if (isReplyAnyGroupMember()) {
                if (from.isNewbie() || isMySend(m.FromUserName)) {
                    return;
                }
                if (console == null) {
                    return;
                }

                if (gf.getMember().isUnknown()) {
                    return;
                }

                String reply = getReply(text, gf.getMember(), gf.getName());
                if (reply != null) {
                    String input = robotName + "@" + from.getName() + SEP + reply;
                    send(null, input);
                }
            } // end any
        } // end group
    }

    private String getMyGroupName(AbstractFrom from, WechatMessage m) {
        Contact c = (Contact)from.getContact();
        Contact gu = c.getMember(getAccount().getUin());
        String name;
        if (gu != null) {
            name = gu.getName();
        } else {
            name = getAccount().getName();
        }
        return name;
    }

    private String getReply(String text, Contact contact, String groupId) {
        if (StringUtils.isEmpty(text)) {
            String reply = emptyReply();
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

    private boolean atMe(AbstractFrom from, WechatMessage m) {
        String text = (String)m.getText();
        Contact c = (Contact)from.getContact();
        Contact gu = c.getMember(getAccount().getUin());
        String name;
        if (gu != null) {
            name = gu.getName();
        } else {
            name = getAccount().getName();
        }
        if (text.contains("@" + name)) {
            return true;
        }
        return false;
    }

    private Contact getAccount() {
        IContact me = client.getAccount();
        return (Contact)me;
    }

    private void send(AbstractFrom from, String message) {
        if (console != null) {
            console.send(message);
        } else if (from != null) {
            WechatMessage msg = client.createMessage(0, message, from.getContact());
            client.sendMessage(msg, from.getContact());
            String name = getAccount().getName();
            String log = WXUtils.formatHtmlOutgoing(name, message, true);
            IMHistoryManager.getInstance()
                .save(client.getWorkDir(IMHistoryManager.HISTORY_NAME), from.getContact().getUin(), log);
        }
    }

    private WechatClient getClient() {
        return (WechatClient)IMClientFactory.getInstance().getWechatClient();
    }

    private boolean isMySend(String uin) {
        Contact me = getAccount();
        if (me != null && me.getUin().equals(uin)) {
            return true;
        }
        return false;
    }
}

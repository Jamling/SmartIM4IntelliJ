package cn.ieclipse.smartim.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jamling on 2017/11/6.
 */
@State(name = "SmartIMSettings", storages = {@Storage(file = "smartim.xml")}) public class SmartIMSettings
    implements PersistentStateComponent<SmartIMSettings.State> {

    private State myState = new State();

    @Nullable @Override

    public State getState() {
        return myState;
    }

    @Override public void loadState(State state) {
        this.myState = state;
    }

    //    public static SmartIMSettings getInstance(Project project) {
    //        instance = ServiceManager.getService(project, SmartIMSettings.class);
    //        return instance;
    //    }

    public static SmartIMSettings getInstance() {
        if (instance == null) {
            instance = ServiceManager.getService(SmartIMSettings.class);
            if (instance == null) {
                instance = new SmartIMSettings();
            }
        }
        return instance;
    }

    private static SmartIMSettings instance;

    public static final String MSG_CSS_DFT =
        "/*仅支持css 1.0规范*/\n" + "/*主属性*/\n" + "body {\n" + "    font-size: 14px; /*文字大小*/\n" + "    text-align: left;\n"
            + "    overflow-x: hidden;\n" + "}\n" + "\n"
            + "/*聊天记录格式<div><span class=\"sender|my\"><span class=\"time\">HH:mm:ss</span> <a href=\"user://sender\">sender</a>: </span><span class=\"content\">Content</span></div>*/\n"
            + ".sender { /*发送人*/\n" + "    display: inline;\n" + "    float: left;\n" + "}\n" + "\n"
            + ".my { /*发送人为自己*/\n" + "    font-size: 1em;\n" + "    font-style: italic;\n" + "    float: left;\n"
            + "}\n" + "\n" + ".content { /* 消息内容 */\n" + "    display: inline-block;\n" + "    white-space: pre-wrap;\n"
            + "    padding-left: 4px;\n" + "}\n" + "\n" + "\n" + "div.error {\n" + "    color: red;\n" + "}\n" + "\n"
            + "img {\n" + "    max-width: 100%;\n" + "    display: block;\n" + "}\n" + "\n" + "br {\n"
            + "    height: 1px;\n" + "    line-height: 1px;\n" + "    min-height: 1px;\n" + "}";

    public static class State {
        public String KEY_SEND = "Enter";
        public boolean SHOW_SEND = false;
        public boolean NOTIFY_MSG = true;
        public boolean NOTIFY_GROUP_MSG = false;
        public boolean NOTIFY_UNREAD = true;

        public boolean NOTIFY_UNKNOWN = false;
        public boolean HIDE_MY_INPUT = true;
        public boolean LOG_HISTORY = true;

        public boolean ROBOT_ENABLE = false;
        public String ROBOT_NAME = "";
        public int ROBOT_TYPE = 0;
        public String ROBOT_KEY = "";
        public String ROBOT_GROUP_WELCOME = "欢迎{user} {memo}";
        public boolean ROBOT_GROUP_ANY = false;
        public boolean ROBOT_FRIEND_ANY = false;
        public String ROBOT_REPLY_EMPTY = "";

        public boolean QN_ENABLE = false;
        public String QN_BUCKET = "";
        public int QN_ZONE = 0;
        public String QN_DOMAIN = "";
        public String QN_AK = "";
        public String QN_SK = "";
        public boolean QN_TS = false;
        public String MSG_CSS = SmartIMSettings.MSG_CSS_DFT;
    }
}

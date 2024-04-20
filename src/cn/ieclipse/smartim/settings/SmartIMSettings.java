package cn.ieclipse.smartim.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jamling on 2017/11/6.
 */
@State(name = "SmartIMSettings", storages = {@Storage("smartim.xml")}) public class SmartIMSettings
    implements PersistentStateComponent<SmartIMSettings.State> {

    private State myState = new State();

    @Nullable @Override

    public State getState() {
        return myState;
    }

    @Override public void loadState(@NotNull State state) {
        this.myState = state;
    }

    public static SmartIMSettings getInstance() {
        if (instance == null) {
            synchronized (SmartIMSettings.class) {
                if (instance == null) {
                    instance = ApplicationManager.getApplication().getService(SmartIMSettings.class);
                }
            }
        }
        return instance;
    }

    private static volatile SmartIMSettings instance;

    public static final String MSG_CSS_DFT =
            """
                    /*仅支持css 1.0规范*/
                    /*主属性*/
                    body {
                        font-size: 14px; /*文字大小*/
                        text-align: left;
                        overflow-x: hidden;
                    }

                    /*聊天记录格式<div><span class="sender|my"><span class="time">HH:mm:ss</span> <a href="user://sender">sender</a>: </span><span class="content">Content</span></div>*/
                    .sender { /*发送人*/
                        display: inline;
                        float: left;
                    }

                    .my { /*发送人为自己*/
                        font-size: 1em;
                        font-style: italic;
                        float: left;
                    }

                    .content { /* 消息内容 */
                        display: inline-block;
                        white-space: pre-wrap;
                        padding-left: 4px;
                    }


                    div.error {
                        color: red;
                    }

                    img {
                        max-width: 100%;
                        display: block;
                    }

                    br {
                        height: 1px;
                        line-height: 1px;
                        min-height: 1px;
                    }""";

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
        public String ROBOT_OPENAI_KEY = "";
        public String ROBOT_OPENAI_EXTRA = "{\n\t\"model\":\"text-davinci-03\"}";
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

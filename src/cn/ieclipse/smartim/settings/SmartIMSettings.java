package cn.ieclipse.smartim.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Jamling on 2017/11/6.
 */
@State(name = "SmartIMSettings", storages = {@Storage("smartim.xml")}) public class SmartIMSettings
    implements PersistentStateComponent<SmartIMSettings.State> {

    private State myState = new State();

    @Override
    public State getState() {
        if (myState == null) {
            myState = new State();
        }
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
    }
}

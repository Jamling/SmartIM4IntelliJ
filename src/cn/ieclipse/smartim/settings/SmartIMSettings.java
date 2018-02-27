package cn.ieclipse.smartim.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Jamling on 2017/11/6.
 */
@State(
        name = "SmartIMSettings",
        storages = {
                @Storage(file = "smartim.xml")
        }
)
public class SmartIMSettings implements PersistentStateComponent<SmartIMSettings.State> {

    private State myState = new State();

    @Nullable
    @Override

    public State getState() {
        return myState;
    }

    @Override
    public void loadState(State state) {
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

    public static class State {
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
    }
}

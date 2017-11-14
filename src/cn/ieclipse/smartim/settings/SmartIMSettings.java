package cn.ieclipse.smartim.settings;

import com.intellij.openapi.components.*;
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
    }
}

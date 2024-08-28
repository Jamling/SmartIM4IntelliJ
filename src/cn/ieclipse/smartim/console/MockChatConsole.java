package cn.ieclipse.smartim.console;

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;

/**
 * Created by Jamling on 2018/1/3.
 */
public class MockChatConsole extends IMChatConsole {
    public MockChatConsole(IContact target, IMPanel imPanel) {
        super(target, imPanel);
        new Thread(this::loadHistories).start();
    }

    @Override public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
        }
    }

    @Override public void post(String msg) {

    }

    @Override public SmartClient getClient() {
        return null;
    }

    @Override public void send(String input) {
        String name = "我";
        String msg = formatInput(name, input);
        System.out.println(msg);
        if (!hideMyInput()) {
            insertDocument(msg);
            IMHistoryManager.getInstance().save(getHistoryDir(), getHistoryFile(), msg);
        }
    }

    @Override public void sendWithoutPost(String msg, boolean raw) {
        if (!hideMyInput()) {
            String name = "me";
            insertDocument(raw ? msg : formatInput(name, msg));
            IMHistoryManager.getInstance().save(getHistoryDir(), getHistoryFile(), msg);
        }
    }

    public void initMockMsg() {

    }
}

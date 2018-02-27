package cn.ieclipse.smartim.console;

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
    }

    @Override
    public void loadHistory(String raw) {

    }

    @Override
    public void post(String msg) {

    }

    @Override
    public SmartClient getClient() {
        return null;
    }

    @Override
    public void loadHistories() {

    }

    @Override
    public void send(String input) {
        String msg = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), "Me",
                input);
        if (!hideMyInput()) {
            insertDocument(msg);
        }
        String msg2 = IMUtils.formatHtmlMsg(false, false,
                System.currentTimeMillis(), "Me", input);
        write(msg2);
    }
}

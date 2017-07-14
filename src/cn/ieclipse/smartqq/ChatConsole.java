package cn.ieclipse.smartqq;

import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jamling on 2017/7/1.
 */
public class ChatConsole extends SimpleToolWindowPanel {

    private SmartClient client;
    private static final String ENTER_KEY = "\n";
    private Object target;

    public ChatConsole(SmartClient client, Object target) {
        super(false, false);
        this.client = client;
        this.target = target;

        initUI();
    }

    public void send(String input) {
        if (target instanceof Friend) {
            try {
                String me = "我";
                if (client.getAccountInfo() != null) {
                    me = client.getAccountInfo().getNick();
                }
                String msg = String.format("%s %s: %s", new SimpleDateFormat("HH:mm:ss").format(new Date()), me, input);
                historyWidget.getDocument().insertString(historyWidget.getDocument().getLength(), msg + ENTER_KEY, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        post(input);
    }

    public void post(final String msg) {
        if (target == null || client == null) {
            return;
        }
        if (target instanceof Friend) {
            client.sendMessageToFriend(((Friend) target).getUserId(),
                    msg);
        } else if (target instanceof Group) {
            client.sendMessageToGroup(((Group) target).getId(),
                    msg);
        } else if (target instanceof Discuss) {
            client.sendMessageToDiscuss(((Discuss) target).getId(),
                    msg);
        }
    }


    private void createUIComponents() {

    }

    public void write(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    historyWidget.getDocument().insertString(historyWidget.getDocument().getLength(), msg + ENTER_KEY, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    JBSplitter splitter;
    ChatHistoryPane top;
    ChatInputPane bottom;
    JEditorPane historyWidget;
    JTextPane inputWidget;
    JButton btnSend;

    public void initUI() {
        top = new ChatHistoryPane();
        bottom = new ChatInputPane();
        historyWidget = top.getEditorPane();
        inputWidget = bottom.getTextPane();
        btnSend = bottom.getBtnSend();
        btnSend.setVisible(false);

        splitter = new JBSplitter(true);
        splitter.setSplitterProportionKey("chat.splitter.key");
        splitter.setFirstComponent(top.getPanel());
        splitter.setSecondComponent(bottom.getPanel());
        setContent(splitter);
        splitter.setPreferredSize(new Dimension(200, 200));
        splitter.setProportion(0.85f);


        inputWidget.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = inputWidget.getText();
                    if (!input.isEmpty()) {
                        inputWidget.setText("");
                        send(input);
                    }
                    e.consume();
                }
            }
        });
    }
}

package cn.ieclipse.smartim.console;

import cn.ieclipse.smartim.AbstractSmartClient;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

/**
 * Created by Jamling on 2017/7/1.
 */
public abstract class IMChatConsole extends SimpleToolWindowPanel {

    public static final String ENTER_KEY = "\n";
    protected IContact contact;
    protected String uin;
    protected IMPanel imPanel;

    public IMChatConsole(IContact target, IMPanel imPanel) {
        super(false, false);
        this.contact = target;
        this.uin = target.getUin();
        this.imPanel = imPanel;
        initUI();
        loadHistories();
    }


    public SmartClient getClient() {
        return imPanel.getClient();
    }

    public abstract void loadHistory(String raw);

    public abstract void post(final String msg);

    public String getHistoryFile() {
        return uin;
    }

    public String getUin() {
        return uin;
    }

    public String trimMsg(String msg) {
        if (msg.endsWith(ENTER_KEY)) {
            return msg;
        }
        return msg + ENTER_KEY;
    }

    public void loadHistories() {
        SmartClient client = getClient();
        if (client != null && client instanceof AbstractSmartClient) {
            List<String> ms = IMHistoryManager.getInstance()
                    .load((AbstractSmartClient) client, getHistoryFile());
            for (String raw : ms) {
                if (!IMUtils.isEmpty(raw)) {
                    try {
                        loadHistory(raw);
                    } catch (Exception e) {
                        error("历史消息记录：" + raw);
                    }
                }
            }
        }
    }

    public void send(final String input) {
        SmartClient client = getClient();
        if (client == null || client.isClose()) {
            error("连接已关闭");
            return;
        }
        if (!client.isLogin()) {
            error("连接已关闭，请重新登录");
            return;
        }
        String name = getClient().getAccount().getName();
        String msg = IMUtils.formatMsg(System.currentTimeMillis(), name, input);
        // if (contact instanceof Friend)
        {
            try {
                historyWidget.getDocument().insertString(historyWidget.getDocument().getLength(), trimMsg(msg), null);
                // historyWidget.setCaretPosition(historyWidget.getDocument().getEndPosition().getOffset());
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        new Thread() {
            @Override
            public void run() {
                post(input);
            }
        }.start();
    }

    public void sendFile(final String file) {
    }

    public void error(Throwable e) {
        error(e == null ? "null" : e.toString());
    }

    public void error(String msg) {
        try {
            historyWidget.getDocument().insertString(
                    historyWidget.getDocument().getLength(),
                    trimMsg(msg), null);
            // historyWidget.setCaretPosition(historyWidget.getDocument().getEndPosition().getOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {

    }

    public void write(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    historyWidget.getDocument().insertString(historyWidget.getDocument().getLength(), trimMsg(msg), null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected JBSplitter splitter;
    protected ChatHistoryPane top;
    protected ChatInputPane bottom;
    protected JEditorPane historyWidget;
    protected JTextPane inputWidget;
    protected JButton btnSend;

    public void initUI() {
        top = new ChatHistoryPane();
        bottom = new ChatInputPane();
        historyWidget = top.getEditorPane();
        inputWidget = bottom.getTextPane();
        btnSend = bottom.getBtnSend();
        btnSend.setVisible(true);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String input = inputWidget.getText();
                if (!input.isEmpty()) {
                    inputWidget.setText("");
                    send(input);
                }
            }
        });

        splitter = new JBSplitter(true);
        splitter.setSplitterProportionKey("chat.splitter.key");
        splitter.setFirstComponent(top.getPanel());
        splitter.setSecondComponent(bottom.getPanel());
        setContent(splitter);
        splitter.setPreferredSize(new Dimension(-1, 200));
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
        initToolBar();
    }

    protected void initToolBar() {
        DefaultActionGroup group = new DefaultActionGroup();
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("SmartQQ", group, false);
        // toolbar.getComponent().addFocusListener(createFocusListener());
        toolbar.setTargetComponent(this);
        setToolbar(toolbar.getComponent());

        initToolBar(group);
    }

    protected void initToolBar(DefaultActionGroup group) {
        AnAction action = new DumbAwareAction("Send", "Send file", AllIcons.FileTypes.Any_type) {
            @Override
            public void actionPerformed(AnActionEvent anActionEvent) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.showDialog(new JLabel(), "选择要发送的文件");
                File f = chooser.getSelectedFile();
                if (f != null) {
                    sendFile(f.getAbsolutePath());
                }
            }
        };
        group.add(action);
    }
}

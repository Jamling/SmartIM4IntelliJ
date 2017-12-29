package cn.ieclipse.smartim.console;

import cn.ieclipse.smartim.AbstractSmartClient;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.WrapHTMLFactory;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.util.BareBonesBrowserLaunch;
import cn.ieclipse.util.StringUtils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;
import com.intellij.ui.ToggleActionButton;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
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

    public boolean hideMyInput() {
        return false;
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
        String msg = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), name, input);
        if (!hideMyInput()) {
            insertDocument(msg);
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

    public void error(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                insertDocument(String.format("<div class=\"error\">%s</div>", msg));
            }
        });
    }

    private void createUIComponents() {

    }

    public void write(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                insertDocument(msg);
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
        btnSend.setVisible(SmartIMSettings.getInstance().getState().SHOW_SEND);
        btnSend.addActionListener(new SendAction());
        inputWidget.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Send");
        inputWidget.getActionMap().put("Send", btnSend.getAction());

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
        initHistoryWidget();
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
                if (!enableUpload()) {
                    return;
                }
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

        AnAction scroll = new ToggleActionButton("Auto Scroll", AllIcons.RunConfigurations.Scroll_down) {

            @Override
            public boolean isSelected(AnActionEvent anActionEvent) {
                return !scrollLock;
            }

            @Override
            public void setSelected(AnActionEvent anActionEvent, boolean b) {
                scrollLock = !b;
            }
        };
        group.add(scroll);
    }

    public class SendAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent e) {
            String input = inputWidget.getText();
            if (!input.isEmpty()) {
                inputWidget.setText("");
                send(input);
            }
        }
    }

    protected boolean scrollLock = false;
    protected boolean uploadLock = false;

    protected boolean enableUpload() {
        return !uploadLock;
    }

    protected void initHistoryWidget() {
        HTMLEditorKit kit = new HTMLEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new WrapHTMLFactory();
            }
        };
        StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {text-align: left;}");
        styleSheet.addRule("div.my {font-size: 1.2rem; font-style: italic;}");
        styleSheet.addRule("div.error {color: red;}");
        styleSheet.addRule("img {max-width: 100%;}");
        try {
            styleSheet.importStyleSheet(
                    new URL("http://dl.ieclipse.cn/r/smartim.css"));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
        String initText = String.format(
                "<html><head></head><body>%s</body></html>", "欢迎使用SmartIM");
        historyWidget.setEditorKit(kit);
        historyWidget.setDocument(doc);
        // historyWidget.setText(initText);
        historyWidget.setEditable(false);
        historyWidget.setBackground(null);
        historyWidget.addHyperlinkListener(new HyperlinkListener() {

            @Override
            public void hyperlinkUpdate(HyperlinkEvent e) {
                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    String desc = e.getDescription();
                    if (!StringUtils.isEmpty(desc)) {
                        hyperlinkActivated(desc);
                    }
                }
            }
        });
    }

    protected boolean hyperlinkActivated(String desc) {
        if (desc.startsWith("user://")) {
            String user = desc.substring(7);
            try {
                inputWidget.getDocument().insertString(inputWidget.getCaretPosition(), "@" + user + " ", null);
            } catch (Exception e) {

            }
        } else if (desc.startsWith("code://")) {
            String code = desc.substring(7);
            int pos = code.lastIndexOf(':');
            String file = code.substring(0, pos);
            int line = Integer.parseInt(code.substring(pos + 1).trim());
            if (line > 0) {
                line--;
            }
            // TODO open file in editor and located to line
        } else {
            BareBonesBrowserLaunch.openURL(desc);
        }
        return false;
    }

    protected void insertDocument(String msg) {
        try {
            HTMLEditorKit kit = (HTMLEditorKit) historyWidget.getEditorKit();
            HTMLDocument doc = (HTMLDocument) historyWidget.getDocument();
            // historyWidget.getDocument().insertString(len - offset,
            // trimMsg(msg), null);
            // Element root = doc.getDefaultRootElement();
            // Element body = root.getElement(1);
            // doc.insertBeforeEnd(body, msg);
            int pos = historyWidget.getCaretPosition();
            kit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            historyWidget.setCaretPosition(scrollLock ? pos : doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

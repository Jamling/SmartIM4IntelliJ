package cn.ieclipse.smartim.console;

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.*;
import cn.ieclipse.smartim.common.*;
import cn.ieclipse.smartim.idea.EditorUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.smartim.settings.StyleConfPanel;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.util.BareBonesBrowserLaunch;
import cn.ieclipse.util.EncodeUtils;
import cn.ieclipse.util.StringUtils;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBSplitter;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.JTextComponent;
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
        new Thread() {
            public void run() {
                loadHistories();
            }
        }.start();
    }

    public SmartClient getClient() {
        return imPanel.getClient();
    }

    public abstract void loadHistory(String raw);

    public abstract void post(final String msg);

    public File getHistoryDir() {
        if (getClient() != null) {
            return getClient().getWorkDir(IMHistoryManager.HISTORY_NAME);
        }
        File dir = IMWindowFactory.getDefault().getWorkDir().getAbsoluteFile();
        return new File(dir, IMHistoryManager.HISTORY_NAME);
    }

    public String getHistoryFile() {
        return EncodeUtils.getMd5(contact.getName());
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
        if (client != null) {
            List<String> ms = IMHistoryManager.getInstance().load(getHistoryDir(),
                    getHistoryFile());
            int size = ms.size();
            for (int i=0; i < size; i++) {
                String raw = ms.get(i);
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

    public void clearHistories() {
        IMHistoryManager.getInstance().clear(getHistoryDir(), getHistoryFile());
        historyWidget.setText("");
    }

    public void clearUnread() {
        if (contact != null && contact instanceof AbstractContact) {
            ((AbstractContact) contact).clearUnRead();
            imPanel.notifyUpdateContacts(0, true);
        }
    }

    public boolean hideMyInput() {
        return false;
    }

    public boolean checkClient(SmartClient client) {
        if (client == null || client.isClose()) {
            error("连接已关闭");
            return false;
        }
        if (!client.isLogin()) {
            error("请先登录");
            return false;
        }
        return true;
    }

    public void send(final String input) {
        SmartClient client = getClient();
        if (!checkClient(client)) {
            return;
        }
        String name = client.getAccount().getName();
        String msg = formatInput(name, input);
        if (!hideMyInput()) {
            insertDocument(msg);
            IMHistoryManager.getInstance().save(getHistoryDir(), getHistoryFile(), msg);
        }
        new Thread() {
            @Override
            public void run() {
                post(input);
            }
        }.start();
    }

    public void sendWithoutPost(final String msg, boolean raw) {
        if (!hideMyInput()) {
            String name = getClient().getAccount().getName();
            insertDocument(raw ? msg : formatInput(name, msg));
            IMHistoryManager.getInstance().save(getHistoryDir(), getHistoryFile(), msg);
        }
    }

    public void sendFile(final String file) {
        new Thread() {
            public void run() {
                uploadLock = true;
                try {
                    sendFileInternal(file);
                } catch (Exception e) {
                    LOG.error("发送文件失败 : " + e);
                    LOG.sendNotification("发送文件失败",
                            String.format("文件：%s(%s)", file, e.getMessage()));
                    error(String.format("发送文件失败：%s(%s)", file, e.getMessage()));
                } finally {
                    uploadLock = false;
                }
            }
        }.start();
    }

    protected void sendFileInternal(final String file) throws Exception{

    }
    protected String encodeInput(String input) {
        return StringUtils.encodeXml(input);
    }
    // 组装成我输入的历史记录，并显示在聊天窗口中
    protected String formatInput(String name, String msg) {
        return IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), name,
                msg);
    }

    public void error(Throwable e) {
        error(e == null ? "null" : e.toString());
    }

    public void error(final String msg) {
        insertDocument(String.format("<div class=\"error\">%s</div>", msg));
    }

    private void createUIComponents() {

    }

    public void write(final String msg) {
        insertDocument(msg);
    }

    protected JBSplitter splitter;
    protected ChatHistoryPane top;
    protected ChatInputPane bottom;
    protected JEditorPane historyWidget;
    protected JTextComponent inputWidget;
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
                if (SmartIMSettings.getInstance().getState().KEY_SEND.equals(SwingUtils.key2string(e))) {
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
        group.add(new SendImageAction(this));
        group.add(new SendFileAction(this));
        // group.add(new SendProjectFileAction(this));
        group.add(new SendProjectFileAction2(this));
        group.add(new ScrollLockAction(this));
        group.add(new ClearHistoryAction(this));
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

    public boolean enableUpload() {
        return !uploadLock;
    }

    public void setScrollLock(boolean scrollLock) {
        this.scrollLock = scrollLock;
    }

    public boolean isScrollLock() {
        return scrollLock;
    }

    protected void initHistoryWidget() {
        HTMLEditorKit kit = new HTMLEditorKit() {
            @Override
            public ViewFactory getViewFactory() {
                return new WrapHTMLFactory();
            }
        };
        final StyleSheet styleSheet = kit.getStyleSheet();
        styleSheet.addRule("body {text-align: left; overflow-x: hidden;}");
        styleSheet.addRule(
                ".my {font-size: 1 em; font-style: italic; float: left;}");
        styleSheet.addRule("div.error {color: red;}");
        styleSheet.addRule("img {max-width: 100%; display: block;}");
        styleSheet.addRule(".sender {display: inline; float: left;}");
        styleSheet.addRule(
                ".content {display: inline-block; white-space: pre-wrap; padding-left: 4px;}");
        styleSheet.addRule(
                ".br {height: 1px; line-height: 1px; min-height: 1px;}");
        RestUtils.loadStyleAsync(styleSheet);
        File f = StyleConfPanel.getCssFile();
        try {
            if (f.exists()) {
                URL url = f.toURI().toURL();
                styleSheet.importStyleSheet(url);
            } else {
                LOG.error("$idea.config.path not exists, smartim will use default css");
            }
        } catch (Exception e) {
            LOG.error("加载SmartIM消息CSS失败", e);
        }
        HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
        String initText = String.format(
                "<html><head></head><body><div class=\"welcome\">%s</div></body></html>", imPanel.getWelcome());
        historyWidget.setContentType("text/html");
        historyWidget.setEditorKit(kit);
        historyWidget.setDocument(doc);
        historyWidget.setText(initText);
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
            EditorUtils.openFile(file, line);
        } else {
            BareBonesBrowserLaunch.openURL(desc);
        }
        return false;
    }

    protected void insertDocument(final String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
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
        });
    }
}

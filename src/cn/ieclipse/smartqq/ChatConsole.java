package cn.ieclipse.smartqq;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectLocator;
import com.intellij.openapi.project.ProjectManager;
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
public class ChatConsole {
    private JPanel panel;
    private JTextArea textArea1;
    private JEditorPane editorPane1;
    private JScrollPane top;
    private JScrollPane bottom;
    private JSplitPane splitPane;

    private SmartClient client;
    private static final String ENTER_KEY = "\n";
    private Object target;

    public ChatConsole(SmartClient client, Object target) {
        this.client = client;
        this.target = target;

        textArea1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = textArea1.getText();
                    if (!input.isEmpty()) {
                        send(input);
                        textArea1.setText("");
                    }
                    e.consume();
                }
            }
        });
    }

    public void send(String input) {
        try {
            String msg = String.format("%s me: %s", new SimpleDateFormat("HH:mm:ss").format(new Date()), input);
            editorPane1.getDocument().insertString(editorPane1.getDocument().getLength(), msg + ENTER_KEY, null);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        post(input);
    }

    public void post(final String msg) {
        if (target == null) {
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

    public JPanel getPanel() {
        return panel;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        Dimension minimumSize = new Dimension(100, 50);
        bottom.setMaximumSize(minimumSize);
    }

    public void write(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    editorPane1.getDocument().insertString(editorPane1.getDocument().getLength(), msg + ENTER_KEY, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initUI() {
        if (splitPane != null) {
            double h = splitPane.getPreferredSize().getHeight();
            double ih = textArea1.getMinimumSize().getHeight();
            splitPane.setDividerLocation((int)(h - ih - splitPane.getDividerSize()));
            splitPane.updateUI();
        }
    }
}

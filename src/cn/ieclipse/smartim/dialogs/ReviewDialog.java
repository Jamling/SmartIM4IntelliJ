package cn.ieclipse.smartim.dialogs;

import cn.ieclipse.smartim.IMWindowFactory;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.smartqq.SmartQQPanel;
import com.intellij.openapi.actionSystem.ActionGroupUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.ex.ActionUtil;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.ui.content.Content;
import com.scienjus.smartqq.model.Friend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReviewDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField text;
    private JTextArea styledText;
    private JPanel targetPanel;
    private JCheckBox openGerritReviewCheckBox;

    public ReviewDialog() {
        setContentPane(contentPane);
        setModal(true);
        setTitle("Code Review");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        updateTarget();
    }

    private void onOK() {
// add your code here
        send();
        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }

    private void updateTarget() {
        targetPanel.setLayout(new GridLayout(0, 1, 0, 0));
        if (IMWindowFactory.getDefault() == null || IMWindowFactory.getDefault().getProject() == null) {
            return;
        }
        ToolWindow window = ToolWindowManager.getInstance(IMWindowFactory.getDefault().getProject()).getToolWindow(IMWindowFactory.TOOL_WINDOW_ID);
        if (window != null) {
            Content[] contents = window.getContentManager().getContents();
            if (contents != null) {
                for (Content content : contents) {
                    if (content.getComponent() != null && content.getComponent() instanceof IMPanel) {
                        IMPanel panel = (IMPanel) content.getComponent();
                        List<IMChatConsole> chats = panel.getConsoleList();
                        if (!chats.isEmpty()) {
                            consoles.addAll(chats);
                            targetPanel.add(new GroupPanel(content.getDisplayName(), chats));
                        }
                    }
                }
            }
        }
    }

    private List<IMChatConsole> consoles = new ArrayList<>();

    private void send() {
        String msg = String.format("%s(Reviews: %s)", text.getText(),
                styledText.getText());
        for (IMChatConsole console : consoles) {
            console.send(msg);
        }
        if (openGerritReviewCheckBox.isSelected()) {
            AnAction action = ActionManager.getInstance().getActionOrStub("");

        }
    }

    public void setData(String path, int line, String text) {
        String msg = String.format("Code: %s:%s ", path, (line + 1));
        this.text.setText(msg);
        this.styledText.setText(text);
    }

    public static class GroupPanel extends JPanel {
        private JPanel panel;
        private JLabel label;

        /**
         * Create the panel.
         */
        public GroupPanel(String name, List<IMChatConsole> chats) {
            setLayout(new BorderLayout(0, 0));

            label = new JLabel(name);
            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    panel.setVisible(!panel.isVisible());
                }
            });
            add(label, BorderLayout.NORTH);

            panel = new JPanel();
            FlowLayout flowLayout = (FlowLayout) panel.getLayout();
            flowLayout.setAlignment(FlowLayout.LEFT);
            add(panel, BorderLayout.CENTER);

            if (chats != null) {
                for (IMChatConsole chat : chats) {
                    panel.add(new JCheckBox(chat.getName()));
                }
            }
        }
    }

    public static void main(String[] args) {
        ReviewDialog dialog = new ReviewDialog();
        dialog.pack();
        IMChatConsole console = new IMChatConsole(new Friend(), null) {
            @Override
            public void loadHistory(String raw) {

            }

            @Override
            public void post(String msg) {

            }
        };
        console.setName("Target1");
        dialog.targetPanel.add(new GroupPanel("test", Arrays.asList(console, console)));
        dialog.setVisible(true);
        System.exit(0);
    }
}

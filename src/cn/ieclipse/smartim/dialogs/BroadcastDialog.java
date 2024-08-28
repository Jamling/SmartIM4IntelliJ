package cn.ieclipse.smartim.dialogs;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.views.CheckBoxTreeCellRenderer;
import cn.ieclipse.smartim.views.CheckBoxTreeNodeSelectionListener;
import cn.ieclipse.smartim.views.IMPanel;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class BroadcastDialog extends JDialog implements ActionListener {

    private final JPanel contentPanel = new JPanel();
    protected JTextArea text;
    protected JTabbedPane tabHost;
    protected IMPanel imPanel;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            BroadcastDialog dialog = new BroadcastDialog(null);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public BroadcastDialog(IMPanel imPanel) {
        this.imPanel = imPanel;
        setLocationRelativeTo(null);
        setSize(400, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(JBUI.Borders.empty(5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(new BorderLayout(0, 0));
        {
            text = new JTextArea();
            text.setRows(4);
            text.setLineWrap(true);
            JScrollPane scrollPane = new JBScrollPane(text);
            contentPanel.add(scrollPane, BorderLayout.NORTH);
        }
        {
            tabHost = new JBTabbedPane(JBTabbedPane.TOP);
            contentPanel.add(tabHost, BorderLayout.CENTER);
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
                okButton.addActionListener(this);
            }
            {
                JButton cancelButton = new JButton("Cancel");
                cancelButton.setActionCommand("Cancel");
                buttonPane.add(cancelButton);
                cancelButton.addActionListener(this);
            }
        }
        initTab(tabHost);
    }

    protected void initTab(JTabbedPane host) {

    }

    protected void initTrees(JTree... trees) {
        for (JTree tree : trees) {
            if (tree != null) {
                initTree(tree);
            }
        }
    }

    protected void initTree(JTree tree) {
        tree.setCellRenderer(new CheckBoxTreeCellRenderer());
        tree.setShowsRootHandles(false);
        tree.setRootVisible(false);
        tree.addMouseListener(new CheckBoxTreeNodeSelectionListener());
    }

    public void updateTrees(JTree... trees) {
        for (JTree tree : trees) {
            if (tree != null) {
                tree.updateUI();
            }
        }
    }

    @Override public void actionPerformed(ActionEvent e) {
        if ("OK".equals(e.getActionCommand())) {
            okPressed();
        } else if ("Cancel".equals(e.getActionCommand())) {

        }
        dispose();
    }

    protected void okPressed() {
        final String text = this.text.getText().trim();
        if (IMUtils.isEmpty(text)) {
            return;
        }
        final List<Object> targets = new ArrayList<>();
        addTarget(targets);
        new Thread(() -> sendInternal(text, targets)).start();
    }

    protected void addTarget(final List<Object> targets) {

    }

    protected void sendInternal(String text, List<Object> targets) {
    }
}

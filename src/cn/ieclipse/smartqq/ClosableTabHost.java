package cn.ieclipse.smartqq;

import com.intellij.ui.TabbedPaneImpl;
import com.intellij.ui.components.JBLabelDecorator;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jamling on 2017/7/3.
 */
public class ClosableTabHost extends TabbedPaneImpl {
    private Insets insets = new Insets(0, 0, 0, 0);

    public ClosableTabHost() {
        super(JTabbedPane.TOP);
    }

    @NotNull
    @Override
    protected Insets getInsetsForTabComponent() {
        return insets;
    }

    @Override
    public Border getBorder() {
        return BorderFactory.createEmptyBorder();
    }

    @Override
    public void insertTab(String title, Icon icon, Component component, String tip, int index) {
        super.insertTab(title, icon, component, tip, index);
        setTabComponentAt(index, createTab(title));
    }

    public Component createTab(String text) {
        JPanel panel = new JBPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JLabel label = JBLabelDecorator.createJBLabelDecorator(text);
        label.setOpaque(false);
        panel.add(label);
        TabButton btn = new TabButton();
        panel.add(btn);
        panel.setBackground(Color.black);
        panel.setOpaque(false);
        UIUtil.setNotOpaqueRecursively(panel);
        return panel;
    }

    public void bling(int index) {
        if (index >= 0 && index < getTabCount()) {
            Component tab = getTabComponentAt(index);
            if (tab != null) {
                new BlingTimer(tab).start();
            }
        }
    }

    private class BlingTimer extends Timer implements ActionListener {
        Component component;
        JLabel label;
        String src;
        Dimension d;
        int count = 4;

        public BlingTimer(Component tab) {
            super(500, null);
            addActionListener(this);
            component = tab;
            if (component != null && component instanceof JPanel) {
                label = (JLabel) ((JPanel) component).getComponent(0);
                src = label.getText();
                d = label.getSize();
                label.setPreferredSize(d);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                label.setText(count % 2 == 0 ? "" : src);
                count--;
                if (count <= 0) {
                    stop();
                }
            } catch (Exception ex) {

            }
        }
    }

    private class TabButton extends JButton implements ActionListener {
        public TabButton() {
            // super(SmartIcons.close);
            int size = 16;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            setOpaque(false);
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEmptyBorder());
            setBorderPainted(false);
            //Making nice rollover effect
            //we use the same listener for all buttons
            // addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = indexOfTabComponent(getParent());
            if (i != -1) {
                removeTabAt(i);
            }
        }

        //we don't want to update UI for this button
        public void updateUI() {
        }

        //paint the cross
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            //shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(Color.GRAY);
            if (getModel().isRollover()) {
                g2.setColor(Color.LIGHT_GRAY);
            }
            int delta = 5;
            g2.drawLine(delta, delta, getWidth() - delta, getHeight() - delta);
            g2.drawLine(getWidth() - delta, delta, delta, getHeight() - delta);
            g2.dispose();
        }
    }
}

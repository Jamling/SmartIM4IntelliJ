package cn.ieclipse.smartim.settings;

import cn.ieclipse.smartim.common.BalloonNotifier;
import cn.ieclipse.util.IOUtils;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StyleConfPanel {
    private JTextPane textPane;
    private JButton btnRestore;
    JPanel panel;

    StyledDocument doc;
    SimpleAttributeSet commentAttr = new SimpleAttributeSet();
    SimpleAttributeSet labelAttr = new SimpleAttributeSet();
    SimpleAttributeSet valueAttr = new SimpleAttributeSet();
    SmartIMSettings settings;

    public StyleConfPanel(SmartIMSettings settings) {
        this.settings = settings;
        doc = textPane.getStyledDocument();
        StyleConstants.setForeground(commentAttr, JBColor.GRAY);

        StyleConstants.setForeground(labelAttr, JBColor.RED);
        StyleConstants.setForeground(valueAttr, JBColor.BLUE);

        StyleContext sc = StyleContext.getDefaultStyleContext();
        Style style = sc.getStyle(StyleContext.DEFAULT_STYLE);
        StyleConstants.setFontSize(style, 16);
        doc.setLogicalStyle(0, style);

        update(false);

        doc.addDocumentListener(new DocumentListener() {

            @Override public void removeUpdate(DocumentEvent e) {
                modify = true;
                Element ele = doc.getParagraphElement(e.getOffset());
                SwingUtilities.invokeLater(() -> deal(ele.getStartOffset(), ele.getEndOffset()));
            }

            @Override public void insertUpdate(DocumentEvent e) {
                modify = true;
                Element ele = doc.getParagraphElement(e.getOffset());
                SwingUtilities.invokeLater(() -> deal(ele.getStartOffset(), ele.getEndOffset()));

            }

            @Override public void changedUpdate(DocumentEvent e) {
            }
        });
        btnRestore.addActionListener(e -> {
            update(true);
        });
        textPane.setMargin(JBUI.insets(5));
    }

    public StyleConfPanel() {
        this(null);
    }

    boolean modify = false;

    private void parse() {
        Element root = doc.getDefaultRootElement();
        int count = root.getElementCount();
        for (int i = 0; i < count; i++) {
            Element e = root.getElement(i);
            deal(e.getStartOffset(), e.getEndOffset());
        }
    }

    private void deal(int startOffset, int endOffset) {
        try {
            String text = doc.getText(startOffset, endOffset - startOffset);
            if (text == null || "".equals(text)) {
                return;
            }
            if (text.trim().startsWith("/*") && text.trim().endsWith("*/")) {
                doc.setCharacterAttributes(startOffset, endOffset - startOffset, commentAttr, true);
            } else {
                int pos = text.indexOf(":");
                if (pos >= 0) {
                    if (pos > 0) {
                        doc.setCharacterAttributes(startOffset, pos, labelAttr, true);
                    }
                    if (pos + 1 < endOffset) {
                        doc.setCharacterAttributes(startOffset + pos + 1, endOffset - startOffset - pos - 1, valueAttr,
                            true);
                    }
                }
                int p1 = text.indexOf("/*");
                if (p1 >= 0) {
                    int p2 = text.indexOf("*/");
                    if (p2 >= 0) {
                        doc.setCharacterAttributes(startOffset + p1, p2 - p1 + 2, commentAttr, true);
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static String readString(InputStream is, Charset charset) {
        byte[] bytes = IOUtils.read2Byte(is);
        if (bytes != null) {
            if (charset != null) {
                return new String(bytes, charset);
            }
            return new String(bytes, StandardCharsets.UTF_8);
        }
        return null;
    }

    private void update(boolean restore) {
        try {
            if (restore) {
                InputStream is = getClass().getResourceAsStream("/smartim.css");
                textPane.setText(readString(is, null));
            } else {
                File f = getCssFile();
                if (f.exists()) {
                    FileInputStream fis = new FileInputStream(f);
                    textPane.setText(readString(fis, null));
                }
            }
            parse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            saveFile();
        } catch (Exception e) {
            e.printStackTrace();
            BalloonNotifier.notify("保存样式失败", "错误原因：" + e.getMessage());
        }
    }

    public static File getCssFile() {
        String dir = System.getProperty("idea.config.path");
        if (dir == null) {
            dir = System.getProperty("log.home");
        }
        File p = new File(dir);
        if (!p.exists()) {
            p.mkdirs();
        }
        File file = new File(dir, "smartim.css");
        return file;
    }

    private void saveFile() throws Exception {
        File file = getCssFile();
        if (!file.exists()) {
            file.createNewFile();
        }
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        String content = textPane.getText();
        byte[] b = content.getBytes();
        raf.write(b);
        raf.close();
        BalloonNotifier.notify("样式保存成功", "目标位置：" + file.getCanonicalPath());
    }

    public void apply() {
        save();
    }

    public void reset() {

    }

    public boolean isModified() {
        return modify;
    }

    public Component createComponent() {
        return panel;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("StyleConfPanel");
        frame.setContentPane(new StyleConfPanel().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

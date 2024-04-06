package cn.ieclipse.smartim.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class UploadPanel extends JPanel {

    private JCheckBox chkEnable;
    private JComboBox<String> comboZone;
    private JTextField textAccessKey;
    private JTextField textSecretKey;
    private JTextField textBucket;
    private JTextField textDomain;
    private JCheckBox chkTs;

    private SmartIMSettings settings;

    public static final String[][] ZONE_VALUE =
        {{"自动", "autoZone"}, {"华东", "huadong"}, {"华北", "huabei"}, {"华南", "huanan"}, {"北美", "beimei"}};

    public static final String[] ZONE_LABEL = {"自动", "华东", "华北", "华南", "北美"};

    /**
     * Create the panel.
     */
    public UploadPanel(SmartIMSettings settings) {
        this.settings = settings;
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[] {0.0, 1.0};
        gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        setLayout(gridBagLayout);

        JTextArea lblNewLabel = new JTextArea(
            "SmartQQ文件传输详情请参考http://api.ieclipse.cn/smartqq\r\n如果您未设置七牛云储存，发送的文件将上传到本人私有储存空间（temp.ieclipse.cn，文件大小有限制而且非永久保存），建议您注册七牛云来实现文件传输\r\n注：如果使用您自己的七牛云，带*的accessKey和secretKey必填，否则不生效哦");
        lblNewLabel.setLineWrap(true);
        lblNewLabel.setEnabled(false);
        lblNewLabel.setEditable(false);
        GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
        gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
        gbc_lblNewLabel.insets = JBUI.insets(5, 5, 5, 0);
        gbc_lblNewLabel.gridwidth = 2;
        gbc_lblNewLabel.gridx = 0;
        gbc_lblNewLabel.gridy = 0;
        add(lblNewLabel, gbc_lblNewLabel);

        chkEnable = new JCheckBox("启用七牛云存储");
        GridBagConstraints gbc_chkEnable = new GridBagConstraints();
        gbc_chkEnable.anchor = GridBagConstraints.WEST;
        gbc_chkEnable.insets = JBUI.insetsBottom(5);
        gbc_chkEnable.gridwidth = 2;
        gbc_chkEnable.gridx = 0;
        gbc_chkEnable.gridy = 1;
        add(chkEnable, gbc_chkEnable);

        JLabel lblNewLabel_1 = new JLabel("机房");
        GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
        gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_1.insets = JBUI.insets(0, 0, 5, 5);
        gbc_lblNewLabel_1.gridx = 0;
        gbc_lblNewLabel_1.gridy = 2;
        add(lblNewLabel_1, gbc_lblNewLabel_1);

        comboZone = new ComboBox<>(ZONE_LABEL);
        comboZone.setEnabled(false);
        GridBagConstraints gbc_comboZone = new GridBagConstraints();
        gbc_comboZone.insets = JBUI.insetsBottom(5);
        gbc_comboZone.fill = GridBagConstraints.HORIZONTAL;
        gbc_comboZone.gridx = 1;
        gbc_comboZone.gridy = 2;
        add(comboZone, gbc_comboZone);

        JLabel lblNewLabel_2 = new JLabel("AccessKey*");
        GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
        gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_2.insets = JBUI.insets(0, 0, 5, 5);
        gbc_lblNewLabel_2.gridx = 0;
        gbc_lblNewLabel_2.gridy = 3;
        add(lblNewLabel_2, gbc_lblNewLabel_2);

        textAccessKey = new JTextField();
        GridBagConstraints gbc_textAccessKey = new GridBagConstraints();
        gbc_textAccessKey.insets = JBUI.insetsBottom(5);
        gbc_textAccessKey.fill = GridBagConstraints.HORIZONTAL;
        gbc_textAccessKey.gridx = 1;
        gbc_textAccessKey.gridy = 3;
        add(textAccessKey, gbc_textAccessKey);
        textAccessKey.setColumns(10);

        JLabel lblNewLabel_3 = new JLabel("SecretKey*");
        GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
        gbc_lblNewLabel_3.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_3.insets = JBUI.insets(0, 0, 5, 5);
        gbc_lblNewLabel_3.gridx = 0;
        gbc_lblNewLabel_3.gridy = 4;
        add(lblNewLabel_3, gbc_lblNewLabel_3);

        textSecretKey = new JTextField();
        GridBagConstraints gbc_textSecretKey = new GridBagConstraints();
        gbc_textSecretKey.insets = JBUI.insetsBottom(5);
        gbc_textSecretKey.fill = GridBagConstraints.HORIZONTAL;
        gbc_textSecretKey.gridx = 1;
        gbc_textSecretKey.gridy = 4;
        add(textSecretKey, gbc_textSecretKey);
        textSecretKey.setColumns(10);

        JLabel lblNewLabel_4 = new JLabel("存储空间");
        GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
        gbc_lblNewLabel_4.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_4.insets = JBUI.insets(0, 0, 5, 5);
        gbc_lblNewLabel_4.gridx = 0;
        gbc_lblNewLabel_4.gridy = 5;
        add(lblNewLabel_4, gbc_lblNewLabel_4);

        textBucket = new JTextField();
        GridBagConstraints gbc_textZone = new GridBagConstraints();
        gbc_textZone.insets = JBUI.insetsBottom(5);
        gbc_textZone.fill = GridBagConstraints.HORIZONTAL;
        gbc_textZone.gridx = 1;
        gbc_textZone.gridy = 5;
        add(textBucket, gbc_textZone);
        textBucket.setColumns(10);

        JLabel lblNewLabel_5 = new JLabel("空间域名");
        GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
        gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
        gbc_lblNewLabel_5.insets = JBUI.insets(0, 0, 5, 5);
        gbc_lblNewLabel_5.gridx = 0;
        gbc_lblNewLabel_5.gridy = 6;
        add(lblNewLabel_5, gbc_lblNewLabel_5);

        textDomain = new JTextField();
        GridBagConstraints gbc_textDomain = new GridBagConstraints();
        gbc_textDomain.insets = JBUI.insetsBottom(5);
        gbc_textDomain.fill = GridBagConstraints.HORIZONTAL;
        gbc_textDomain.gridx = 1;
        gbc_textDomain.gridy = 6;
        add(textDomain, gbc_textDomain);
        textDomain.setColumns(10);

        chkTs = new JCheckBox("给上传的文件添加时间戳（下载时强制更新缓存）");
        GridBagConstraints gbc_chkTs = new GridBagConstraints();
        gbc_chkTs.anchor = GridBagConstraints.WEST;
        gbc_chkTs.gridwidth = 2;
        gbc_chkTs.gridx = 0;
        gbc_chkTs.gridy = 7;
        add(chkTs, gbc_chkTs);
    }

    public boolean isModified() {
        return settings.getState().QN_ZONE != comboZone.getSelectedIndex() || settings.getState().QN_ENABLE != chkEnable
            .isSelected() || settings.getState().QN_TS != chkTs.isSelected() || !settings.getState().QN_AK
            .equals(textAccessKey.getText().trim()) || !settings.getState().QN_SK.equals(textSecretKey.getText().trim())
            || !settings.getState().QN_BUCKET.equals(textBucket.getText().trim()) || !settings.getState().QN_DOMAIN
            .equals(textDomain.getText().trim());
    }

    public void reset() {
        int idx = settings.getState().QN_ZONE;
        if (idx >= 0 && idx < comboZone.getItemCount()) {
            comboZone.setSelectedIndex(idx);
        }
        chkEnable.setSelected(settings.getState().QN_ENABLE);
        chkTs.setSelected(settings.getState().QN_TS);
        textAccessKey.setText(settings.getState().QN_AK);
        textSecretKey.setText(settings.getState().QN_SK);
        textBucket.setText(settings.getState().QN_BUCKET);
        textDomain.setText(settings.getState().QN_DOMAIN);
    }

    public void apply() {
        settings.getState().QN_ZONE = comboZone.getSelectedIndex();
        settings.getState().QN_ENABLE = chkEnable.isSelected();
        settings.getState().QN_TS = chkTs.isSelected();
        settings.getState().QN_AK = textAccessKey.getText().trim();
        settings.getState().QN_SK = textSecretKey.getText().trim();
        settings.getState().QN_BUCKET = textBucket.getText().trim();
        settings.getState().QN_DOMAIN = textDomain.getText().trim();
    }
}

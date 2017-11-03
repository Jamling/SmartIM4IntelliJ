package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.views.IMPanel;

/**
 * Created by Jamling on 2017/11/1.
 */
public class QQSendCallback extends IMSendCallback {
    public QQSendCallback(IMPanel imPanel) {
        super(imPanel);
    }

    @Override
    protected IMPanel getIMPanel() {
        return (SmartQQPanel) imPanel;
    }
}

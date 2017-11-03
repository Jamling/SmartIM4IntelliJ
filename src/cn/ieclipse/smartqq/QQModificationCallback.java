package cn.ieclipse.smartqq;

import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.model.IContact;
import com.scienjus.smartqq.model.QQContact;

/**
 * Created by Jamling on 2017/11/1.
 */
public class QQModificationCallback implements ModificationCallback {
    protected SmartQQPanel imPanel;

    public QQModificationCallback(SmartQQPanel imPanel) {
        this.imPanel = imPanel;
    }

    @Override
    public void onContactChanged(IContact iContact) {
        if (iContact instanceof QQContact) {
            imPanel.notifyUpdateContacts(0, true);
        }
    }
}

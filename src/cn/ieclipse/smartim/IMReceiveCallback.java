package cn.ieclipse.smartim;

import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.common.LOG;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.settings.SmartIMSettings;
import cn.ieclipse.smartim.views.IMPanel;
import cn.ieclipse.util.EncodeUtils;

public abstract class IMReceiveCallback implements ReceiveCallback {
    protected IMChatConsole lastConsole;
    protected IMPanel fContactView;
    
    public IMReceiveCallback(IMPanel fContactView) {
        this.fContactView = fContactView;
    }
    
    protected abstract String getNotifyContent(AbstractMessage message,
            AbstractFrom from);
            
    protected abstract String getMsgContent(AbstractMessage message,
            AbstractFrom from);
            
    protected void handle(boolean unknown, boolean notify,
            AbstractMessage message, AbstractFrom from,
            AbstractContact contact) {
        SmartClient client = fContactView.getClient();
        String msg = getMsgContent(message, from);
        if (!unknown) {
            String hf = EncodeUtils.getMd5(from.getContact().getName());
            IMHistoryManager.getInstance().save(client, hf, msg);
        }
        
        if (notify) {
            boolean hide = unknown
                    && !SmartIMSettings.getInstance().getState().NOTIFY_UNKNOWN;
            try {
                hide = hide || from.getMember().getUin()
                        .equals(fContactView.getClient().getAccount().getUin());
            } catch (Exception e) {
            }
            if (hide || fContactView.isCurrent(contact)) {
                // don't notify
            }
            else {
                CharSequence content = getNotifyContent(message, from);
                Notifications.notify(fContactView, contact, contact.getName(),
                        content);
            }
        }
        
        IMChatConsole console = fContactView.findConsoleById(contact.getUin(),
                false);
        if (console != null) {
            lastConsole = console;
            console.write(msg);
            fContactView.highlight(console);
        }
        if (!fContactView.isCurrent(console)) {
            if (contact != null) {
                contact.increaceUnRead();
            }
        }
        
        if (contact != null) {
            contact.setLastMessage(message);
        }
        
        fContactView.notifyUpdateContacts(0, false);
    }
    
    @Override
    public void onReceiveError(Throwable e) {
        if (e == null) {
            return;
        }
        if (lastConsole != null) {
            lastConsole.error(e);
        }
        else {
            LOG.error("微信接收异常" + e);
            LOG.sendNotification("错误", e.getMessage());
        }
    }
}

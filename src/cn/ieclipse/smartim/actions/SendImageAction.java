package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.console.IMChatConsole;
import icons.SmartIcons;

public class SendImageAction extends SendFileAction {

    public SendImageAction(IMChatConsole console) {
        super("图片", "发送图片", SmartIcons.image);
        this.filter = filter_image;
        this.console = console;
    }

}

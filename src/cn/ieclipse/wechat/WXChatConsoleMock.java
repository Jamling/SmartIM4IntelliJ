package cn.ieclipse.wechat;

import cn.ieclipse.smartim.console.MockChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMPanel;

public class WXChatConsoleMock extends MockChatConsole {

    public WXChatConsoleMock(IContact target, IMPanel imPanel) {
        super(target, imPanel);
    }
    
    @Override
    public String getHistoryFile() {
        return "wechat.txt";
    }
    
    @Override
    protected String formatInput(String name, String input) {
        return WXUtils.formatHtmlOutgoing(name, input, true);
    }
    
    @Override
    public void initMockMsg() {
        send("中国万岁Abc");
        send("<b>中国万岁Abc</b>");
        send("百度www.baidu.com");
        send("百度www.baidu.com欢迎你");
        send("内事不决问<a href=\"www.baidu.com\">百度</a>外事不决问谷哥");
        String s = "        List<String> groups = new ArrayList<>();\n"
                + "        List<Integer> starts = new ArrayList<>();\n"
                + "        List<Integer> ends = new ArrayList<>();\n" + "        while (m.find()) {\n"
                + "            starts.add(m.start());\n" + "            ends.add(m.end());\n"
                + "            groups.add(m.group());\n" + "        }";
        send(s);
        s = "图片https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo_top_86d58ae1.png你造么";
        send(s);
    }
}

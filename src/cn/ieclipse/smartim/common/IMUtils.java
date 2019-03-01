/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim.common;

import cn.ieclipse.util.FileUtils;
import cn.ieclipse.util.Patterns;
import cn.ieclipse.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2017年8月14日
 */
public class IMUtils {

    /**
     * Get file name from file path
     *
     * @param path file path
     * @return file name
     */
    public static String getName(String path) {
        File f = new File(path);
        String name = f.getName();
        return name;
    }

    public static String formatFileSize(long length) {
        if (length > (1 << 20)) {
            return length / (1 << 20) + "M";
        } else if (length > (1 << 10)) {
            return length / (1 << 10) + "K";
        }
        return length + "B";
    }

    public static boolean isEmpty(CharSequence text) {
        return text == null || text.length() == 0;
    }

    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }


    public static String encodeHtml(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return "";
        } else {
            return StringUtils.encodeXml(msg);
        }
    }

    public static String formatMsg(long time, String name, CharSequence msg) {
        String s1 = new SimpleDateFormat("HH:mm:ss").format(time);
        return String.format("%s %s: %s\n", s1, name, msg);
    }

    public static boolean isMySendMsg(String raw) {
        return raw.startsWith("<div")
                || raw.matches("^\\d{2}:\\d{2}:\\d{2} [.\\s\\S]*");
    }

    public static String formatHtmlMsg(String msg, boolean encodeHtml) {
        // TODO only replace the non-html tag space;
        String m = encodeHtml(msg);
        m = m.replaceAll("\r?\n", "<br/>");
        String content;
        if (encodeHtml) {
            content = autoLink(autoReviewLink(m).replace(" ", "&nbsp;"));
        }
        else {
            content = m.replace(" ", "&nbsp;");
        }
        return content;
    }

    public static String formatHtmlMsg(long time, String name,
                                       CharSequence msg) {
        return formatHtmlMsg(false, true, time, name, msg.toString());
    }

    public static String formatHtmlMyMsg(long time, String name,
                                         CharSequence msg) {
        return formatHtmlMsg(true, true, time, name, msg.toString());
    }

    public static String formatHtmlMsg(boolean my, boolean encodeHtml,
                                       long time, String name, String msg) {
        String t = new SimpleDateFormat("HH:mm:ss").format(time);
        String clz = my ? "my" : "sender";
        String content = formatHtmlMsg(msg, encodeHtml);
        return String.format(DIV_ROW_FORMAT, clz, t, name, name, content);
    }

    public static String autoReviewLink(String input) {
        Matcher m = Pattern.compile(CODE_REGEX, Pattern.MULTILINE)
                .matcher(input);
        if (m.find()) {
            String linkText = m.group().substring(6).trim();
            int s = m.start() + 6;
            int e = s + linkText.length();
            StringBuilder sb = new StringBuilder(m.group());
            sb.delete(s, e);
            String url = String.format("<a href=\"code://%s\">%s</a>", linkText,
                    linkText);
            sb.insert(s, url);
            String reviews = StringUtils.encodeXml(input.substring(m.end()));
            sb.append(reviews);
            return sb.toString();
        }
        return input;
    }

    public static String autoLink(String input) {
        Pattern p = Patterns.WEB_URL;// Pattern.compile(LINK_REGEX, Pattern.MULTILINE);
        Matcher m = p.matcher(input);

        List<String> groups = new ArrayList<>();
        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        while (m.find()) {
            starts.add(m.start());
            ends.add(m.end());
            groups.add(m.group());
        }
        if (!starts.isEmpty()) {
            StringBuilder sb = new StringBuilder(input);
            int offset = 0;
            for (int i = 0; i < starts.size(); i++) {
                int s = starts.get(i);
                int e = ends.get(i);
                String g = groups.get(i);
                String http = null;

                String ucs = "";
                String rg = UCS_REGEX_BEGIN.matcher(g).replaceAll("$2");
                if (g.length() > rg.length()) {
                    ucs = g.substring(0, g.length() - rg.length());
                    g = rg;
                    s = s + ucs.length();
                }

                rg = UCS_REGEX_END.matcher(g).replaceAll("$1");
                if (g.length() > rg.length()) {
                    ucs = g.substring(rg.length());
                    g = rg;
                    e = e - ucs.length();
                }

                if (!PROTOCOL.matcher(g).find()) {
                    boolean f = g.startsWith("www.") || g.endsWith(".com")
                            || g.endsWith(".cn");
                    if (!f) {
                        continue;
                    }
                    else {
                        http = "http://";
                    }
                }

                int pos = offset + s;
                if (pos > 2) {
                    char c = sb.charAt(pos - 1);
                    if (c == '\'' || c == '"') {
                        c = sb.charAt(pos - 2);
                        if (c == '=') {
                            continue;
                        }
                    } else if (c == '>') {
                        continue;
                    }
                }
                sb.delete(offset + s, offset + e);
                String link = http == null ? g : http + g;
                String ng = g;
                if (IMG_EXTS.indexOf(
                        FileUtils.getExtension(g).toLowerCase()) >= 0) {
                    ng = String.format(
                            "<a href=\"%s\"><img src=\"%s\" alt=\"%s\" border=\"0\"/></a>",
                            link, link, "无法预览，请尝试点击");
                }
                else {
                    ng = String.format("<a href=\"%s\">%s</a>", link, g);
                }
                sb.insert(offset + s, ng);
                offset += ng.length() - g.length();
            }
            return sb.toString();
        }
        return input;
    }

    public static final String DIV_SENDER_FORMAT = "<span class=\"%s\"><span class=\"time\">%s</span> <a href=\"user://%s\">%s</a>: </span>";
    public static final String DIV_CONTENT_FORMAT = "<span class=\"content\">%s</span>";
    public static final String DIV_ROW_FORMAT = String.format("<div>%s%s</div>",
            DIV_SENDER_FORMAT, DIV_CONTENT_FORMAT);
    public static final List<String> IMG_EXTS = Arrays.asList("png", "jpg", "gif", "webp");
    public static final String CODE_REGEX = "Code: [\\S ]+:[\\d]+ ?";
    public static final String LINK_REGEX = "(https?|ftp|file)://(([\\w-~]+).)+([\\w-~\\/])+(((?!\\.)(\\S))+(\\.\\w+(\\?(\\w+=\\S&?)*)?)?)?";
    public static final String UCS_CHAR = "[" + "\u00A0-\uD7FF"
            + "\uF900-\uFDCF" + "\uFDF0-\uFFEF" + "\uD800\uDC00-\uD83F\uDFFD"
            + "\uD840\uDC00-\uD87F\uDFFD" + "\uD880\uDC00-\uD8BF\uDFFD"
            + "\uD8C0\uDC00-\uD8FF\uDFFD" + "\uD900\uDC00-\uD93F\uDFFD"
            + "\uD940\uDC00-\uD97F\uDFFD" + "\uD980\uDC00-\uD9BF\uDFFD"
            + "\uD9C0\uDC00-\uD9FF\uDFFD" + "\uDA00\uDC00-\uDA3F\uDFFD"
            + "\uDA40\uDC00-\uDA7F\uDFFD" + "\uDA80\uDC00-\uDABF\uDFFD"
            + "\uDAC0\uDC00-\uDAFF\uDFFD" + "\uDB00\uDC00-\uDB3F\uDFFD"
            + "\uDB44\uDC00-\uDB7F\uDFFD"
            + "&&[^\u00A0[\u2000-\u200A]\u2028\u2029\u202F\u3000]]";
    public static final Pattern UCS_REGEX_END = Pattern
            .compile("(.+?)(" + UCS_CHAR + "+$)");
    public static final Pattern UCS_REGEX_BEGIN = Pattern
            .compile("^(" + UCS_CHAR + "+)" + "(.+?)");
    public static final Pattern PROTOCOL = Pattern.compile(Patterns.PROTOCOL);
}

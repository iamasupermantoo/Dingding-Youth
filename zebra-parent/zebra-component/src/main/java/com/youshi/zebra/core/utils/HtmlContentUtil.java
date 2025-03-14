package com.youshi.zebra.core.utils;

import java.util.Stack;

import org.apache.commons.lang3.StringUtils;
import org.cyberneko.html.HTMLEntities;

/**
 * 处理html内容
 * 
 * 
 * @author wangsch
 * @createTime 2011-3-16
 */
public abstract class HtmlContentUtil {

    private static final int HTML_ENTITY_TRY = 10;

    /**
     * 截断html文本
     * 
     * @param html
     * @param length 标签不记入长度
     * @return
     */
    public static String abbreviate(String html, int length) {
        return abbreviate(html, length, "...");
    }

    /**
     * 截断html文本
     * 
     * @param html
     * @param length 标签不记入长度
     * @param suffix 后缀
     * @return
     */
    public static String abbreviate(String html, int length, String suffix) {
        if (StringUtils.isBlank(html)) {
            return html;
        }

        // 因为有标签，先申请两倍的容量
        StringBuilder result = new StringBuilder(length * 2);
        // 已经截取的长度
        int count = 0;
        // 在标签中
        boolean inTag = false;
        boolean inHtmlEntity = false;
        // 标签
        StringBuilder currentTag = null;
        Stack<String> tags = new Stack<String>();
        char[] charArray = html.toCharArray();
        int charLength = charArray.length;
        for (int i = 0; i < charLength; i++) {
            char c = charArray[i];
            boolean append = true;

            if (c == '<' && !inTag && !inHtmlEntity) {
                // 开始一个标签
                inTag = true;
                currentTag = new StringBuilder();
            } else if (c == '<' && inTag && !inHtmlEntity) {
                // 已经出现过一次开始标签了，避免出错，使上一次出现的标签开始变为实体
                cancelLastTag(result, "<", "&lt;");
                append = true;
                count++;
            } else if (c == '>' && inTag && !inHtmlEntity) {
                // 结束一个标签
                processTag(tags, currentTag);
                inTag = false;
            } else if (c == '>' && !inTag && !inHtmlEntity) {
                // 没有开始标签，避免
                result.append("&gt;");
                append = false;
                count++;
            } else if (inTag || inHtmlEntity) {
                // 标签
                if (inTag) {
                    currentTag.append(c);
                }
                if (c == ';') { // 结束掉
                    inHtmlEntity = false;
                    count++;
                }
            } else {
                // 做html entity尝试
                if (c == '&') {
                    if (!inHtmlEntity) {
                        // 子扫描
                        int fenhaoIndex = -1; // 呃，不好意思⋯⋯
                        for (int j = i + 1; j < i + HTML_ENTITY_TRY && j < charLength; j++) {
                            if (charArray[j] == '&') {
                                break;
                            }
                            if (charArray[j] == ';') {
                                fenhaoIndex = j;
                                break;
                            }
                        }

                        if (fenhaoIndex > 0) {
                            // 找到分号
                            String possbileHtmlEntity = html.substring(i, fenhaoIndex + 1);
                            String htmlEntityName = possbileHtmlEntity.substring(1,
                                    possbileHtmlEntity.length() - 1);
                            int htmlEntity = HTMLEntities.get(htmlEntityName);
                            if (htmlEntity > 0) {
                                inHtmlEntity = true;
                            }
                        }
                    }
                }

                if (!inHtmlEntity) {
                    count++;
                }
            }
            if (append) {
                result.append(c);
            }
            if (count == length) {
                // 到了，结束
                break;
            }
        }
        if (inTag) {
            // 删除最后一个不完整的标记
            result.delete(result.lastIndexOf("<"), result.length());
        }

        if (result.length() < html.length()) { // TODO 给可见字符计数
            result.append(suffix);
        }

        // 补齐tag
        while (!tags.empty()) {
            String tag = tags.pop();
            result.append("</");
            result.append(tag);
            result.append(">");
        }
        return result.toString();
    }

    private static void cancelLastTag(StringBuilder buffer, String c, String entity) {
        // 取消上一次出现的标签
        int pos = buffer.lastIndexOf(c);
        if (pos != -1) {
            buffer.replace(pos, pos + 1, entity);
        }
    }

    private static void processTag(Stack<String> tags, StringBuilder tag) {
        // tag: 没有两端的 <>
        // FIXME 改成 StringUtils.trim ??
        // 删除多余的空格
        while (tag.length() > 0 && // 避免出现 index 溢出
                (tag.charAt(tag.length() - 1) == ' ' || tag.charAt(tag.length() - 1) == '\t')) {
            tag.delete(tag.length() - 1, tag.length());

        }

        if (tag.lastIndexOf("/") == tag.length() - 1) {
            // 单独闭合的标签，不处理
            return;
        }
        // 取到第一个空格前的有效内容
        int space = tag.indexOf(" ");
        if (space == -1) {
            space = tag.indexOf("\t");
        }
        if (space >= 0) {
            tag = tag.delete(space, tag.length());
        }

        if (tag.charAt(0) == '/') {
            // 结束标签
            tag.delete(0, 1);
            // 如果上来就遇到 </xxx>，没有开始标记，不用弹出
            if (!tags.empty()) {
                String endTag = tags.peek();
                if (StringUtils.equalsIgnoreCase(endTag, tag.toString().trim())) {
                    // 匹配，删除
                    tags.pop();
                    return;
                } else {
                    // 不匹配，压栈
                    tags.push(tag.toString().trim());
                }
            }
        }
        tags.push(tag.toString().trim());
    }

    public static String removeFirstEmptyPTag(String html) {
        // <p>标签前是不是空的？，如果是空的才继续处理
        String prefix = StringUtils.substringBefore(html, "<p>");
        if (!StringUtils.isBlank(prefix)) {
            return html;
        }

        // <p></p>之间的部分，如果是空的，返回 </p>之后的
        String content = StringUtils.substringBetween(html, "<p>", "</p>");
        if (content == null) {
            return html;
        }
        content = StringUtils.replaceEach(content, //
                new String[] { "<br/>", "<BR/>", "<br />", "<BR />", "&nbsp;" }, //
                new String[] { "", "", "", "", "" });
        if (StringUtils.isBlank(content)) {
            return StringUtils.substringAfter(html, "</p>");
        }
        return html;
    }
}

package com.dorado.runner.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.google.common.collect.Table;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class HtmlTableRender {

    public static final class HtmlTableRenderBuilder<R, C, V> {

        private StringBuilder stringBuilder;

        private String topLeft;

        private Function<C, String> columnFormator;

        private Function<R, String> rowFormator;

        public HtmlTableRenderBuilder<R, C, V> withStringBuilder(StringBuilder stringBuilder) {
            this.stringBuilder = stringBuilder;
            return this;
        }

        public HtmlTableRenderBuilder<R, C, V> withTopLeft(String topLeft) {
            this.topLeft = topLeft;
            return this;
        }

        public HtmlTableRenderBuilder<R, C, V> withColumnFormator(Function<C, String> columnFormator) {
            this.columnFormator = columnFormator;
            return this;
        }

        public HtmlTableRenderBuilder<R, C, V> withRowFormator(Function<R, String> rowFormator) {
            this.rowFormator = rowFormator;
            return this;
        }

        public StringBuilder render(Table<R, C, V> table) {
            return HtmlTableRender.render(stringBuilder, table, topLeft, columnFormator,
                    rowFormator);
        }

    }

    public static final <R, C, V> HtmlTableRenderBuilder<R, C, V> newBuilder() {
        return new HtmlTableRenderBuilder<>();
    }

    private static final <R, C, V> StringBuilder render(StringBuilder s, Table<R, C, V> table,
            String topLeft, Function<C, String> columnFormator, Function<R, String> rowFormator) {
        if (s == null) {
            s = new StringBuilder();
        }
        s.append("<style>table {border-collapse: collapse;}</style>");
        s.append("<table border=\"1\"><tr><th>").append(topLeft).append("</th>");
        for (C column : table.columnKeySet()) {
            s.append("<th>").append(columnFormator == null ? column : columnFormator.apply(column))
                    .append("</th>");
        }
        s.append("</tr>");

        List<C> columnKeySet = new ArrayList<>(table.columnKeySet()); // 这里之所以拷贝一下，是因为原来的set迭代性能太低了
        for (Entry<R, Map<C, V>> rowEntry : table.rowMap().entrySet()) {
            s.append("<tr><td>")
                    .append(rowFormator == null ? rowEntry.getKey() : rowFormator.apply(rowEntry
                            .getKey())).append("</td>");
            Map<C, V> rows = rowEntry.getValue();
            for (C column : columnKeySet) {
                V value = rows.get(column);
                s.append("<td>");
                s.append(value == null ? "" : value);
                s.append("</td>");
            }
            s.append("</tr>");
        }
        s.append("</table>");
        return s;
    }
}

package com.dorado.push.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.StringUtils;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class EmojiConverter {

    private EmojiConverter(Function<List<Integer>, String> extractor) {
        this.converter = extractor;
    }

    enum Type {
        UNICODE, SOFTBANK
    }

    private Map<List<Integer>, String> convertMap;

    private final Function<List<Integer>, String> converter;

    public static class Builder {

        private Type from;

        private Type to;

        private Function<List<Integer>, String> converter;

        public Builder from(Type type) {
            this.from = type;
            return this;
        }

        public Builder to(Type type) {
            this.to = type;
            return this;
        }

        public Builder converter(Function<List<Integer>, String> converter) {
            this.converter = converter;
            return this;
        }

        public EmojiConverter build() {
            EmojiConverter converter = new EmojiConverter(this.converter);
            readMap(converter);
            return converter;
        }

        private static final String TRIM_PATTERN = "[^0-9A-F]*";

        public void readMap(EmojiConverter converter) {
            Map<List<Integer>, String> result = new HashMap<List<Integer>, String>();
            converter.convertMap = result;
            XMLEventReader reader = null;
            try {
                XMLInputFactory factory = XMLInputFactory.newInstance();
                InputStream stream = EmojiConverter.class.getClassLoader().getResourceAsStream(
                        "emoji4unicode.xml");
                reader = factory.createXMLEventReader(stream);
                while (reader.hasNext()) {
                    XMLEvent event = reader.nextEvent();
                    if (event.isStartElement()) {
                        StartElement element = (StartElement) event;
                        if (element.getName().getLocalPart().equals("e")) {
                            Attribute fromAttr = element.getAttributeByName(new QName(from
                                    .toString().toLowerCase()));
                            Attribute toAttr = element.getAttributeByName(new QName(to.toString()
                                    .toLowerCase()));
                            if (fromAttr == null) {
                                continue;
                            }
                            List<Integer> fromCodePoints = new ArrayList<Integer>();
                            String from = fromAttr.getValue();
                            if (from.length() > 6) {
                                String[] froms = from.split("\\+");
                                for (String part : froms) {
                                    fromCodePoints.add(Integer.parseInt(
                                            part.replaceAll(TRIM_PATTERN, ""), 16));
                                }
                            } else {
                                fromCodePoints.add(Integer.parseInt(
                                        from.replaceAll(TRIM_PATTERN, ""), 16));
                            }
                            if (toAttr == null) {
                                result.put(fromCodePoints, null);
                            } else {
                                String to = toAttr.getValue();
                                StringBuilder toBuilder = new StringBuilder();
                                if (to.length() > 6) {
                                    String[] tos = to.split("\\+");
                                    for (String part : tos) {
                                        toBuilder.append(Character.toChars(Integer.parseInt(
                                                part.replaceAll(TRIM_PATTERN, ""), 16)));
                                    }
                                } else {
                                    toBuilder.append(Character.toChars(Integer.parseInt(
                                            to.replaceAll(TRIM_PATTERN, ""), 16)));
                                }
                                result.put(fromCodePoints, toBuilder.toString());
                            }
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (XMLStreamException e) {}
                }
            }
        }
    }

    public String convert(String input) {
        StringBuilder result = new StringBuilder();
        int[] codePoints = toCodePointArray(input);
        for (int i = 0; i < codePoints.length; i++) {
            List<Integer> key2 = null;
            if (i + 1 < codePoints.length) {
                key2 = new ArrayList<Integer>();
                key2.add(codePoints[i]);
                key2.add(codePoints[i + 1]);
                if (convertMap.containsKey(key2)) {
                    if (converter != null) {
                        result.append(converter.apply(key2));
                    } else {
                        String value = convertMap.get(key2);
                        if (value != null) {
                            result.append(value);
                        }
                    }
                    //result.append(value);
                    i++;
                    continue;
                }
            }
            List<Integer> key1 = new ArrayList<Integer>();
            key1.add(codePoints[i]);
            if (convertMap.containsKey(key1)) {
                if (converter != null) {
                    result.append(converter.apply(key1));
                } else {
                    String value = convertMap.get(key1);
                    if (value != null) {
                        result.append(value);
                    }
                }
                //result.append(value);
                continue;
            }
            result.append(Character.toChars(codePoints[i]));
        }
        return result.toString();
    }

    private static int[] toCodePointArray(String str) {
        char[] ach = str.toCharArray();
        int len = ach.length;
        int[] acp = new int[Character.codePointCount(ach, 0, len)];
        int j = 0;
        for (int i = 0, cp; i < len; i += Character.charCount(cp)) {
            cp = Character.codePointAt(ach, i);
            acp[j++] = cp;
        }
        return acp;
    }

    private static volatile EmojiConverter unicodeToSoftBank;

    public static final EmojiConverter getUnicodeToSoftBank() {
        if (unicodeToSoftBank == null) {
            synchronized (EmojiConverter.class) {
                if (unicodeToSoftBank == null) {
                    unicodeToSoftBank = new EmojiConverter.Builder().from(Type.UNICODE)
                            .to(Type.SOFTBANK).converter(i -> "[表情]").build();
                }
            }
        }
        return unicodeToSoftBank;
    }

    private static volatile EmojiConverter unicodeToEmpty;

    public static final EmojiConverter getUnicodeToEmpty() {
        if (unicodeToEmpty == null) {
            synchronized (EmojiConverter.class) {
                if (unicodeToEmpty == null) {
                    unicodeToEmpty = new EmojiConverter.Builder().from(Type.UNICODE)
                            .to(Type.SOFTBANK).converter((i) -> "").build();
                }
            }
        }
        return unicodeToEmpty;
    }

    private static volatile EmojiConverter unicodeToHtml;

    private static volatile EmojiConverter softbankToUnicode;

    public static final EmojiConverter getSoftbankToUnicode() {
        if (softbankToUnicode == null) {
            synchronized (EmojiConverter.class) {
                if (softbankToUnicode == null) {
                    softbankToUnicode = new EmojiConverter.Builder().from(Type.SOFTBANK)
                            .to(Type.UNICODE).build();
                }
            }
        }
        return softbankToUnicode;
    }

    public static final EmojiConverter getUnicodeToHtml() {
        if (unicodeToHtml == null) {
            synchronized (EmojiConverter.class) {
                if (unicodeToHtml == null) {
                    unicodeToHtml = new EmojiConverter.Builder()
                            .from(Type.UNICODE)
                            .to(Type.SOFTBANK)
                            .converter(
                                    value -> {
                                        StringBuilder r = new StringBuilder();
                                        r.append("<span class=\"emoji emoji");
                                        boolean first = true;
                                        for (Integer i : value) {
                                            if (first) {
                                                r.append(StringUtils.removeStart(
                                                        Integer.toHexString(i), "0"));
                                                first = false;
                                            } else {
                                                r.append(Integer.toHexString(i));
                                            }
                                        }
                                        r.append("\">");
                                        for (Integer i : value) {
                                            r.appendCodePoint(i);
                                        }
                                        r.append("</span>");
                                        return r.toString();
                                    }).build();
                }
            }
        }
        return unicodeToHtml;
    }
}

package com.youshi.zebra.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class ChineseToPinyinHelper {

    private static final String DATA_FILE = "pinyin/unicode_to_hanyu_pinyin.txt";

    private Map<Integer, String[]> pinyinMap = new HashMap<>();

    private final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    private ChineseToPinyinHelper() {
        //init
        InputStream dictInputStream = getClass().getClassLoader().getResourceAsStream(DATA_FILE);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(dictInputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] codeAndPinyin = line.split("\\s");
                if (codeAndPinyin == null || codeAndPinyin.length < 2) {
                    logger.warn("found invalid format.line:" + line);
                    continue;
                }//end 
                String[] tmpPinyins = codeAndPinyin[1].split(",");
                Set<String> pinyinSet = new HashSet<>(tmpPinyins.length);
                for (String pinyin : tmpPinyins) {
                    String[] p = pinyin.split("\\d");
                    if (p.length > 0) {
                        pinyinSet.add(p[0]);
                    } else {
                        pinyinSet.add(pinyin);
                    }
                }
                if (pinyinSet.size() > 0) {
                    pinyinMap.put(Integer.parseInt(codeAndPinyin[0].trim(), 16),
                            pinyinSet.toArray(new String[0]));
                }
            }
            logger.info("init hanyu pinyin db succ.");
        } catch (IOException e) {
            logger.warn("init hanyu pinyin db err.", e);
        }
    }

    public String[] getHanyuPinyins(char word) {
        if (word < 256) {
            return null;
        }
        return this.pinyinMap.get((int) word);
    }

    //lazy的单例
    private static class ChineseToPinyinResource {

        public static final ChineseToPinyinHelper HELPER = new ChineseToPinyinHelper();
    }

    public static String[] toHanyuPinyins(char word) {
        return ChineseToPinyinResource.HELPER.getHanyuPinyins(word);
    }

    /**
     * 模糊音
     * --声母模糊音
     * z=zh
     * c=ch
     * s=sh
     * l=n
     * f=h
     * r=l
     * --韵母模糊音
     * an=ang
     * en=eng
     * in=ing
     * ian=iang
     * uan=uang
     * 
     * @param pingyin
     * @return
     */
    public static String fuzzyPinyin(String pingyin) {
        return null;
    }

    public static void main(String[] args) {
        String[] tmpPinyins = "de5,di4,di2".split(",");
        Set<String> pinyinSet = new HashSet<>(tmpPinyins.length);
        for (String pinyin : tmpPinyins) {
            String[] p = pinyin.split("\\d");
            if (p.length > 0) {
                pinyinSet.add(p[0]);
            } else {
                pinyinSet.add(pinyin);
            }
        }

        for (String pinyin : pinyinSet.toArray(new String[0])) {
            System.out.println(pinyin);
        }
    }

}

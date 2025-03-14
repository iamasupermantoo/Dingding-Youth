package com.youshi.zebra.core.utils;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public class EntryUtils {

 
    private static final class EntryImpl<K, V> implements Map.Entry<K, V> {

        private final K key;

        private V value;

        /**
         * @param key
         * @param value
         */
        private EntryImpl(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            this.value = value;
            return value;
        }

        @Override
        public String toString() {
            return key + "=" + value;
        }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue())) {
                    return true;
                }
            }
            return false;
        }

    }

    public static <K, V> Map.Entry<K, V> newEntry(final K key, final V value) {
        return new EntryImpl<K, V>(key, value);
    }

    public static void main(String[] args) {
        Entry<Integer, Integer> entry1 = newEntry(1, 2);
        Entry<Integer, Integer> entry2 = newEntry(2, 3);
        Entry<Integer, Integer> entry11 = newEntry(1, 2);
        System.out.println(entry1);
        System.out.println(entry1.equals(entry2));
        System.out.println(entry1.equals(entry11));
    }

}

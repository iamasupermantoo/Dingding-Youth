package com.youshi.zebra.core.utils;

import java.util.HashSet;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public final class MorePredicates {

    public static <T, K> Predicate<T> distinctUsing(Function<T, K> function) {
        return new Predicate<T>() {

            private final HashSet<K> distinct = new HashSet<>();

            @Override
            public boolean test(T t) {
                return distinct.add(function.apply(t));
            }
        };
    }

    public static <T> Predicate<T> after(T element) {
        return new Predicate<T>() {

            private boolean started = element == null;

            @Override
            public boolean test(T t) {
                if (started) {
                    return true;
                } else {
                    if (Objects.equals(t, element)) {
                        started = true;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        };
    }
}

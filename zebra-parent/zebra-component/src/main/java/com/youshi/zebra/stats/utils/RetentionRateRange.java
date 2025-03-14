package com.youshi.zebra.stats.utils;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public enum RetentionRateRange {
    FIRST_WEEK(1, 7), //
    SECOND_WEEK(8, 14), //
    THIRD_WEEK(15, 21);

    private final int startDay;

    private final int endDay;

    RetentionRateRange(int startDay, int endDay) {
        this.startDay = startDay;
        this.endDay = endDay;
    }

    public static RetentionRateRange of(int daysBetween) {
        for (RetentionRateRange value : RetentionRateRange.values()) {
            if (value.startDay <= daysBetween && value.endDay >= daysBetween) {
                return value;
            }
        }
        return null;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    @Override
    public String toString() {
        return this.name() + "(" + this.startDay + "-" + this.endDay + ")";
    }

}

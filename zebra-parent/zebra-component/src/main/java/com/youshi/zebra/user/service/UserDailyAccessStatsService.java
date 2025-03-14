package com.youshi.zebra.user.service;

import java.util.BitSet;
import java.util.Date;
import java.util.List;

import com.dorado.framework.crud.service.RetrieveById;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserDailyAccessStatsService extends RetrieveById<Date, BitSet> {

    void record(int userId);

    List<Date> getAccessdDate(int userId, Date startDate, Date endDate);

    static void main(String[] args) {
        BitSet bitSet = new BitSet();
        bitSet.stream();
        System.out.println("fin.");
    }
}

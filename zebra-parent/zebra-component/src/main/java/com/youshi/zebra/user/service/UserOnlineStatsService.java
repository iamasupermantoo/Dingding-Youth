package com.youshi.zebra.user.service;

import com.dorado.framework.crud.service.RetrieveById;
import com.dorado.mvc.reqcontext.Platform;
import com.youshi.zebra.user.model.UserOnlineStats;
import com.dorado.mvc.reqcontext.AppVer;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserOnlineStatsService extends RetrieveById<Integer, UserOnlineStats> {

    void record(int userId, Platform platform, AppVer appVer, Integer channel, long ip);

    void recordLatestActivityTime(int userId);

    void removeUserWhenChangeSchool(int schoolId, int userId);

    Long getLatestActivityTime(int userId);
}

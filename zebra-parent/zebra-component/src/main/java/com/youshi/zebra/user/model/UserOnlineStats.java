package com.youshi.zebra.user.model;

import java.util.Date;

import com.dorado.mvc.reqcontext.Platform;
import com.dorado.mvc.reqcontext.WebRequestContext;
import com.dorado.framework.crud.model.HasData;
import com.dorado.mvc.reqcontext.AppVer;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface UserOnlineStats extends HasData {

    Date getUpdateTime();

    Platform getPlatform();

    AppVer getAppVer();

    long getIp();

    default String ipv4() {
        return WebRequestContext.longToIpv4(getIp());
    }

    Integer getChannel();

}

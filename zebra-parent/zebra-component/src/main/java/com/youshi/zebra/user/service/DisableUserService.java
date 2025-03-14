package com.youshi.zebra.user.service;

import java.util.BitSet;
import java.util.Collection;
import java.util.Map;

/**
 * @author wangsch
 *
 * @date 2016-09-12
 */
public interface DisableUserService {

    boolean isUserDisabled(int userId);

    Map<Integer, Boolean> getDisableUserMap(Collection<Integer> ids);

    BitSet getAllDisabledUser();

    void setUserDisabled(int userId);

}

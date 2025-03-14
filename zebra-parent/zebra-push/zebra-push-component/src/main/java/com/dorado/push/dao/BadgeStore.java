package com.dorado.push.dao;

public interface BadgeStore {

    public Integer incBadge(Integer user);

    public void setBadge(Integer user, int badge);

    public Integer getBadge(Integer user);
}

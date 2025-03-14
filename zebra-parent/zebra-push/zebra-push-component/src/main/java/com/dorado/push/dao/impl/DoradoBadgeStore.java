/**
 * 
 */
package com.dorado.push.dao.impl;

import org.springframework.stereotype.Service;

import com.dorado.push.dao.BadgeStore;


@Service("doradoBadgeStore")
public class DoradoBadgeStore implements BadgeStore {

	@Override
	public Integer incBadge(Integer user) {
		return 1;
	}

	@Override
	public void setBadge(Integer user, int badge) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer getBadge(Integer user) {
		// TODO Auto-generated method stub
		return null;
	}

//    DRedis<Integer> badgeStore;
//
//    public DoradoBadgeStore() {
//        DRedisFactory factory = DRedisFactoryBase.getInstance();
//        badgeStore = factory.getClient("dorado:id:badge", Integer.class);
//    }
//
//    @Override
//    public void setBadge(Integer user, int badge) {
//        badgeStore.set(user.toString(), badge);
//    }
//
//    @Override
//    public Integer getBadge(Integer user) {
//        return badgeStore.get(user.toString());
//    }
//
//    @Override
//    public void incBadge(Integer user) {
//        badgeStore.incr(user.toString());
//    }
}

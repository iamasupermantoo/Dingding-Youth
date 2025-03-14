package com.youshi.zebra.core.web.binder;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dorado.mvc.antispam.BaseBinder;
import com.dorado.mvc.model.JsonResultView;
import com.youshi.zebra.core.constants.ZebraMetaCode;
import com.youshi.zebra.core.web.annotation.URIStats;
import com.youshi.zebra.core.web.annotation.URIStats.StatsType;
import com.youshi.zebra.core.web.annotation.URIStats.StatsWhen;
import com.youshi.zebra.stats.service.UserStatsService;
import com.youshi.zebra.stats.utils.StatsUtils;

/**
 * 基于URI（接口调用）的统计处理切面。
 * 
 * @author wangsch
 * @date 2017年1月3日
 */
@Aspect
@Component("statsBinder")
public class URIStatsBinder extends BaseBinder {
	
	/**
	 * Controller方法处理前后执行拦截，根据{@link StatsWhen}判断，是在接口执行前还是在接口执行后，添加统计逻辑。
	 * 
	 * @param joinPoint
	 * @return
	 * @throws Throwable
	 */
	@Around("@annotation(com.youshi.zebra.core.web.annotation.URIStats)")
	public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
		Method method = getMethod(joinPoint);
		URIStats stats = method.getAnnotation(URIStats.class);
		StatsType type = stats.value();
		StatsWhen when = stats.when();
		
		switch (when) {
		case ANY:
			doStat(type, joinPoint);
			Object result = joinPoint.proceed(joinPoint.getArgs());
			return result;
		case SUCC:
			result = joinPoint.proceed(joinPoint.getArgs());
			boolean isSucc = isSucc(result);
			if(isSucc) {
				doStat(type, joinPoint);
			}
			return result;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	private boolean isSucc(Object result) {
		if(result instanceof JsonResultView) {
			int metaCode = ((JsonResultView) result).getMeta().getCode();
			return metaCode == ZebraMetaCode.Success.getCode();
		}
		return true;
	}
	
	private void doStat(StatsType type, ProceedingJoinPoint joinPoint) {
		switch (type) {
		case register:
			break;

		default:
			break;
		}
	}
}

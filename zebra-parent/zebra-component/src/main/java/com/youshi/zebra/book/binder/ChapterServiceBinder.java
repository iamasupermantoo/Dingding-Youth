package com.youshi.zebra.book.binder;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dorado.mvc.antispam.BaseBinder;
import com.youshi.zebra.book.constants.BookStatus;
import com.youshi.zebra.book.service.BookService;

/**
 * 教材章节Service，做拦截：判断教材状态、更改教材状态
 * 
 * @author wangsch
 * @date 2017年3月8日
 */
//@Aspect
//@Component("chapterServiceBinder")
public class ChapterServiceBinder extends BaseBinder {
	
	@Autowired
	private BookService bookAdminService;
	
	@Around("within(com.youshi..*) && @annotation(com.dorado.metrics.annotation.MethodCallMetrics)")
	public Object method(ProceedingJoinPoint joinPoint) throws Throwable {
		// before
		Method method = getMethod(joinPoint);
		
		// 执行目标方法
		Object result = joinPoint.proceed();
		
		// after
//		bookAdminService.updateStatus(bookId, BookStatus.Modified);
		
		return result;
	}
}

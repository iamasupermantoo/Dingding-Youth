package com.youshi.zebra.reaction;

import org.junit.Test;

import com.dorado.framework.utils.DoradoBeanFactory;
import com.youshi.zebra.reaction.service.StudentReactionService;

/**
 * 
 * @author wangsch
 * @date 2017年2月23日
 */
public class StudentReactionTest {
	@Test
	public void add() {
		StudentReactionService service = DoradoBeanFactory.getBean(StudentReactionService.class);
		service.createReaction(110089, 1, 1);
		service.createReaction(110089, 1, 2);
		
	}
}

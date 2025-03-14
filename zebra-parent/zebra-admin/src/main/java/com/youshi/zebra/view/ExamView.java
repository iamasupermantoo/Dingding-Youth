package com.youshi.zebra.view;

import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.exam.dao.ExamDAO.ExamStatus;
import com.youshi.zebra.exam.model.ExamModel;
import com.youshi.zebra.exam.service.ExamService.ExamLevel;

public class ExamView {
		private ExamModel delegate;
		private ZebraBuildContext context;
		
		public ExamView(ExamModel delegate, ZebraBuildContext context) {
			this.delegate = delegate;
			this.context = context;
		}
		
		public String getId() {
			return delegate.getUuid();
		}
		
		public String getName() {
			return delegate.getName();
		}
		
		public ExamStatus getStatus() {
			return ExamStatus.fromValue(delegate.getStatus());
		}
		
		public ExamLevel getLevel() {
			return ExamLevel.fromValue(delegate.getLevel());
		}
		
		public long getCreateTime() {
			return delegate.getCreateTime();
		}
		
	}
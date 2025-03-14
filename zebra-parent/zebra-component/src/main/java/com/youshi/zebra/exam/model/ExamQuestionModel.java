package com.youshi.zebra.exam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.dorado.framework.crud.model.impl.AbstractModel;
import com.dorado.framework.crud.model.util.ModelUtils;
import com.dorado.framework.utils.DoradoMapperUtils;

/**
 * 
 * @author codegen
 */
public class ExamQuestionModel extends AbstractModel<Integer>{
	public enum ExamQuestionKeys {
		// db 字段
		exam_id,
		num,
		title,
		options,
		options_mode,
	
		// data字段
		right_answer,
	}
	
	private Integer examId;
	private Integer num;
	private String title;
	private String options;
	private Integer optionsMode;
	
	public ExamQuestionModel(
			int id, String data, long createTime, int status,
			Integer examId,
			Integer num,
			String title,
			String options,
			Integer optionsMode
			) {
		super(id, data, createTime, status);
		this.examId = examId;
		this.num = num;
		this.title = title;
		this.options = options;
		this.optionsMode = optionsMode;
	}

	public Integer getExamId() {
		return examId;
	}
	public Integer getNum() {
		return num;
	}
	public String getTitle() {
		return title;
	}
	public String getOptions() {
		return options;
	}
	public Integer getOptionsMode() {
		return optionsMode;
	}
	public String getRightAnswer() {
		return ModelUtils.getString(this, ExamQuestionKeys.right_answer);
	}
	public List<TitleItem> getTitleItems() {
		if(StringUtils.isEmpty(getTitle())) {
			return null;
		}
		return DoradoMapperUtils.fromJSON(getTitle(), ArrayList.class, TitleItem.class);
	}
	
	public List<OptionItemWrapper> getOptionItems() {
		if(StringUtils.isEmpty(getOptions())) {
			return null;
		}
		return DoradoMapperUtils.fromJSON(getOptions(), ArrayList.class, OptionItemWrapper.class);
	}
}

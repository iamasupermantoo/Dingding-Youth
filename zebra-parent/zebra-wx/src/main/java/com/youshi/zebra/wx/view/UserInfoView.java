package com.youshi.zebra.wx.view;

import com.dorado.framework.crud.model.util.ModelUtils;
import com.youshi.zebra.core.web.view.ZebraBuildContext;
import com.youshi.zebra.counts.model.UserCountsModel;
import com.youshi.zebra.user.model.UserModel;
import com.youshi.zebra.user.model.UserModel.UserKey;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * @author wangsch
 * @date 2017年1月11日
 */
public class UserInfoView {
    private final UserModel model;
    
    private final ZebraBuildContext buildContext;

	private UserCountsModel counts;
    
    public UserInfoView(UserModel model, ZebraBuildContext buildContext) {
        this.model = model;
        this.buildContext = buildContext;
        this.counts = model.getCounts();
    }
    
    public String getId() {
    	return model.getUuid();
    }
    
    public ImageView getHeadImage() {
		return new ImageView(buildContext.getImage(model.getHeadImageId()));
	}

	public String getName() {
		return model.getName();
	}
	
	public Integer getType() {
		return model.getType();
	}
	
	public Integer getGender() {
		return model.getGender();
	}
	
	public String getSignature() {
		return model.getSignature();
	}
	
	@ApiModelProperty(value = "已完成课时")
	public Integer getFinishedCnt() {
		if(counts == null) {
			return 0;
		}
		return counts.getFinishedCount();
	}
	
	@ApiModelProperty(value = "全部课程")
	public Integer getCourseCnt() {
		if(counts == null) {
			return 0;
		}
		return counts.getCourseCount();
	}

	@ApiModelProperty(value = "累计时长")
	public Integer getDuration() {
		if(counts == null) {
			return 0;
		}
		return counts.getDuration();
	}
	
	@ApiModelProperty(value = "评测时填写的回答。逗号分隔的字符串，大写，如：A,B,A,C")
	public String getAnswers() {
		return ModelUtils.getString(model, UserKey.exam_answers);
	}
	
	@ApiModelProperty(value = "评测时的评测结果，1: 一级，2：二级，3：三级")
	public Integer getLevel() {
		return ModelUtils.getInt(model, UserKey.exam_level);
	}
	
}

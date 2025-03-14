package com.dorado.gotopage;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dorado.framework.crud.model.util.UuidUtils;
import com.dorado.gotopage.constant.GotoPage;
import com.dorado.gotopage.constant.GotoPageKeys;
import com.youshi.zebra.course.constants.CourseMetaStatus;
import com.youshi.zebra.course.model.CourseMetaModel;
import com.youshi.zebra.course.service.CourseMetaService;
import com.youshi.zebra.user.service.UserService;

/**
 * gotoPage一些辅助工具
 * 
 * 
 * @author wangsch
 * @date		2016年11月8日
 *
 */
@Component
public class GotoPageHelper {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private CourseMetaService courseMetaService;
	
	
	
	/**
	 * 
	 * @author wangsch
	 * @date		2016年11月8日
	 *
	 */
	public class ParsedGotoPage {
		/**
		 * dataId，字符串。uuid/id
		 */
		public final String dataId;
		
		/**
		 * dataId，int型。目标的id
		 */
		public final Integer dataIntId;
		
		/**
		 * {@link GotoPage}类型
		 */
		public final GotoPage gotoPageType;
		
		/**
		 * gotoPage参数
		 */
		public final Map<String, Object> gotoPageParams;
		
		public ParsedGotoPage(String dataId, Integer dataIntId, GotoPage gotoPageType, 
				Map<String, Object> gotoPageParams) {
			this.dataId = dataId;
			this.dataIntId = dataIntId;
			this.gotoPageType = gotoPageType;
			this.gotoPageParams = gotoPageParams;
		}
		
		public boolean isValid() {
			return gotoPageType != null && MapUtils.isNotEmpty(gotoPageParams);
		}
		
	}
	
	public ParsedGotoPage parseGotoPage(GotoPage gotoPageType, Map<String, Object> extraParams) {
		return parseGotoPage(null, gotoPageType, extraParams);
	}
	
	
	/**
	 * 解析gotoPage，根据type构造gotoPage结构。返回{@link ParsedGotoPage}，
	 * 包含了客户端做gotoPage的必要信息。
	 * 
	 * @param dataId					目标的字符串id，uuid
	 * @param gotoPageType	类型
	 * @param extraParams		额外的参数，如果需要的话
	 * @return								{@link ParsedGotoPage}
	 */
	public ParsedGotoPage parseGotoPage(String dataId, GotoPage gotoPageType, Map<String, Object> extraParams) {
		Map<String, Object> gotoParamsMap = new HashMap<>();
		Integer dataIntId = GotoPageKeys.EMPTY_DATA_ID;
		switch(gotoPageType){
		case FEEDBACK:
			break;
			
		case COURSE:
			dataIntId = UuidUtils.toIntId(CourseMetaModel.class, dataId);
			CourseMetaModel cm = courseMetaService.getById(dataIntId);
			if(cm == null || cm.getStatus() != CourseMetaStatus.Normal.getValue()) {
				throw new IllegalArgumentException();
			}
			
			gotoParamsMap.put(GotoPageKeys.dataId.getKey(), dataId);
			break;
			
		case URL:
			String url = (String)extraParams.get(GotoPageKeys.url.getKey());
			gotoParamsMap.put(GotoPageKeys.url.getKey(), url);
			
			break;
		
		case NONE:
			break;
		
		default:
			throw new IllegalArgumentException("Unsupported gotoPage: " + gotoPageType);
		}
		
		return new ParsedGotoPage(dataId, dataIntId, gotoPageType, gotoParamsMap);
	}
}

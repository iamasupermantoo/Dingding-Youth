package com.youshi.zebra.conroller.scholarship;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.scholarship.dao.ScholarshipRetainRecordDAO.ScholarshipRetainRecordStatus;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.scholarship.service.ScholarshipAdminService;
import com.youshi.zebra.view.ScholarshipRetainRecordView;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年11月10日
 */
@RestController
@RequestMapping(value = "/scholarship/admin")
public class ScholarshipAdminController {
	
	@Autowired
	private ScholarshipAdminService scholarshipAdminService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	
	@RequestMapping(value = "/retain/record/list")
	public ModelAndView records(
			@RequestParam(value = "status", required = false) ScholarshipRetainRecordStatus status,
			@ApiIgnore @Uuid(value = "cursor", type = ScholarshipRetainRecordModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue = "20") Integer limit
			) {
		ModelAndView mav = new ModelAndView("scholarship/retain_record_list");
		PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> page = scholarshipAdminService.getRecords(status, cursor, limit);
		Map<String, Object> modelMap = viewBuilder.buildToMap(page, "records", 
				ExplicitViewMapper.getInstance().setViewClass(ScholarshipRetainRecordView.class));
		
		return mav.addAllObjects(modelMap);
	}
	
	@RequestMapping(value = "/retain/pass", method=RequestMethod.POST)
	public Object pass(
			@ApiIgnore @Uuid(value = "record", type = ScholarshipRetainRecordModel.class) Integer recordId
			) {
		scholarshipAdminService.retainPass(recordId);
		
		return JsonResultView.SUCCESS;
	}
	
	@RequestMapping(value = "/retain/unpass", method=RequestMethod.POST)
	public Object unpass(
			@ApiIgnore @Uuid(value = "record", type = ScholarshipRetainRecordModel.class) Integer recordId
			) {
		scholarshipAdminService.retainUnpass(recordId);
		
		return JsonResultView.SUCCESS;
	}
}

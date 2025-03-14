package com.youshi.zebra.controller.scholarship;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorado.framework.crud.model.HasUuid;
import com.dorado.framework.model.PageView;
import com.dorado.mvc.arg_resolve.annotation.Uuid;
import com.dorado.mvc.arg_resolve.annotation.Visitor;
import com.dorado.mvc.model.JsonResultView;
import com.dorado.mvc.view.ExplicitViewMapper;
import com.dorado.mvc.view.ViewBuilder;
import com.youshi.zebra.ZebraCommonApiTags;
import com.youshi.zebra.course.model.CourseModel;
import com.youshi.zebra.course.model.LiveMetaModel;
import com.youshi.zebra.lesson.model.LessonModel;
import com.youshi.zebra.scholarship.WxShareInfo;
import com.youshi.zebra.scholarship.model.ScholarshipModel;
import com.youshi.zebra.scholarship.model.ScholarshipRetainRecordModel;
import com.youshi.zebra.scholarship.service.ScholarshipService;
import com.youshi.zebra.scholarship.service.ShareService;
import com.youshi.zebra.view.ScholarshipRetainRecordView;
import com.youshi.zebra.view.ScholarshipView;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 
 * @author wangsch
 * @date 2017年11月10日
 */
@RequestMapping(value = "/scholarship")
@RestController
public class ScholarshipController {
	@Autowired
	private ScholarshipService scholarshipService;
	
	@Autowired
	private ShareService shareService;
	
	@Autowired
	private ViewBuilder viewBuilder;
	
	@ApiOperation(value = "我的奖学金", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "", method = RequestMethod.GET)
	public Object scholarship(
			@ApiIgnore @Visitor Integer userId
			) {
		JsonResultView result = new JsonResultView();
		ScholarshipModel scholarship = scholarshipService.getScholarship(userId);
		return result.addValue("info", new ScholarshipView(scholarship));
	}
	
	@ApiOperation(value = "获取微信分享信息", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "大课id", name="lm", dataType="String", required=false, paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=false, paramType="query")
	})
	@RequestMapping(value = "/share", method = RequestMethod.GET)
	public Object share(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lm", type=LiveMetaModel.class, required = false)  Integer liveMetaId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class, required = false) Integer courseId
			) {
		WxShareInfo shareInfo = shareService.getWxShareInfo(userId, liveMetaId, courseId);
		return new JsonResultView().addValue("info", shareInfo);
	}
	
	@ApiOperation(value = "微信分享成功后，调用此接口", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
		@ApiImplicitParam(value = "大课id", name="lm", dataType="String", required=false, paramType="query"),
		@ApiImplicitParam(value = "课程id", name="cid", dataType="String", required=false, paramType="query"),
		@ApiImplicitParam(value = "lesson id", name="lid", dataType="String", required=false, paramType="query"),
	})
	@RequestMapping(value = "/shareOK", method = RequestMethod.GET)
	public Object shareOK(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "lm", type=LiveMetaModel.class, required = false)  Integer liveMetaId,
			@ApiIgnore @Uuid(value = "cid", type=CourseModel.class, required = false) Integer courseId,
			@ApiIgnore @Uuid(value = "lid", type=LessonModel.class, required = false) Integer lessonId
			) {
		shareService.shareOK(userId, liveMetaId, courseId, lessonId);
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "申请提现", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/retain/apply", method = RequestMethod.GET)
	public Object retainApply(
			@ApiIgnore @Visitor Integer userId
			) {
		scholarshipService.retainApply(userId);
		
		return JsonResultView.SUCCESS;
	}
	
	@ApiOperation(value = "提现记录，分页查询", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/retain/record/list", method = RequestMethod.GET)
	public Object retainRecords(
			@ApiIgnore @Visitor Integer userId,
			@ApiIgnore @Uuid(value = "cursor", type=ScholarshipRetainRecordModel.class, required=false) Integer cursor,
			@RequestParam(value = "limit", defaultValue="20") Integer limit
			) {
		PageView<ScholarshipRetainRecordModel, HasUuid<Integer>> page = scholarshipService
				.getRecords(userId, cursor, limit);
		JsonResultView result = viewBuilder.build(page, "records", 
				ExplicitViewMapper.getInstance().setViewClass(ScholarshipRetainRecordView.class));
		
		return result;
	}
	
	@ApiOperation(value = "保存银行卡信息", tags={ZebraCommonApiTags.FINAL})
	@ApiImplicitParams({
		@ApiImplicitParam(value = "z", name="z", required=true, dataType="String", paramType="query"),
	})
	@RequestMapping(value = "/bank/save", method = RequestMethod.GET)
	public Object saveBankInfo(
			@ApiIgnore @Visitor Integer userId,
			@RequestParam(value = "bankUser") String bankUser,
			@RequestParam(value = "bankUserMobile") String bankUserMobile,
			@RequestParam(value = "bankName") String bankName,
			@RequestParam(value = "bankCardNum") String bankCardNum
			) {
		scholarshipService.saveBankInfo(userId, bankUser, bankUserMobile, bankName, bankCardNum);
		return JsonResultView.SUCCESS;
	}
}

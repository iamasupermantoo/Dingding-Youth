package com.youshi.zebra.core.constants;

import java.util.Collections;

import com.dorado.framework.crud.model.HasMetaCode.MetaCode;
import com.dorado.mvc.model.JsonResultView;

public enum ZebraMetaCode implements MetaCode {
    ///////////////////////////////////////////////////// 通用code //////////////////////////////////////////////////////////
	Success(1, "成功"),
    
	/** ticket无效，请客户端踢票 */
    Unauthorized(401, "抱歉，无法识别您的身份"),
    
    /** 没有权限访问（不能做这个事情，可能是修改别人的数据） */
    Forbidden(403, "抱歉，您不能执行此操作"),
    
    /** 404 */
    NotFound(404, "抱歉，请求的地址不存在"),
    
    /** 请客户端安抚用户，服务器GG */
    ServerFailed(500, "糟糕，服务器出错了"),
    
    //////////////////////////////////////////////////// 业务code ////////////////////////////////////////////////////////////
    /** 登陆失败（密码错误或者手机号错误） */
    LoginFailed(40000, "手机号或密码不正确"),
    
    /** 密码错误，修改密码时 */
    OldPasswordWrong(40001, "密码不正确"),
    
    // ---------mobile 注册
    MobileNotRegistered(40010, "该手机号还未注册"),
    MobileAlreadyRegistered(40011, "该手机号已经被注册了"),
    
    // ---------第三方登录
    /** 第三方登录获取验证码时 */
    MobileAlreadyBinded(40012, "该手机号已经被注册了"),
    
    
    // END
    
    // --------- 验证码相关，靠这些状态保障安全性
    MobileCodeThreadHold(40020, "验证码获取过多，请明天再试"),
    
    /** 输入的验证码不正确 */
    MobileCodeWrong(40021, "验证码不正确，请重新输入"),
    
    /** 验证码未验证，正常流程不会出现（可能是攻击） */
    MobileCodeNotVerifyed(40022, "验证码未验证"),
    
    /** 验证码已经过期 */
    MobileCodeExpired(40023, "验证码已过期，请重新获取"),
    
    /** 验证错误次数超过阀值后失效 */
    MobileCodeInvalid(40024, "验证次数过多，请重新获取验证码"),
    
    /** 验证码不存在 */
    MobileCodeNone(40025, "请先获取验证码"),
    // END
    
    // --------- 通用业务code
    /** 账号被封禁 */
    UserBlocked(40030, "抱歉，您的帐号被封禁了"),
    
    /** 图片上传失败 */
    ImageUploadError(40031, "抱歉，图片上传失败"),
    
    /** spammer信息 */
    Spammer(40032, "抱歉，您的输入包含敏感内容"),
    
    /** 操作太快，在重要的写请求上，要做频率限制 */
    OpTooFast(40033, "抱歉，您的操作过于频繁"),
    
    /** 对象被删除 */
    EntityNotNormal(40034, "抱歉，您操作的对象不存在"),
    
    EntityStatusInvalid(40037, "抱歉，您操作的对象状态异常"),
    
    EntityNotFound(40038, "抱歉，您操作的对象不存在"),
    
    
    /** 输入校验失败 */
    ParmVerifyFail(40035, "抱歉，您的输入不正确"),
    
    /** 音频上传失败 */
    AudioUploadError(40036, "抱歉，音频上传失败"),
    
    // END

    // ---------connect
    /*
     * userId（用户） 绑定 externalUserId(外部用户，微信、qq)，拿微信举例：
     * 1. ConnectAlreadyBinded：	用户已经绑定过微信
     * 2. ConnectBindedByOther：	微信被其他用户绑定了
     * 
     * 这两种情况是不同的
     * 
     */
    /** 用户之前已绑定过了 */
    ConnectAlreadyBinded(40040, "抱歉，该手机号已绑定到其他账号"),
    
    /** 目标账号已被其他用户绑定, 暂未使用 */
    ConnectBindedByOther(40041, "抱歉，账号已被绑定"),
    
    ConnectNotBinded(40042, "抱歉，您还未完成绑定"),
    // END
    
    /** 举报自己 */
    TipoffSelf(40060, "您太有才了，举报自己"), 
    
    /** 作业答案修改失败 */
    HomeworkAnswerModifyFail(40070, "作业已经批改，不可编辑"),
    
    // lesson相关
    LessonTimePeriodInvalid(40080, "排课时间必须满足：“结束时间” - “结束时间” > 0，且要求两个时间格式为：HH:mm"),
    TeacherLessonTimePeriodConflict(40081, "排课时间和老师已有的时间安排有冲突，请检查后重试"),
    StudentLessonTimePeriodConflict(40082, "排课时间和学生已有的时间安排有冲突，请检查后重试"),
    // END
    
    
    
    // 支付
    AlipayCallFail(40090, "与支付宝通信失败"),
    WeixinPayCallFail(40091, "与微信支付通信失败"),
    ApplePayCallFail(40092, "与苹果支付通信失败"),
    
    // 订单
    OrderNotFound(40100, "订单不存在"),
    
    
    // ---------order
    OrderStatusInvalid(40090, "订单当前的状态不支持您的操作"),
    PreOrderAlreadyExist(40091, "您已预约过此课程，不必重复预约哦"),
    ProductInfoInvalid(40092, "未能获取商品信息"),
    TryCourseAlreadyExist(40093, "您已预约过此试听课，不必重复预约哦"),
    // END
    
    
    // ---------live
    LiveLessonStatusInvalid(40100, "当前课状态不正常"),
    LessonTimeInvalid(40101, "当前时间不允许此操作"),
    RoomInfoFieldInvalid(40102, "课堂房间信息不正常"),
    // END
    
    
    
    // --------
    BookAlreadyUsed(400200, "不允许的操作，因为教材已关联课程"),
    // END
    
    NoSuitableExam(400201, "没有适合孩子年龄的测评"), 
    

    
    // --------schoraship
    RetainMinAmount(400250, "还不满足提现最低额度哦"),
    // END
    
    
    
    
    RecommendFeedAlreadyExist(1, ""), 
    
    RecommendFeedCancel(2, ""), 
    
    
    
    
    ;

    private int code;

    private String desc;

    private String errInfo;

    ZebraMetaCode(int code) {
    	this(code, null);
    }
    
    public ZebraMetaCode setErrInfo(String errInfo) {
        this.errInfo = errInfo;
        return this;
    }

    ZebraMetaCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public JsonResultView toView() {
        return new JsonResultView(this, Collections.emptyMap());
    }

	@Override
	public int getCode() {
		return code;
	}

	@Override
	public String getDesc() {
		return desc;
	}
	
	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@Override
	public String getErrInfo() {
        return errInfo;
    }

}

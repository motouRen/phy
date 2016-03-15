package config;

public interface Urls {
	/*
	 * 服务器端地址
	 */
	public static final String BASE_URL = "https://yoyub.winning.com.cn:7062";
	/*
	 * 检测版本
	 */
	public static final String CHECK_VERSION_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/comm/checkVersionEx";
	/*
	 * 报告位置
	 */
	public static final String REPORT_LOCATION_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/doctor/reportLocation";

	/*
	 * 登录
	 */
	public static final String LOGIN_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/doctor/login";
	/*
	 * 消息
	 */
	public static final String MESSAGE_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByDoctorUnclosed";
	/*
	 * 询问者头像
	 */
	public static final String GRAVIDAHEADPHOTO_URL = "https://yoyub.oss-cn-hangzhou.aliyuncs.com/headPhoto/";

	/*
	 * 医生资料
	 */
	public static final String DOCTORINFO_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/doctor/info";
	/**
	 * 切换开关
	 * */
	public static final String RECEIVE_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/doctor/switchReceive";
	/**
	 * 获取由医生回复的问诊历史
	 * */
	public static final String INQUIRYBYDOCTORCLOSE_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByDoctorclosed";
	/**
	 * 获取由医助回复的问诊历史
	 * */
	public static final String INQUIRYBYDOCTORWITHASSISTANTCLOSED_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByDoctorWithAssistantclosed";
	/**
	 * https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/
	 * inquiryByDoctorWithAssistantclosed
	 * 
	 * 获取待上门的上门服务列表
	 * */
	public static final String FACEBYDOCTORUNSERVICED_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/faceByDoctorUnserviced";
	/**
	 * 获取待关闭的上门服务列表
	 * */
	public static final String FACEBYDOCTORSERVICED_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/faceByDoctorServiced";
	/**
	 * 获取已完成的上门服务列表
	 * */
	public static final String FACEBYDOCTORCLOSED_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/faceByDoctorClosed";
	/**
	 * 关闭上门服务
	 * */
	public static final String CLOSE_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/close";

	/**
	 * 待上门出发
	 * */
	public static final String BEGIN_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/go";

	/**
	 * 验证上门出发
	 * */
	public static final String VERIFICATE_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/face/verificate";

	/**
	 * 根据问诊id获取单个问诊头
	 * */
	public static final String INQUIRYBYKEY_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByKey";

	/**
	 * 根据问诊id获取一批问诊头
	 * */
	public static final String INQUIRYBYKEYS_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/inquiryByKeys";

	/**
	 * 根据问诊ID获取问诊体
	 * */
	public static final String INQUIRYCONTENT_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/content";
	/**
	 * 根据问诊体ID获取单条问诊体内容
	 * */
	public static final String SINGLECONTENT_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/singleContent";

	/**
	 * 提交一条问诊体内容
	 * */
	public static final String COMMITCONTENT_URL = "https://yoyub.winning.com.cn:7062/yoyub/iPlat/inquiry/commitContent";

}

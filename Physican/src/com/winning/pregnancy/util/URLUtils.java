package com.winning.pregnancy.util;

public class URLUtils {
	// public static String HOSTMAIN = "http://192.168.1.119:8080";
	public static String HOSTMAIN = "https://yoyub.winning.com.cn:7062";
	// http://120.26.151.45:7062/yoyub/iPlat/inquiry/inquiryHasCommented
	public static String HOSTMAIN1 = " http://120.26.151.45:7062";
	public static String HOST = "120.26.151.45";

	public static String HOSTMES = "";
	public static String POST = "5222";
	public static String APIKEY = "1234567899";

	public static final String URLUPDATE = "/yoyub/iPlat/checkVersion";// 医生列表
	public static final String URLLOGINTEST = "/yoyub/iPlat/inquiry/defaultDoctor";// 医生列表
	public static final String URLREGUSER = "/yoyub/iPlat/inquiry/inquiryHasCommented";// 评价列表
	public static final String URLREGUSER1 = "/yoyub/iPlat/inquiry/inquiryHasCommented";// 评价列表
	public static final String URLRJYLS = "/yoyub/iPlat/valueAdded/queryCoinSubsidiary";// 交易历史
	public static final String URLRDQFW = "/yoyub/iPlat/inquiry/inquiryByGravidaUnfinished";// 当前服务
	public static final String URLLSFW = "/yoyub/iPlat/inquiry/inquiryByGravidaFinished";// 历史服务
	public static final String URLLWDYS = "/yoyub/iPlat/inquiry/gravidaDoctor";// 获取已关注的医生
	public static final String URLCOMMITCONTENT = "/yoyub/iPlat/inquiry/commitContent";// 提交单条问诊内容
	public static final String URLSINGLECONTENT = "/yoyub/iPlat/inquiry/singleContent";// 根据问诊内容ID，获取单条问诊聊天内容
	public static final String URLCONTENT = "/yoyub/iPlat/inquiry/content";// 根据问诊ID，获取该问诊的所有聊天内容
	public static final String URLORDER = "/yoyub/iPlat/inquiry/order";// 购买一次问诊
	public static final String URLINQUIRYBYKEY = "/yoyub/iPlat/inquiry/inquiryByKey";// 根据问诊ID，获取单个问诊会话
	public static final String URLCOMMENT = "/yoyub/iPlat/inquiry/comment";// 根据问诊ID，对本次问诊会话进行评价
	public static final String URLINQUIRYBYGRAVIDADOCTORUNCLOSED = "/yoyub/iPlat/inquiry/inquiryByGravidaDoctorUnclosed";// 根据孕妇ID，医生ID，获取正在进行中的问诊会话
	public static final String URLLOGIN = "/yoyub/iYxt/login";// 登录
	public static final String URLSENDVERIFICATIONCODE = "/yoyub/iYxt/sendVerificationCode";// 发送验证码
	public static final String URLREGIST = "/yoyub/iYxt/regist";// 注册
	public static final String URLCOINSL = "/yoyub/iPlat/valueAdded/queryCoinBuyRule";// 获取孕币购买档次
	public static final String URLFINDDZXXCITY = "/yoyub/iYxt/cityByLocated";// 根据定位名称查找
	public static final String URLFINDDZXXCITYBYPARENT = "/yoyub/iYxt/cityByProvince";// 根据父节点编码查找
	public static final String URLFINDDZXXPROVINCE = "/yoyub/iYxt/province";// 查找省份
	public static final String URLFINDYXTYYXXK = "/yoyub/iYxt/hospitalByCity";// 根据地址查找医院
	public static final String URLBUILDARCHIVE = "/yoyub/iYxt/buildArchive";// 自助建档
	public static final String URLPATIENTCARDS = "/yoyub/iYxt/patientCards";// 获取孕妇在医院的就诊卡
	public static final String URLBINDCARD = "/yoyub/iYxt/bindCard";// 获取孕妇在医院的就诊卡
	public static final String URLFINDPASSWORD = "/yoyub/iYxt/findPassword";// 找回密码
	public static final String URLMODIFYPASSWORD = "/yoyub/iYxt/modifyPassword";// 修改密码

	public static final String URLXGYFZL = "/yoyub/iPlat/modifyGravida";// 修改孕妇资料
	public static final String URLMRQD = "/yoyub/iPlat/valueAdded/everydaySign";// 每日签到
	public static final String URLLSJF = "/yoyub/iPlat/valueAdded/queryIntegralSubsidiary";// 查询积分历史

	public static final String URLJFGZ = HOSTMAIN + "/yyb/jf/jfgz.html";// 积分规则
	public static final String URLYQYS = HOSTMAIN + "/yyb/jkxj/nbnc.html";// 孕期饮食
	public static final String URLCJWT = HOSTMAIN + "/yyb/Cjwt_Cjwtlist.action";// 常见问题
	public static final String URLGLBD = HOSTMAIN + "/yyb/glcf/glby.html";// 高龄必读
	public static final String URLYEZC = HOSTMAIN + "/yyb/zcxj/bzdw.html";// 育儿政策
	public static final String URLBYZS = HOSTMAIN + "/yyb/byzs/yqzj.html";// 备孕知识
	public static final String URLBCCJJH = "/yoyub/iPlat/inspectPlan/saveAlert";// 保存产检计划
	public static final String URLSFGZ = "/yoyub/iPlat/inquiry/careDoctor";// 是否关注医生
	public static final String URLGMYB = "/yoyub/iPlat/valueAdded/buyCoin";// 根据档次ID，购买孕币

	public static final String URLYYGHKSPB = "yxt/yyghkspb.html";// 预约
	public static final String URLYYYXX = "yxt/yyyxx.html";// 预约历史
	public static final String URLDTGHKSPB = "yxt/dtghkspb.html";// 挂号
	public static final String URLYGHXX = "yxt/yghxx.html";// 挂号历史
	public static final String URLJCBGSY = "yxt/jcbgsy.html";// 检查
	public static final String URLJYBGSY = "yxt/jybgsy.html";// 检验

}
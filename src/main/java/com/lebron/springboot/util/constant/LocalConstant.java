package com.lebron.springboot.util.constant;

public class LocalConstant {

	public static final int FLAG_TRUE = 1;
	public static final int FLAG_FALSE = 0;

	public static final String SESSION_USER_KEY = "USER_NAME";
	public static final String SESSION_TOKEN_KEY = "sess";

	public static final String SERVER_FEATURE_NAME = "gridMarket";

	public static final String REQUEST_TIME_OUT = "Request time out";
	
	
	public static final String GRIDMARKET_STARTLIVE_SUCCESS = "0";
	public static final String SUCCESS_APPOINTMENTSTART = "2147483648";
	public static final String FAILED_UNKNOWN = "2147483649";
	public static final String FAILED_PEERNOTFOUND = "2147483650";//时间到了  配对还没到
	public static final String FAILED_PEERINCORRECTSTATUS = "2147483651";//配对来了  但是source状态不对、offline
	public static final String FAILED_INTERRUPTED = "2147483652";//"interrupt":true的情况下   第一个被打断的返回值   继续执行第二个任务
	public static final String FAILED_RECEIVERBUSY = "2147483653";//执行上一个预约任务
	public static final String FAILED_RECEIVERLIVING = "2147483654";//接口不支持
	public static final String FAILED_NOTSUPPORTED = "2147483655";//接口不支持
	public static final String OPERATION_NOT_DEFINED = "0x80520102";//接口不支持
	

}

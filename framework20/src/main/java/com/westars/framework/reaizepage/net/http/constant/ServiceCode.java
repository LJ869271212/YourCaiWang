package com.westars.framework.reaizepage.net.http.constant;

import java.util.HashMap;

import android.annotation.SuppressLint;

public class ServiceCode {

	// Http请求过程
	public static final int HTTP_START = 10000;
	public static final int HTTP_PROGRESS = 10001;

	// Http请求异常处理返回值
	public static final int EXCEPTION = 600;
	public static final int IO_EXCEPTION = 601;
	public static final int CLIENTPROTOCOL_EXCEPTION = 602;
	public static final int PARSE_EXCEPTION = 603;
	public static final int ILLEGALSTATE_EXCEPTION = 604;
	public static final int UNSUPPORTEDENCODING_EXCEPTION = 605;

	// http 请求异常提示
	@SuppressLint("UseSparseArrays")
	public static HashMap<Integer, String> HTTP_ERROR_INFO_MAP = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;
		{
			put(EXCEPTION, "服务器异常，请联系我们的修理工。");
			put(IO_EXCEPTION, "写入失败喽，请联系我们的修理工。");
			put(CLIENTPROTOCOL_EXCEPTION, "协议有点点问题哦，请联系我们的修理工。");
			put(PARSE_EXCEPTION, "解析出现了一点点小问题，请联系我们的修理工。");
			put(ILLEGALSTATE_EXCEPTION, "输出有点小问题，请联系我们的修理工。");
			put(UNSUPPORTEDENCODING_EXCEPTION, "字符转换出现了点小问题，请联系我们的修理工。");
		}
	};
}

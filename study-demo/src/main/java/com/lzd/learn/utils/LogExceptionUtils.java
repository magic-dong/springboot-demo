package com.lzd.learn.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 日志异常信息工具类
 * @author lzd
 * @date 2019年6月13日
 * @version
 */
public class LogExceptionUtils {

	/**
	 * 获取堆异常信息
	 * @author lzd
	 * @date 2019年6月13日:上午10:29:52
	 * @param throwable
	 * @return
	 * @description
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		try {
			throwable.printStackTrace(printWriter);
			return stringWriter.toString();
		} finally {
			printWriter.close();
		}
	}
}

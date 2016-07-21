package com.frc.teacherhelper.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateUtil {
	private static final Object lockObj = new Object();
	
	public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** ��Ų�ͬ������ģ���ʽ��sdf��Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	public static String getDateTime(String pattern) {
		return getSdf(pattern).format(new Date());
	}
	public static String getDateTime() {
		return getSdf(DEFAULT_DATETIME_PATTERN).format(new Date());
	}
	
	private static SimpleDateFormat getSdf(final String pattern) {
		ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

		// �˴���˫���жϺ�ͬ����Ϊ�˷�ֹsdfMap������������put�ظ���sdf
		if (tl == null) {
			synchronized (lockObj) {
				tl = sdfMap.get(pattern);
				if (tl == null) {
					// ֻ��Map�л�û�����pattern��sdf�Ż������µ�sdf������map
					System.out.println("put new sdf of pattern " + pattern + " to map");

					// �����ǹؼ�,ʹ��ThreadLocal<SimpleDateFormat>���ԭ��ֱ��new
					// SimpleDateFormat
					tl = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					sdfMap.put(pattern, tl);
				}
			}
		}

		return tl.get();
	}

	public static String format(Date date, String pattern) {
		return getSdf(pattern).format(date);
	}

	public static Date parse(String dateStr, String pattern) throws ParseException {
		return getSdf(pattern).parse(dateStr);
	}
}

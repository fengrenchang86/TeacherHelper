package com.frc.teacherhelper.util;

public class StringUtil {
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 ? true : false;
	}
	public static String trim(String str) {
		return isEmpty(str) ? "" : str.trim();
	}
}

package com.ssinfo.corider.app.util;

public class StringUtil {
	
	public static boolean isBlankOrNull(String value) {
		if (value != null && !value.trim().equals("")) {
			return false;
		} else {
			return true;
		}
	}

}

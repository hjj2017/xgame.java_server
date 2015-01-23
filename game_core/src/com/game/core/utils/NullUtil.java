package com.game.core.utils;

public class NullUtil {
	public static <T> T getVal(T val, T defaultVal) {
		if (val == null) {
			return defaultVal;
		} else {
			return val;
		}
	}
}

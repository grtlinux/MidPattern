package org.tain.shuffle01;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {

	private static boolean flag;

	private static Map<String, List<String>> params;

	static {
		flag = true;
		params = new HashMap<>();
	}

	///////////////////////////////////////////////////////////////////////////

	public static String getClassInfo() {
		final StackTraceElement e = Thread.currentThread().getStackTrace()[2];

		StringBuffer sb = new StringBuffer();

		if (flag) sb.append(e.getClassName()).append('.').append(e.getMethodName()).append("()");
		if (flag) sb.append(" - ");
		if (flag) sb.append(e.getFileName()).append('(').append(e.getLineNumber()).append(')');

		return sb.toString();
	}

	///////////////////////////////////////////////////////////////////////////

	public static void setParams(String[] args) throws Exception {
		List<String> options = null;
		for (String arg : args) {
			if (arg.charAt(0) == '-') {
				if (arg.length() < 2) {
					throw new IllegalArgumentException("Error at argument " + arg);
				}
				options = new ArrayList<>();
				params.put(arg.substring(0), options);
			} else if (options != null) {
				options.add(arg);
			} else {
				throw new IllegalArgumentException("Illegal parameter usage.");
			}
		}
	}

	public static List<String> getValues(String key) {
		return params.get(key);
	}

	public static String getValue(String key, int idx) {
		List<String> options = getValues(key);
		if (options == null)
			return null;

		if (idx < 0 || options.size() <= idx)
			return null;

		return options.get(idx);
	}
}

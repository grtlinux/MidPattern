package org.tain.json;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public final class JsonUtils {

	private static final boolean flag;
	private static final String className;

	static {
		flag = true;
		className = new Object(){}.getClass().getEnclosingClass().getName();
	}

	///////////////////////////////////////////////////////////////////////////

	public static Map stringToJsonMap(String strJson) {
		return stringToObject(strJson, HashMap.class);
	}

	public static Object stringToJsonClass(String strJson, Class clazz) {
		return stringToObject(strJson, clazz);
	}

	public static <T> T stringToObject(String strJson, Class<T> valueType) {
		try {
			return new ObjectMapper().readValue(strJson, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static String jsonToString(Object jsonObject) {
		return objectToString(jsonObject);
	}

	public static String gainJsonToString(Object jsonObject) {
		return gainJsonToString(jsonObject, true);
	}

	public static String gainJsonToString(Object jsonObject, boolean resultAddFlag) {
		if (resultAddFlag) {
			Map map = new HashMap();
			map.put("result", jsonObject);
			return objectToString(map);
		} else {
			return objectToString(jsonObject);
		}
	}

	public static String objectToString(Object json) {
		try {
			return new ObjectMapper().writeValueAsString(json);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String callbackObjectToString(String callback, Object json) {
		String retval = objectToString(json);
		if (!"".equals(callback)) {
			retval = callback + "(" + retval + ")";  // for javascript
		}

		return retval;
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + className);
	}
}

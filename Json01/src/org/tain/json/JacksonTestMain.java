package org.tain.json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class JacksonTestMain {

	private static final boolean flag;
	private static final String className;
	static {
		flag = true;
		className = new Object(){}.getClass().getEnclosingClass().getName();
	}

	///////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws Exception {
		if (flag) System.out.println(">>>>> " + className);

		if (flag) test01(args);  // json string -> map (1)
		if (flag) test02(args);  // json string -> map (2)
		if (flag) test03(args);  // map -> pretty print
		if (flag) test04(args);  // map -> json file
		if (flag) test05(args);  // json file -> map
		//if (flag) test06(args);
	}

	private static void test01(String[] args) throws Exception {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String strJson = "{ \"name\":\"mkgil\", \"age\":25 }";
			Map<String, Object> map = new HashMap<String, Object>();
			map = objectMapper.readValue(strJson, new TypeReference<Map<String, Object>>(){});
			System.out.println(">>>>> " + map);
			System.out.println(">>>>> " + map.get("name"));
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test02(String[] args) throws Exception {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String strJson = "{ \"name\":\"mkyoun\", \"age\":29, \"messages\": [ \"msg 1\", \"msg 2\", \"msg 3\" ]}";
			Map<String, Object> map = new HashMap<String, Object>();
			map = objectMapper.readValue(strJson, new TypeReference<Map<String, Object>>(){});
			System.out.println(">>>>> " + map);
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) map.get("messages");
			System.out.println(">>>>> " + list);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test03(String[] args) throws Exception {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "mkgil");
			map.put("age", 25);

			ObjectMapper objectMapper = new ObjectMapper();
			String strJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(map);
			System.out.println(">>>>> " + strJson);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test04(String[] args) throws Exception {

		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", "mkyoun");
			map.put("age", 25);
			List<String> lstMessages = new ArrayList<String>();
			lstMessages.add("msg 1");
			lstMessages.add("msg 2");
			lstMessages.add("msg 3");
			lstMessages.add("msg 4");
			map.put("messages", lstMessages);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(new File("user.json"), map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void test05(String[] args) throws Exception {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(new File("user.json"), new TypeReference<Map<String, Object>>(){});
			System.out.println(">>>>> " + map);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

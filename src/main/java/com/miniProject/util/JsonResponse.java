package com.miniProject.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;


public class JsonResponse {
	public static Map<String, Object> toMap(String json) {

		Map<String, Object> map = new HashMap<String, Object>();

		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(json, new TypeReference<HashMap<String, Object>>() {});
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
	}
	
	public static String toJSON(Map<String, Object> map){
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void main(String [] args) {
		String code = "{\"code\":2,\"info\":{\"name\":1}}";
		System.out.println(JsonResponse.toMap(code));

	}

}
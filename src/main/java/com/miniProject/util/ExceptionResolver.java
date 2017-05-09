package com.miniProject.util;

import java.util.Map;

import org.codehaus.jackson.JsonParseException;

public class ExceptionResolver {
	public static Map<String, Object> catchExceptionToRet(Exception ex){
		if(ex instanceof JsonParseException){
			return RetMapUtils.createRetMap(8401, "请求参数错误");
		}
		return null;
	}
}

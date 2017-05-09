package com.miniProject.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RetMapUtils {
	/***
	 *数字范围判断(int,double) 
	 **/
	public static Map<String, Object> affirmIllageDigit(Object val,Object start,Object end,String msg,int code){
		if(val==null){
			return createRetMap(code, msg);
		}else if(val instanceof Integer){
			if((Integer)val < (Integer)start||(Integer)val >(Integer)end){
				return createRetMap(code,msg);
			}
		}else if(val instanceof Double){
			if((Double)val < (Double)start||(Double)val >(Double)end){
				return createRetMap(code,msg);
			}
		}
		return null;
	}
	public static Map<String, Object> createRetMap(int code,String msg){
		Map<String, Object>retMap=new HashMap<>();
		retMap.put("code", code);
		retMap.put("msg", msg);
		return retMap;
	}
	public static Map<String, Object> affirmNullWithMap(int code,String msg,Object ...sts){
		if(affirmNull(sts))
			return createRetMap(code, msg);
		return null;
	}
	public static Map<String, Object> affirmNullStringWithMap(int code,String msg,String ...sts){
		if(affirmNullString(sts))
			return createRetMap(code, msg);
		return null;
	}
	public static boolean affirmNullString(String ...sts){
		for(String st:sts){
			if(st==null||st.trim().length()<1){
				return true;
			}
		}
		return false;
	}
	public static boolean affirmNull(Object ...sts){
		for(Object st:sts){
			if(st==null){
				return true;
			}
		}
		return false;
	}
	/**
	  *判断时间是否过期，true表示已过期，false表示未过期
	  *@param date 需要判断的时间 
	  **/
	 public static boolean expirationTime(Date date){
		 Date nowTime=new Date((System.currentTimeMillis()-(30*60*1000)));
		 return compareTo(nowTime,date);
	 }
	 /**
	  *比较两个时间，如果第一个时间比较迟，则为true，否则为false
	  *@param firstDate 
	  *@param secondDate
	  **/
	private static boolean compareTo(Date firstDate,Date secondDate){
		return (firstDate.getTime()>secondDate.getTime())?true:false;
	}
}

package com.example.bot.spring.echo;

import java.lang.reflect.Field;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

public class JsonUtil {
	
	private static ExclusionStrategy skipDesc;
	private static Gson gson;
	
	public static String objectToString(Object obj) {
		init();
		String result = new Gson().toJson(obj);
		return result;
	};
	
	public static <T> T stringToObject(String json, Class<T> clazz) {
		init();
		T result = new Gson().fromJson(json, clazz);
		return result;
	};
	
	public static <T> T stringToObject(String json, Type type) {
		init();
		T result = new Gson().fromJson(json, type);
		return result;
	};
	
	/**
	 * 初始化略過欄位
	 */
	static void init(){
		if(gson == null || skipDesc == null){
			skipDesc = new Exclude();
			gson = new GsonBuilder().addDeserializationExclusionStrategy(skipDesc).addSerializationExclusionStrategy(skipDesc).create();
		}
		
	}
	
	/**
	 * 避免因為父子物件有相同欄位造成 Gson 噴錯的問題
	 * @author Yang
	 *
	 */
	protected static class Exclude implements ExclusionStrategy {

	    @Override
	    public boolean shouldSkipClass(Class<?> arg0) {
	        // 所有Class都該印出Log
	        return false;
	    }

	    @Override
	    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
	    	String fieldName = fieldAttributes.getName();
	        Class<?> theClass = fieldAttributes.getDeclaringClass();
	        
	        // 判斷是否有重複的欄位
	        return isFieldInSuperclass(theClass, fieldName);
	    }
	    private boolean isFieldInSuperclass(Class<?> subclass, String fieldName)
	    {
	    	// 取得父類別
	        Class<?> superclass = subclass.getSuperclass();
	        Field field;

	        while(superclass != null)
	        {   
	        	//尋找父類別中相同欄位名稱的欄位
	            field = getField(superclass, fieldName);

	            if(field != null)//有找到則不印出Log
	                return true;

	            //迴圈找出多層繼承關係的類別中是否有重複的欄位
	            superclass = superclass.getSuperclass();
	        }

	        return false;
	    }

	    private Field getField(Class<?> theClass, String fieldName)
	    {
	        try
	        {
	            return theClass.getDeclaredField(fieldName);
	        }
	        catch(Exception e)
	        {
	            return null;
	        }
	    }
	}
}

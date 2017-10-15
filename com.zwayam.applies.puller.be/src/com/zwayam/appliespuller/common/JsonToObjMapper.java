package com.zwayam.appliespuller.common;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import com.zwayam.appliespuller.mongo.models.JobApplication;

public class JsonToObjMapper {

	public static JobApplication convertToJobApp(HashMap<String, Object> hm, HashMap<String, String> fieldMap) throws java.text.ParseException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		JobApplication jobApp = new JobApplication();
		for (String key : fieldMap.keySet()) {
				if (hm.containsKey(fieldMap.get(key))) {
					Field declaredField = jobApp.getClass().getDeclaredField(key);					
					if(declaredField.getType().getName().equals(Date.class.getTypeName()))
					{
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Long dateInMillSec = Long.parseLong(hm.get(fieldMap.get(key)).toString()
								.replaceAll("\\/Date|\\(|\\+0000|\\)\\/", ""));
						formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
						declaredField.set(jobApp, formatter.parse(formatter.format(new Date(dateInMillSec)))); 
						
					}
					else if(declaredField.getType().getName().equals(Integer.class.getName())) 
					{						
						declaredField.set(jobApp, hm.get(fieldMap.get(key))); 
					}
					else 
					{	
						declaredField.set(jobApp, hm.get(fieldMap.get(key)));
					}
				}
			}
		return jobApp;
	}

}

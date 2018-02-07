package com.smart.iworld.rpc.discover;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CustomMethodIntercept implements MethodInterceptor {

	@Override
	public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {


		return null;
	}

	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Date date = getBeforeDay(31);
		System.out.println(System.currentTimeMillis() - startTime);
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date());
		
		System.out.println(date);
		gc.set(Calendar.AM_PM, 0);
		System.out.println(gc.getTime());
		System.out.println(gc.get(Calendar.AM));
	}
	
	public static Date getBeforeDay(int count){
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date());
		gc.add(Calendar.DAY_OF_MONTH, -count);
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date date = gc.getTime();
		try {
			date = df.parse(df.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getBeforeDay1(int count){
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date());
		gc.add(Calendar.DAY_OF_MONTH, -count);
		gc.set(Calendar.HOUR, -12);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		//gc.set(Calendar.AM_PM, 0);
		
		return gc.getTime();
	}
	
}

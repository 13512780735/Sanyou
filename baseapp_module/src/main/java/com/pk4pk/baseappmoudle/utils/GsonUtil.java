package com.pk4pk.baseappmoudle.utils;

import android.util.Log;

import com.google.gson.Gson;


/**
 *
 * @Author : 蔡文锋
 * @DateTime：2015年5月30日 上午1:36:37
 * @Description : Gson
 */

public class GsonUtil {
	private static final String TAG = "GsonUtil";
	private  static Gson gson=new Gson();

    private GsonUtil(){

    }
    
    //获取实例
    public static Gson getInstall(){
    	synchronized (GsonUtil.class) {
    		if(gson==null){
        		gson=new Gson();
        	}
		}
    	return gson;
    }

    
    //解析json
    public static <T> T GetFromJson(String json, Class<T> t){
    	try {
    		 return gson.fromJson(json,t);
		} catch (Exception e) {
			Log.e(TAG, "解析json出错  :" + e.getMessage());
			return null;
		}
    }
    
    public static String ObjectToJson(Object o){
    	return gson.toJson(o);
    }

	
}

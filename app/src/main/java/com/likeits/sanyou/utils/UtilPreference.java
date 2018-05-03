package com.likeits.sanyou.utils;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferece 工具类
 */
public class UtilPreference {

	/**
	 * SharedPreference名称
	 */
	private static final String PREFERENCE_FILE_NAME = "dock_preferences";

	/**
	 * 添加String串到SharedPreference中
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void saveString(final Context context, final String key,
                                  final String value) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor = preference.edit();
		editor.putString(key, value);
		editor.commit();
	}


	/**
	 * 清除本地所有数据
	 * 
	 * @param context
	 */
	public static void clearLocalValues(final Context context) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor = preference.edit();
		editor.clear();
		editor.commit();
	}
	
	/**
	 * 清除除用户名、密码数据之外的所有本地数据
	 * 
	 * @param context
	 */
	public static void clearNotKeyValues(final Context context) {
		String userName = getStringValue(context, "userName");
		String password = getStringValue(context, "password");
		//boolean setUpGuide = getBooleanValue(context, "setUpGuide");
		String login_history = getStringValue(context, "login_history");
		clearLocalValues(context);
		
		saveString(context, "userName", userName);
		saveString(context, "password", password);
		saveString(context, "login_history", login_history);
		//saveBoolean(context, "setUpGuide", setUpGuide);
	}

	/**
	 * 添加int到SharedPreference中
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void saveInt(final Context context, String key, int value) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor = preference.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * 添加boolean到SharedPreference中
	 * 
	 * @param context
	 *            Context
	 * @param key
	 *            键
	 * @param value
	 *            值
	 */
	public static void saveBoolean(final Context context, String key,
                                   Boolean value) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preference.edit();
		editor = preference.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * 获取String
	 * 
	 * @param context
	 * @param key
	 *            名称
	 * @return 键对应的值，如果找不到对应的值， 则返回Null
	 */
	public static String getStringValue(final Context context, final String key) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return preference.getString(key, null);
	}

	/**
	 * 获取Boolean
	 * 
	 * @param context
	 * @param key
	 *            名称
	 * @return 键对应的值，如果找不到对应的值， 则返回false
	 */
	public static boolean getBooleanValue(final Context context,
			final String key) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return preference.getBoolean(key, false);
	}

	/**
	 * 获取String
	 * 
	 * @param context
	 * @param key
	 *            名称
	 * @return 键对应的值，如果找不到对应的值， 则返回-1
	 */
	public static int getIntValue(final Context context, final String key) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return preference.getInt(key, -1);
	}
	
	/**
	 * 获取String
	 * 
	 * @param context
	 * @param key
	 *            名称
	 * @param defaultValue
	 * @return 键对应的值，如果找不到对应的值， 则返回默认值
	 */
	public static int getIntValue(final Context context, final String key, int defaultValue) {
		SharedPreferences preference = context.getSharedPreferences(
				PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return preference.getInt(key, defaultValue);
	}

	

}

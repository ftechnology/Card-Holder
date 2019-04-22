package com.rfit.card.holder.digitalholder.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class SharedPrefUtil {

	public static final String KEY_REGISTERD = "registered";
	public static final String KEY_PIN = "pin";
	public static final String KEY_SECREET_QUESTION = "secreet_ques";
	public static final String KEY_SECREET_ANSWER = "secreet_ans";
	
	

	/**
	 * Set value in shared preference
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setSetting(Context context, String key, String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	/**
	 * Set boolean value in shared preference
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setSetting(Context context, String key, boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	/**
	 * Set int value in shared preference
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void setSetting(Context context, String key, int value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();

	}

	/**
	 * get value from shared preference
	 * 
	 * @param context
	 * @param key
	 * @return string
	 */
	public static String getSetting(Context context, String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(key, null);

	}

	/**
	 * get value from shared preference if not found return given default value
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return string
	 */
	public static String getSetting(Context context, String key,
                                    String defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getString(key, defaultValue);

	}

	/**
	 * get boolean value from shared preference if not found return given
	 * default value
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return boolean
	 */
	public static boolean getBooleanSetting(Context context, String key,
                                            boolean defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getBoolean(key, defaultValue);

	}

	/**
	 * get integer value from shared preference if not found return given
	 * default value
	 * 
	 * @param context
	 * @param key
	 * @param defaultValue
	 * @return boolean
	 */
	public static int getIntSetting(Context context, String key,
                                    int defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getInt(key, defaultValue);

	}

	/**
	 * remove item from shared preference
	 * 
	 * @param context
	 * @param key
	 */
	public static void removeSetting(Context context, String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public static boolean isExistKey(Context context, String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.contains(key);
	}

	public static long getLongSetting(Context context, String key,
                                      long defaultValue) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getLong(key, defaultValue);

	}

	public static void setSetting(Context context, String key, long value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putLong(key, value);
		editor.commit();

	}

	public static float getFloatSetting(Context context, String key,
                                        float defValue) {

		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sp.getFloat(key, defValue);
	}

	public static void setSetting(Context context, String key, float value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putFloat(key, value);
		editor.commit();

	}
}

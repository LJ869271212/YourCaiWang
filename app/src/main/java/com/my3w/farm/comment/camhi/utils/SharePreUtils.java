package com.my3w.farm.comment.camhi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * �����ļ�(ƫ�����ù�����)
 * @author  creat by lt
 *
 */
public class SharePreUtils {
	/**
	 * �洢�ַ����������ļ���
	 * @param preName  �ļ���
	 * @param context
	 * @param key      �洢�ļ�
	 * @param values   ��Ҫ��ŵ�ֵ
	 * @return         ����ɹ��ı�־
	 */
	public static Boolean putString(String preName,Context context,String key,String values){
		SharedPreferences shared=context.getSharedPreferences(preName, context.MODE_PRIVATE);
		Editor editor=shared.edit();
		editor.putString(key, values);
		return editor.commit();
	}
	/**
	 * �洢���ֵ������ļ���
	 * @param preName �ļ���
	 * @param context
	 * @param key     �洢�ļ� 
	 * @param values  ��Ҫ��ŵ�ֵ
	 * @return        ����ɹ��ı�־
	 */ 
	public static Boolean putInt(String preName,Context context,String key,int values){
		SharedPreferences shared=context.getSharedPreferences(preName, context.MODE_PRIVATE);
		Editor editor=shared.edit();
		editor.putInt(key, values);
		return editor.commit();
	}
	
	
	
	/**
	 * �������ļ��ж�ȡ�ַ���
	 * @param preName  �ļ���
	 * @param context
	 * @param key      ��ֵ
	 * @return         ��ֵ��Ӧ���ַ�����Ĭ�Ϸ��� ""
	 */
	public static String getString(String preName,Context context,String key){
		SharedPreferences shared=context.getSharedPreferences(preName, context.MODE_PRIVATE);
		return shared.getString(key, "");
	}
	
	/**
	 * �������ļ��ж�ȡ        int
	 * @param preName �ļ���
	 * @param context
	 * @param key     ��ֵ
	 * @return        ��ֵ��Ӧ��int Ĭ�Ϸ���-1
	 */
	public static int getInt(String preName,Context context,String key){
		SharedPreferences shared=context.getSharedPreferences(preName, context.MODE_PRIVATE);
		return shared.getInt(key, -1);
		
	}
	
	
	

}







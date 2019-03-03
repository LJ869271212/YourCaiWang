package com.westars.framework.util.hash;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.annotation.SuppressLint;
import android.util.Log;

public class Md5 {

	private static String LOG_TAG = "FileMd5";

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public Md5() {

	}

	/**
	 * 字符串MD5
	 * 
	 * @param str
	 *            - 字符串s
	 * @return MD5值
	 */
	public static String StringMd5(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] bs = md5.digest(str.getBytes());
			StringBuilder sb = new StringBuilder(40);
			for (byte x : bs) {
				if ((x & 0xff) >> 4 == 0) {
					sb.append("0").append(Integer.toHexString(x & 0xff));
				} else {
					sb.append(Integer.toHexString(x & 0xff));
				}
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			Log.w(LOG_TAG, e.toString());
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 文件MD5
	 * 
	 * @param filename
	 *            - 文件路径
	 * @return MD5值
	 */
	public static String FileMd5(String filename) {
		InputStream fis;
		byte[] buffer = new byte[1024];
		int numRead = 0;
		MessageDigest md5;
		try {
			fis = new FileInputStream(filename);
			md5 = MessageDigest.getInstance("MD5");
			while ((numRead = fis.read(buffer)) > 0) {
				md5.update(buffer, 0, numRead);
			}
			fis.close();
			return _toHexString(md5.digest());
		} catch (Exception e) {
			Log.w(LOG_TAG, e.toString());
			e.printStackTrace();
			return "";
		}
	}

	@SuppressLint("DefaultLocale")
	protected static String _toHexString(byte[] b) {
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString().toLowerCase();
	}
}

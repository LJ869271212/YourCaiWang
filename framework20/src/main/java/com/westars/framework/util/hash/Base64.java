package com.westars.framework.util.hash;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.util.Log;

public class Base64 {
	private static String LOG_TAG = "Base64";

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

	/**
	 * 字符串加密，编码模式
	 * 
	 * @param str
	 *            - 需要加密的字符串
	 * @param charset
	 *            - 编码格式：UTF-8，GB2312
	 * @return
	 */
	public static String base64encodeString(String str, String charset) {
		Base64 base = new Base64();
		String base64String;
		try {
			base64String = base.encode(str.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			base64String = "";
			Log.w(LOG_TAG, e.toString());
		}
		return base64String;
	}

	/**
	 * 字符串加密
	 * 
	 * @param str
	 *            - 需要加密的字符串
	 * @return Base64
	 */
	public static String base64encodeString(String str) {
		Base64 base = new Base64();
		String base64String;
		try {
			base64String = base.encode(str.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			base64String = "";
			Log.w(LOG_TAG, e.toString());
		}
		return base64String;
	}

	/**
	 * 文件加密
	 * 
	 * @param path
	 *            - 文件路径
	 * @return Base64
	 */
	public static String base64encodeFile(String path) {
		String base64String;
		try {
			File file = new File(path);
			FileInputStream inputFile = new FileInputStream(file);
			byte[] buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();

			Base64 base = new Base64();
			base64String = base.encode(buffer);
		} catch (FileNotFoundException e) {
			base64String = "";
			Log.w(LOG_TAG, e.toString());
		} catch (IOException e) {
			base64String = "";
			Log.w(LOG_TAG, e.toString());
		}
		return base64String;
	}

	/**
	 * 字符串解密，编码模式
	 * 
	 * @param str
	 *            Base64内容
	 * @param charset
	 *            - 编码格式：UTF-8，GB2312
	 * @return 解密后的字符串
	 */
	public static String base64decodeString(String str, String charset) {
		Base64 base = new Base64();
		byte[] s = base.decode(str);
		String rstr;
		try {
			rstr = new String(s, charset);
		} catch (UnsupportedEncodingException e) {
			rstr = "";
			Log.w(LOG_TAG, e.toString());
		}
		return rstr;
	}

	/**
	 * 字符串解密
	 * 
	 * @param str
	 *            Base64内容
	 * @return 解密后的字符串
	 */
	public static String base64decodeString(String str) {
		Base64 base = new Base64();
		byte[] s = base.decode(str);
		return new String(s);
	}

	private String encode(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;
		int n = 0;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 0x0ff) << 8) | (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;

			if (n++ >= 14) {
				n = 0;
				buf.append(" ");
			}
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16) | ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}
		return buf.toString();
	}

	private byte[] decode(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			_decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private int _decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	private void _decode(String s, OutputStream os) throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;

			if (i == len)
				break;

			int tri = (_decode(s.charAt(i)) << 18) + (_decode(s.charAt(i + 1)) << 12) + (_decode(s.charAt(i + 2)) << 6)
					+ (_decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);

			i += 4;
		}
	}
}

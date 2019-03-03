package com.westars.framework.reaizepage.download.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

import com.westars.framework.util.hash.Md5;

public class FileInfo {

	private String url;

	private String savePath;

	/**
	 * 文件信息类
	 * 
	 * @param url
	 *            文件下载地址
	 * @param savePath
	 *            文件保存路径
	 */
	public FileInfo(String url, String savePath) {
		this.url = url;
		this.savePath = savePath;
	}

	/**
	 * 返回文件名称
	 * 
	 * @return
	 */
	public String getFileName() {
		if (!url.equals("")) {
			if (url.indexOf("?") > -1) {
				Log.e("xxz", url);
				String[] b = url.split("[?]");
				if (b.length > 0) {
					url = b[0];
				}
			}

			String[] a = url.split("/");
			if (a.length > 0) {
				return a[a.length - 1];
			}
		}
		return "";
	}

	/**
	 * 返回文件路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		return savePath + getFileName();
	}

	/**
	 * 返回文件是否存在
	 * 
	 * @return
	 */
	public boolean isFilePath() {
		File file = new File(getFilePath());
		return file.exists();
	}

	/**
	 * 返回临时文件路径
	 * 
	 * @return
	 */
	public String getFileTempPath() {
		return savePath + getFileName() + ".tmp";
	}

	/**
	 * 返回临时文件是否存在
	 * 
	 * @return
	 */
	public boolean isFileTempPath() {
		File file = new File(getFileTempPath());
		return file.exists();
	}

	/**
	 * 返回配置文件
	 * 
	 * @return
	 */
	public String getFileCfgPath() {
		return savePath + getFileName() + ".cfg";
	}

	/**
	 * 返回配置文件是否存在
	 * 
	 * @return
	 */
	public boolean isFileCfgPath() {
		File file = new File(getFileCfgPath());
		return file.exists();
	}

	/**
	 * 返回TempMD5
	 * 
	 * @return
	 */
	public String getTempMd5() {
		if (isFileTempPath()) {
			return Md5.FileMd5(getFileTempPath());
		} else {
			return "";
		}
	}

	/**
	 * 返回MD5
	 * 
	 * @return
	 */
	public String getMd5() {
		if (isFilePath()) {
			return Md5.FileMd5(getFilePath());
		} else {
			return "";
		}
	}

	/**
	 * 检测完整度
	 */
	public void isComplete() {
		if (isFileTempPath()) {
			File fileTemp = new File(getFileTempPath());
			if (fileTemp.length() <= 0) {
				fileTemp.delete();
				if (isFileCfgPath()) {
					File fileCfg = new File(getFileCfgPath());
					fileCfg.delete();
				}
			}
		}
	}

	/**
	 * 返回下载配置位置
	 * 
	 * @return
	 */
	public long getConfig() {
		File file = new File(getFileCfgPath());
		if (file.exists()) {
			try {
				FileInputStream fis = new FileInputStream(file);
				int length = fis.available();
				byte[] buffer = new byte[length];
				fis.read(buffer);
				String res = EncodingUtils.getString(buffer, "UTF-8");
				fis.close();
				if (!res.equals("")) {
					try {
						long star = Long.parseLong(res);
						return star;
					} catch (Exception e) {
						return 0;
					}
				} else {
					return 0;
				}
			} catch (FileNotFoundException e) {
				return 0;
			} catch (IOException e) {
				return 0;
			}
		} else {
			return 0;
		}
	}

	/**
	 * 写入配置文件
	 * 
	 * @param val
	 */
	public boolean setConfig(String val) {
		try {
			File file = new File(getFileCfgPath());
			FileOutputStream fop = new FileOutputStream(file);
			byte[] contentInBytes = val.getBytes();
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 把临时文件重命名为新文件
	 */
	public void copyTemp() {
		// 判断文件是否存在，存在删除
		if (isFilePath()) {
			File file = new File(getFilePath());
			file.delete();
		}

		// 判断临时文件是否存在
		if (isFileTempPath()) {
			// 获取临时文件
			File file = new File(getFileTempPath());

			// 重命名临时文件
			file.renameTo(new File(getFilePath()));

			// 删除临时文件
			file.delete();

			// 删除临时配置
			File files = new File(getFileCfgPath());
			files.delete();
		}
	}

	public String toString() {
		return "FileName = " + getFileName() + ", FilePath = " + getFilePath() + ", isFilePath = " + isFilePath() + ", FileTempPath = "
				+ getFileTempPath() + ", isFileTempPath = " + isFileTempPath() + ", MD5 = " + getMd5() + ", Config = " + getConfig()
				+ ", temp md5:" + getTempMd5();
	}
}

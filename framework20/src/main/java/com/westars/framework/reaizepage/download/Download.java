package com.westars.framework.reaizepage.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

import com.westars.framework.reaizepage.download.util.FileInfo;
import com.westars.framework.reaizepage.download.util.QiniuMd5;

/**
 * 断点续传下载类
 * 
 * <pre>
 * // 下载demo
 * Download down = new Download(&quot;http://7xk3ee.media1.z0.glb.clouddn.com/fengsizhenming.mp4&quot;, &quot;/storage/sdcard0/Download/&quot;);
 * down.setDownloadComplete(new DowloadComplete() {
 * 
 * 	&#064;Override
 * 	public void complete(File file) {
 * 		Log.i(&quot;xxz&quot;, &quot;Download complete file:&quot; + file.getPath());
 * 	}
 * });
 * down.setDownloadError(new DowloadError() {
 * 
 * 	&#064;Override
 * 	public void error(int what, String error) {
 * 		Log.i(&quot;xxz&quot;, &quot;Download error:&quot; + what + &quot;,&quot; + error);
 * 	}
 * });
 * down.setDownloadProgress(new DowloadProgress() {
 * 
 * 	&#064;Override
 * 	public void progress(float val) {
 * 		Log.i(&quot;xxz&quot;, &quot;Download progress:&quot; + val);
 * 	}
 * });
 * down.start();
 * </pre>
 * 
 * @author Aports
 * 
 */
public class Download {

	public final static int MD5_NULL = 1;

	public final static int TEMPFILE_CREATE_ERROR = 2;

	public final static int CFGFILE_CREATE_ERROR = 3;

	public final static int CFGFILE_WRITE_ERROR = 4;

	public final static int GET_MD5_ERROR = 5;

	private String TAG = "Download";

	private String url;

	private String savePath;

	private String md5;

	private boolean get;

	private DowloadProgress ProgressListener;

	private DowloadComplete CompleteListener;

	private DowloadError ErrorListener;

	// 下载状态枚举
	public enum DOWLOAD_STATE {
		NONE, DOWLOAD, SUCCESS, ERROR
	};

	// 下载状态记录
	public DOWLOAD_STATE DOWLOAD_FLAG = DOWLOAD_STATE.NONE;

	// 是否结束下载
	private boolean isExit = false;

	/**
	 * 文件下载初始化（七牛服务器直接使用当前方法）
	 * 
	 * @param url
	 * @param savePath
	 * @param get
	 *            是否获取七牛MD5
	 */
	public Download(String url, String savePath, boolean get) {
		this.url = url;
		this.savePath = savePath;
		this.get = get;
	}

	/**
	 * 文件下载初始化（非七牛使用当前方法）
	 * 
	 * @param url
	 * @param savePath
	 * @param md5
	 */
	public Download(String url, String savePath, String md5) {
		this.url = url;
		this.savePath = savePath;
		this.md5 = md5;

		// 校验md5是否为空
		if (md5 == null || md5.equals("")) {
			throw new IllegalStateException("is file md5 null!");
		}
	}

	/**
	 * 设置下载进度回调
	 * 
	 * @param listener
	 */
	public void setDownloadProgress(DowloadProgress listener) {
		this.ProgressListener = listener;
	}

	/**
	 * 设置下载完成回调
	 * 
	 * @param listener
	 */
	public void setDownloadComplete(DowloadComplete listener) {
		this.CompleteListener = listener;
	}

	/**
	 * 设置下载错误回调
	 * 
	 * @param listener
	 */
	public void setDownloadError(DowloadError listener) {
		this.ErrorListener = listener;
	}

	/**
	 * 开始下载
	 */
	public void start() {
		// 表示为开始下载
		DOWLOAD_FLAG = DOWLOAD_STATE.DOWLOAD;
		isExit = false;

		new DownloadThread().start();
	}

	/**
	 * 终止下载
	 */
	public void end() {
		isExit = true;
	}

	/**
	 * 返回下载状态
	 * 
	 * @return
	 */
	public DOWLOAD_STATE getState() {
		return DOWLOAD_FLAG;
	}

	/**
	 * 下载进度条
	 * 
	 * @author Aports
	 * 
	 */
	public interface DowloadProgress {
		public void progress(float val);
	}

	/**
	 * 下载完成
	 * 
	 * @author Aports
	 * 
	 */
	public interface DowloadComplete {
		public void complete(File file);
	}

	/**
	 * 下载错误
	 * 
	 * @author Aports
	 * 
	 */
	public interface DowloadError {
		public void error(int what, String error);
	}

	/**
	 * 返回文件是否存在
	 * 
	 * @return
	 */
	public boolean isFile() {
		FileInfo info = new FileInfo(url, savePath);
		return info.isFilePath();
	}

	/**
	 * 下载类
	 * 
	 * @author Aports
	 * 
	 */
	public class DownloadThread extends Thread {
		@Override
		public void run() {
			// 判断是否获取外部MD5
			if (get) {
				if (md5 == null || md5.equals("")) {
					md5 = new QiniuMd5(url).getMd5();
				} else {
					md5 = "";
				}
			} else {
				md5 = "";
			}

			// 获取文件信息
			FileInfo info = new FileInfo(url, savePath);

			// 判断文件是否存在
			if (info.isFilePath()) {
				// 文件存在直接返回文件路径
				File file = new File(info.getFilePath());
				if (file.length() > 0) {
					// 当数据不为0的时候
					if (CompleteListener != null)
						CompleteListener.complete(file);
					DOWLOAD_FLAG = DOWLOAD_STATE.SUCCESS;
				} else {
					// 当数据为0的时候
					file.delete();
					if (md5 != null && !md5.equals("")) {
						// 存在MD5，MD5不匹配，读取配置开始再次下载
						download(info, false);
					} else {
						// 不存在MD5，进入下载后读取配置文件和文件大小对比，如果相同直接完成
						download(info, true);
					}
				}
			} else {
				// 文件不存在开始下载

				// 判断临时文件是否存在
				if (info.isFileTempPath()) {
					// 判断md5是否存在
					if (md5 != null && !md5.equals("")) {
						// 判断MD5是否对的
						if (md5.equals(info.getTempMd5())) {
							// 存在MD5，并且完整度为100，直接完成命名，返回路径
							info.copyTemp();

							File file = new File(info.getFilePath());
							if (CompleteListener != null)
								CompleteListener.complete(file);
							DOWLOAD_FLAG = DOWLOAD_STATE.SUCCESS;
						} else {
							// 存在MD5，MD5不匹配，读取配置开始再次下载
							download(info, false);
						}
					} else {
						// 不存在MD5，进入下载后读取配置文件和文件大小对比，如果相同直接完成
						download(info, true);
					}
				} else {
					// 临时文件不存在，开始直接下载
					download(info, false);
				}
			}

			Log.i(TAG, "Download " + info.toString() + ", get MD5:" + md5);
		}

		/**
		 * 开始下载
		 * 
		 * @param info
		 */
		public void download(FileInfo info, boolean sizeCheck) {

			// 下载前检测配置完整度
			info.isComplete();

			// 判断文件夹是否存在
			File file = new File(savePath);
			if (!file.exists()) {
				file.mkdirs();
			}

			// 创建临时文件
			if (!info.isFileTempPath()) {
				try {
					File fileTemp = new File(info.getFileTempPath());
					fileTemp.createNewFile();
				} catch (IOException e) {
					if (ErrorListener != null)
						ErrorListener.error(TEMPFILE_CREATE_ERROR, e.toString());
					DOWLOAD_FLAG = DOWLOAD_STATE.ERROR;
				}
			}

			// 创建临时记录文件
			if (info.isFileTempPath()) {
				if (!info.isFileCfgPath()) {
					try {
						File fileCfg = new File(info.getFileCfgPath());
						fileCfg.createNewFile();
					} catch (IOException e) {
						if (ErrorListener != null)
							ErrorListener.error(CFGFILE_CREATE_ERROR, e.toString());
						DOWLOAD_FLAG = DOWLOAD_STATE.ERROR;
					}
				}
			}

			// 判断当前下载是否是下载状态
			if (DOWLOAD_FLAG == DOWLOAD_STATE.DOWLOAD) {
				// 判断临时文件和临时配置是否已经存在
				if (info.isFileTempPath() && info.isFileCfgPath()) {
					// 获取开始下载的位置
					long downloadStart = info.getConfig();

					Log.i("xxz", "gif：download get config " + downloadStart);

					try {
						URL urls = new URL(url);

						// 获取文件大小
						HttpURLConnection conn_test = (HttpURLConnection) urls.openConnection();
						conn_test.setConnectTimeout(3000);
						conn_test.setReadTimeout(6000);
						conn_test.connect();
						int size = conn_test.getContentLength();

						// 判断是否不存在MD5，如果不存在就直接用大小对比下载完整度
						if (sizeCheck) {
							// 对比如果大于等于配置文件大小的时候直接重命名
							if (downloadStart >= size) {
								info.copyTemp();
								// 完成后返回
								if (CompleteListener != null) {
									File files = new File(info.getFilePath());
									CompleteListener.complete(files);
									DOWLOAD_FLAG = DOWLOAD_STATE.SUCCESS;
								}
								return;
							}
						}

						// 开始下载文件
						HttpURLConnection conn = (HttpURLConnection) urls.openConnection();
						conn.setRequestProperty("Range", "bytes=" + downloadStart + "-" + size);
						conn.connect();

						InputStream is = conn.getInputStream();

						RandomAccessFile oSavedFile = new RandomAccessFile(info.getFileTempPath(), "rw");
						oSavedFile.seek(downloadStart);

						// 记录下载大小
						int downloadSize = 0;

						byte buf[] = new byte[1024];
						do {
							int numread = is.read(buf);
							if (numread <= 0) {
								info.copyTemp();
								// 完成后返回
								if (CompleteListener != null) {
									File files = new File(info.getFilePath());
									CompleteListener.complete(files);
									DOWLOAD_FLAG = DOWLOAD_STATE.SUCCESS;
								}
								break;
							} else {
								// 第一次查看配置是有存在，如果存在递增到内容中，并且设置为0
								if (downloadStart > 0) {
									downloadSize += downloadStart;
									downloadStart = 0;
								}

								// 记录下载大小
								downloadSize += numread;

								// 写入配置
								if (info.setConfig(String.valueOf(downloadSize))) {
									// 进度条
									float percent = (float) (downloadSize) / size;
									if (ProgressListener != null) {
										ProgressListener.progress(percent * 100);
									}
								} else {
									if (ErrorListener != null)
										ErrorListener.error(CFGFILE_WRITE_ERROR, "cfg写入错误");
									DOWLOAD_FLAG = DOWLOAD_STATE.ERROR;
									break;
								}
							}
							oSavedFile.write(buf, 0, numread);

							// 手动结束
							if (isExit) {
								break;
							}
						} while (true);
						oSavedFile.close();
						is.close();
					} catch (MalformedURLException e) {
						if (ErrorListener != null)
							ErrorListener.error(CFGFILE_WRITE_ERROR, e.toString());
						DOWLOAD_FLAG = DOWLOAD_STATE.ERROR;

						Log.i("xxz", "Download error MalformedURLException " + e.toString());
					} catch (IOException e) {
						if (ErrorListener != null)
							ErrorListener.error(CFGFILE_WRITE_ERROR, e.toString());
						DOWLOAD_FLAG = DOWLOAD_STATE.ERROR;

						Log.i("xxz", "Download error IOException " + e.toString());
					}
				}
			}
		}
	};

}

package com.zet.utils;

import java.io.File;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.lidroid.xutils.utils.LogUtils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

public class PdfUtils {

	/**
	 * 本地版本号
	 */
	public static String WPS_DEFAULT_VERSION = "7.1";

	public static String WPS_NETWORK_DOWNLOAD = "/apk/WPSOffice_106.apk";

	public static String WPS_PACKAGE_NAME = "cn.wps.moffice_eng";

	public static String WPS_BROWSE_ACTIVITY_NAME = "cn.wps.moffice.documentmanager.PreStartActivity2";

	public static String ASSERT_WPS_NAME = "KingsoftOffice.apk";
	public static final String KEY_DEFAULT = "123456";
	public static String ctxPath = Environment.getExternalStorageDirectory()
			.toString() + "/ZET/1";

	/**
	 * 打开pdf
	 * 
	 * @param mContext
	 * @param path
	 */
	public static String openPDF(final Context mContext, String path,
			boolean needDecrypt) {
		String versionname = PdfUtils.isInstallWPS(mContext);
		String newPath;
		if (needDecrypt) {
			String fileName = path;
			File sourceFile = new File(ctxPath);
			if (!sourceFile.exists()) {
				sourceFile.mkdirs();
			}
			if (!sourceFile.isDirectory()) {
			}
			String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
			String sourceFilePath = fileName;
			String targetFilePath = fileName.substring(0, fileName.lastIndexOf("."))
					+ "_NEW." + fileType;
			File targetFile = new File(targetFilePath);
			if(targetFile.exists()){
				targetFile.delete();
			}
			if (AESCryptUtil.decrypt(sourceFilePath, targetFilePath,
					KEY_DEFAULT)) {
				LogUtils.e("文件解密成功：" + fileName);
			} else {
				LogUtils.e("文件解密失败：" + fileName);
			}
			newPath = targetFilePath;
		} else {
			newPath = path;
		}
		File file = new File(newPath);
		PdfUtils.openUseWPS(mContext, Uri.fromFile(file));
		return newPath;
	}

	public static String decrypt(String fileName) {
		File sourceFile = new File(ctxPath);
		if (!sourceFile.exists()) {
			sourceFile.mkdirs();
		}

		if (!sourceFile.isDirectory()) {
			return null;
		}

		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		String sourceFilePath = ctxPath + "/" + fileName;
		String targetFilePath = ctxPath + "/"
				+ fileName.substring(0, fileName.lastIndexOf(".")) + "_NEW."
				+ fileType;
		if (AESCryptUtil.decrypt(sourceFilePath, targetFilePath, KEY_DEFAULT)) {
			LogUtils.e("文件解密成功：" + fileName);
		} else {
			LogUtils.e("文件解密失败：" + fileName);
		}
		return targetFilePath;
	}

	/**
	 * 判断是否安装wps
	 * 
	 * @param context
	 * @return 如果未安装wps则返回null, 安装了wps则返回应用程序的版本号
	 */
	public static String isInstallWPS(Context context) {
		String versionName = null;
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(WPS_PACKAGE_NAME, 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			versionName = null;
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 使用WPS打开指定uri
	 * 
	 * @param context
	 * @param uri
	 */
	public static void openUseWPS(Context context, Uri uri) {
		try {
			Intent intent = new Intent();
			ComponentName cnComponentName = new ComponentName(WPS_PACKAGE_NAME,
					WPS_BROWSE_ACTIVITY_NAME);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setComponent(cnComponentName);
			// 获取文件file的MIME类型
			// String type = "application/pdf";
			// 设置intent的data和Type属性。
			// intent.setDataAndType(uri, type);
			intent.setData(uri);
			context.startActivity(intent);
		} catch (Exception e) {

		}
	}

}

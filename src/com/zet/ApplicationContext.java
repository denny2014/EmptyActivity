package com.zet;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.utils.LogUtils;
import com.zet.activity.DepartMentSelectActivity;
import com.zet.activity.EmailDetailActivity;
import com.zet.activity.InfoDetailActivity;
import com.zet.activity.LoadingActivity;
import com.zet.activity.LoginActivity;
import com.zet.activity.LoginEmailActivity;
import com.zet.activity.ModifyPassActivity;
import com.zet.activity.ModifyServiceActivity;
import com.zet.activity.MonthAndDaySelectActivity;
import com.zet.activity.NewsInBriefInfoDetailActivity;
import com.zet.activity.SearchListActivity;
import com.zet.activity.TypeActivity;
import com.zet.utils.CrashHandler;

/**
 * Created by macchen on 15/4/3.
 */
public class ApplicationContext extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
		JPushInterface.init(this); // 初始化 JPush
		LogUtils.allowE = true;
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	/**
	 * 显示加载界面
	 * 
	 * @param context
	 */
	public static void showLoading(Context context) {
		showLauncher(context, LoadingActivity.class, null);
	}

	/**
	 * 显示登录
	 * 
	 * @param context
	 */
	public static void showLogin(Context context) {
		showLauncher(context, LoginActivity.class, null);
	}

	/**
	 * 显示修改密码
	 * 
	 * @param context
	 */
	public static void showModifyPass(Context context) {
		showLauncher(context, ModifyPassActivity.class, null);
	}
	/**
	 * 显示修改服务器
	 * 
	 * @param context
	 */
	public static void showModifyService(Context context) {
		showLauncher(context, ModifyServiceActivity.class, null);
	}
	
	/**
	 * 显示分类
	 * 
	 * @param context
	 */
	public static void showType(Context context, Bundle bundle) {
		showLauncher(context, TypeActivity.class, bundle);
	}

	/**
	 * 搜索界面
	 * 
	 * @param context
	 */
	public static void showSearch(Context context, Bundle bundle) {
		showLauncher(context, SearchListActivity.class, bundle);
	}

	/**
	 * 登陆邮箱
	 * 
	 * @param context
	 */
	public static void loginEmail(Context context, Bundle bundle) {
		showLauncher(context, LoginEmailActivity.class, bundle);
	}

	/**
	 * 显示信息详情
	 * 
	 * @param context
	 * @param bundle
	 */
	public static void showDetail(Context context, Bundle bundle) {
		String docType = bundle.getString("docType");
		if (!TextUtils.isEmpty(docType)&&(docType.equals("05") || docType == "05")) {
			showLauncher(context, NewsInBriefInfoDetailActivity.class, bundle);
		} else
			showLauncher(context, InfoDetailActivity.class, bundle);
	}

	/**
	 * 显示邮件信息详情
	 * 
	 * @param context
	 * @param bundle
	 */
	public static void showEmailDetail(Context context, Bundle mBundle ,int requestCode) {
		// TODO Auto-generated method stub
		showLauncher(context, EmailDetailActivity.class, mBundle,requestCode);
	}

	/**
	 * 部门选择等界面
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void showSelect(Context context, Bundle bundle,
			int requestCode) {
		showLauncher(context, DepartMentSelectActivity.class, bundle,
				requestCode);
	}
	/**
	 * 选择月，日等界面
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void showSelectMonthAndDay(Context context, Bundle bundle,
			int requestCode) {
		showLauncher(context, MonthAndDaySelectActivity.class, bundle,
				requestCode);
	}

	/**
	 * 启动方法
	 * 
	 * @param context
	 * @param toClass
	 * @param bundle
	 */
	public static void showLauncher(Context context, Class toClass,
			Bundle bundle) {
		showLauncher(context, toClass, bundle, -1);
	}

	/**
	 * 启动方法
	 * 
	 * @param context
	 * @param toClass
	 * @param bundle
	 * @param requestCode
	 */
	public static void showLauncher(Context context, Class toClass,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, toClass);
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if (requestCode != -1 && context instanceof Activity) {
			((Activity) context).startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

}

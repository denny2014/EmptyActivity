package com.zet.activity;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.AppUtils;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.PhoneUtils;
import com.bdkj.bdlibrary.utils.SerializeUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.zet.ApplicationContext;
import com.zet.BaseActivity;
import com.zet.Constants;
import android.view.Display;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.asyncHandler.BoolHandler;
import com.zet.asyncHandler.LoginHandler;
import com.zet.model.UserInfo;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;
import com.zet.utils.InterfaceConst;

/**
 * 登录界面 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
	@ViewInject((R.id.et_password))
	EditText etPassword;
	@ViewInject((R.id.et_username))
	EditText etUsername;
	@ViewInject((R.id.rememberPsw))
	ImageView cRememberView;
	@ViewInject(R.id.llt_service_ip)
	LinearLayout lltServerIP;
	@ViewInject(R.id.et_service_ip)
	TextView etServiceIP;
	@ViewInject(R.id.tv_show_imei)
	TextView tvImei;
	@ViewInject(R.id.btn_ensure)
	Button btnEnsure;
	@ViewInject(R.id.iv_setting)
	ImageView ivSetting;
	@ViewInject(R.id.tv_version)
	TextView tvVersion;
	@ViewInject(R.id.llt_top_layer)
	FrameLayout flLayer;

	SharedPreferences setting;
	public final static String RETURN_FOR_RESULT = "com.zet.activity.LoginActivity";

	private boolean cRemember = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(RETURN_FOR_RESULT);
		registerReceiver(broadcastReceiver, intentFilter);
		SharedPreferences.Editor editor = LConfigUtils.getPreferences(this,
				InterfaceConst.emailSetting).edit();
		editor.putBoolean("addNotifications", false);
		editor.commit();
		initView();
		getResolution();
	}

	private void getResolution() {
		// TODO Auto-generated method stub
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		float density = displayMetrics.density; // 得到密度
		float width = displayMetrics.widthPixels;// 得到宽度
		float height = displayMetrics.heightPixels; // 得到高度
		System.out.println(width +", " + height);
	}

	private void initView() {
		// flLayer.setVisibility(Constants.USE_IMEI ? View.GONE : View.VISIBLE);
		setting = LConfigUtils.getPreferences(this, "setting");
		String username = setting.getString("name", "");
		String password = setting.getString("password", "");
		boolean checked = setting.getBoolean("isChecked", false);
		if(Constants.is_YS){
			ivSetting.setVisibility(View.INVISIBLE);
		}
		etUsername.setText(username);
		etPassword.setText(password);
		cRememberView.setImageResource(R.drawable.ic_checkbox_checked_new);
		cRemember = true;
		tvVersion.setText(getString(R.string.version, AppUtils.getVersionName(mContext)));
		String imei = PhoneUtils.getIMEI(mContext);
		tvImei.setText(imei);
		String company = LConfigUtils.getString(mContext, "appconfig.company");
		if (company.equals("")) {
			HttpUtils.get(mContext, Constants.OTHER_SERVER_URL, ZetParams.getCompanyName(imei), HandlerFactory.getHandler(BoolHandler.class, new BaseNetHandler(this, Constants.GETCOMMPANY_ACTION)));
		} else {
			initCompany(company);
		}
		// if (Constants.USE_IMEI && imei != null) {
		// HttpUtils.get(mContext, Constants.SERVER_URL,
		// ZetParams.getImeiLogin(imei),
		// HandlerFactory.getHandler(LoginHandler.class, new BaseNetHandler(new
		// INetProxy(mContext, this), Constants.LOGIN_IMEI_ACTION)));
		// }
	}

	private void initCompany(String name) {
		TextView tvCompany = (TextView) findViewById(R.id.tv_company);
		tvCompany.setText(name);
		tvCompany.setTextColor(Color.parseColor("#7FD6F7"));
		tvCompany.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_mediumn_small));
	}

	@OnClick({ R.id.tv_modify_pass, R.id.btn_login, R.id.iv_setting, R.id.btn_ensure, R.id.rememberPsw })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rememberPsw:
			if (cRemember) {
				cRemember = false;
				cRememberView.setImageResource(R.drawable.ic_checkbox_nor_normal_new);
			} else {
				cRemember = true;
				cRememberView.setImageResource(R.drawable.ic_checkbox_checked_new);
			}

			break;
		case R.id.tv_modify_pass:
			ApplicationContext.showModifyPass(mContext);
			// overridePendingTransition(R.anim.anim_down_in,
			// R.anim.anim_down_out);
			break;
		case R.id.btn_login:
			String username = etUsername.getText().toString();
			String password = etPassword.getText().toString();
			if (TextUtils.isEmpty(username)) {
				ToastUtils.showError(mContext, R.string.activity_login_username_disallow_null);
			} else if (TextUtils.isEmpty(password)) {
				ToastUtils.showError(mContext, R.string.activity_login_pass_disallow_null);
			} else {
				String imei = PhoneUtils.getIMEI(mContext);
				HttpUtils.get(mContext, Constants.SERVER_URL, ZetParams.getLogin(username, password, imei), HandlerFactory.getHandler(LoginHandler.class, new BaseNetHandler(new INetProxy(mContext, this), Constants.LOGIN_ACTION)));
			}
			break;
		case R.id.iv_setting:
			ApplicationContext.showModifyService(mContext);
			// String ip = LConfigUtils.getString(mContext, "appconfig.ip",
			// Constants.DEFAULT_SERVER_IP);
			// int default_index = getDefaultServerIndex(ip);
			// etServiceIP.setTag(default_index);
			// etServiceIP.setText(Constants.service_urls_text[default_index]);
			//
			// ToastUtils.showInfo(mContext,
			// R.string.activity_login_setting_tip);
			// ivSetting.setVisibility(View.GONE);
			// btnEnsure.setVisibility(View.VISIBLE);
			// tvImei.setVisibility(View.VISIBLE);
			// lltServerIP.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_ensure:
			if (TextUtils.isEmpty(etServiceIP.getText().toString())) {
				ToastUtils.showWarn(mContext, R.string.activity_login_setting_server_disallow_null);
			} else {
				String snumber = Constants.service_urls_value[Integer.parseInt(etServiceIP.getTag().toString())];
				boolean isUpdateCommpnay = false;
				if (snumber.indexOf("http://") > -1) {
					snumber = snumber.replace("http://", "");
				}
				// 判断服务器地址是否修改了
				isUpdateCommpnay = !snumber.equals(LConfigUtils.getString(mContext, "appconfig.ip"));
				LConfigUtils.setString(mContext, "appconfig.ip", snumber);
				ToastUtils.showSuccess(mContext, R.string.activity_login_setting_success);
				Constants.initServerIP(snumber);
				tvImei.setVisibility(View.GONE);
				lltServerIP.setVisibility(View.GONE);
				ivSetting.setVisibility(View.VISIBLE);
				btnEnsure.setVisibility(View.GONE);
				KeyBoardUtils.hideKeyBoard(LoginActivity.this);
				// 如果服务器地址被修改则重新获取公司地址
				if (isUpdateCommpnay) {
					LConfigUtils.setString(mContext, "appconfig.company", "");
					HttpUtils.get(mContext, Constants.OTHER_SERVER_URL, ZetParams.getCompanyName(tvImei.getText().toString()), HandlerFactory.getHandler(BoolHandler.class, new BaseNetHandler(this, Constants.GETCOMMPANY_ACTION)));
				}
			}
			break;
		}
	}

	private int getDefaultServerIndex(String ip) {
		for (int i = 0; i < Constants.service_urls_text.length; i++) {
			if (Constants.service_urls_text[i].equals(ip)) {
				return i;
			}
		}
		return 0;
	}

	@OnClick(R.id.et_service_ip)
	public void et_service_ip_click(View v) {
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("选择服务器").setIcon(R.drawable.ic_launcher).setItems(Constants.service_urls_text, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				etServiceIP.setText(Constants.service_urls_text[which]);
				etServiceIP.setTag(which);
			}
		}).create();
		alertDialog.show();
	}

	@Override
	public boolean success(String type, Object objects) {
		Object[] data = (Object[]) objects;
		if (Constants.LOGIN_ACTION.equals(type) || Constants.LOGIN_IMEI_ACTION.equals(type)) {
			UserInfo userInfo = (UserInfo) data[1];
			SerializeUtils.writeObject(new File(getCacheDir(), "user.info"), userInfo);
			// ToastUtils.showSuccess(mContext,
			// R.string.activity_login_success);
			SharedPreferences.Editor editor = setting.edit();
			editor.putString("name", cRemember ? etUsername.getText().toString() : "");
			editor.putString("password", cRemember ? etPassword.getText().toString() : "");
			editor.putBoolean("isChecked", cRemember);
			editor.commit();
			saveEmailMessage(userInfo);
			ApplicationContext.showType(mContext, null);
			finish();
		} else if (Constants.GETCOMMPANY_ACTION.equals(type)) {
			String company = (String) data[1];
			if (!"".equals(company)) {
				initCompany(company);
				LConfigUtils.setString(mContext, "appconfig.company", company);
			}
		}
		return super.success(type, objects);
	}

	private void saveEmailMessage(UserInfo userInfo) {
		SharedPreferences emailSetting = LConfigUtils.getPreferences(this, InterfaceConst.emailSetting);
		SharedPreferences.Editor editor = emailSetting.edit();
		editor.putString(InterfaceConst.emailname, userInfo.getUSERMAIL());
		editor.putString(InterfaceConst.emailpassword, userInfo.getUSERMAILPASS());
		editor.putBoolean("havaAlreadyLogin", true);
		editor.commit();
	}

	// @Override
	// public boolean start(String type, Object objects) {
	// if (Constants.LOGIN_IMEI_ACTION.equals(type)) {
	// return true;
	// }
	// return super.start(type, objects);
	// }
	//
	// @Override
	// public boolean failure(String type, Object objects) {
	// if (Constants.LOGIN_IMEI_ACTION.equals(type)) {
	// flLayer.setVisibility(View.VISIBLE);
	// }
	// return super.failure(type, objects);
	// }
	//
	// @Override
	// public boolean dataFailure(String type, Object objects) {
	// if (Constants.LOGIN_IMEI_ACTION.equals(type)) {
	// flLayer.setVisibility(View.VISIBLE);
	// }
	// return super.dataFailure(type, objects);
	// }
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			LConfigUtils.setString(mContext, "appconfig.company", "");
			HttpUtils.get(mContext, Constants.OTHER_SERVER_URL, ZetParams.getCompanyName(tvImei.getText().toString()), HandlerFactory.getHandler(BoolHandler.class, new BaseNetHandler(LoginActivity.this, Constants.GETCOMMPANY_ACTION)));
		};
	};
}
package com.zet.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.widget.TextView;

import com.bdkj.bdlibrary.model.VersionInfo;
import com.bdkj.bdlibrary.utils.AppUtils;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.NetworkUtils;
import com.bdkj.bdlibrary.utils.PhoneUtils;
import com.bdkj.bdlibrary.utils.StorageUtils;
import com.bdkj.bdlibrary.utils.VersionManager;
import com.jsong.android.library.api.NetParameters;
import com.jsong.android.library.api.NetRequestInterface;
import com.jsong.android.library.api.NetRequestInterfaceImp;
import com.jsong.android.library.api.NetResponseListener;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.ApplicationContext;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.asyncHandler.BoolHandler;
import com.zet.asyncHandler.CheckVersionHandler;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.utils.ParseJsonUtil;
import com.zet.utils.RefreshEmailList;

/**
 * 欢迎界面
 */
@ContentView(R.layout.activity_loading)
public class LoadingActivity extends BaseActivity implements VersionManager.VersionUpdateListener {
	/**
	 * Called when the activity is first created.
	 */
	@ViewInject(R.id.tv_version)
	TextView tvVersion;

	public final int START_APP = 0;
	public boolean isEnable = false;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case START_APP:
				if (!isEnable) {
					isEnable = true;
					if (Constants.USE_IMEI) {
						Bundle bundle = new Bundle();
						bundle.putInt("type", 1);
						ApplicationContext.showType(mContext, bundle);
					} else {
						ApplicationContext.showLogin(mContext);
					}
					finish();
					overridePendingTransition(0, 0);
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tvVersion.setText(getString(R.string.version, AppUtils.getVersionName(mContext)));
		initServerIP();
		AskTaskLoad(1);
		mContext.startService(new Intent(mContext, RefreshEmailList.class));
		String company = LConfigUtils.getString(mContext, "appconfig.company");
		if (company.equals("")) {
			String imei = PhoneUtils.getIMEI(mContext);
			HttpUtils.get(mContext, Constants.OTHER_SERVER_URL, ZetParams.getCompanyName(imei), HandlerFactory.getHandler(BoolHandler.class, new BaseNetHandler(this, Constants.GETCOMMPANY_ACTION)));
		} else {
			initCompany(company);
		}
		if (NetworkUtils.getNetworkState(mContext) != NetworkUtils.NONE_CONNECT_STATE) {
			HttpUtils.get(mContext, Constants.SERVER_URL, ZetParams.checkVersion(AppUtils.getVersionName(mContext), PhoneUtils.getIMEI(mContext)), HandlerFactory.getHandler(CheckVersionHandler.class, new BaseNetHandler(this, Constants.CHECK_VERSION_ACTION)));
			handler.sendEmptyMessageDelayed(START_APP, 2500);
		} else {
			handler.sendEmptyMessageDelayed(START_APP, 1500);
		}
	}

	/**
	 * 初始化ip地址
	 */
	private void initServerIP() {
		String ip = LConfigUtils.getString(mContext, "appconfig.ip", Constants.DEFAULT_SERVER_IP);
		Constants.initServerIP(ip);
	}

	private void initCompany(String name) {
		TextView tvCompany = (TextView) findViewById(R.id.tv_company);
		tvCompany.setText(name);
		tvCompany.setTextColor(Color.parseColor("#7FD6F7"));
		tvCompany.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.text_mediumn_small));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean success(String type, Object objects) {
		// TODO Auto-generated method stub
		Object[] data = (Object[]) objects;
		if (Constants.CHECK_VERSION_ACTION.equals(type)) {
			String wpsUrl = (String) data[2];
			String wpsVer = (String) data[3];
			if (!wpsVer.equals("")) {
				LConfigUtils.setString(mContext, "appconfig.wpsVer", wpsVer);
			}
			if (!wpsUrl.equals("")) {
				LConfigUtils.setString(mContext, "appconfig.wpsUrl", wpsUrl);
			}
			if (isEnable)
				return super.success(type, objects);
			VersionInfo info = (VersionInfo) data[1];
			info.setCurVersion(AppUtils.getVersionName(mContext));
			info.setDirPath(StorageUtils.isMount() ? StorageUtils.getStorageDirectory() + Constants.ROOT_DIRECTION : getCacheDir().getAbsolutePath());
			if ((info.getUpdate() > 0) && !info.getUrl().equals("")) {
				switch (info.getUpdate()) {
				case 1: // 更新（不提示)
					break;
				case 2: // 更新（提示)
				case 3: // 强制更新
					handler.removeMessages(START_APP);
					VersionManager manager = new VersionManager(mContext, info);
					manager.setOnVersionUpdateListener(this);
					manager.updateVersion();
					break;
				}
			} else {
				handler.sendEmptyMessage(START_APP);
			}
		} else if (Constants.GETCOMMPANY_ACTION.equals(type)) {
			String company = (String) data[1];
			if (!"".equals(company)) {
				initCompany(company);
				LConfigUtils.setString(mContext, "appconfig.company", company);
			}
		}
		return super.success(type, objects);
	}

	@Override
	public boolean failure(String type, Object objects) {
		// TODO Auto-generated method stub
		if (Constants.CHECK_VERSION_ACTION.equals(type)) {
			handler.sendEmptyMessage(START_APP);
		}
		return super.failure(type, objects);
	}

	@Override
	public boolean dataFailure(String type, Object objects) {
		// TODO Auto-generated method stub
		if (Constants.CHECK_VERSION_ACTION.equals(type)) {
			handler.sendEmptyMessage(START_APP);
		}
		return super.dataFailure(type, objects);
	}

	@Override
	public void updateCancel(boolean isForceup) {
		// TODO Auto-generated method stub
		if (isForceup) {
			finish();
		} else {
			handler.sendEmptyMessage(START_APP);
		}
	}

	@Override
	public void updateFinish(boolean isForceup) {
		// TODO Auto-generated method stub
		finish();
	}

	private void AskTaskLoad(int tag) {
		// TODO Auto-generated method stub
		NetParameters parameters = new com.jsong.android.library.api.NetParameters();
		parameters.addParam(NetRequestInterface.REQUESTYPE, NetRequestInterface.REQUESTBYGET);
		String VersionName = getString(R.string.version, AppUtils.getVersionName(mContext));
		String versioncode = getString(R.string.version, AppUtils.getVersionCode(mContext));
		String systemVersioncode = android.os.Build.VERSION.RELEASE;
		LogUtils.e("VersionName : " + VersionName);
		LogUtils.e("versioncode : " + versioncode);
		LogUtils.e("SystemVersioncode: " + systemVersioncode);
		parameters.addParam("url", Constants.SERVER_URL);
		parameters.addParam("action", "getversion");
		parameters.addParam("imei", PhoneUtils.getIMEI(mContext));
		parameters.addParam("versionint", VersionName);
		parameters.addParam("versioncode", versioncode);// fileNO1
		parameters.addParam("systemversion", systemVersioncode);// sdocyear1
		parameters.addParam("debug", "1");

		new NetRequestInterfaceImp().dorequest(parameters, new NetResponseListener() {
			@Override
			public void onException(Exception e, int tag) {
			}

			@Override
			public void onComplete(Object object, int tag) {
				LogUtils.e(object.toString());
				if (ParseJsonUtil.getInstance().parseIsSuccess(object.toString())) {
				} else {
				}
			}
		}, mContext, tag);
	}
}

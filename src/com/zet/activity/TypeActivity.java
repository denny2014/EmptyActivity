package com.zet.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.bdkj.bdlibrary.utils.DialogUtils;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.PhoneUtils;
import com.bdkj.bdlibrary.utils.SerializeUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.bdkj.bdlibrary.utils.WindowUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.ApplicationContext;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.adapter.ViewPageAdapter;
import com.zet.asyncHandler.LoginHandler;
import com.zet.model.UserInfo;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;
import com.zet.utils.InterfaceConst;

/**
 * 类型选择界面 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_type)
public class TypeActivity extends BaseActivity {
	@ViewInject(R.id.type_pager)
	ViewPager mPager;
	@ViewInject(R.id.point_lin)
	LinearLayout mLayout;
	List<View> viewLists;
	private ViewPageAdapter mAdapter;
	private ArrayList<ImageView> image_points = new ArrayList<ImageView>(); // 存放点的view
	private int count;
	private int currentItem = 0;
	private int oldPosition = 0;
	private View typeItemOneView;
	private View typeItemTwoView;
	private TextView[] mTextViews = new TextView[10];
	private SharedPreferences emailSetting;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// if (!Constants.USE_IMEI) {
		// setContentView(R.layout.activity_type);
		// }
		// else{
		// setContentView(R.layout.activity_type_for_login);
		// }
		viewLists = new ArrayList<View>();
		typeItemOneView = getLayoutInflater().inflate(
				R.layout.activity_type_item1, null);
		typeItemTwoView = getLayoutInflater().inflate(
				R.layout.activity_type_item2, null);
		viewLists.add(typeItemOneView);
		viewLists.add(typeItemTwoView);
		mAdapter = new ViewPageAdapter(viewLists);
		mPager.setAdapter(mAdapter);
		count = mAdapter.getCount();
		typeItemOneView.findViewById(R.id.btn_type_1).setOnClickListener(
				mClickListener);
		typeItemOneView.findViewById(R.id.btn_type_2).setOnClickListener(
				mClickListener);
		typeItemOneView.findViewById(R.id.btn_type_3).setOnClickListener(
				mClickListener);
		typeItemOneView.findViewById(R.id.btn_type_4).setOnClickListener(
				mClickListener);
		// typeItemOneView.findViewById(R.id.btn_type_5).setOnClickListener(
		// mClickListener);
		typeItemOneView.findViewById(R.id.btn_type_6).setOnClickListener(
				mClickListener);
		typeItemOneView.findViewById(R.id.btn_type_7).setOnClickListener(
				mClickListener);
		// typeItemTwoView.findViewById(R.id.btn_type_7).setOnClickListener(
		// mClickListener);
		typeItemTwoView.findViewById(R.id.btn_type_8).setOnClickListener(
				mClickListener);
		typeItemTwoView.findViewById(R.id.btn_type_9).setOnClickListener(
				mClickListener);
		typeItemTwoView.findViewById(R.id.btn_type_10).setOnClickListener(
				mClickListener);
		emailSetting = LConfigUtils.getPreferences(this,
				InterfaceConst.emailSetting);
		for (int i = 0; i < count; i++) {
			ImageView image_point = new ImageView(TypeActivity.this);
			image_point.setScaleType(ScaleType.FIT_XY);
			LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(
					15, 15);
			params1.leftMargin = 10;
			params1.rightMargin = 10;
			image_point.setLayoutParams(params1);
			//
			image_points.add(image_point);
			image_point
					.setBackgroundResource(i == mPager.getCurrentItem() ? R.drawable.dot_normal_new
							: R.drawable.dot_focused_new);
			mLayout.addView(image_point);
		}

		mPager.setOnPageChangeListener(new MyListener());
		int type = getIntent().getIntExtra("type", 0);
		findViewById(R.id.btn_back).setVisibility(
				Constants.USE_IMEI ? View.VISIBLE : View.GONE);
		if (type == 1) {
			String imei = PhoneUtils.getIMEI(mContext);
			if (imei != null) {
				HttpUtils.get(mContext, Constants.SERVER_URL, ZetParams
						.getImeiLogin(imei), HandlerFactory.getHandler(
						LoginHandler.class, new BaseNetHandler(new INetProxy(
								mContext, this, true,
								getString(R.string.dialog_recognition_devide)),
								Constants.LOGIN_IMEI_ACTION)));
			}
		}
	}

	@OnClick({ R.id.btn_back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SharedPreferences.Editor editor = LConfigUtils.getPreferences(this,
				InterfaceConst.emailSetting).edit();
		editor.putBoolean("havaAlreadyLogin", false);
		editor.commit();
	}

	@Override
	public boolean failure(String type, Object objects) {
		showFailure();
		return true;
	}

	@Override
	public boolean dataFailure(String type, Object objects) {
		showFailure();
		return true;
	}

	public void showFailure() {
		DialogUtils.showConfirmNoTitle(
				mContext,
				getString(R.string.activity_type_recognition_devide_fail),
				getString(R.string.activity_type_recognition_devide_reset),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						String imei = PhoneUtils.getIMEI(mContext);
						if (imei != null) {
							HttpUtils
									.get(mContext,
											Constants.SERVER_URL,
											ZetParams.getImeiLogin(imei),
											HandlerFactory
													.getHandler(
															LoginHandler.class,
															new BaseNetHandler(
																	new INetProxy(
																			mContext,
																			TypeActivity.this,
																			true,
																			mContext.getString(R.string.dialog_recognition_devide)),
																	Constants.LOGIN_IMEI_ACTION)));
						}
					}
				}, getString(R.string.activity_type_recognition_devide_exit),
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				}, Gravity.CENTER);
	}

	@Override
	public boolean success(String type, Object objects) {
		Object[] data = (Object[]) objects;
		if (Constants.LOGIN_ACTION.equals(type)
				|| Constants.LOGIN_IMEI_ACTION.equals(type)) {
			UserInfo userInfo = (UserInfo) data[1];
			SerializeUtils.writeObject(new File(getCacheDir(), "user.info"),
					userInfo);
	        SharedPreferences emailSetting = LConfigUtils.getPreferences(this, InterfaceConst.emailSetting);
	        SharedPreferences.Editor editor = emailSetting.edit();
	        editor.putString(InterfaceConst.emailname, userInfo.getUSERMAIL());
	        editor.putString(InterfaceConst.emailpassword, userInfo.getUSERMAILPASS());
	        editor.putBoolean("havaAlreadyLogin", true);
	        editor.commit();
		}
		return super.success(type, objects);
	}

	/** 动态效果图改变时的监听类 */
	class MyListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			currentItem = position;
			image_points.get(oldPosition).setBackgroundResource(
					R.drawable.dot_focused_new);
			image_points.get(position).setBackgroundResource(
					R.drawable.dot_normal_new);
			oldPosition = position;

		}

	}

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String docType = null;
			String work_type = ""; // 01 工作汇报；02 部门预算；03 预算执行；04 工作报告；05 依法行政
			switch (v.getId()) {
			case R.id.btn_type_1:// 财经政策
				docType = "01";
				break;
			case R.id.btn_type_2:// 法律法规
				docType = "02";
				break;
			case R.id.btn_type_3:// 政策解读
				docType = "21";
				break;
			case R.id.btn_type_4:// 财经政策简讯
				docType = "11";
				break;
			case R.id.btn_type_5:// 江苏财经概况
				docType = "11";
				break;
				//1 工作汇报；2 部门预算；3 预算执行；4 工作报告；5 依法行政；
			case R.id.btn_type_6:// 工作汇报
				work_type = "1";
				docType = "06";
				break;
			case R.id.btn_type_7:// 预算执行
				work_type = "3";
				docType = "06";
				break;
			case R.id.btn_type_8:// 部门预算
				work_type = "2";
				docType = "06";
				break;
			case R.id.btn_type_9:// 依法行政
				work_type = "5";
				docType = "06";
				break;
			case R.id.btn_type_10://工作报告
				work_type = "4";
				docType = "06";
				break;
			}
			if (docType != null) {
				Bundle bundle = new Bundle();
				bundle.putString("docType", docType);
				if (docType.equals("06")) {
					boolean havaAlreadyLogin = emailSetting.getBoolean(
							"havaAlreadyLogin", false);
					havaAlreadyLogin = havaAlreadyLogin || Constants.USE_IMEI;
					String emailUsername = emailSetting.getString(
							InterfaceConst.emailname, "");
					String emailPssword = emailSetting.getString(
							InterfaceConst.emailpassword, "");
					if (havaAlreadyLogin && !TextUtils.isEmpty(emailUsername)
							&& !TextUtils.isEmpty(emailPssword)) {
						if (emailUsername.contains("@qq")) {
							bundle.putString("docType", docType);
							bundle.putString("workType", work_type);
							ApplicationContext.showSearch(mContext, bundle);
						} else {
							ToastUtils.show(mContext, "目前只支持QQ邮箱登陆");
						}
					} else
						ToastUtils.show(mContext, "账号或密码为空，请联系管理员");
					// ApplicationContext.loginEmail(mContext, bundle);
				} else
					ApplicationContext.showSearch(mContext, bundle);
			}
		}
	};
}
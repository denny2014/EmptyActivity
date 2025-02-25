package com.zet.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;
import com.zet.ZetParams;
import com.zet.asyncHandler.BoolHandler;
import com.zet.net.BaseNetHandler;
import com.zet.net.HandlerFactory;
import com.zet.net.INetProxy;

/**
 * 修改密码界面 Created by macchen on 15/4/3.
 */
@ContentView(R.layout.activity_modify_pass)
public class ModifyPassActivity extends BaseActivity {
	@ViewInject(R.id.et_username)
	EditText etUsername;
	@ViewInject(R.id.et_oldpass)
	EditText etOldPass;
	@ViewInject(R.id.et_newpass)
	EditText etNewPass;
	@ViewInject(R.id.et_passensure)
	EditText etEnsurePass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.title_bar_img).setVisibility(View.GONE);
		findViewById(R.id.iv_collect).setVisibility(View.GONE);
		findViewById(R.id.iv_help).setVisibility(View.GONE);
		((TextView) findViewById(R.id.title_bar_text)).setText("修改密码");
//		findViewById(R.id.llt_modify_pass).setOnTouchListener(
//				new View.OnTouchListener() {
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//
//						return true;
//					}
//				});
	}

	@OnClick({ R.id.btn_modify, R.id.btn_cancel, R.id.tv_back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_modify:
			String uname = etUsername.getText().toString();
			String oldpass = etOldPass.getText().toString();
			String newpass = etNewPass.getText().toString();
			String ensurepass = etEnsurePass.getText().toString();
			if (TextUtils.isEmpty(uname) || TextUtils.isEmpty(oldpass)
					|| TextUtils.isEmpty(newpass)
					|| TextUtils.isEmpty(ensurepass)) {

				ToastUtils.showError(mContext,
						R.string.activity_modify_pass_fill_error);
			} else if (!newpass.equals(ensurepass)) {
				ToastUtils.showError(mContext,
						R.string.activity_modify_pass_twice_error);
			} else {
				HttpUtils.get(mContext, Constants.SERVER_URL, ZetParams
						.getChangePass(uname, oldpass, newpass), HandlerFactory
						.getHandler(BoolHandler.class, new BaseNetHandler(
								new INetProxy(mContext, this),
								Constants.CHANGE_PSW_ACTION)));
			}
			break;
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.tv_back:
			finish();
			break;
		}
	}

	@Override
	public void finish() {
		KeyBoardUtils.hideKeyBoard(this);
		super.finish();
		// overridePendingTransition(R.anim.anim_down_in, R.anim.anim_down_out);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		finish();
//		return super.onTouchEvent(event);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	// @Override
	// public void onAttachedToWindow() {
	// super.onAttachedToWindow();
	// Window window = getWindow();
	// WindowManager.LayoutParams layoutParams = window.getAttributes();
	// layoutParams.width = WindowUtils.getWidth(mContext) * 3 / 4;
	// layoutParams.gravity = Gravity.CENTER;
	// window.setAttributes(layoutParams);
	// }

	@Override
	public boolean success(String type, Object objects) {
		ToastUtils.showSuccess(mContext, R.string.activity_modify_pass_success);
		finish();
		return super.success(type, objects);
	}
}
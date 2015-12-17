package com.zet.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.bdkj.bdlibrary.utils.LConfigUtils;
import com.bdkj.bdlibrary.utils.ToastUtils;
import com.lidroid.xutils.annotation.ContentView;
import com.lidroid.xutils.annotation.ViewInject;
import com.lidroid.xutils.event.OnClick;
import com.lidroid.xutils.utils.LogUtils;
import com.zet.BaseActivity;
import com.zet.Constants;
import com.zet.R;

/**
 * 修改服务器界面 Created by zjy on 15/9/13.
 */
@ContentView(R.layout.activity_modify_service)
public class ModifyServiceActivity extends BaseActivity {
	@ViewInject(R.id.select_service_image01)
	ImageView serviceView1;
	@ViewInject(R.id.select_service_image02)
	ImageView serviceView2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		findViewById(R.id.title_bar_img).setVisibility(View.GONE);
		findViewById(R.id.iv_collect).setVisibility(View.GONE);
		findViewById(R.id.iv_help).setVisibility(View.GONE);
		((TextView) findViewById(R.id.title_bar_text)).setText("设置服务器");
		String ip = LConfigUtils.getString(mContext, "appconfig.ip", Constants.DEFAULT_SERVER_IP);
        int default_index = getDefaultServerIndex(ip);
        LogUtils.e(""+default_index);
        if(default_index==0){
        	serviceView1.setImageResource(R.drawable.select_services_selected);
        	serviceView2.setImageResource(R.drawable.select_services_normal);
        }else if(default_index==1){
        	serviceView2.setImageResource(R.drawable.select_services_selected);
        	serviceView1.setImageResource(R.drawable.select_services_normal);
        }
//        ToastUtils.showInfo(mContext, R.string.activity_login_setting_tip);
	}
	   private int getDefaultServerIndex(String ip){
	    	for (int i = 0; i < Constants.service_urls_text.length; i++) {
				if(Constants.service_urls_value[i].equals(ip)){
					return i;
				}
			}
	    	return 0;
	    }
	@OnClick({ R.id.select_service_view01, R.id.select_service_view02, R.id.tv_back })
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.select_service_view01:
			serviceView1.setImageResource(R.drawable.select_services_selected);
        	serviceView2.setImageResource(R.drawable.select_services_normal);
        	setServicesIp(0);
        	finish();
			break;
		case R.id.select_service_view02:
			serviceView2.setImageResource(R.drawable.select_services_selected);
        	serviceView1.setImageResource(R.drawable.select_services_normal);
        	setServicesIp(1);
        	finish();
			break;
		case R.id.tv_back:
			finish();
			break;
		}
	}
private void setServicesIp(int tag){
	String snumber = Constants.service_urls_value[tag];
    boolean isUpdateCommpnay = false;
    if (snumber.indexOf("http://") > -1) {
        snumber = snumber.replace("http://", "");
    }
    //判断服务器地址是否修改了
    isUpdateCommpnay = !snumber.equals(LConfigUtils.getString(mContext, "appconfig.ip"));
    LConfigUtils.setString(mContext, "appconfig.ip", snumber);
    ToastUtils.showSuccess(mContext, R.string.activity_login_setting_success);
    Constants.initServerIP(snumber);
    KeyBoardUtils.hideKeyBoard(ModifyServiceActivity.this);
    //如果服务器地址被修改则重新获取公司地址
    if (isUpdateCommpnay) {
    	Intent data = new Intent();
		data.setAction(LoginActivity.RETURN_FOR_RESULT); // 设置广播的action
		sendBroadcast(data);
    }
}
	@Override
	public void finish() {
		KeyBoardUtils.hideKeyBoard(this);
		super.finish();
		// overridePendingTransition(R.anim.anim_down_in, R.anim.anim_down_out);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	// finish();
	// return super.onTouchEvent(event);
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean success(String type, Object objects) {
		ToastUtils.showSuccess(mContext, R.string.activity_modify_pass_success);
		finish();
		return super.success(type, objects);
	}
}
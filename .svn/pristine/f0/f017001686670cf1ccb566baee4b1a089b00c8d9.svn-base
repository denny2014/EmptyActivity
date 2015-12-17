package com.zet.net;

import android.os.Message;
import com.lidroid.xutils.utils.LogUtils;
import com.loopj.android.http.TextHttpResponseHandler;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 继承TextHttpResponseHandler,用于处理通信后的结果
 * @author macchen
 *
 */
public class BaseHandler extends TextHttpResponseHandler {

	public BaseNetHandler handler;

	public void setHandler(BaseNetHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		LogUtils.i("开始走接口");
		handleMessage(BaseNetHandler.NET_START, new Object());
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
		// TODO Auto-generated method stub
		LogUtils.i("onFailure--->String--->" + arg2);
		handleMessage(BaseNetHandler.NET_FAILURE, new Object());
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		super.onCancel();
		handleMessage(BaseNetHandler.NET_CANCEL, new Object());
	}

	@Override
	public void onFinish() {
		// TODO Auto-generated method stub
		super.onFinish();
		handleMessage(BaseNetHandler.NET_FINISH, new Object());
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, String arg2) {
		// TODO Auto-generated method stub
		LogUtils.i("json--->String--->" + arg2);
		try {
			onSuccess(arg0, arg1, new JSONObject(arg2));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			onFailure(arg0,arg1,arg2,new Throwable());
		}
	}

	@Override
	public void onProgress(int bytesWritten, int totalSize) {
		// TODO Auto-generated method stub
		super.onProgress(bytesWritten, totalSize);
		// handleMessage(BaseNetHandler.NET_PROGRESS, bytesWritten);
	}

	public void onSuccess(int arg0, Header[] arg1, JSONObject jsonObject) {
		// TODO Auto-generated method stub
	}

	public void success(Object... objects) {
		handleMessage(BaseNetHandler.NET_SUCCESS, objects);
	}

	public void dataErrorMsg(Object... objects) {
		handleMessage(BaseNetHandler.NET_DATA_FAIL, objects);
	}

	public void handleMessage(int what, Object... objects) {
		if (handler != null) {
			Message message = Message.obtain();
			message.what = what;
			message.obj = objects;
			handler.sendMessage(message);
		}
	}

}

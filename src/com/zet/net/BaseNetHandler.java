package com.zet.net;

import android.os.Handler;
import android.os.Message;

/**
 * 通信Handler,与主线程通信
 * @author macchen
 *
 */
public class BaseNetHandler extends Handler {
	/**
	 * 通信成功
	 */
	public static final int NET_SUCCESS = 0;
	/**
	 * 通信失败
	 */
	public static final int NET_FAILURE = 1;
	/**
	 * 通信数据返回错误
	 */
	public static final int NET_DATA_FAIL = 2;
	/**
	 * 通信开始
	 */
	public static final int NET_START = 3;
	/**
	 * 通信完成
	 */
	public static final int NET_FINISH = 4;
	/**
	 * 通信取消
	 */
	public static final int NET_CANCEL = 5;
	/**
	 * 通信进度
	 */
	public static final int NET_PROGRESS = 6;
	private INetListener listener;
	private String type;

	public BaseNetHandler(INetListener listener, String type) {
		this.listener = listener;
		this.type = type;
	}

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		super.handleMessage(msg);
		if (listener == null)
			return;
		switch (msg.what) {
		case NET_SUCCESS:
			listener.success(type, msg.obj);
			break;

		case NET_FAILURE:
			listener.failure(type, msg.obj);
			break;

		case NET_DATA_FAIL:
			listener.dataFailure(type, msg.obj);
			break;

		case NET_START:
			listener.start(type, msg.obj);
			break;

		case NET_FINISH:
			listener.finish(type, msg.obj);
			break;

		case NET_CANCEL:
			listener.cancel(type, msg.obj);
			break;

		case NET_PROGRESS:
			listener.progress(type, msg.obj);
			break;
		}
	}
}

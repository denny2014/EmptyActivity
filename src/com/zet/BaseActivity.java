package com.zet;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import com.bdkj.bdlibrary.utils.HttpUtils;
import com.bdkj.bdlibrary.utils.KeyBoardUtils;
import com.lidroid.xutils.utils.InjectUtils;
import com.zet.net.INetListener;

/**
 * Created by macchen on 15/4/3.
 */
public class BaseActivity extends Activity implements INetListener {
    protected Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        InjectUtils.inject(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        HttpUtils.getClient().cancelRequests(mContext, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        KeyBoardUtils.hideKeyBoard(this);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean success(String type, Object objects) {
        return false;
    }

    @Override
    public boolean failure(String type, Object objects) {
        return false;
    }

    @Override
    public boolean dataFailure(String type, Object objects) {
        return false;
    }

    @Override
    public boolean start(String type, Object objects) {
        return false;
    }

    @Override
    public boolean cancel(String type, Object objects) {
        return false;
    }

    @Override
    public boolean finish(String type, Object objects) {
        return false;
    }

    @Override
    public boolean progress(String type, Object objects) {
        return false;
    }
}

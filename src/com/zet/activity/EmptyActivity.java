package com.zet.activity;

import android.app.Activity;
import android.os.Bundle;
import com.zet.ApplicationContext;
import com.zet.Constants;

/**
 * 空白Activity
 * Created by macchen on 15/5/18.
 */
public class EmptyActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Constants.USE_IMEI) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("type",1);
//            ApplicationContext.showType(this, bundle);
//        }
//        else {
            ApplicationContext.showLoading(this);
//        }
        finish();
    }
}
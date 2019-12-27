package com.farwolf.express;

import android.util.Log;

import com.farwolf.express.module.ScanerModule;
import com.farwolf.weex.app.WeexApplication;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;

import org.androidannotations.annotations.EApplication;

/**
 * Created by zhengjiangrong on 2017/5/18.
 */
@EApplication
public class MVApplication extends WeexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtil.init(getApplicationContext());
        try {
            //入口

            WXSDKEngine.registerModule("ScanerModule", ScanerModule.class);
        } catch (WXException e) {
            e.printStackTrace();
        }

    }
}

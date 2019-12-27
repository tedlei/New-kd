package com.farwolf.express.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

import com.farwolf.express.R;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.taobao.weex.utils.WXFileUtils;

import java.util.HashMap;


public class WeexPageActivity extends BaseWeexPageActivity {
    private ViewGroup mContainer;
    String jsPath;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            jsPath = savedInstanceState.getString("url");
        }else {
            jsPath = getIntent().getStringExtra("url");
        }
        setContentView(R.layout.activity_weexpage);

        mContainer = (ViewGroup) findViewById(R.id.container);

        // 定义参数
        HashMap<String, Object> options = new HashMap<String, Object>();
        HashMap<String,String> params = new HashMap<String, String>();
        params.put("platform", "Android");
        options.put("params", params);
        options.put(WXSDKInstance.BUNDLE_URL, jsPath);
        mWXSDKInstance.renderByUrl("WXSamplexx", jsPath, options, null,  WXRenderStrategy.APPEND_ASYNC);
//        mWXSDKInstance.renderByUrl("WXSamplexx", WXFileUtils.loadAsset(jsPath, this), options, null,  WXRenderStrategy.APPEND_ASYNC);
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if(view.getParent() == null) {
            mContainer.addView(view);
        }
        mContainer.requestLayout();
    }

}
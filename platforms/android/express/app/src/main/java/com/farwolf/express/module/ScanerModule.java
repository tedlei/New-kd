package com.farwolf.express.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.farwolf.express.ui.StartTramActivity;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

public class ScanerModule extends WXModule {
    static  ScanerModule instarnce;
    static  Context mContext;

    static WXSDKInstance mWXSDKInstanceT;
    public static ScanerModule getInstarnce(Context context) {
        if (instarnce == null) {
            instarnce = new ScanerModule();
        }

        mContext = context;
        return instarnce;


    }



    @JSMethod(uiThread = false)
    public void getParams(String param){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory("com.moon.android.intent.category.WEEX");
        Activity activity = (Activity) mWXSDKInstance.getContext();
        intent.setData(Uri.parse(param));
        activity.startActivity(intent);
    }

    /***
     * 快递员扫描跳转
     * @param exName        快递员名字
     * @param exPhoneNum    快递员电话
     * @param token         登录token
     * @param com           默认快递公司
     */
    @JSMethod(uiThread = true)
    public void openExScanerActivity(String exName,String exPhoneNum,String token,String com) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }
        Intent intent = new Intent(mContext, StartTramActivity.class);
        SharedPreferencesUtil.getInstance().putSP("token",token);
        intent.putExtra("activity","ExScanerActivity");
        intent.putExtra("exName",exName);
        intent.putExtra("exPhoneNum",exPhoneNum);
        intent.putExtra("exCom",com);
        ((Activity)mContext).startActivity(intent);
    }



    /***
     * 代办点扫描跳转
     * @param address       代办点地址
     * @param token         登录token
     */


    // TODO: 2019/9/11  代办点不需要快递公司，错误回填

    @JSMethod(uiThread = true)
    public void openScanerActivity(String address,String token) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }
        Intent intent = new Intent(mContext, StartTramActivity.class);
        intent.putExtra("activity","MainScanerActivity");
        SharedPreferencesUtil.getInstance().putSP("token",token);
        intent.putExtra("address",address);
        ((Activity) mContext).startActivity(intent);
    }


    /**
     * 进入主页面自行调用 必须首先调用
     * @param baseUrl
     */
    @JSMethod(uiThread = true)
    public void initBaseUrlOrBaseCom(String baseUrl){
        if (baseUrl != null && !baseUrl.isEmpty()) {
            SharedPreferencesUtil.getInstance().putSP("baseUrl",baseUrl);
            Utils.initBaseUrl(baseUrl);
        }else{
            Utils.initBaseUrl("http://39.100.11.220:8080/");
        }
    }

    /***
     * 扫描跳转
     * @param token         登录token
     */
    @JSMethod(uiThread = true)
    public void openSearchPackageActivity(String token) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }
        Intent intent = new Intent(mContext, StartTramActivity.class);
        intent.putExtra("activity","SearchPackageActivity");
        SharedPreferencesUtil.getInstance().putSP("token",token);
        ((Activity) mContext).startActivity(intent);

    }


    /***
     * 退件
     * @param token         登录token
     * @param  type         1 快递员 2 代办点
     */
    @JSMethod(uiThread = true)
    public void openOutPackageActivity(String token,int type) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }

        Intent intent = new Intent(mContext, StartTramActivity.class);
        if(type == 2)
            intent.putExtra("activity","OutPackageActivity");
        else
            intent.putExtra("activity","OutExPackageActivity");

        SharedPreferencesUtil.getInstance().putSP("token",token);
        ((Activity) mContext).startActivity(intent);
    }




    /***
     * 出库
     * @param token
     */
    @JSMethod(uiThread = true)
    public void openOutLibActivity(String token) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }
        Intent intent = new Intent(mContext, StartTramActivity.class);
        intent.putExtra("activity","OutLibPackageActivity");
        SharedPreferencesUtil.getInstance().putSP("token",token);
        ((Activity) mContext).startActivity(intent);
    }

    /***
     * 设置是否播放声音 weex必须在初始的时候调用,默认开启
     * @param isPlay 0 播放语音提示 1 滴滴 2 关闭播放
     */
    @JSMethod(uiThread = false)
    public void setIsPlay(int isPlay) {
        if (mContext==null) {
            mContext = mWXSDKInstance.getContext();
        }
        SharedPreferencesUtil.getInstance().putIntSP("isPlay",isPlay);
    }



}

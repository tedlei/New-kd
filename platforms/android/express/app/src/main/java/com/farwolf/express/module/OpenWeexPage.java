package com.farwolf.express.module;

import android.content.Context;
import android.content.Intent;

import com.farwolf.weex.activity.WeexActivity;
import com.farwolf.weex.activity.WeexActivity_;

import java.util.HashMap;
import java.util.Map;

public class OpenWeexPage {
    static OpenWeexPage instance;
    static  Context mContext;
    public static OpenWeexPage getInstance(Context context) {
        mContext = context;
        return instance = new OpenWeexPage();
    }
    /***
     *
     * @param type 1 跳转到查询搜索 2 跳转到出库搜索
     *
     */
    public  void startInquireQueryChild(int type) {
        String url = "app/pages/page_lm/inquire/inquireQueryChild.js";
        // String url = "http://192.168.3.9:8890/js/pages/page_lm/inquire/inquireQueryChild.js";
        Intent intent = new Intent(mContext, WeexActivity_.class);
        intent.putExtra("url",url);

        HashMap<String,Object> param = new HashMap<>();
        param.put("type",type);

        intent.putExtra("param",param);
        mContext.startActivity(intent);
    }

    /***
     *
     * @param type 1 查询扫描成功后跳转到详情页 2 查询扫描成功后跳转到详情页
     */
    public  void startInquireDetail(String orderNum, int type) {
        // String url = "http://192.168.3.9:8890/js/pages/page_lm/inquire/inquireDetail.js";
        String url = "app/pages/page_lm/inquire/inquireQueryChild.js";
        Intent intent = new Intent(mContext, WeexActivity_.class);
        intent.putExtra("url",url);

        HashMap<String,Object> param = new HashMap<>();
        param.put("type",type);
        param.put("orderNum",orderNum);

        intent.putExtra("param",param);

        mContext.startActivity(intent);
    }



    /***
     * 退件扫描成功后跳转到详情页,需退件列表跳转到详情页
     * pages/page_lm/courierDetail/courierDetail.js
     * @param orderNum orderId 快递单号
     * @param type 1 快递员 2 代办点 ————
     */
    public  void startCourierDetail(String orderNum,int type) {
        String url = "app/pages/page_lm/courierDetail/courierDetail.js";
        Intent intent = new Intent(mContext, WeexActivity_.class);
        intent.putExtra("url",url);

        HashMap<String,Object> param = new HashMap<>();
        param.put("type",type);
        param.put("orderNum",orderNum);

        intent.putExtra("param",param);
        //这中形式传
        mContext.startActivity(intent);
    }

    /***
     * 跳转选择短信模板,入库的时候 后台根据当前登陆Id查询已选择模板
     */
    public  void startGetSmsItem() {
        String url = "app/pages/page_lm/smsTemplate/smsTemplate.js";
        Intent intent = new Intent(mContext, WeexActivity_.class);
        intent.putExtra("url",url);
        HashMap<String,Object> param = new HashMap<>();
        intent.putExtra("param",param);
        mContext.startActivity(intent);
    }

    /***
     * 跳转到退件搜索startQpQueryChild
     * pages/page_lm/quitPiece/qpQueryChild.js
     * @param type 1 快递员 2 代办点 ————
     */
    public  void startQpQueryChild(int type) {
        String url = "app/pages/page_lm/quitPiece/qpQueryChild.js";
        Intent intent = new Intent(mContext, WeexActivity_.class);
        intent.putExtra("url",url);

        HashMap<String,Object> param = new HashMap<>();
        param.put("type",type);
        intent.putExtra("param",param);
        mContext.startActivity(intent);
    }


}

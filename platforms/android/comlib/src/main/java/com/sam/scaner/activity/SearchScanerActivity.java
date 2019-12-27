package com.sam.scaner.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilerecognition.engine.RecogResult;
import com.sam.scaner.R;
import com.sam.scaner.bean.ScanerSearchBean;
import com.sam.scaner.utils.HTTPCallBack;
import com.sam.scaner.utils.JSON;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;
import com.scaner.phonenumber.ui.ScanerActivity;

import java.util.HashMap;
import java.util.Map;

public abstract class SearchScanerActivity extends ScanerActivity {

    ImageView       mFlashImage;
    LinearLayout    bottomView;
    TextView        tv_toobar_title;
    RelativeLayout  rl_flash;
    boolean         isOpen          = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout topView = (RelativeLayout) getLayoutInflater().inflate( R.layout.view_activity_top_bar, null, false);
        topView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int topHeight = topView.getMeasuredHeight();
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topHeight));
        mFlashImage = topView.findViewById(R.id.iv_flash);
        setTopMargin(48);
        addView2Root(topView);
        tv_toobar_title = topView.findViewById(R.id.tv_toobar_title);
        rl_flash = topView.findViewById(R.id.rl_flash);
        tv_toobar_title.setText("查询");

        bottomView = (LinearLayout) getLayoutInflater().inflate(R.layout.view_activity_out_package, null, false);
        bottomView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int bottomHeight = bottomView.getMeasuredHeight();
        bottomView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bottomHeight));
        addView2BottomLayout(bottomView);

        bottomView.setOnClickListener(view -> {
            startInquireQueryChild(1);
        });
        mFlashImage.setOnClickListener(view -> {
            openFlash(isOpen);
            isOpen = !isOpen;
            mFlashImage.setImageResource(isOpen ? R.drawable.flash_off_y : R.drawable.flash_on_y);
        });
        findViewById(R.id.tv_back).setOnClickListener(view -> finish());

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        openFlash(false);
    }

    public void setScanText(String text) {
        mQrCodeFinderView.setScanText(text);
    }


    @Override
    protected void handlerRes(RecogResult result, Bitmap bmImage) {
        super.handlerRes(result, bmImage);
        if(!lineNum.equals("")){
            setScanText("订单号: " + lineNum);
            getOrderStatus(lineNum);
        }

    }

    protected abstract void startInquireQueryChild(int type);
    protected abstract void startInquireDetail(String orderNum,int type);

    /***
     * 代办点查询
     * @param orderNum
     */
    private void getOrderStatus(String orderNum) {
        Map<String, String> parms = new HashMap<>();
        parms.put("orderId", orderNum);
        String token = SharedPreferencesUtil.getInstance().getSP("token");
        Utils.getInstance(this).post("manager/order/find", JSON.toJSONString(parms), token, new HTTPCallBack() {
            @Override
            public void Succ(String res) {
                ScanerSearchBean orderScanerDataBean = JSON.parseObject(res, ScanerSearchBean.class);
                initScan();
                if (orderScanerDataBean != null ) {
                    if(orderScanerDataBean.getCode() == 200){
                        if (orderScanerDataBean.getData().size() == 1) {
                             startInquireDetail(orderNum,1);
                        }else{
                            Utils.toast(SearchScanerActivity.this, "未查询到数据!");
                        }

                    }else {
                        Utils.toast(SearchScanerActivity.this, orderScanerDataBean.getMessage());
                    }
                } else {
                    Utils.toast(SearchScanerActivity.this, "暂无快递信息");
                }
            }

            @Override
            public void Fail(String errorStr) {
                initScan();
                Utils.toast(SearchScanerActivity.this, "暂无快递信息");
            }
        });
    }


    @Override
    protected void outTimeWarning() {

    }


}

package com.sam.scaner.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kyleduo.switchbutton.SwitchButton;
import com.mobilerecognition.engine.RecogResult;
import com.sam.scaner.R;
import com.sam.scaner.bean.DataBean;
import com.sam.scaner.bean.ExBean;
import com.sam.scaner.bean.OrderCompanyBean;
import com.sam.scaner.bean.OrderScanerDataBean;
import com.sam.scaner.bean.ScanBean;
import com.sam.scaner.utils.HTTPCallBack;
import com.sam.scaner.utils.JSON;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;
import com.scaner.phonenumber.ui.ScanerActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ScanerMainActivity extends ScanerActivity {
    ImageView mFlashImage;
    LinearLayout bottomView;
    TextView btn_close_scaner, btn_ok_in;
    View popView;
    View vv_top_padding;
    TextView tv_goods_num;
    EditText et_ex_ps_name,
            et_ex_ps_phone_num,
            et_goods_num,
            et_order_name,
            et_order_num,
            et_phone_name,
            et_phone_num;
    TextView et_address_num;
    TextView tv_toobar_title;
    TextView btn_re_start_scaner,
            tv_get_package_num,
            et_package_num,
            btn__hh_re_start_scaner;
    SwitchButton switch_is_smms;

    boolean isOpen = false;

    ArrayList<ScanBean> scanList = new ArrayList<>();
    AlertDialog dialog;
    String exName = "";  //快递员姓名
    String exPhoneNum = ""; //快递员手机号
    String goodsNum = ""; // 货号
    String orderComName = ""; //快递公司名字
    String orderNum = ""; //快递单号
    String userName = ""; //用户名字
    String userPhoneNum = ""; //用户手机号
    String address = ""; //放置地址

    ArrayList<String> localScanList = new ArrayList<>();
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        new Thread(() -> localScanList = (ArrayList<String>) SharedPreferencesUtil.getInstance().readObject(this.getApplicationContext(), "localScanList")).start();
        address = getIntent().getStringExtra("address");
        RelativeLayout topView = (RelativeLayout) getLayoutInflater().inflate(R.layout.view_activity_top_bar_mb, null, false);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getApplicationContext(), 88)));
        mFlashImage = topView.findViewById(R.id.iv_flash);
        setTopMargin(48);
        addView2Root(topView);
        topView.findViewById(R.id.btn_msm_mb).setOnClickListener(view -> startGetSmsItem());
        tv_toobar_title = findViewById(R.id.tv_toobar_title);
        bottomView = (LinearLayout) getLayoutInflater().inflate(R.layout.view_activity_bottom, null, false);
        bottomView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getApplicationContext(), 55)));
        addView2BottomLayout(bottomView);

        btn_ok_in = bottomView.findViewById(R.id.btn_ok_in);
        btn_close_scaner = bottomView.findViewById(R.id.btn_close_scaner);
        tv_toobar_title.setText("入库");
        btn_ok_in.setText("确定入库");
        btn_close_scaner.setText("结束入库");
        btn_ok_in.setOnClickListener(view -> okScanerNext());
        btn_close_scaner.setOnClickListener(view -> {
            String orderIntegration = btn_close_scaner.getTag() + "";
            if(orderIntegration.equals("") || orderIntegration.equals("null")) {
                finish();
            }else {
                Intent intent = new Intent(getApplicationContext(), InPackageRecordActivity.class);
                intent.putExtra("orderIntegration", orderIntegration);
                startActivity(intent);
                finish();
            }

        });

        mFlashImage.setOnClickListener(view -> {
            openFlash(isOpen);
            isOpen = !isOpen;
            mFlashImage.setImageResource(isOpen ? R.drawable.flash_off_y : R.drawable.flash_on_y);
        });
        findViewById(R.id.tv_back).setOnClickListener(view -> exitFinish());
        if (popView == null) {
            popView = LayoutInflater.from(this).inflate(R.layout.view_activity_scaner, null);
            tv_get_package_num = popView.findViewById(R.id.tv_get_package_num);
            vv_top_padding = popView.findViewById(R.id.vv_top_padding);
            int topMpar = (int) (getWindow().getWindowManager().getDefaultDisplay().getWidth() + 90);
            vv_top_padding.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, topMpar));
            popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = popView.getMeasuredHeight();
            popView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
            setContentLayoutAndPadding(popView, 0);
            et_goods_num = popView.findViewById(R.id.et_goods_num);
            et_order_name = popView.findViewById(R.id.et_order_name);
            switch_is_smms = popView.findViewById(R.id.switch_is_smms);
            et_order_num = popView.findViewById(R.id.et_order_num);
            et_package_num = popView.findViewById(R.id.et_package_num);
            et_phone_name = popView.findViewById(R.id.et_phone_name);
            et_phone_num = popView.findViewById(R.id.et_phone_num);
            tv_goods_num = popView.findViewById(R.id.tv_goods_num);
            et_address_num = popView.findViewById(R.id.et_address_num);
            btn_re_start_scaner = popView.findViewById(R.id.btn_re_start_scaner);
            btn__hh_re_start_scaner = popView.findViewById(R.id.btn__hh_re_start_scaner);
            et_ex_ps_name = popView.findViewById(R.id.et_ex_ps_name);
            et_ex_ps_phone_num = popView.findViewById(R.id.et_ex_ps_phone_num);
            btn_re_start_scaner.setOnClickListener(view -> {
                initScaner();
                scrollToView(50);
            });
            btn__hh_re_start_scaner.setOnClickListener(view -> {
                initScaner();
                scrollToView(50);
            });

            et_phone_num.addTextChangedListener(new MyTextWatcher(et_phone_num));
            et_ex_ps_name.addTextChangedListener(new MyTextWatcher(et_ex_ps_name));
            et_ex_ps_phone_num.addTextChangedListener(new MyTextWatcher(et_ex_ps_phone_num));
            et_order_name.addTextChangedListener(new MyTextWatcher(et_order_name));
            et_order_num.addTextChangedListener(new MyTextWatcher(et_order_num));
            et_phone_name.addTextChangedListener(new MyTextWatcher(et_phone_name));
            getOrderInfo("",false);
        }
        btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
    }

    protected abstract void startGetSmsItem();

    private void exitFinish() {
        finish();

    }


    //默认进入 设置高度 padding

    class MyTextWatcher implements TextWatcher {

        View mView;

        public MyTextWatcher(View view) {
            mView = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            showBtnStatus(mView);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitFinish();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        openFlash(false);
    }

    @Override
    protected void outTimeWarning() {

    }

    public void setScanText(String text) {
        mQrCodeFinderView.setScanText(text);
    }


    @Override
    protected void handlerRes(RecogResult result, Bitmap bmImage) {
        super.handlerRes(result, bmImage);
        if (!lineNum.equals("")) {
            if (phoneNum.equals("")) {
                getOrderInfo(lineNum,true);
            }else{
                Utils.getInstance(this).play(Utils.AudioEnum.tipCheckInputInfo.mAudioName);//确认完整
            }
            setScanText("订单号: " + lineNum + (phoneNum.equals("") ? " 识别手机号中... " : "  手机号: " + phoneNum));
            setGoodsNum(lineNum, phoneNum);
            showBtnStatus(btn_ok_in);

        }

    }


    private void showExListDialog(ExBean exBean) {
        if (exBean.getShippers().size() == 1) {
            et_order_name.setText(exBean.getShippers().get(0).getShipperName());
            et_order_name.setTag(exBean.getShippers().get(0).getShipperCode());
        } else {
            if (exBean.getShippers().size() > 0) {
                String items[] = new String[exBean.getShippers().size()];
                for (int i = 0; i < exBean.getShippers().size(); i++) {
                    items[i] = exBean.getShippers().get(i).getShipperName();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("选择快递公司");
                builder.setItems(items, (dialog, which) -> {
                    dialog.dismiss();
                    et_order_name.setText(exBean.getShippers().get(which).getShipperName());
                    et_order_name.setTag(exBean.getShippers().get(which).getShipperCode());
                    showBtnStatus(et_order_name);
                });
                if (dialog == null) {
                    dialog = builder.create();
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }

            }
        }


    }




    private void setGoodsNum(String orNum, String phNum) {
        if (!isOrderNotSource) {
            et_phone_num.setText(phNum);
        }
        et_order_num.setText(orNum);
        orderNum = orNum;
        userPhoneNum = phNum;
        showPackAgeNum();
    }

    private void showPackAgeNum() {
//        if (tv_get_package_num != null) {
//            tv_get_package_num.setText("集件包裹数:" + packAgeNum);
//        }
//        if (et_package_num != null) {
//            et_package_num.setText(packAgeNum + "");
//        }
    }



    private boolean checkIsInputSuc(View view) {
        orderComName = et_order_name.getText().toString();
        orderNum = et_order_num.getText().toString();
        goodsNum = et_goods_num.getText().toString();
        userName = et_phone_name.getText().toString();
        userPhoneNum = et_phone_num.getText().toString();
        address = et_address_num.getText().toString();

        if (view.getId() == et_phone_num.getId()) {
            if (userName.equals("")) {
                if (userPhoneNum.length() == 11) {
                    String userNameT = SharedPreferencesUtil.getInstance().getSP(userPhoneNum);
                    if (userNameT != null && userNameT.length() > 0) {
                        et_phone_name.post(() -> et_phone_name.setText(userNameT));
                    }
                }
            }
        }



        int isSuc = 0;
        if (userPhoneNum.length() < 11 || userPhoneNum.equals("")  ) {
            isSuc++;
        }
        if (orderNum.equals("") || orderComName.equals("") ) {
            isSuc++;
        }

        if (goodsNum.equals("")) {
            isSuc++;
        }
        return isSuc > 0 ? false : true;
    }


    private void initScaner() {
        isOrderNotSource = false;
        btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
        mQrCodeFinderView.setScanText("识别订单中...");
        et_order_name.setText("");
        et_order_num.setText("");
        et_phone_num.setText("");
        et_ex_ps_name.setText("");
        et_ex_ps_phone_num.setText("");
        et_address_num.setText("");
        et_phone_name.setText("");
        tv_get_package_num.setText("集件包裹:0");
        initScan();

        popView.findViewById(R.id.ll_ex_num).setVisibility(View.GONE);
        tv_get_package_num.setVisibility(View.GONE);
        et_package_num.setVisibility(View.GONE);
        popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int height = popView.getMeasuredHeight();
        popView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
    }

    private void showBtnStatus(View view) {
        if (checkIsInputSuc(view)) {
            btn_ok_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
        }
    }


    /**
     * 绑定代办点扫描
     *
     * @param bean
     */
    private void setEtTag3Content(DataBean bean) {
        et_goods_num.setText(goodsNum);
        btn_ok_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));

        if (bean.getUserId() != null && !bean.getUserId().equals("")) {
            et_ex_ps_phone_num.setText(bean.getUserId());
            popView.findViewById(R.id.ll_ex_num).setVisibility(View.VISIBLE);
        }
        if (bean.getCompany() != null && !bean.getCompany().equals("")) {
            et_order_name.setText(bean.getCompany());
        }
        if (bean.getOrderId() != null && !bean.getOrderId().equals("")) {
            et_order_num.setText(bean.getOrderId());
        }


        if (bean.getNickName() != null && !bean.getNickName().equals("")) {
            et_phone_name.setText(bean.getNickName());
        }



        if (bean.getExpressOwnerTel() != null && !bean.getExpressOwnerTel().equals("")) {
            et_phone_num.setText(bean.getExpressOwnerTel());
        }

        if (bean.getUserName() != null && !bean.getUserName().equals("")) {
            et_ex_ps_name.setText(bean.getUserName());
        }

        if (bean.getAgentAddress() != null && !bean.getAgentAddress().equals("")) {
            et_address_num.setText(bean.getAgentAddress());
        }
    }

    private void okScanerNext() {
        if (!checkIsInputSuc(btn_ok_in)) {
            return;
        }
        boolean isCheck = false;
        if (localScanList != null) {
            for (String s : localScanList) {
                if (s.equals(orderNum)) {
                    isCheck = true;
                    break;
                }
            }
        }

        for (ScanBean bean : scanList) {
            if (bean.getOrderId().equals(orderNum)) {
                isCheck = true;
                break;
            }
        }
        //判断是否重复
        if (isCheck) {
            Utils.toast(this, "订单重复入库");
            Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.repetition.mAudioName);
            return;
        }
        scanList.add(new ScanBean(orderNum, userPhoneNum, address, userName, orderComName));
        SharedPreferencesUtil.getInstance().putSP(userPhoneNum, userName);
        if (localScanList == null) {
            localScanList = new ArrayList<>();
        }
        localScanList.add(orderNum);
        inPackage();
        initScaner();
        showPackAgeNum();


    }


    /***
     * 入库
     */
    private void inPackage() {
        progressDialog.show();
        String token = SharedPreferencesUtil.getInstance().getSP("token");
        String listJson = JSON.toJSONString(scanList);
        int isSmms = switch_is_smms.isChecked() ? 2 : 1;
        Utils.getInstance(this).post("manager/order/add?smss=" + isSmms, listJson, token, new HTTPCallBack() {
            @Override
            public void Succ(String res) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    int status = jsonObject.getInt("code");
                    if (status == 200) {
                        scanList.clear();
                        Utils.toast(ScanerMainActivity.this, "扫描入库成功!");
                        Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.enterIntoSucceed.mAudioName);
                        showPackAgeNum();
                        initGoodsNum();
                    } else {
                        String message = jsonObject.getString("message");
                        Utils.toast(ScanerMainActivity.this, message);
                        if(message.contains("重复")){
                            Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.repetition.mAudioName);
                        }else {
                            Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.toast(ScanerMainActivity.this, "扫描入库失败!");
                    Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
                }

            }

            @Override
            public void Fail(String errorStr) {
                progressDialog.dismiss();
                btn_ok_in.setEnabled(true);
                Utils.toast(ScanerMainActivity.this, "扫描入库失败!");
                Utils.getInstance(ScanerMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
            }
    });
}


    private boolean isOrderNotSource = false;

    /***
     * 代办点查询包裹信息
     * @param orderNum
     * @param isInitGoodsNum
     */
    private void getOrderInfo(String orderNum ,boolean isInitGoodsNum) {
        Map<String, String> parms = new HashMap<>();
        parms.put("orderId", orderNum);
        String token = SharedPreferencesUtil.getInstance().getSP("token");
        Utils.getInstance(this).post("/manager/order/scan", JSON.toJSONString(parms), token, new HTTPCallBack() {
            @Override
            public void Succ(String res) {
                OrderScanerDataBean orderScanerDataBean = JSON.parseObject(res, OrderScanerDataBean.class);
                if (orderScanerDataBean != null && orderScanerDataBean.getCode() == 200) {
                    showPackAgeNum();
                    goodsNum = orderScanerDataBean.getMessage();
                    setEtTag3Content(orderScanerDataBean.getData());
                    btn_close_scaner.setTag(orderScanerDataBean.getData().getOrderIntegration());
                    isOrderNotSource = true;
                    tv_get_package_num.setVisibility(View.VISIBLE);
                    popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                    int height = popView.getMeasuredHeight();
                    popView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height));
                    showBtnStatus(popView);
                } else {
                    if (orderScanerDataBean.getCode() == 201) {
                        if(!isInitGoodsNum){
                            goodsNum = orderScanerDataBean.getMessage();
                            if (!goodsNum.equals("")) {
                                et_goods_num.setText(goodsNum+"");
                            }
                        }
                    }
                    if(isInitGoodsNum)  restartScanT(orderNum);
                }
            }

            @Override
            public void Fail(String errorStr) {
                restartScanT(orderNum);
            }
        });
    }

    /**
     * 生成货号
     */
    private void initGoodsNum() {
        if(goodsNum.equals("")) return;
        int tmppackage = Integer.valueOf(goodsNum);
        tmppackage += 1;
        goodsNum = String.valueOf(tmppackage);
        et_goods_num.setText(goodsNum);
    }
    private void restartScanT(String orderNum) {
        setIsAvailable(true);
        setIsQrCodeOne(false);
        restartScan();
        isOrderNotSource = false;
        getOrderCom(orderNum);
    }


    /***
     * @param orderNum 是获取快递商
     */
    private void getOrderCom(String orderNum) {
        Map<String, String> parms = new HashMap<>();
        parms.put("orderId", orderNum);
        String token = SharedPreferencesUtil.getInstance().getSP("token");
        Utils.getInstance(this).post("order/ordercompany", JSON.toJSONString(parms), token, new HTTPCallBack() {
            @Override
            public void Succ(String res) {
                OrderCompanyBean orderCompanyBean = JSON.parseObject(res, OrderCompanyBean.class);
                if (orderCompanyBean != null && orderCompanyBean.getCode() == 200) {
                    ExBean ex = JSON.parseObject(orderCompanyBean.getData(), ExBean.class);
                    showExListDialog(ex);
                }
            }

            @Override
            public void Fail(String errorStr) {
                Log.i("SamTam", errorStr);
            }
        });
    }

    private void log(String msg) {
        Log.i("SamTam", msg);
    }
}

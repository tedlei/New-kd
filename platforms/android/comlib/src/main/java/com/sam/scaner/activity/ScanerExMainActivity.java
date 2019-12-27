package com.sam.scaner.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
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

import com.kyleduo.switchbutton.SwitchButton;
import com.mobilerecognition.engine.RecogResult;
import com.sam.scaner.R;
import com.sam.scaner.bean.ExBean;
import com.sam.scaner.bean.OrderCompanyBean;
import com.sam.scaner.bean.ScanBean;
import com.sam.scaner.utils.HTTPCallBack;
import com.sam.scaner.utils.JSON;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;
import com.scaner.phonenumber.ui.ScanerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ScanerExMainActivity extends ScanerActivity {
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
    AlertDialog.Builder builder;

    String exName = "";  //快递员姓名
    String exPhoneNum = ""; //快递员手机号
    String goodsNum = ""; // 货号
    String orderComName = ""; //快递公司名字
    String orderNum = ""; //快递单号
    String userName = ""; //用户名字
    String userPhoneNum = ""; //用户手机号
    String address = ""; //放置地址

    String defaultCom = "";

    int localPackAgeNum = 0;
    int packAgeNum = 0;   //快递件数
    ArrayList<String> localScanList = new ArrayList<>();
    String[] dataAddressList;
    ProgressDialog progressDialog;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exName = getIntent().getStringExtra("exName");
        exPhoneNum = getIntent().getStringExtra("exPhoneNum");
        defaultCom = getIntent().getStringExtra("exCom");
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        new Thread(() -> localScanList = (ArrayList<String>) SharedPreferencesUtil.getInstance().readObject(this.getApplicationContext(), "localScanListEx")).start();
        address = SharedPreferencesUtil.getInstance().getSP("address");
        RelativeLayout topView = (RelativeLayout) getLayoutInflater().inflate(R.layout.view_activity_top_bar_mb, null, false);
        topView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getApplicationContext(), 88)));
        mFlashImage = topView.findViewById(R.id.iv_flash);
        setTopMargin(48);
        addView2Root(topView);
        topView.findViewById(R.id.btn_msm_mb).setOnClickListener(view -> startGetSmsItem());
        String tmplocalPackAgeNum = SharedPreferencesUtil.getInstance().getSP("localPackAgeNumEx");
        if (tmplocalPackAgeNum != null && !tmplocalPackAgeNum.equals("")) {
            localPackAgeNum = Integer.valueOf(tmplocalPackAgeNum);
        }
        tv_toobar_title = findViewById(R.id.tv_toobar_title);
        bottomView = (LinearLayout) getLayoutInflater().inflate(R.layout.view_activity_bottom, null, false);
        bottomView.setLayoutParams(new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utils.dip2px(getApplicationContext(), 55)));
        addView2BottomLayout(bottomView);

        btn_ok_in = bottomView.findViewById(R.id.btn_ok_in);
        btn_close_scaner = bottomView.findViewById(R.id.btn_close_scaner);
        tv_toobar_title.setText("扫描");
        btn_ok_in.setText("确认放置");
        btn_close_scaner.setText("结束扫描");

        btn_ok_in.setOnClickListener(view -> okScanerNext());
        btn_close_scaner.setOnClickListener(view -> {
            if (scanList.size() == 0) {
                finish();
            } else {
                progressDialog.show();
                inPackage();
            }
        });

        mFlashImage.setOnClickListener(view -> {
            openFlash(isOpen);
            isOpen = !isOpen;
            mFlashImage.setImageResource(isOpen ? R.drawable.flash_off_y : R.drawable.flash_on_y);
        });
        findViewById(R.id.tv_back).setOnClickListener(view -> {
            exitFinish();
        });
        if (popView == null) {
            popView = LayoutInflater.from(this).inflate(R.layout.view_activity_scaner, null);
            tv_get_package_num = popView.findViewById(R.id.tv_get_package_num);
            vv_top_padding = popView.findViewById(R.id.vv_top_padding);
            popView.findViewById(R.id.ll_goods_num).setVisibility(View.GONE);
            popView.findViewById(R.id.ll_package_num).setVisibility(View.VISIBLE);
            popView.findViewById(R.id.ll_ex_num).setVisibility(View.GONE);
            popView.findViewById(R.id.ll_address_num).setVisibility(View.VISIBLE);


            int topMpar = (int) (getWindow().getWindowManager().getDefaultDisplay().getWidth() + 90);
            vv_top_padding.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, topMpar));
            popView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

            int width = popView.getMeasuredWidth();
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
                scrollToView(1);
            });
            btn__hh_re_start_scaner.setOnClickListener(view -> {
                initScaner();
                scrollToView(1);
            });

            et_phone_num.addTextChangedListener(new MyTextWatcher(et_phone_num));
            et_ex_ps_name.addTextChangedListener(new MyTextWatcher(et_ex_ps_name));
            et_ex_ps_phone_num.addTextChangedListener(new MyTextWatcher(et_ex_ps_phone_num));
            et_order_name.addTextChangedListener(new MyTextWatcher(et_order_name));
            et_order_num.addTextChangedListener(new MyTextWatcher(et_order_num));
            et_phone_name.addTextChangedListener(new MyTextWatcher(et_phone_name));

            et_address_num.setOnClickListener(view ->{
                    showAddressListDialog();
            });
            et_address_num.setText(address);
            initGoodsNum();
        }
        et_ex_ps_name.setText(exName);
        et_ex_ps_phone_num.setText(exPhoneNum);

        btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
    }

    protected abstract void startGetSmsItem();


    private void showAddressListDialog() {

        if(dataAddressList==null ) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择代办的地址");
        int checkItem = 0;
        for (int i = 0; i < dataAddressList.length; i++) {
            if(address.equals(dataAddressList[i])){
                checkItem = i;
                break;
            }
        }

        builder.setSingleChoiceItems(dataAddressList, checkItem, (dialog, which) -> {
            dialog.dismiss();
            address = dataAddressList[which];
            SharedPreferencesUtil.getInstance().putSP("address", address);
            et_address_num.setText(address);
            showBtnStatus(et_address_num);
        });

        builder.create().show();

    }

    private void exitFinish() {
        if (scanList.size() <= 0) {
            finish();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("提示");
            builder.setMessage("是否取消当前操作,放置包裹将被清空,是否需要进行入库");
            builder.setPositiveButton("需要入库", (dialogInterface, i) -> inPackage());
            builder.setNegativeButton("取消", (dialogInterface, i) ->  finish());

            builder.create().show();
        }
    }


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
            if (mView.getId() == R.id.et_address_num) {

            }
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
                getOrderCom(lineNum);
            }else{
                Utils.getInstance(this).play(Utils.AudioEnum.tipCheckInputInfo.mAudioName);
            }
            setScanText("订单号: " + lineNum + (phoneNum.equals("") ? " 识别手机号中... " : "  手机号: " + phoneNum));
            setGoodsNum(lineNum, phoneNum);
            showBtnStatus(btn_ok_in);

        }
    }

    private void showTestBitmap(boolean isShow, Bitmap bitmap) {
        findViewById(R.id.iv_test_bitmap).setVisibility(isShow ? View.VISIBLE : View.GONE);
    }


    //默认选择？还是自定选择？如果没查询出来  就将我传的值赋值到公司 输入框中去
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
        et_phone_num.setText(phNum);
        et_order_num.setText(orNum);
        orderNum = orNum;
        userPhoneNum = phNum;

    }

    private void showPackAgeNum() {
        if (tv_get_package_num != null) {
            tv_get_package_num.setText("集件包裹数:" + packAgeNum);
        }
        if (et_package_num != null) {
            et_package_num.setText(packAgeNum + "");
        }
    }


    public String convertTimestamp2Date(Long timestamp, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date(timestamp));
    }

    private boolean checkIsInputSuc(View view) {
        orderComName = et_order_name.getText().toString();
        orderNum = et_order_num.getText().toString();
        exName = et_ex_ps_name.getText().toString();
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

        if (orderNum.equals("") || orderComName.equals("") || userPhoneNum.length() < 11  || userPhoneNum.equals("")) {
            isSuc++;
        }
        if (address.equals("")) {
            isSuc++;
        }
        return isSuc > 0 ? false : true;
    }

    /**
     * 生成货号
     */
    private void initGoodsNum() {
        String tmpPackAgeNum = "";

        int tmppackage = localPackAgeNum + packAgeNum;
        tmppackage++;
        if (tmppackage < 10) {
            tmpPackAgeNum = "000" + tmppackage;
        } else if (tmppackage >= 10 && tmppackage < 100) {
            tmpPackAgeNum = "00" + tmppackage;
        } else if (tmppackage >= 100 && tmppackage < 1000) {
            tmpPackAgeNum = "0" + tmppackage;
        }
        String goos = convertTimestamp2Date(System.currentTimeMillis(), "MMdd");
        goodsNum = goos + tmpPackAgeNum;
        et_goods_num.setText(goodsNum);
    }

    private void initScaner() {
        btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
        mQrCodeFinderView.setScanText("识别订单中...");
        et_order_name.setText("");
        et_order_num.setText("");
        et_phone_num.setText("");
        et_phone_name.setText("");
        showTestBitmap(false, null);
        initScan();
    }

    private void showBtnStatus(View view) {
        if (checkIsInputSuc(view)) {
            btn_ok_in.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            btn_ok_in.setBackgroundColor(getResources().getColor(R.color.unenabled));
        }
    }



    private void okScanerNext() {
        if (!checkIsInputSuc(btn_ok_in)) {
            return;
        }
        if (address.equals("")) {
            Utils.toast(this, "请选择放置地址!");
            Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.placeFailed.mAudioName);
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
            Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.repetition.mAudioName);
            return;
        }
        scanList.add(new ScanBean(orderNum, userPhoneNum, address, userName, orderComName));
        SharedPreferencesUtil.getInstance().putSP(userPhoneNum, userName);
        if (localScanList == null) {
            localScanList = new ArrayList<>();
        }
        localScanList.add(orderNum);
        packAgeNum++;
        initScaner();
        showPackAgeNum();
        Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.placeSucceed.mAudioName);
        et_address_num.setEnabled(false);

    }



    private void inPackage() {
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
                        Utils.toast(ScanerExMainActivity.this, "扫描入库成功!");
                        Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.enterIntoSucceed.mAudioName);
                        new Thread(() -> SharedPreferencesUtil.saveObject(getApplicationContext(), "localScanListEx", localScanList)).start();
                        //保存快递件数
                        SharedPreferencesUtil.getInstance().putSP("localScanListEx", (localPackAgeNum + packAgeNum) + "");
                        packAgeNum = 0;
                        showPackAgeNum();
                    } else {
                        String message = jsonObject.getString("message");
                        Utils.toast(ScanerExMainActivity.this, message);
                        Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Utils.toast(ScanerExMainActivity.this, "扫描入库失败!");
                    Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
                }

            }

            @Override
            public void Fail(String errorStr) {
                progressDialog.dismiss();
                Utils.toast(ScanerExMainActivity.this, "扫描入库失败!");
                Utils.getInstance(ScanerExMainActivity.this).play(Utils.AudioEnum.enterIntoFailed.mAudioName);
            }
        });
    }



    /***
     * 快递员扫描 代办点 同时扫描
     * @param orderNum
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
                    Log.i("SamTag",defaultCom+"------------");
                    if (ex!=null && ex.getShippers()!=null && ex.getShippers().size() > 0) {
                        showExListDialog(ex);
                    }else{
                        setComNameData();
                    }
                }
            }
            @Override
            public void Fail(String errorStr) {
                setComNameData();
            }
        });

    }

    private void setComNameData(){
        orderComName = defaultCom;
        et_order_name.setText(orderComName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList(false);
    }

    private void getAddressList(boolean showDialog) {
        Map<String, String> parms = new HashMap<>();
        String token = SharedPreferencesUtil.getInstance().getSP("token");
        Utils.getInstance(this).post("orderAddress/getUserAddress", JSON.toJSONString(parms), token, new HTTPCallBack() {
            @Override
            public void Succ(String res) {
                log(res);
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray dataArr = jsonObject.getJSONArray("data");
                    JSONArray data1Arr = jsonObject.getJSONArray("data1");
                    List<String> tmpAddressList = new ArrayList<>();
                    if(dataArr!=null && dataArr.length() > 0){
                        for (int i = 0; i < dataArr.length(); i++) {
                            tmpAddressList.add(dataArr.getString(i));
                        }
                    }
                    if(data1Arr!=null && data1Arr.length() > 0){
                        for (int i = 0; i < data1Arr.length(); i++) {
                            tmpAddressList.add(data1Arr.getString(i));
                        }
                    }
                    dataAddressList = tmpAddressList.toArray(new String[tmpAddressList.size()]);
                    int tmpIndex = 0 ;
                    for (String s : dataAddressList) {
                        if(address.equals(s)){
                            tmpIndex ++;
                            break;
                        }
                    }
                    if(tmpIndex <= 0){
                        et_address_num.setText("");
                        address = "";
                    }
                    if (showDialog) {
                        showAddressListDialog();
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
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

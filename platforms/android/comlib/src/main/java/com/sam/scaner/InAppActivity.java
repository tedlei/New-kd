package com.sam.scaner;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sam.scaner.activity.OutEx;
import com.sam.scaner.activity.OutMain;
import com.sam.scaner.activity.OutScanerMainActivity;
import com.sam.scaner.activity.ScanerExMainActivity;
import com.sam.scaner.activity.ScanerMainActivity;
import com.sam.scaner.activity.SearchScanerActivity;
import com.sam.scaner.bean.LoginBean;
import com.sam.scaner.utils.HTTPCallBack;
import com.sam.scaner.utils.JSON;
import com.sam.scaner.utils.SharedPreferencesUtil;
import com.sam.scaner.utils.Utils;

import java.util.HashMap;
import java.util.Map;


public class InAppActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesUtil.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        Utils.initBaseUrl("http://h2436m0141.wicp.vip/");
        ActivityCompat.requestPermissions(InAppActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    String token = "";
    String name = "";
    String phoneNum = "";
    String address = "";

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_login) {
            Map<String, String> parmas = new HashMap<>();
            String telNo = ((EditText)findViewById(R.id.et_phone_num)).getText().toString();
            String password = ((EditText)findViewById(R.id.et_pwd)).getText().toString();

            parmas.put("telNo", telNo);
            parmas.put("password", password);
            Utils.getInstance(this).post("orderUser/login", JSON.toJSONString(parmas), "", new HTTPCallBack() {
                @Override
                public void Succ(String res) {
                    LoginBean loginBean = JSON.parseObject(res, LoginBean.class);
                    if (loginBean != null && loginBean.getCode()== 200) {
                        token = loginBean.getData().getSessionId();
                        name = loginBean.getData().getUserName();
                        phoneNum = loginBean.getData().getUserPhone();
                        address = loginBean.getData().getAddress();
                        view.post(() -> {
                            ((TextView) findViewById(R.id.tv_loging_data)).setText(loginBean.getData().toString());
                            findViewById(R.id.btn_ex).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_scaner).setVisibility(View.VISIBLE);
                            findViewById(R.id.btn_out_package).setVisibility(View.VISIBLE);
//                            findViewById(R.id.btn_search).setVisibility(View.VISIBLE);
//                            findViewById(R.id.btn_all_search).setVisibility(View.VISIBLE);
//                            findViewById(R.id.btn_ex_search).setVisibility(View.VISIBLE);
                        });


                    }
                }

                @Override
                public void Fail(String errorStr) {
                    view.post(() -> ((TextView) findViewById(R.id.tv_loging_data)).setText(errorStr));
                }
            });
        } else {

            int id = view.getId();

            if (id == R.id.btn_audio) {
                Utils.getInstance(this).play(Utils.AudioEnum.alreadyGoOut.mAudioName);
            } else {
                if (token.equals("")) {
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), ScanerMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("exName", name);
                bundle.putString("exPhoneNum", phoneNum);
                bundle.putString("address", address);
                SharedPreferencesUtil.getInstance().putSP("token", token);
                bundle.putString("token", token);

                if (id == R.id.btn_ex) {
                    intent = new Intent(getApplicationContext(), OutEx.class);
                }
                else if (id == R.id.btn_all) {
                    bundle.putInt("scanerTag", 3);
                    intent = new Intent(getApplicationContext(), OutMain.class);
                }
//                else if (id == R.id.btn_scaner) {
//                    bundle.putInt("scanerTag", 2);
//                } else if (id == R.id.btn_out_package) {
//                    intent = new Intent(getApplicationContext(), OutEx.class);
//                } else if (id == R.id.btn_all_search) {
//                    bundle.putInt("scanerTag", 3);
//                    intent = new Intent(getApplicationContext(), SearchScanerActivity.class);
//                } else if (id == R.id.btn_ex_search) {
//                    bundle.putInt("scanerTag", 1);
//                    intent = new Intent(getApplicationContext(), SearchScanerActivity.class);
//                } else if (id == R.id.btn_search) {
//                    bundle.putInt("scanerTag", 2);
//                    intent = new Intent(getApplicationContext(), SearchScanerActivity.class);
//                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }
}

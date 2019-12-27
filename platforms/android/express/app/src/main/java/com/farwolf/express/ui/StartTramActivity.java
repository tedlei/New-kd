package com.farwolf.express.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.sam.scaner.activity.ScanerExMainActivity;
import com.sam.scaner.activity.ScanerMainActivity;
import com.sam.scaner.utils.SharedPreferencesUtil;

/***
 * 过度activity
 */
public class StartTramActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(StartTramActivity.this, new String[]{Manifest.permission.CAMERA}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    switch (getIntent().getStringExtra("activity")) {
                        case "ExScanerActivity":
                            intent.setClass(this, ScanExPageActivity.class);
                            break;
                        case "MainScanerActivity":
                            intent.setClass(this, ScanMainPageActivity.class);
                            break;
                        case "SearchPackageActivity":
                            intent.setClass(this,SearchPackageActivity.class);
                            break;
                        case "OutPackageActivity":
                            intent.setClass(this,OutPackageActivity.class);
                            break;
                        case "OutExPackageActivity":
                            intent.setClass(this,OutExPackageActivity.class);
                            break;
                        case "OutLibPackageActivity":
                            intent.setClass(this,OutLibPackageActivity.class);
                            break;
                    }
                    Bundle bundle = getIntent().getExtras();
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "没有相机权限", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        }

    }

}

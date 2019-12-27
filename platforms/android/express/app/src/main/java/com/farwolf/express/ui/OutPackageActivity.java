package com.farwolf.express.ui;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.OutScanerMainActivity;

/***
 * 退件
 */
public class OutPackageActivity extends OutScanerMainActivity {

    @Override
    protected void startCourierDetail(String s,int type) {
        OpenWeexPage.getInstance(this).startCourierDetail(s,type);
    }

    @Override
    protected void startQpQueryChild(int type) {
        OpenWeexPage.getInstance(this).startQpQueryChild(type);
    }
}

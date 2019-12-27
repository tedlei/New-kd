package com.farwolf.express.ui;

import android.util.Log;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.OutLibraryScanerActivity;
import com.sam.scaner.utils.SharedPreferencesUtil;

/***
 * 出库
 */
public class OutLibPackageActivity extends OutLibraryScanerActivity {
    @Override
    protected void startInquireQueryChild(int i) {
        OpenWeexPage.getInstance(this).startInquireQueryChild(i);
    }

    @Override
    protected void startInquireDetail(String s, int i) {
        OpenWeexPage.getInstance(this).startInquireDetail(s,i);
    }
}

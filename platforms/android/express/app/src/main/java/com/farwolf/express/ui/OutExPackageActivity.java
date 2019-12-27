package com.farwolf.express.ui;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.OutExScanerActivity;

public class OutExPackageActivity extends OutExScanerActivity {
    @Override
    protected void startCourierDetail(String s, int i) {
        OpenWeexPage.getInstance(this).startCourierDetail(s,i);
    }

    @Override
    protected void startQpQueryChild(int i) {
        OpenWeexPage.getInstance(this).startQpQueryChild(i);
    }
}

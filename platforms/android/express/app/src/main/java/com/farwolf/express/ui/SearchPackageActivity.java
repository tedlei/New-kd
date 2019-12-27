package com.farwolf.express.ui;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.SearchScanerActivity;

/***
 * 搜索
 */
public class SearchPackageActivity extends SearchScanerActivity {

    @Override
    protected void startInquireQueryChild(int i) {
        OpenWeexPage.getInstance(this).startInquireQueryChild(i);

    }

    @Override
    protected void startInquireDetail(String s, int i) {
        OpenWeexPage.getInstance(this).startInquireDetail(s,i);
    }
}

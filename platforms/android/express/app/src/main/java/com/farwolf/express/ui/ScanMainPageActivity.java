package com.farwolf.express.ui;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.ScanerExMainActivity;
import com.sam.scaner.activity.ScanerMainActivity;

public class ScanMainPageActivity extends ScanerMainActivity {
    @Override
    protected void startGetSmsItem() {
        OpenWeexPage.getInstance(this).startGetSmsItem();
    }

}

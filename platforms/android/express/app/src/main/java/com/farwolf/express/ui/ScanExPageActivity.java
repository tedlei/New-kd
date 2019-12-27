package com.farwolf.express.ui;

import com.farwolf.express.module.OpenWeexPage;
import com.sam.scaner.activity.ScanerExMainActivity;

public class ScanExPageActivity extends ScanerExMainActivity {
    @Override
    protected void startGetSmsItem() {
        OpenWeexPage.getInstance(this).startGetSmsItem();
    }
}

package com.farwolf.express.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.RelativeLayout

import com.farwolf.weex.activity.WeexActivity

class TestActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val button = Button(applicationContext)
        button.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 200)
        button.text = "check"
        button.setOnClickListener {
            val intent = Intent(applicationContext, WeexPageActivity::class.java)
            intent.putExtra("url", "app/entry.js")
            //            intent.putExtra("url","app/pages/page_lm/courierDetail/courierDetail.js");
            startActivity(intent)
        }
        setContentView(button)
    }
}

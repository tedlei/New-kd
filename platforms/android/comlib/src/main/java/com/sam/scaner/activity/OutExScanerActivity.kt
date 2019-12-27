package com.sam.scaner.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.mobilerecognition.engine.RecogResult
import com.sam.scaner.R
import com.sam.scaner.bean.ScanerSearchBean
import com.sam.scaner.utils.HTTPCallBack
import com.sam.scaner.utils.JSON
import com.sam.scaner.utils.SharedPreferencesUtil
import com.sam.scaner.utils.Utils
import com.scaner.phonenumber.ui.ScanerActivity
import kotlinx.android.synthetic.main.view_activity_top_bar.view.*

import java.util.HashMap

abstract class OutExScanerActivity : ScanerActivity() {
    var isOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topView = layoutInflater.inflate(R.layout.view_activity_top_bar, null, false) as RelativeLayout
        topView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val topHeight = topView.measuredHeight
        topView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topHeight)
        setTopMargin(48)
        addView2Root(topView)
        topView.tv_toobar_title.text = "退件处理"
        val bottomView = layoutInflater.inflate(R.layout.view_activity_out_package, null, false) as LinearLayout
        bottomView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bottomHeight = bottomView.measuredHeight
        bottomView.layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bottomHeight)
        addView2BottomLayout(bottomView)

        bottomView.setOnClickListener {startQpQueryChild(1) }
        topView.iv_flash.setOnClickListener {
            openFlash(isOpen)
            isOpen = !isOpen
            topView.iv_flash.setImageResource(if (isOpen) R.drawable.flash_off_y else R.drawable.flash_on_y)
        }
        topView.tv_back.setOnClickListener {finish() }

    }


    override fun onDestroy() {
        super.onDestroy()
        openFlash(false)
    }

    fun setScanText(text: String) {
        mQrCodeFinderView.setScanText(text)
    }


    override fun handlerRes(result: RecogResult, bmImage: Bitmap?) {
        super.handlerRes(result, bmImage)
        if (lineNum != "") {
            setScanText("订单号: $lineNum")
            getOrderStatus(lineNum)
        }

    }

    protected abstract fun startCourierDetail(orderNum: String, type: Int)
    protected abstract fun startQpQueryChild(type: Int)
    /***
     * 代办点查询
     * @param orderNum
     */
    private fun getOrderStatus(orderNum: String) {
        val parms = HashMap<String, String>()
        parms["orderId"] = orderNum
        val token = SharedPreferencesUtil.getInstance().getSP("token")
        Utils.getInstance(this).post("manager/order/find", JSON.toJSONString(parms), token, object : HTTPCallBack {
            override fun Succ(res: String) {
                val orderScanerDataBean = JSON.parseObject(res, ScanerSearchBean::class.java)
                initScan()
                if (orderScanerDataBean != null) {
                    if (orderScanerDataBean.code == 200) {
                        if (orderScanerDataBean.data.size == 1) {
                            startCourierDetail(orderNum, 1)
                        } else {
                            Utils.toast(this@OutExScanerActivity, "未查询到数据!")
                        }
                    } else {
                        Utils.toast(this@OutExScanerActivity, orderScanerDataBean.message)
                    }
                } else {
                    Utils.toast(this@OutExScanerActivity, "暂无快递信息")
                }
            }

            override fun Fail(errorStr: String) {
                initScan()
                Utils.toast(this@OutExScanerActivity, "暂无快递信息")
            }
        })
    }


    override fun outTimeWarning() {

    }


}

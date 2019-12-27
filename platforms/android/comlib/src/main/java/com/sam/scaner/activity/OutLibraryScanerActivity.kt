package com.sam.scaner.activity

import android.graphics.Bitmap
import android.os.Bundle
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

import java.util.HashMap

abstract class OutLibraryScanerActivity : ScanerActivity() {

    lateinit var mFlashImage: ImageView
    lateinit var bottomView: LinearLayout
    lateinit var tv_toobar_title: TextView
    var isOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topView = layoutInflater.inflate(R.layout.view_activity_top_bar, null, false) as RelativeLayout
        topView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val topHeight = topView.measuredHeight
        topView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topHeight)
        mFlashImage = topView.findViewById(R.id.iv_flash)
        setTopMargin(48)
        addView2Root(topView)
        tv_toobar_title = topView.findViewById(R.id.tv_toobar_title)
        tv_toobar_title.text = "出库"

        bottomView = layoutInflater.inflate(R.layout.view_activity_out_package, null, false) as LinearLayout
        bottomView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bottomHeight = bottomView.measuredHeight
        bottomView.layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bottomHeight)
        addView2BottomLayout(bottomView)

        bottomView.setOnClickListener { startInquireQueryChild(2) }
        mFlashImage.setOnClickListener {
            openFlash(isOpen)
            isOpen = !isOpen
            mFlashImage.setImageResource(if (isOpen) R.drawable.flash_off_y else R.drawable.flash_on_y)
        }
        topView.findViewById<View>(R.id.tv_back).setOnClickListener {finish() }

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
            getOrderInfo(lineNum)
        }

    }

    protected abstract fun startInquireQueryChild(type: Int)
    protected abstract fun startInquireDetail(orderNum: String, type: Int)

    private fun getOrderInfo(orderNum: String) {
        val parms = HashMap<String, String>()
        parms.put("orderId", orderNum)
        val token = SharedPreferencesUtil.getInstance().getSP("token")
        Utils.getInstance(this).post("manager/order/find", JSON.toJSONString(parms), token, object : HTTPCallBack {
            override fun Succ(res: String) {
                initScan()
                val orderScanerDataBean = JSON.parseObject<ScanerSearchBean>(res, ScanerSearchBean::class.java)
                if (orderScanerDataBean != null) {
                    if (orderScanerDataBean.code == 200) {
                        if (orderScanerDataBean.data.size == 1) {
                            startInquireDetail(orderNum, 2)
                        } else {
                            Utils.toast(this@OutLibraryScanerActivity, "未查询到数据!")
                        }

                    } else {
                        Utils.toast(this@OutLibraryScanerActivity, orderScanerDataBean.message)
                    }
                } else {
                    Utils.toast(this@OutLibraryScanerActivity, "暂无快递信息")
                }
            }

            override fun Fail(errorStr: String) {
                initScan()
                Utils.toast(this@OutLibraryScanerActivity, "暂无快递信息")
            }
        })
    }


    override fun outTimeWarning() {

    }


}

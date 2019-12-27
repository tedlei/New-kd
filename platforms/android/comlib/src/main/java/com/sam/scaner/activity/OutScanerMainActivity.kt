package com.sam.scaner.activity

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.mobilerecognition.engine.RecogResult
import com.sam.scaner.R
import com.sam.scaner.adapter.AdapterItemClick
import com.sam.scaner.adapter.QuitPackageRecordAdapter
import com.sam.scaner.bean.BaseBean
import com.sam.scaner.bean.DataBean
import com.sam.scaner.bean.OrderScanerDataBean
import com.sam.scaner.bean.ScanerSearchBean
import com.sam.scaner.utils.HTTPCallBack
import com.sam.scaner.utils.JSON
import com.sam.scaner.utils.SharedPreferencesUtil
import com.sam.scaner.utils.Utils
import com.scaner.phonenumber.ui.ScanerActivity

import org.json.JSONException
import org.json.JSONObject

import java.util.ArrayList
import java.util.HashMap

abstract class OutScanerMainActivity : ScanerActivity(), AdapterItemClick {

    lateinit var mFlashImage: ImageView
    lateinit var bottomView: LinearLayout
    lateinit var needOutPackageView: RelativeLayout
    lateinit var tv_toobar_title: TextView
    lateinit var tv_out_package: TextView
    lateinit var tv_need_out_package: TextView
    lateinit var rl_flash: RelativeLayout

     var isOpen = false
     var indexNum = 0

    lateinit var mQuitPackageRecordList: RecyclerView
    lateinit var adapter: QuitPackageRecordAdapter

    var proList: MutableList<DataBean> = ArrayList()


    private val screenHeight: Int
        get() {
            val dm = applicationContext.resources.displayMetrics
            return dm.heightPixels
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val topView = layoutInflater.inflate(R.layout.view_activity_top_bar_menu, null, false) as RelativeLayout
        topView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val topHeight = topView.measuredHeight
        topView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, topHeight)
        mFlashImage = topView.findViewById(R.id.iv_flash)
        setTopMargin(92)
        addView2Root(topView)
        tv_toobar_title = topView.findViewById(R.id.tv_toobar_title)
        rl_flash = topView.findViewById(R.id.rl_flash)
        tv_out_package = topView.findViewById(R.id.tv_out_package)
        tv_need_out_package = findViewById(R.id.tv_need_out_package)
        tv_toobar_title.text = "退件处理"
        tv_out_package.setOnClickListener {chageMenu(0) }

        tv_need_out_package.setOnClickListener {chageMenu(1) }

        bottomView = layoutInflater.inflate(R.layout.view_activity_out_package, null, false) as LinearLayout
        bottomView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val bottomHeight = bottomView.measuredHeight
        bottomView.layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, bottomHeight)
        addView2BottomLayout(bottomView)

        bottomView.setOnClickListener {startQpQueryChild(2) }
        mFlashImage.setOnClickListener {
            openFlash(isOpen)
            isOpen = !isOpen
            mFlashImage.setImageResource(if (isOpen) R.drawable.flash_off_y else R.drawable.flash_on_y)
        }
        findViewById<View>(R.id.tv_back).setOnClickListener {finish() }
        needOutPackageView = layoutInflater.inflate(R.layout.view_need_quit_package, null, false) as RelativeLayout
        val needOutPackageViewHeight = screenHeight - Utils.dip2px(applicationContext, 92f)
        needOutPackageView.layoutParams = LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, needOutPackageViewHeight)
        addView2ScanerHandle(needOutPackageView)

        mQuitPackageRecordList = needOutPackageView.findViewById(R.id.recycler_list)
        mQuitPackageRecordList.layoutManager = LinearLayoutManager(applicationContext)
        adapter = QuitPackageRecordAdapter(applicationContext, proList, this)
        mQuitPackageRecordList.adapter = adapter

    }

    private fun getProList() {
        val token = SharedPreferencesUtil.getInstance().getSP("token")
        Utils.getInstance(this).post("manager/order/productlist", "", token, object : HTTPCallBack {
            override fun Succ(res: String) {
                proList.clear()
                val outListBean = JSON.parseObject(res, BaseBean::class.java)
                if (outListBean.data != null) {
                    proList.addAll(outListBean.data)
                }
                adapter.notifyDataSetChanged()
            }

            override fun Fail(errorStr: String) {

            }
        })
    }

    private fun chageMenu(indexCheck: Int) {
        if (indexNum == indexCheck) return
        indexNum = indexCheck
        tv_toobar_title.text = if (indexNum == 0) "退件处理" else "需要退件快递"
        rl_flash.visibility = if (indexCheck == 0) View.VISIBLE else View.GONE
        tv_out_package.setTextColor(resources.getColor(if (indexCheck == 0) R.color.colorPrimary else R.color.text_color))
        tv_out_package.setBackgroundColor(resources.getColor(if (indexCheck == 0) R.color.white else R.color.menuCheckColor))
        tv_need_out_package.setTextColor(resources.getColor(if (indexCheck != 0) R.color.colorPrimary else R.color.text_color))
        tv_need_out_package.setBackgroundColor(resources.getColor(if (indexCheck != 0) R.color.white else R.color.menuCheckColor))
        needOutPackageView.visibility = if (indexCheck == 0) View.GONE else View.VISIBLE
        showScanerHandleLayout(indexCheck == 0)
        if (indexCheck == 0) {
            setIsAvailable(true)
        } else {
            setIsAvailable(false)
            getProList()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        openFlash(false)
    }


    override fun handlerRes(result: RecogResult, bmImage: Bitmap?) {
        super.handlerRes(result, bmImage)

        if (lineNum != "") {
            mQrCodeFinderView.setScanText("订单号: $lineNum")
            getOrderInfo(lineNum)
        }

    }

    protected abstract fun startCourierDetail(orderNum: String, type: Int)
    protected abstract fun startQpQueryChild(type: Int)

    override fun outTimeWarning() {

    }


    override fun onItemClick(view: View, pos: Int) {
        val dataBean = adapter.getItem(pos)
        if (dataBean != null) {
            outScaner(dataBean.orderId)
        }
    }


    /**
     * 退件
     *
     * @param orderNum
     */
    private fun outScaner(orderNum: String) {
        val parms = HashMap<String, String>()
        parms["orderId"] = orderNum
        val token = SharedPreferencesUtil.getInstance().getSP("token")
        Utils.getInstance(this).post("manager/order/determine", JSON.toJSONString(parms), token, object : HTTPCallBack {
            override fun Succ(res: String) {
                try {
                    val jsonObject = JSONObject(res)
                    val code = jsonObject.getString("code")
                    if (code == "200") {
                        getProList()
                        Utils.getInstance(this@OutScanerMainActivity).play(Utils.AudioEnum.handBackSucceed.mAudioName)
                    } else {
                        Utils.getInstance(this@OutScanerMainActivity).play(Utils.AudioEnum.handBackFailed.mAudioName)
                    }
                } catch (e: JSONException) {
                    Utils.getInstance(this@OutScanerMainActivity).play(Utils.AudioEnum.handBackFailed.mAudioName)
                }

            }

            override fun Fail(errorStr: String) {
                Utils.getInstance(this@OutScanerMainActivity).play(Utils.AudioEnum.handBackFailed.mAudioName)
            }
        })
    }


    private fun getOrderInfo(orderNum: String) {
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
                            startCourierDetail(orderNum, 2)
                        } else {
                            Utils.toast(this@OutScanerMainActivity, "未查询到数据!")
                        }

                    } else {
                        Utils.toast(this@OutScanerMainActivity, orderScanerDataBean.message)
                    }
                } else {
                    Utils.toast(this@OutScanerMainActivity, "暂无快递信息")
                }
            }

            override fun Fail(errorStr: String) {
                initScan()
                Utils.toast(this@OutScanerMainActivity, "暂无快递信息")
            }
        })
    }
}

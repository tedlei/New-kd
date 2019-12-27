package com.sam.scaner.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sam.scaner.R
import com.sam.scaner.adapter.AdapterItemClick
import com.sam.scaner.adapter.InPackageRecordAdapter
import com.sam.scaner.bean.BaseBean
import com.sam.scaner.bean.DataBean
import com.sam.scaner.utils.HTTPCallBack
import com.sam.scaner.utils.JSON
import com.sam.scaner.utils.SharedPreferencesUtil
import com.sam.scaner.utils.Utils
import kotlinx.android.synthetic.main.activity_in_package_record.*
import kotlinx.android.synthetic.main.view_activity_top_bar.view.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class InPackageRecordActivity : AppCompatActivity(), AdapterItemClick {

    lateinit var adapter: InPackageRecordAdapter
    var proList: MutableList<DataBean> = ArrayList()
    lateinit var orderIntegration: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_package_record)
        orderIntegration = intent.getStringExtra("orderIntegration")
        initView()
        getEndscanProList(1)
        getProItemNum()
    }

    private fun initView() {
        view_top_bar.tv_back.setOnClickListener { finish() }
        btn_is_in_package.setOnClickListener { finish() }
        view_top_bar.tv_toobar_title.text = "入库快件记录"
        recycler_list.layoutManager = LinearLayoutManager(this)
        adapter = InPackageRecordAdapter(applicationContext, proList, this)
        recycler_list.adapter = adapter

    }


    private fun getEndscanProList(orderstatus: Int) {
        val params = HashMap<String, Any>()
        params["orderstatus"] = orderstatus
        params["orderIntegration"] = orderIntegration
        val jsonData = JSON.toJSONString(params)
        val token = SharedPreferencesUtil.getInstance().getSP("token")
        Utils.getInstance(this).post("/manager/order/endscan", jsonData, token, object : HTTPCallBack {
            override fun Succ(res: String) {
                val outListBean = JSON.parseObject(res, BaseBean::class.java)
                proList.clear()
                if (outListBean.data != null) {
                    proList.addAll(outListBean.data)
                }
                adapter.notifyDataSetChanged()
            }

            override fun Fail(errorStr: String) {

            }
        })
    }

    private fun getProItemNum() {
        val token = SharedPreferencesUtil.getInstance().getSP("token")

        val params = HashMap<String, Any>()
        params["orderIntegration"] = orderIntegration
        val jsonData = JSON.toJSONString(params)
        Utils.getInstance(this).post("manager/order/getnum", jsonData, token, object : HTTPCallBack {
            override fun Succ(res: String) {
                try {
                    val jsonObject = JSONObject(res)
                    val amount = jsonObject.getString("amount")
                    val allout = jsonObject.getString("allout")
                    val allrepertory = jsonObject.getString("allrepertory")
                    tv_in_package_suc.text = "入库成功($amount)"
                    tv_in_package_fail.text = "错放件($allout)"
                    tv_in_package_lack.text = "缺少件($allrepertory)"
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }

            override fun Fail(errorStr: String) {

            }
        })
    }

    override fun onItemClick(view: View, pos: Int) {
        val dataBean = adapter.getItem(pos)

    }
}

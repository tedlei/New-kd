package com.sam.scaner.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.sam.scaner.R
import com.sam.scaner.bean.DataBean

class QuitPackageRecordAdapter(internal var context: Context, internal var datas: List<DataBean>, internal var mItemClick: AdapterItemClick) : RecyclerView.Adapter<QuitPackageRecordAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_need_quit_package, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_out_is_ok.setOnClickListener { view -> mItemClick.onItemClick(view, position) }
        var item:DataBean = datas[position]
        holder.tv_package_status.text = "状态:" + if (item.orderStatus == "1") "入库成功" else if (item.orderStatus == "5") "错放件" else "缺少件"
        val color = context.resources.getColor(if (item.orderStatus == "1") R.color.text_color else R.color.red)
        holder.tv_package_status.setTextColor(color)
        holder.tv_ex_name.text = "快递公司:" + item.getCompany()
        holder.tv_user_name.text = "姓名:" + item.getUserName()
        holder.tv_order_num.text = "单号:" + item.getOrderId()
        holder.tv_phone_num.text = "手机号:" + item.getUserId()

        holder.tv_courier_name.text = "快递员:${item.userName}"
        holder.tv_courier_phone_num.text = "电话:${item.userId}"
        val shelves = item.getShelves() + ""
        if (shelves == "null" || shelves == "null") {
            holder.tv_goods_num.visibility = View.GONE
        } else {
            holder.tv_goods_num.text = "货号:" + item.getShelves()
        }

    }

    override fun getItemCount(): Int {
        return datas.size
    }

    fun getItem(pos: Int): DataBean {
        return datas[pos]
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_ex_name: TextView
        var tv_user_name: TextView
        var tv_package_status: TextView
        var tv_order_num: TextView
        var tv_phone_num: TextView
        var tv_goods_num: TextView
        var tv_courier_name: TextView
        var tv_courier_phone_num: TextView
        var tv_out_is_ok: TextView

        init {
            tv_ex_name = itemView.findViewById(R.id.tv_ex_name)
            tv_user_name = itemView.findViewById(R.id.tv_user_name)
            tv_package_status = itemView.findViewById(R.id.tv_package_status)
            tv_order_num = itemView.findViewById(R.id.tv_order_num)
            tv_phone_num = itemView.findViewById(R.id.tv_phone_num)
            tv_goods_num = itemView.findViewById(R.id.tv_goods_num)
            tv_courier_name = itemView.findViewById(R.id.tv_courier_name)
            tv_courier_phone_num = itemView.findViewById(R.id.tv_courier_phone_num)
            tv_out_is_ok = itemView.findViewById(R.id.tv_out_is_ok)
        }
    }
}
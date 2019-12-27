package com.sam.scaner.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.scaner.R;
import com.sam.scaner.bean.DataBean;

import java.util.List;

public class InPackageRecordAdapter extends RecyclerView.Adapter<InPackageRecordAdapter.ViewHolder> {
    Context context;
    List<DataBean> datas;
    AdapterItemClick adapterItemClick;

    public InPackageRecordAdapter(Context context, List<DataBean> datas,AdapterItemClick itemClick) {
        this.context = context;
        this.datas = datas;
        this.adapterItemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_package_record, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataBean dataItem = datas.get(position);
        String statusStats = dataItem.getOrderStatus();
        holder.tv_package_status.setText("状态:"+(statusStats.equals("1")?"入库成功":(statusStats.equals("5")?"错放件":"缺少件")));
        int color = context.getResources().getColor(statusStats.equals("1")?R.color.text_color:R.color.red);
        holder.tv_package_status.setTextColor(color);
        holder.tv_ex_name.setText("快递公司:"+dataItem.getCompany());
        holder.tv_user_name.setText("姓名:"+dataItem.getUserName());
        holder.tv_order_num.setText("单号:"+dataItem.getOrderId());
        holder.tv_phone_num.setText("手机号:"+dataItem.getUserId());
        String shelves = dataItem.getShelves()+"";
        if(shelves.equals("null") || shelves.equals("null")){
            holder.tv_goods_num.setVisibility(View.GONE);
        }else {
            holder.tv_goods_num.setText("货号:"+dataItem.getShelves());
        }
        holder.ll_root.setOnClickListener((view -> adapterItemClick.onItemClick(holder.ll_root,position)));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_ex_name;
        TextView tv_user_name;
        TextView tv_package_status;
        TextView tv_order_num;
        TextView tv_phone_num;
        TextView tv_goods_num;
        LinearLayout ll_root;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_ex_name = itemView.findViewById(R.id.tv_ex_name);
            tv_user_name = itemView.findViewById(R.id.tv_user_name);
            tv_package_status = itemView.findViewById(R.id.tv_package_status);
            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_phone_num = itemView.findViewById(R.id.tv_phone_num);
            tv_goods_num = itemView.findViewById(R.id.tv_goods_num);
            ll_root = itemView.findViewById(R.id.ll_root);
        }
    }
    public DataBean getItem(int pos){
        return datas.get(pos);
    }
}
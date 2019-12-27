package com.sam.scaner.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.scaner.R;
import com.sam.scaner.bean.DataBean;

import java.util.List;

public class SearchScanerAdapter extends RecyclerView.Adapter<SearchScanerAdapter.ViewHolder> {
    Context context;
    List<DataBean> datas;
    AdapterItemClick mItemClick;
    public SearchScanerAdapter(Context context, List<DataBean> datas,AdapterItemClick itemClick) {
        this.context = context;
        this.datas = datas;
        this.mItemClick = itemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_change_data.setOnClickListener(view -> {
            this.mItemClick.onItemClick(holder.tv_change_data,position);
        });
        holder.tv_scaner_out.setOnClickListener(view -> {
            this.mItemClick.onItemClick(holder.tv_scaner_out,position);
        });
        holder.tv_scan_get_package.setOnClickListener(view -> {
            this.mItemClick.onItemClick(holder.tv_scan_get_package,position);
        });
        holder.rl_content_layout.setOnClickListener(view -> {
            this.mItemClick.onItemClick(holder.rl_content_layout,position);
        });

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_company_name;
        TextView tv_order_num;
        TextView tv_goods_num;
        TextView tv_in_status;
        TextView tv_user_phone_num;
        TextView tv_change_data;
        TextView tv_scaner_out;
        TextView tv_scan_get_package;
        RelativeLayout rl_content_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_company_name = itemView.findViewById(R.id.tv_company_name);
            tv_order_num = itemView.findViewById(R.id.tv_order_num);
            tv_goods_num = itemView.findViewById(R.id.tv_goods_num);
            rl_content_layout = itemView.findViewById(R.id.rl_content_layout);
            tv_in_status = itemView.findViewById(R.id.tv_in_status);
            tv_user_phone_num = itemView.findViewById(R.id.tv_user_phone_num);
            tv_change_data = itemView.findViewById(R.id.tv_change_data);
            tv_scaner_out = itemView.findViewById(R.id.tv_scaner_out);
            tv_scan_get_package = itemView.findViewById(R.id.tv_scan_get_package);
        }
    }


}
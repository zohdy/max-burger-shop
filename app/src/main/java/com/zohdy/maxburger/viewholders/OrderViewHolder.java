package com.zohdy.maxburger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.ItemClickListener;

/**
 * Created by peterzohdy on 14/11/2017.
 */

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewOrderId;
    public TextView textViewOrderUsername;
    public TextView textViewOrderStatus;
    private ItemClickListener itemClickListener;

    public OrderViewHolder(View itemView) {
        super(itemView);

        textViewOrderId = itemView.findViewById(R.id.tv_order_id);
        textViewOrderStatus = itemView.findViewById(R.id.tv_order_status);
        textViewOrderUsername = itemView.findViewById(R.id.tv_order_username);


        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}

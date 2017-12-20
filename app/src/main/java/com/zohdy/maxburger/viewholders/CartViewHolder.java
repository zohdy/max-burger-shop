package com.zohdy.maxburger.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.ItemClickListener;
import com.zohdy.maxburger.models.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by peterzohdy on 11/11/2017.
 */

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewCartName;
    public TextView textViewPrice;
    public TextView textViewCartCount;

    private ImageView imageViewDeleteIcon;

    private ItemClickListener itemClickListener;

    public CartViewHolder(View itemView) {
        super(itemView);

        imageViewDeleteIcon = itemView.findViewById(R.id.iv_delete_icon);
        textViewCartName = itemView.findViewById(R.id.tv_cart_item_name);
        textViewPrice = itemView.findViewById(R.id.tv_cart_item_price);
        textViewCartCount = itemView.findViewById(R.id.tv_shopping_cart_badge_item_cart);

        imageViewDeleteIcon.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}




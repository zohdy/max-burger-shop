package com.zohdy.maxburger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.RecyclerViewItemClickListener;
import com.zohdy.maxburger.models.Order;

import java.util.List;

/**
 * Created by peterzohdy on 12/11/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<Order> cartListData;
    private Context context;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public CartAdapter(List<Order> cartListData, Context context) {
        this.cartListData = cartListData;
        this.context = context;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.item_cart, parent, false);

        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartViewHolder cartViewHolder, final int position) {

        // Setup the views
        int price = (Integer.parseInt(cartListData.get(position).getPrice())) *
                    (Integer.parseInt(cartListData.get(position).getQuantity()));
        cartViewHolder.textViewCartName.setText(cartListData.get(position).getFoodName());
        cartViewHolder.textViewCartCount.setText(cartListData.get(position).getQuantity());
        cartViewHolder.textViewPrice.setText(String.valueOf(price) + " kr");
    }

    @Override
    public int getItemCount() {
        return cartListData.size();
    }


    public void setOnRecyclerViewClicklickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }


    // Inner Class
    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewCartName;
        private TextView textViewPrice;
        private TextView textViewCartCount;
        private ImageView imageViewDeleteIcon;

        private CartViewHolder(View itemView) {
            super(itemView);

            imageViewDeleteIcon = itemView.findViewById(R.id.iv_delete_icon);
            textViewCartName = itemView.findViewById(R.id.tv_cart_item_name);
            textViewPrice = itemView.findViewById(R.id.tv_cart_item_price);
            textViewCartCount = itemView.findViewById(R.id.tv_shopping_cart_badge_item_cart);
            //bind the delete icon as to a clickListener
            imageViewDeleteIcon.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(recyclerViewItemClickListener != null) {
                recyclerViewItemClickListener.onRecyclerItemClick(view, getAdapterPosition());
            }
        }
    }



}
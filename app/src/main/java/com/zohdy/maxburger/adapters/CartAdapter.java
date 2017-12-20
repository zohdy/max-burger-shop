package com.zohdy.maxburger.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.database.SQLiteHelper;
import com.zohdy.maxburger.interfaces.ItemClickListener;
import com.zohdy.maxburger.interfaces.OnDataChangeListener;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.viewholders.CartViewHolder;

import java.util.List;

/**
 * Created by peterzohdy on 12/11/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder> {

    private List<Order> cartListData;

    private Context context;

    private OnDataChangeListener onDataChangeListener;

    public CartAdapter(List<Order> cartListData, Context context) {
        this.cartListData = cartListData;
        this.context = context;
    }

    public void setOnDataChangeListener(OnDataChangeListener onDataChangeListener) {
        this.onDataChangeListener = onDataChangeListener;
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
        int price = (Integer.parseInt(cartListData.get(position).getPrice())) * (Integer.parseInt(cartListData.get(position).getQuantity()));
        cartViewHolder.textViewCartName.setText(cartListData.get(position).getFoodName());
        cartViewHolder.textViewCartCount.setText(cartListData.get(position).getQuantity());
        cartViewHolder.textViewPrice.setText(String.valueOf(price) + " kr");

        //TODO -  move to Acticity
        // Click listener for the trash icon in shopping cart
        cartViewHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                SQLiteHelper dbHelper = SQLiteHelper.getInstance(context);
                dbHelper.deleteOrderItem(cartListData, position);

                // Update the counter on the cartImage correctly
                int numOfItemsToRemove = Integer.parseInt(cartListData.get(position).getQuantity());
                Common.badgeCounter -= numOfItemsToRemove;

                // Interface method is implemented in CartActivity
                onDataChangeListener.onDataChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartListData.size();
    }



}
package com.zohdy.maxburger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.RecyclerViewItemClickListener;

/**
 * Created by peterzohdy on 07/11/2017.
 */

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewFood;
    public ImageView imageViewFood;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

        textViewFood = itemView.findViewById(R.id.tv_food_name);
        imageViewFood = itemView.findViewById(R.id.iv_food_image);
        itemView.setOnClickListener(this);
    }

    public void setRecyclerViewItemClickListener(RecyclerViewItemClickListener recyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener;
    }

    @Override
    public void onClick(View view) {
        recyclerViewItemClickListener.onRecyclerItemClick(view, getAdapterPosition());
    }
}

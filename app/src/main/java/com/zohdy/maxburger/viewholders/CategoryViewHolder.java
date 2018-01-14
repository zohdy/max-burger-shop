package com.zohdy.maxburger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.RecyclerViewItemClickListener;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewCategoryName;
    public ImageView imageViewCategory;
    private RecyclerViewItemClickListener recyclerViewItemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        textViewCategoryName = itemView.findViewById(R.id.tv_category_name);
        imageViewCategory = itemView.findViewById(R.id.iv_category_image);
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


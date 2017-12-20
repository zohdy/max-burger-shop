package com.zohdy.maxburger.viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.interfaces.ItemClickListener;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView textViewCategoryName;

    public ImageView imageViewCategory;

    private ItemClickListener itemClickListener;

    public CategoryViewHolder(View itemView) {
        super(itemView);

        textViewCategoryName = itemView.findViewById(R.id.tv_category_name);
        imageViewCategory = itemView.findViewById(R.id.iv_category_image);
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


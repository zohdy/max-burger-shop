package com.zohdy.maxburger.interfaces;

import android.view.View;

/**
 * Created by peterzohdy on 06/11/2017.
 */

// This interface is used to attach a callback to an item click in a Recyclerview
public interface RecyclerViewItemClickListener {

    void onRecyclerItemClick(View view, int position);
}

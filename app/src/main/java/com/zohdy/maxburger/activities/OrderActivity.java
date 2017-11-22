package com.zohdy.maxburger.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.models.Request;
import com.zohdy.maxburger.viewholders.OrderViewHolder;

public class OrderActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> recyclerAdapter;

    FirebaseDatabase database;
    DatabaseReference requests_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();
        requests_table = database.getReference("Requests");

        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders(Common.currenUser.getName());
    }

    private void loadOrders(String name) {
        recyclerAdapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.item_order,
                OrderViewHolder.class,
                requests_table.orderByChild("name").equalTo(name)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request request, int position) {
                viewHolder.textViewOrderId.setText(recyclerAdapter.getRef(position).getKey());
                viewHolder.textViewOrderUsername.setText(request.getName());
                viewHolder.textViewOrderStatus.setText(convertStatus(request.getStatus()));
            }
        };
        recyclerView.setAdapter(recyclerAdapter);
    }

    private String convertStatus(String status) {
        if (status.equals("0")) {
            return "Igang";
        }
        if (status.equals("1")) {
            return "Færdig";
        }
        return null;
    }
}

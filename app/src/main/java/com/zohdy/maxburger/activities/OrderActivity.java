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
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.OrderRequest;
import com.zohdy.maxburger.viewholders.OrderViewHolder;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder> recyclerAdapter;
    private FirebaseDatabase database;
    private DatabaseReference requests_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        database = FirebaseDatabase.getInstance();
        requests_table = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);

        initRecyclerView();

        loadOrders(Common.currentUser.getPhoneNumber());
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Setting recyclerview items from orderRequest table based on query (phoneNumber)
    private void loadOrders(String query) {
        recyclerAdapter = new FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder>(
                OrderRequest.class,
                R.layout.item_order,
                OrderViewHolder.class,
                requests_table.orderByChild("phoneNumber").equalTo(query)) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderRequest orderRequest, int position) {
                viewHolder.textViewOrderId.setText(recyclerAdapter.getRef(position).getKey());
                viewHolder.textViewOrderUsername.setText(orderRequest.getPhoneNumber());
                viewHolder.textViewOrderStatus.setText(convertStatus(orderRequest.getStatus()));
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

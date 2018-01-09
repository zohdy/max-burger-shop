package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.OrderRequest;
import com.zohdy.maxburger.viewholders.OrderViewHolder;

public class OrderActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder> recyclerAdapter;
    private DatabaseReference requestsTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        requestsTable = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);

        setupRecyclerView();
        loadOrders(Common.currentUser.getPhoneNumber());
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.rv_orders);
        recyclerView.setHasFixedSize(true);
        LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
    }

    // Filter requestTable by current users PhoneNumber
    private void loadOrders(String query) {
        recyclerAdapter = new FirebaseRecyclerAdapter<OrderRequest, OrderViewHolder>(
                OrderRequest.class,
                R.layout.item_order,
                OrderViewHolder.class,
                requestsTable.orderByChild(Constants.FIREBASE_DB_DATA_PHONENUMBER).equalTo(query))
        {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, OrderRequest orderRequest, int position) {
                    viewHolder.textViewOrderId.setText(recyclerAdapter.getRef(position).getKey());
                    viewHolder.textViewOrderUsername.setText(orderRequest.getPhoneNumber());
                    viewHolder.textViewOrderStatus.setText(convertStatus(orderRequest.getStatus()));
            }
        };

        // If there are 0 orders waiting, display a SnackBar
        requestsTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount() == 0) {

                    setSnackBar();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void setSnackBar() {

        Snackbar snackbar = Snackbar.make(findViewById(R.id.order_layout), "Ingen aktuelle ordrer", Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToHomeIntent = new Intent(OrderActivity.this, HomeActivity.class);
                startActivity(backToHomeIntent);
            }
        });

        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.primary_light));
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) snackBarView.getLayoutParams();
        layoutParams.gravity = Gravity.TOP;
        layoutParams.setMargins(0, 100, 0, 0);
        snackBarView.setLayoutParams(layoutParams);

        TextView snackBarActionTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_action);
        TextView snackBarTextView = snackBarView.findViewById(android.support.design.R.id.snackbar_text);
        snackBarTextView.setTextSize(24);
        snackBarActionTextView.setMinLines(3);
        snackBarActionTextView.setTextSize(20);
        snackBarTextView.setTextColor(getResources().getColor(R.color.secondary_text));
        snackBarActionTextView.setTextColor(getResources().getColor(R.color.secondary_text));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            snackBarTextView.setTypeface(getResources().getFont(R.font.flamaregular));
            snackBarActionTextView.setTypeface(getResources().getFont(R.font.flamabold));
        }
        snackbar.show();
    }

    // TODO Make implementation of OrderHistoryTable
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

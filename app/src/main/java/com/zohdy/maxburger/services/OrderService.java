package com.zohdy.maxburger.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.activities.OrderReadyActivity;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.OrderRequest;

import java.util.ArrayList;

public class OrderService extends Service {

    String orderId;

    private FirebaseDatabase database;
    private DatabaseReference orderRequestTable;
    private DatabaseReference orderHistoryTable;

    private ArrayList<String> orderIdList;
    private OrderRequest selectedOrderRequest;

    @Override
    public void onCreate() {
        super.onCreate();

        orderIdList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        orderRequestTable = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent.getExtras() != null && !intent.getExtras().isEmpty()) {
            orderId = intent.getExtras().getString(Constants.ORDER_ID);
            orderIdList.add(orderId);
        }

        handleOrder();

        return START_STICKY;
    }

    private void handleOrder() {

        orderRequestTable.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                selectedOrderRequest = dataSnapshot.getValue(OrderRequest.class);

                if (selectedOrderRequest != null && selectedOrderRequest.getStatus().equals("1")) {
                    String orderId = dataSnapshot.getKey();
                    startOrderReadyActivity(orderId);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }


    private void startOrderReadyActivity(String orderReadyId) {
        Intent orderReadyIntent = new Intent(this, OrderReadyActivity.class);
        orderReadyIntent.putExtra(Constants.ORDER_READY_ID, orderReadyId);
        orderReadyIntent.putExtra(Constants.ORDER_REQUEST_OBJECT, selectedOrderRequest);
        startActivity(orderReadyIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Common.createToast(this, "Service Done");
    }
}

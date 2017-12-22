package com.zohdy.maxburger.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.models.OrderRequest;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderReadyActivity extends AppCompatActivity {

    private TextView textViewOrderReady;

    private Button buttonOrderReady;

    private ImageView imageViewBurgerGif;

    private String orderID;

    private MediaPlayer mediaPlayer;

    private FirebaseDatabase database;

    private DatabaseReference orderHistoryTable;
    private DatabaseReference orderRequestTable;

    OrderRequest finishedOrderRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_ready);

        if(getIntent() != null) {
            orderID = getIntent().getStringExtra(Constants.ORDER_READY_ID);
            finishedOrderRequest = (OrderRequest) getIntent().getParcelableExtra(Constants.ORDER_REQUEST_OBJECT);
        }

        database = FirebaseDatabase.getInstance();
        orderRequestTable = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_REQUESTS);
        orderHistoryTable = database.getReference(Constants.FIREBASE_DB_TABLE_ORDER_HISTORY);


        initLayout();
        playAlarmSound();
        handleOrderReadyButton();
        displayGifImage();
    }

    private void initLayout() {

        imageViewBurgerGif = findViewById(R.id.iv_burger_gif);
        buttonOrderReady = findViewById(R.id.btn_order_ready);
        textViewOrderReady = findViewById(R.id.tv_order_ready);
        textViewOrderReady.setText(" Kære " + Common.currentUser.getName() + "\n Din mad med ordre nr.\n " + orderID + " er klar!");
    }

    private void displayGifImage() {
        Glide.with(getApplicationContext())
                .asGif()
                .load(R.drawable.burger_gif)
                .into(imageViewBurgerGif);
    }

    private void playAlarmSound() {

        mediaPlayer = new MediaPlayer();
        Uri myUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getApplicationContext(), myUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.start();
    }


    private void handleOrderReadyButton() {
        buttonOrderReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopPlaying();
                Intent backToHomeActivityIntent = new Intent(OrderReadyActivity.this, HomeActivity.class);
                moveOrderToHistoryTable();
                startActivity(backToHomeActivityIntent);
            }
        });
    }


    private void stopPlaying() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onStop() {
        stopPlaying();
        super.onStop();
    }


    private void moveOrderToHistoryTable() {

        // Create a new map to store the relevant key / value pairs for the History tabel
        HashMap<String, Object> orderHistoryHashMap = new HashMap<>();

        // Convert the orderId to current time and add relevant items from the orderRequest
        orderHistoryHashMap.put("TimeOfOrder", convertOrderIdToDate());
        orderHistoryHashMap.put("Customer", finishedOrderRequest.getPhoneNumber());
        orderHistoryHashMap.put("TotalAmount", finishedOrderRequest.getTotalAmount());
        orderHistoryHashMap.put("SpecialInstructions", finishedOrderRequest.getSpecialInstructions());
        orderHistoryHashMap.put("FoodItems", finishedOrderRequest.getFoodItems());


        // Push() creates a unique ID for each node in table
        orderHistoryTable.push().setValue(orderHistoryHashMap);

        // Remove the finished order from Order Request Table
        orderRequestTable.child(orderID).removeValue();
    }

    private String convertOrderIdToDate() {

        Date date = new Date(Long.parseLong(orderID));
        return date.toString();
    }
}

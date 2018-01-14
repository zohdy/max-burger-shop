package com.zohdy.maxburger.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.database.DatabaseHelper;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.Order;
import com.zohdy.maxburger.services.OrderService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private Button buttonSignUp;
    SharedPreferences sharedPreferences;
    Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getApplicationContext().getSharedPreferences(Constants.MAX_BURGER_PREFS, 0);

        initLayout();

        // Check for network connection
        if (isOnline(this)) {

            // For offline sync
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            // If shopping cart is not empty
            retrieveBadgeCounterValue();

            setupClickListeners();
            startOrderService();

        } else {
            buttonSignIn.setVisibility(View.GONE);
            buttonSignUp.setVisibility(View.GONE);

            Snackbar.make(findViewById(R.id.main_activity_layout),
                    "Ingen netværksforbindelse",
                    Snackbar.LENGTH_INDEFINITE)
                    .show();
        }
    }

    private void initLayout() {
        buttonSignIn = findViewById(R.id.btn_sign_in);
        buttonSignUp = findViewById(R.id.btn_sign_up);
    }

    private void setupClickListeners() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(MainActivity.this, SignInActivity.class);
                startActivity(signInIntent);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

    private void startOrderService() {
        Intent orderServiceIntent = new Intent(MainActivity.this, OrderService.class);
        startService(orderServiceIntent);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }


    private void retrieveBadgeCounterValue() {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(MainActivity.this);
        List<Order> currentCart = databaseHelper.getItemsInCart();
        int badgeCounterValue = 0;

        for (Order order : currentCart) {
            badgeCounterValue += (Integer.parseInt(order.getQuantity()));
        }

        editor = sharedPreferences.edit();
        editor.putInt(Constants.BADGE_COUNTER, badgeCounterValue);
        editor.apply();
    }
}

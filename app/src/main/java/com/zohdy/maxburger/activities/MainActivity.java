package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.FirebaseDatabase;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.database.SQLiteHelper;
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.services.OrderService;

public class MainActivity extends AppCompatActivity {

    private Button buttonSignIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // For offline sync
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        initViews();
        initClickListeners();

        // Clear any items in shopping cart when app is restarted
        SQLiteHelper dbHelper = SQLiteHelper.getInstance(MainActivity.this);
        dbHelper.clearCart();

        startOrderService();
    }


    private void startOrderService() {
        Intent orderServiceIntent = new Intent(MainActivity.this, OrderService.class);
        startService(orderServiceIntent);
    }

    private void initClickListeners() {

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

    private void initViews() {

        buttonSignIn = findViewById(R.id.btn_sign_in);
        buttonSignUp = findViewById(R.id.btn_sign_up);
    }
}

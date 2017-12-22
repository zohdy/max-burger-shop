package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;

public class OrderConfirmationActivity extends AppCompatActivity {

    TextView confirmationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_order_confirmation);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        confirmationTextView = findViewById(R.id.tv_confirmation);
        confirmationTextView.setText("Tak " + Common.currentUser.getName() + "!\n\nVi har modtaget din ordre!");
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent homeActivityIntent = new Intent(OrderConfirmationActivity.this, HomeActivity.class);
        startActivity(homeActivityIntent);
        return true;
    }
}

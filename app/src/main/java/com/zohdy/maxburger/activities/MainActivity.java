package com.zohdy.maxburger.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zohdy.maxburger.R;
import com.zohdy.maxburger.database.DBOpenHelper;

public class MainActivity extends AppCompatActivity {


    private Button buttonSignIn;
    private Button buttonSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        DBOpenHelper dbOpenHelper = DBOpenHelper.newInstance(MainActivity.this);
        dbOpenHelper.clearCart();

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

package com.zohdy.maxburger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.zohdy.maxburger.R;
import com.zohdy.maxburger.common.Common;
import com.zohdy.maxburger.models.User;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class SignInActivity extends AppCompatActivity {

    EditText editTextPhoneNumber;
    EditText editTextPassWord;
    Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextPhoneNumber = findViewById(R.id.et_phone);
        editTextPassWord = findViewById(R.id.et_password);
        buttonSignIn = findViewById(R.id.btn_sign_in);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Reference to the users table
        final DatabaseReference table_user = database.getReference("User");

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // Check if user exist in database
                        if(dataSnapshot.child(editTextPhoneNumber.getText().toString()).exists()) {
                            //Get User information
                            progressDialog.dismiss();

                            User user = dataSnapshot.child(editTextPhoneNumber.getText().toString()).getValue(User.class);

                            if(user.getPassword().equals(editTextPassWord.getText().toString())) {
                                Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                Common.currenUser = user;
                                startActivity(homeIntent);
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User does not exist in database", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
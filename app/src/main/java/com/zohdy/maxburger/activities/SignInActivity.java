package com.zohdy.maxburger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.zohdy.maxburger.interfaces.Constants;
import com.zohdy.maxburger.models.User;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class SignInActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber;
    private EditText editTextPassWord;

    private Button buttonSignIn;

    private FirebaseDatabase database;
    private DatabaseReference table_user;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextPhoneNumber = findViewById(R.id.et_phone);
        editTextPassWord = findViewById(R.id.et_password);
        buttonSignIn = findViewById(R.id.btn_sign_in);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference(Constants.FIREBASE_DB_TABLE_USER);

        handleSignInButton();
    }

    private void handleSignInButton() {
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user exist in database
                        if(dataSnapshot.child(editTextPhoneNumber.getText().toString()).exists()) {

                            progressDialog.dismiss();

                            //Get user values from database and set values as object 'user'
                            User user = dataSnapshot.child(editTextPhoneNumber.getText().toString()).getValue(User.class);

                            // check if user objects password matches with user input
                            if(user.getPassword().equals(editTextPassWord.getText().toString())) {
                                Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                                Common.currentUser = user;
                                startActivity(homeIntent);
                                finish();

                            } else {
                                Common.createToast(SignInActivity.this, "Wrong password");
                            }
                        } else {
                            // If username (phonenumber) does not exist inside database
                            progressDialog.dismiss();
                            Common.createToast(SignInActivity.this, "User does not exist in database");
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
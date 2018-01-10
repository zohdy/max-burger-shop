package com.zohdy.maxburger.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextPhone;
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button buttonSignup;
    private DatabaseReference tableUser;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initViews();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        tableUser = database.getReference(Constants.FIREBASE_DB_TABLE_USER);

        handleSignupButton();
    }

    private void initViews() {
        editTextName = findViewById(R.id.et_name);
        editTextPhone = findViewById(R.id.et_phone);
        editTextPassword = findViewById(R.id.et_password);
        buttonSignup = findViewById(R.id.btn_sign_up);
        editTextEmail = findViewById(R.id.et_email);
    }

    private void handleSignupButton() {
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Vent venligst...");
                progressDialog.show();

                tableUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if key(phone) already exists in OrderDatabase
                        if(dataSnapshot.child(editTextPhone.getText().toString()).exists()) {
                            progressDialog.dismiss();
                            Common.createToast(SignUpActivity.this, "Denne bruger er allerede registreret!");
                        } else {
                            progressDialog.dismiss();
                            String phoneNumber = editTextPhone.getText().toString();
                            String name = editTextName.getText().toString();
                            String password = editTextPassword.getText().toString();
                            String email = editTextEmail.getText().toString();
                            if(phoneNumber.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty()) {
                                Common.createToast(SignUpActivity.this, "Alle felter skal udfyldes");
                            } else {
                                // Create new user based on text input values
                                User newUser = new User(name, password, phoneNumber, email);

                                // Add table to Firebase
                                tableUser.child(editTextPhone.getText().toString()).setValue(newUser);
                                Common.createToast(SignUpActivity.this, "Registrering successful!");
                            }
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
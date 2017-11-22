package com.zohdy.maxburger.activities;

import android.app.ProgressDialog;
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
import com.zohdy.maxburger.models.User;

/**
 * Created by peterzohdy on 06/11/2017.
 */

public class SignUpActivity extends AppCompatActivity {

    EditText editTextName;
    EditText editTextPhone;
    EditText editTextPassword;
    EditText editTextEmail;
    Button buttonSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextName = findViewById(R.id.et_name);
        editTextPhone = findViewById(R.id.et_phone);
        editTextPassword = findViewById(R.id.et_password);
        buttonSignup = findViewById(R.id.btn_sign_up);
        editTextEmail = findViewById(R.id.et_email);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        // Reference to the users table
        final DatabaseReference table_user = database.getReference("User");

        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this);
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if key(phone) already exists in OrderDatabase
                        if(dataSnapshot.child(editTextPhone.getText().toString()).exists()) {
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Phone number already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            String phoneNumber = editTextPhone.getText().toString();
                            String name = editTextName.getText().toString();
                            String password = editTextPassword.getText().toString();
                            String email = editTextEmail.getText().toString();
                            User newUser = new User(name, password, phoneNumber, email);
                            table_user.child(editTextPhone.getText().toString()).setValue(newUser);
                            Toast.makeText(SignUpActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                            finish();
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
package com.hmdapps.greenpurse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;


public class ResgistrationActivity extends AppCompatActivity {

    private EditText memail;
    private EditText mpassword;
    private EditText mConfirmpass;
    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistration);

        mAuth=FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        registration();
    }
    private void registration() {
        memail = findViewById(R.id.email_registration);
        mpassword = findViewById(R.id.Password_registration);
        Button btnregis = findViewById(R.id.button_registration);
        TextView mSingin = findViewById(R.id.signin_here);
        mConfirmpass = findViewById(R.id.ConfirPass_reg);

        btnregis.setOnClickListener(view -> {

            String email = memail.getText().toString().trim();
            String pass = mpassword.getText().toString().trim();
            String confPass = mConfirmpass.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                memail.setError("Email Required...");
                return;
            }
            if (TextUtils.isEmpty(pass)){
                mpassword.setError("Password Required...");
            }
            if (pass.equals(confPass)){
                mDialog.setMessage("Processing...");
                mDialog.show();

                mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Registration Complete",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    }else {
                        mDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Registration failed..",Toast.LENGTH_LONG).show();
                    }
                });
            }else {
                mConfirmpass.setError("No confirm password");
            }



        });

        mSingin.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),MainActivity.class)));

    }



}
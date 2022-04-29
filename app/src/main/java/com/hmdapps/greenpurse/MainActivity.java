package com.hmdapps.greenpurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;

    private FirebaseAuth mAuth;
    private ProgressDialog mDialog;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        login();

        //bach mayb9acch idkhl kolla merra
        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        }


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void login() {
        mEmail = findViewById(R.id.email_login);
        mPassword = findViewById(R.id.Password_login);
        Button btnlogin = findViewById(R.id.button_login);
        TextView mForgetPass = findViewById(R.id.forget_password);
        TextView mSingupHere = findViewById(R.id.signup_reg);

        btnlogin.setOnClickListener(view -> {

            String email = mEmail.getText().toString().trim();
            String pass = mPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)){
                mEmail.setError("Email Required...");
                return;
            }
            if (TextUtils.isEmpty(pass)){
                mPassword.setError("Password Required...");
            }
            mDialog.setMessage("Processing...");
            mDialog.show();

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login Complete",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                }else {
                    mDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Login failed..",Toast.LENGTH_LONG).show();
                }
            });
        });

        mSingupHere.setOnClickListener(view -> {

            startActivity(new Intent(getApplicationContext(),ResgistrationActivity.class));

        });

        mForgetPass.setOnClickListener(view -> {

            final EditText resetMail = new EditText(view.getContext());
            final AlertDialog.Builder passwordReserDialog = new AlertDialog.Builder(view.getContext());
            passwordReserDialog.setTitle("Reset Password ?");
            passwordReserDialog.setMessage("Enter your Recived reset Link.");
            passwordReserDialog.setView(resetMail);

            passwordReserDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                String mail = resetMail.getText().toString();
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),"Reset Link Sent To Your Email",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(),"Error ! Reset Link is Not Sent To Your Email",Toast.LENGTH_LONG).show());
            });
            passwordReserDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            passwordReserDialog.create().show();

        });

    }}
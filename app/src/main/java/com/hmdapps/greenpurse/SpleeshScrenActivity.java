package com.hmdapps.greenpurse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

public class SpleeshScrenActivity extends AppCompatActivity {
    ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spleesh_scren);

        imageView=findViewById(R.id.logo_splush);
        imageView.setImageResource(R.drawable.new_logo);

        Runnable runnable= () -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        };
        new Handler().postDelayed(runnable,2000);
    }
}
package com.hmdapps.greenpurse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmdapps.greenpurse.Model.Data;
import java.util.ArrayList;

public class AssetGraphActivity extends AppCompatActivity {

    LineChart lineChart;
    int incomeValue;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_graph);

        Button back= findViewById(R.id.btnBack);
        back.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),HomeActivity.class)));

        MobileAds.initialize(this, initializationStatus -> {
        });
        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        lineChart =findViewById(R.id.ligne_chart);

        ArrayList<Entry> incomeListe = new ArrayList<>();
        ArrayList<String> dateListe = new ArrayList<>();


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        assert mUser != null;
        String uid=mUser.getUid();
        DatabaseReference mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index=0;
                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    assert data != null;
                    incomeValue = data.getAmount();
                    date = data.getDate();
                    incomeListe.add(new Entry(index,incomeValue));
                    dateListe.add(date);
                    index++;
                }
                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(50f);
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateListe));
                LineDataSet lineDataSet =new LineDataSet(incomeListe,"Income");
                lineDataSet.getValueTextColor(Color.WHITE);
                lineDataSet.setValueTextSize(18f);
                LineData barData=new LineData(lineDataSet);
                lineChart.setData(barData);
                lineChart.getDescription().setEnabled(false);
                lineChart.animateY(2000);
                YAxis rightYAxis = lineChart.getAxisRight();
                rightYAxis.setEnabled(false);
                lineDataSet.setLineWidth(4f);
                lineDataSet.setCircleRadius(3f);
                lineChart.setTouchEnabled(true);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
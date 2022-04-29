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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmdapps.greenpurse.Model.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExpenseAssetGraph extends AppCompatActivity {
    Date sdf;
    LineChart lineChart;
    int incomeValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_asset_graph);

        lineChart =findViewById(R.id.expense_chart);
        ArrayList<Entry> expenseList = new ArrayList<>();
        ArrayList<String> dateListe = new ArrayList<>();
        Button back= findViewById(R.id.btnBack);
        back.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),HomeActivity.class)));
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        assert mUser != null;
        String uid=mUser.getUid();
        DatabaseReference mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int index=0;
                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    incomeValue = data.getAmount();
                    String date=data.getDate();
                    dateListe.add(date);
                    expenseList.add(new Entry(index,incomeValue));
                    index++;
                }

                XAxis xAxis = lineChart.getXAxis();
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setLabelRotationAngle(50f);
                lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dateListe));
                LineDataSet lineDataSet =new LineDataSet(expenseList,"Expenses");
                lineDataSet.getValueTextColor(Color.RED);
                lineDataSet.setValueTextSize(16f);
                LineData lineData=new LineData(lineDataSet);
                lineChart.setData(lineData);
                lineChart.getDescription().setEnabled(false);
                lineChart.animateY(2000);
                YAxis rightYAxis = lineChart.getAxisRight();
                rightYAxis.setEnabled(false);
                lineDataSet.setLineWidth(4f);
                lineDataSet.setCircleRadius(3f);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
package com.hmdapps.greenpurse;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hmdapps.greenpurse.Model.Data;

import java.text.DateFormat;
import java.util.Date;


@SuppressWarnings("rawtypes")
public class IncomeFragment extends Fragment {

    private DatabaseReference mIncomeDatabase;

    private RecyclerView recyclerView;
    private TextView incomeTotalSum;

    private EditText edtAmount, edtType, edtNote, edtDate;
    private Button btnUpdate, btnDelete;

    private String type;
    private String note;
    private String date;
    private int amount;
    private String post_key;


    public IncomeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_income, container, false);



        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uid = mUser.getUid();
        mIncomeDatabase = FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        recyclerView = myview.findViewById(R.id.recycler_id_income);
        incomeTotalSum = myview.findViewById(R.id.income_txt_result);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int totalvalue = 0;
                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    totalvalue += data.getAmount();
                    String stTotalValue = String.valueOf(totalvalue);
                    incomeTotalSum.setText(stTotalValue);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data> options =
                new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mIncomeDatabase, Data.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Data, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false));
            }
            @Override
            protected void onBindViewHolder(MyViewHolder viewholder, @SuppressLint("RecyclerView") int position, @NonNull final Data model) {
                viewholder.setAmmount(model.getAmount());
                viewholder.setType(model.getType());
                viewholder.setNote(model.getNote());
                viewholder.setDate(model.getDate());
                viewholder.mView.setOnClickListener(view -> {
                    post_key=getRef(position).getKey();
                    amount=model.getAmount();
                    type=model.getType();
                    note=model.getNote();
                    date=model.getDate();
                    updateDataItem();
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setType(String type) {
            TextView mType = mView.findViewById(R.id.type_txt_income);
            mType.setText(type);
        }
        public void setNote(String note) {
            TextView mNote = mView.findViewById(R.id.note_txt_income);
            mNote.setText(note);
        }
        public void setDate(String date) {
            TextView mDate = mView.findViewById(R.id.date_txt_income);
            mDate.setText(date);
        }
        public void setAmmount(int ammount) {
            TextView mAmmount = mView.findViewById(R.id.ammount_txt_income);
            String stammount = String.valueOf(ammount);
            mAmmount.setText(stammount);
        }
    }
    private void updateDataItem(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.update_data_item,null);
        mydialog.setView(myview);
        edtAmount=myview.findViewById(R.id.ammount_edt);
        edtType=myview.findViewById(R.id.type_edt);
        edtNote=myview.findViewById(R.id.note_edt);
        edtDate=myview.findViewById(R.id.date_edt);
        edtAmount.setText(String.valueOf(amount));
        edtAmount.setSelection(String.valueOf(amount).length());
        edtType.setText(type);
        edtType.setSelection(type.length());
        edtNote.setText(note);
        edtNote.setSelection(note.length());
        edtDate.setText(date);
        edtDate.setSelection(edtDate.length());
        btnUpdate=myview.findViewById(R.id.btnUpdate);
        btnDelete=myview.findViewById(R.id.btnDelete);
        final AlertDialog dialog = mydialog.create();
        btnUpdate.setOnClickListener(view -> {
            type=edtType.getText().toString().trim();
            note=edtNote.getText().toString().trim();
            String.valueOf(amount);
            String mdamount;
            mdamount=edtAmount.getText().toString().trim();
            int myamount= Integer.parseInt(mdamount);
            date=edtDate.getText().toString().trim();
            if (TextUtils.isEmpty(date)){
                date= DateFormat.getDateInstance().format(new Date());
            }
            Data data= new Data(myamount,type,note,post_key,date);
            mIncomeDatabase.child(post_key).setValue(data);
            dialog.dismiss();
        });
        btnDelete.setOnClickListener(view -> {
            mIncomeDatabase.child(post_key).removeValue();
            dialog.dismiss();
        });
        dialog.show();
    }
}
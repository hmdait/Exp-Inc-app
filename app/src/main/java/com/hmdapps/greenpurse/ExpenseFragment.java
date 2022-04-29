package com.hmdapps.greenpurse;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    private DatabaseReference mExpenseDatabase;

    private RecyclerView recyclerView;
    private TextView expenseTotalSum;

    private EditText edtAmount, edtType, edtNote, edtDate;

    private String type, date;
    private String note;
    private int amount;
    private String post_key;

    public ExpenseFragment() {
    }


    // TODO: Rename and change types and number of parameters


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_expense, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        assert mUser != null;
        String uid = mUser.getUid();

        mExpenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);

        recyclerView = myview.findViewById(R.id.rececler_id_expense);
        expenseTotalSum = myview.findViewById(R.id.expense_txt_result);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int expenseSum = 0;
                for (DataSnapshot mysnapshot : snapshot.getChildren()) {
                    Data data = mysnapshot.getValue(Data.class);
                    assert data != null;
                    expenseSum += data.getAmount();
                    String stTotalValue = String.valueOf(expenseSum);
                    expenseTotalSum.setText(stTotalValue);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myview;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(mExpenseDatabase, Data.class).setLifecycleOwner(this)
                .build();

        FirebaseRecyclerAdapter<Data, IncomeFragment.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Data, IncomeFragment.MyViewHolder>(options) {

            @NonNull
            @Override
            public IncomeFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new IncomeFragment.MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false));
            }

            @Override
            protected void onBindViewHolder(IncomeFragment.MyViewHolder viewholder, @SuppressLint("RecyclerView") int position, @NonNull final Data model) {
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
        edtDate.setSelection(date.length());

        Button btnUpdate = myview.findViewById(R.id.btnUpdate);
        Button btnDelete = myview.findViewById(R.id.btnDelete);

        final AlertDialog dialog = mydialog.create();

        btnUpdate.setOnClickListener(view -> {

            type=edtType.getText().toString().trim();
            note=edtNote.getText().toString().trim();
            date=edtDate.getText().toString().trim();
            String.valueOf(amount);
            String mdamount;
            mdamount=edtAmount.getText().toString().trim();
            int myamount= Integer.parseInt(mdamount);

            if (TextUtils.isEmpty(date)){ date= DateFormat.getDateInstance().format(new Date()); }

            Data data= new Data(myamount,type,note,post_key,date);
            mExpenseDatabase.child(post_key).setValue(data);
            dialog.dismiss();

        });
        btnDelete.setOnClickListener(view -> {

            mExpenseDatabase.child(post_key).removeValue();

            dialog.dismiss();
        });
        dialog.show();

    }

}
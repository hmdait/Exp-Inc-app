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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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


public class DachboarrdFragment extends Fragment {
    private FloatingActionButton fab_income_btn;
    private FloatingActionButton fab_expense_btn;
    private TextView fab_income_txt;
    private TextView fab_expense_txt;
    private boolean isOpen=false;
    private Animation FadOpen,FadeClose;
    private DatabaseReference mIncomeDatabase;
    private DatabaseReference mExpenseDatabase;
    private TextView totalIncomeResult;
    private TextView totalExpenseResult;
    private RecyclerView mRecyclerIncome;
    private RecyclerView mRecyclerExpense;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myview =  inflater.inflate(R.layout.fragment_dachboarrd, container, false);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser= mAuth.getCurrentUser();
        assert mUser != null;
        String uid=mUser.getUid();
        mIncomeDatabase= FirebaseDatabase.getInstance().getReference().child("IncomeData").child(uid);
        mExpenseDatabase=FirebaseDatabase.getInstance().getReference().child("ExpenseDatabase").child(uid);
        FloatingActionButton fab_main_btn = myview.findViewById(R.id.fb_main_plus_btn);
        fab_income_btn=myview.findViewById(R.id.income_ft_btn);
        fab_expense_btn=myview.findViewById(R.id.expense_Ft_btn);
        fab_income_txt=myview.findViewById(R.id.income_ft_text);
        fab_expense_txt=myview.findViewById(R.id.expense_ft_text);
        FadOpen= AnimationUtils.loadAnimation(getActivity(),R.anim.fade_open);
        FadeClose=AnimationUtils.loadAnimation(getActivity(),R.anim.fade_close);
        totalIncomeResult=myview.findViewById(R.id.income_set_result);
        totalExpenseResult=myview.findViewById(R.id.expense_set_result);
        mRecyclerIncome=myview.findViewById(R.id.recycler_income);
        mRecyclerExpense=myview.findViewById(R.id.recycler_expense);
        fab_main_btn.setOnClickListener(v -> {
            addData();
            if (isOpen){
                fab_income_btn.startAnimation(FadeClose);
                fab_expense_btn.startAnimation(FadeClose);
                fab_income_btn.setClickable(false);
                fab_expense_btn.setClickable(false);
                fab_income_txt.startAnimation(FadeClose);
                fab_expense_txt.startAnimation(FadeClose);
                fab_income_txt.setClickable(false);
                fab_expense_txt.setClickable(false);
                isOpen=false;
            }else {
                fab_income_btn.startAnimation(FadOpen);
                fab_expense_btn.startAnimation(FadOpen);
                fab_income_btn.setClickable(true);
                fab_expense_btn.setClickable(true);
                fab_income_txt.startAnimation(FadOpen);
                fab_expense_txt.startAnimation(FadOpen);
                fab_income_txt.setClickable(true);
                fab_expense_txt.setClickable(true);
                isOpen=true;
            }
        });

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalsum = 0;
                for (DataSnapshot mysnap : dataSnapshot.getChildren()) {
                    Data data = mysnap.getValue(Data.class);
                    assert data != null;
                    totalsum += data.getAmount();
                    String stResult = String.valueOf(totalsum);
                    totalExpenseResult.setText(stResult+".00");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mIncomeDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalsum = 0;
                for (DataSnapshot mysnap : dataSnapshot.getChildren()) {
                    Data data = mysnap.getValue(Data.class);
                    assert data != null;
                    totalsum += data.getAmount();
                    String stResult = String.valueOf(totalsum);
                    totalIncomeResult.setText(stResult+".00");
                } }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        LinearLayoutManager layoutManagerIncome = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerIncome.setStackFromEnd(true);
        layoutManagerIncome.setReverseLayout(true);
        mRecyclerIncome.setHasFixedSize(true);
        mRecyclerIncome.setLayoutManager(layoutManagerIncome);
        LinearLayoutManager layoutManagerExpense = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        layoutManagerExpense.setReverseLayout(true);
        layoutManagerExpense.setStackFromEnd(true);
        mRecyclerExpense.setHasFixedSize(true);
        mRecyclerExpense.setLayoutManager(layoutManagerExpense);
        return myview;
    }

    private void ftAnimation(){
        if (isOpen){

            fab_income_btn.startAnimation(FadeClose);
            fab_expense_btn.startAnimation(FadeClose);
            fab_income_btn.setClickable(false);
            fab_expense_btn.setClickable(false);
            fab_income_txt.startAnimation(FadeClose);
            fab_expense_txt.startAnimation(FadeClose);
            fab_income_txt.setClickable(false);
            fab_expense_txt.setClickable(false);
            isOpen=false;

        }else {
            fab_income_btn.startAnimation(FadOpen);
            fab_expense_btn.startAnimation(FadOpen);
            fab_income_btn.setClickable(true);
            fab_expense_btn.setClickable(true);
            fab_income_txt.startAnimation(FadOpen);
            fab_expense_txt.startAnimation(FadOpen);
            fab_income_txt.setClickable(true);
            fab_expense_txt.setClickable(true);
            isOpen=true;
        }
    }

    private void addData(){
        fab_income_btn.setOnClickListener(view -> incomeDataInsert());
        fab_expense_btn.setOnClickListener(v -> expenseDataInsert());
    }

    public void incomeDataInsert(){
        AlertDialog.Builder mydialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View myview = inflater.inflate(R.layout.custom_layout_insertdata, null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        final EditText edtAmmount=myview.findViewById(R.id.ammount_edt);
        final EditText edtType=myview.findViewById(R.id.type_edt);
        final EditText edtNote=myview.findViewById(R.id.note_edt);
        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCansel=myview.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(v -> {
            String type=edtType.getText().toString().trim();
            String ammount=edtAmmount.getText().toString().trim();
            String note=edtNote.getText().toString().trim();

            if (TextUtils.isEmpty(type)){
                edtType.setError("Required Type..");
                return;
            }

            if (TextUtils.isEmpty(ammount)){
                edtAmmount.setError("Required amount..");
                return;
            }

            int ourammontint=Integer.parseInt(ammount);

            if (TextUtils.isEmpty(note)){
                edtNote.setError("Required note..");
                return;
            }

            String id=mIncomeDatabase.push().getKey();
            String mDate = DateFormat.getDateInstance().format(new Date());
            Data data=new Data(ourammontint,type,note,id,mDate);
            assert id != null;
            mIncomeDatabase.child(id).setValue(data);
            Toast.makeText(getActivity(),"Data ADDED", Toast.LENGTH_SHORT).show();
            ftAnimation();
            dialog.dismiss();
        });

        btnCansel.setOnClickListener(v -> {
            ftAnimation();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void expenseDataInsert(){
        AlertDialog.Builder mydialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=LayoutInflater.from(getActivity());
        View myview=inflater.inflate(R.layout.custom_layout_insertdata,null);
        mydialog.setView(myview);
        final AlertDialog dialog = mydialog.create();
        dialog.setCancelable(false);
        EditText ammount=myview.findViewById(R.id.ammount_edt);
        EditText type=myview.findViewById(R.id.type_edt);
        EditText note=myview.findViewById(R.id.note_edt);
        Button btnSave=myview.findViewById(R.id.btnSave);
        Button btnCansel=myview.findViewById(R.id.btnCancel);
        btnSave.setOnClickListener(v -> {
            String tmAmmount=ammount.getText().toString().trim();
            String tmtype=type.getText().toString().trim();
            String tmnote=note.getText().toString().trim();

            if (TextUtils.isEmpty(tmAmmount)){
                ammount.setError("Requires Fields...");
                return;
            }

            int inamount=Integer.parseInt(tmAmmount);

            if (TextUtils.isEmpty(tmtype)){
                type.setError("Requires Fields...");
                return;
            }
            if (TextUtils.isEmpty(tmnote)){
                note.setError("Requires Fields...");
                return;
            }

            String id=mExpenseDatabase.push().getKey();
            String mDate=DateFormat.getDateInstance().format(new Date());
            Data data=new Data(inamount,tmtype,tmnote,id,mDate);
            assert id != null;
            mExpenseDatabase.child(id).setValue(data);
            Toast.makeText(getActivity(),"Data added",Toast.LENGTH_SHORT).show();
            ftAnimation();
            dialog.dismiss();
        });

        btnCansel.setOnClickListener(v -> {
            ftAnimation();
            dialog.dismiss();
        });
        dialog.show();
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Data>options=
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mIncomeDatabase,Data.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Data,IncomeViewHolder>incomeAdapter=new FirebaseRecyclerAdapter<Data, IncomeViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull IncomeViewHolder holder, int position, @NonNull Data model) {
                holder.setIncomeAmount(model.getAmount());
                holder.setIncomeType(model.getType());
                holder.setIncomeDate(model.getDate());
            }
            @NonNull
            @Override
            public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new IncomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashbourd_income,parent,false));
            }
        };
        mRecyclerIncome.setAdapter(incomeAdapter);
        FirebaseRecyclerOptions<Data>option=
                new FirebaseRecyclerOptions.Builder<Data>()
                        .setQuery(mExpenseDatabase,Data.class)
                        .setLifecycleOwner(this)
                        .build();
        FirebaseRecyclerAdapter<Data,ExpenseViewHolder> expenseAdapter=new FirebaseRecyclerAdapter<Data, ExpenseViewHolder>(option) {
            @Override
            protected void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position, @NonNull Data model) {
                holder.setExpenseAmount(model.getAmount());
                holder.setExpenseType(model.getType());
                holder.setExpenseDate(model.getDate());
            }

            @NonNull
            @Override
            public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new ExpenseViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashbourd_expense,parent,false));
            }
        };
        mRecyclerExpense.setAdapter(expenseAdapter);
    }

    public static class IncomeViewHolder extends RecyclerView.ViewHolder{
        View mIncomeView;
        public IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            mIncomeView=itemView;
        }
        public void setIncomeAmount(int amount){
            TextView mAmount=mIncomeView.findViewById(R.id.amount_income_ds);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }
        public void setIncomeType(String type){
            TextView mType=mIncomeView.findViewById(R.id.type_income_ds);
            mType.setText(type);
        }
        public void setIncomeDate(String date){
            TextView mDate=mIncomeView.findViewById(R.id.date_income_ds);
            mDate.setText(date);
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder{
        View mExpenseView;
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            mExpenseView=itemView;
        }
        public void setExpenseAmount(int amount){
            TextView mAmount=mExpenseView.findViewById(R.id.amount_expense_ds);
            String strAmount=String.valueOf(amount);
            mAmount.setText(strAmount);
        }
        public void setExpenseType(String type){
            TextView mType=mExpenseView.findViewById(R.id.type_expense_ds);
            mType.setText(type);
        }
        public void setExpenseDate(String date){
            TextView mDate=mExpenseView.findViewById(R.id.date_expense_ds);
            mDate.setText(date);
        }
    }
}
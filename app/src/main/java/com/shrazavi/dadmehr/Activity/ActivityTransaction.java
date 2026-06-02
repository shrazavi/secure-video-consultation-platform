package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterTransAction;
import com.shrazavi.dadmehr.DataClass.Transaction;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityTransaction extends AppCompatActivity {
    ArrayList<Transaction> trans;
    RecyclerAdapterTransAction recyclerAdaptertransaction;
    RecyclerView recyclerViewTransaction;
    LinearLayoutManager linearLayoutManager;
    Retrofitinformation RI;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        linearLayoutManager = new LinearLayoutManager(G.context);

        trans = new ArrayList<>();
        recyclerViewTransaction = findViewById(R.id.transaction_list);

        recyclerViewTransaction.setHasFixedSize(true);
        recyclerViewTransaction.setLayoutManager(linearLayoutManager);

        Call<ArrayList<Transaction>> calltransaction = RI.getTransaction(BasicActivity.content, BasicActivity.userid);
        calltransaction.enqueue(new Callback<ArrayList<Transaction>>() {
            @Override
            public void onResponse(Call<ArrayList<Transaction>> call, Response<ArrayList<Transaction>> response) {
                trans = response.body();
                recyclerViewTransaction.setAdapter(recyclerAdaptertransaction = new RecyclerAdapterTransAction(trans, G.context));


                recyclerAdaptertransaction.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<ArrayList<Transaction>> call, Throwable t) {
                Log.e("transerror", t + "");
            }
        });


//        ChatDatabase chatDatabase = new ChatDatabase(G.context);
//        Cursor cursor = chatDatabase.getAllTransaction();
//        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//
//            final String price = cursor.getString(1);
//            final String time = cursor.getString(2);
//            final long date = cursor.getLong(3);
//            final String status = cursor.getString(4);
//
//            Transaction transaction = new Transaction();
////            transaction.read = "read";
//            transaction.price = price;
//            transaction.time = time;
//            transaction.date = date;
//            transaction.status = status;
//
//            trans.add(transaction);
//        }
//        recyclerAdaptertransaction = new RecyclerAdapterTransAction( trans,ActivityTransaction.this);
        recyclerViewTransaction.setAdapter(new RecyclerAdapterTransAction(trans, ActivityTransaction.this));





    }
}
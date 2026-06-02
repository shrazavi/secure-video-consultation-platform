package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;

public class ActivityTransactionDetail extends AppCompatActivity {

    TextView txtprice, txtdate, txttime, txtcode, txtstatus;
    String price, date, time, code, status;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        txtprice = (TextView) findViewById(R.id.txt_tr_dt_price);
        txtdate = (TextView) findViewById(R.id.txt_tr_dt_date);
        txttime = (TextView) findViewById(R.id.txt_tr_dt_time);
        txtcode = (TextView) findViewById(R.id.txt_tr_dt_code);
        txtstatus = (TextView) findViewById(R.id.txt_tr_dt_status);


        price = (String) getIntent().getExtras().get("price");
        date = (String) getIntent().getExtras().get("date");
        time = (String) getIntent().getExtras().get("time");
        code = (String) getIntent().getExtras().get("code");
        status = (String) getIntent().getExtras().get("status");

        txtprice.setText(price);
        txtdate.setText(date);
        txttime.setText(time);
        txtcode.setText(code);

        if (status.equals("successful")) {
            txtstatus.setText("موفق");
            txtstatus.setTextColor(G.context.getResources().getColor(R.color.timemychat));
        } else {

            txtstatus.setTextColor(G.context.getResources().getColor(R.color.unsuccesful));
            txtstatus.setText("ناموفق");
        }
    }


}

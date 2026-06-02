package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.SolarCalendar;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;

public class ActivityFinalPayment extends AppCompatActivity {
    String enk;
    SecretKeySpec Key;
    SecretKeySpec secretKey;
    String vu = "",userid,time,id,account,cash,code;
    TextView txt_account, txt_cash, txt_code, txt_date, txt_time;
    Long date;
//    int cash = 0;
    Button btnfinal;
    Retrofitinformation RI;

    public Handler uploadHandler;

    // public static SharedPreferences preferences;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalpaeyment);
        txt_account = findViewById(R.id.txt_final_amount);
        txt_cash = findViewById(R.id.txt_fianal_cash);
        txt_code = findViewById(R.id.txt_fianal_code);
        txt_date = findViewById(R.id.txt_fianal_date);
        txt_time = findViewById(R.id.txt_fianal_time);

        btnfinal = findViewById(R.id.btn_final_payment);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        userid = BasicActivity.userid;
        vu = BasicActivity.vu;
        time = (String) getIntent().getExtras().get("time");
        cash = (String) getIntent().getExtras().get("cash");
        code = (String) getIntent().getExtras().get("code");
        date = (Long) getIntent().getExtras().get("date");
        account = (String) getIntent().getExtras().get("price");
        txt_account.setText(account);
        txt_cash.setText(cash);
        txt_code.setText(code);
        txt_time.setText(time);
        setdate(date,txt_date);

        btnfinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
            }
        });


    }
    private void setdate(long date, TextView dateText) {


        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date);

        SolarCalendar sc = new SolarCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), cal1.get(Calendar.DAY_OF_WEEK));
        dateText.setText(sc.getCurrentShamsidate());

    }
}
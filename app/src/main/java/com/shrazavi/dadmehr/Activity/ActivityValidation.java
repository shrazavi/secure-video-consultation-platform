package com.shrazavi.dadmehr.Activity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.AsyncaTaskSendCode;
import com.shrazavi.dadmehr.AsyncaTaskSendNumber;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;


public class ActivityValidation extends AppCompatActivity {
    public static EditText edtCode;
    private TextView timerValue;
    private TextView txtnumber;
    public int counter;
    int time;

    String name = "", vu = "", melli = "", ostan = "", shahr = "", parvane = "", experience = "", pass = "", number = "";
    int type = 0, base = 0;

    Button btnSend;
    static Context dialogContext;
    String edtNumber = "";
    String username = "";

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);
        edtCode = (EditText) findViewById(R.id.edtcode);
        btnSend = (Button) findViewById(R.id.btnvalidation);
        dialogContext = ActivityValidation.this;
        timerValue = (TextView) findViewById(R.id.txt_time);
        txtnumber = (TextView) findViewById(R.id.txt_num_validation);

        txtnumber.setText(edtNumber);
        vu = (String) getIntent().getExtras().get("vu");
        if(vu.equals("vl")) {
            edtNumber = (String) getIntent().getExtras().get("number");
            username = (String) getIntent().getExtras().get("username");
            name = (String) getIntent().getExtras().get("name");
            melli = (String) getIntent().getExtras().get("melli");
            ostan = (String) getIntent().getExtras().get("ostan");
            shahr = (String) getIntent().getExtras().get("shahr");
            parvane = (String) getIntent().getExtras().get("parvane");
            experience = (String) getIntent().getExtras().get("experienc");
            type = (Integer) getIntent().getExtras().get("type");
            base = (Integer) getIntent().getExtras().get("base");
            pass = (String) getIntent().getExtras().get("pass");
            number = (String) getIntent().getExtras().get("number");

        }else {
            edtNumber = (String) getIntent().getExtras().get("number");
            username = (String) getIntent().getExtras().get("username");
            name = (String) getIntent().getExtras().get("name");
            melli = (String) getIntent().getExtras().get("melli");

        }

//        String id = G.preferences.getString("id", "not");
//        Log.e("userid",id);
        time = 0;
//message(ActivityValidation.this);
        new CountDownTimer(50000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerValue.setText(millisUntilFinished / 1000 + "");

            }

            public void onFinish() {
                timerValue.setText("ارسال مجدد کد");
                time = 1;
            }
        }.start();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AsyncaTaskSendCode(G.phpurl + "checkcode.php",
                        edtNumber,
                        edtCode.getText().toString(),
                        username,
                        name,
                        vu,
                        melli,
                        ostan,
                        shahr,
                        parvane,
                        experience,
                        type,
                        base,
                        pass).execute();
                Log.e("edtNumber", edtNumber);
                Log.e("edtCode", edtCode.getText().toString());
            }
        });

        timerValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time == 1) {

                    new AsyncaTaskSendNumber(G.phpurl + "index.php", edtNumber).execute();

                    new CountDownTimer(50000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            timerValue.setText(millisUntilFinished / 1000 + "");

                        }

                        public void onFinish() {
                            timerValue.setText("ارسال مجدد کد");
                            time = 1;
                        }
                    }.start();
                }

            }
        });


    }

    public static void message(Context context) {

        Toast.makeText(context, "Network Available Do operations", Toast.LENGTH_LONG).show();
    }
}

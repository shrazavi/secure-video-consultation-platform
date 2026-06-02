package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.DataClass.DayReserve;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityReserve extends AppCompatActivity {
    Spinner spinday, spintime;
    Retrofitinformation RInode;
    String content;
    String enk;
    SecretKeySpec Key;
    String vu;
    Button btnreserve;
    ArrayList<DayReserve> arrday = new ArrayList<>();
    String[] arrtime;
    ArrayList<String> newtime = new ArrayList<>();
    Thread thread, thread2;
    String time,userid,vakilid,day;
//    public SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        spinday = (Spinner) findViewById(R.id.spn_reserve_day);
        spintime = (Spinner) findViewById(R.id.spn_reserve_time);
        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        vakilid = (String) getIntent().getExtras().get("username");
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        userid = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key=new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+userid+"-"+R.string.developer), Base64.NO_WRAP);

        btnreserve = (Button) findViewById(R.id.btn_reserve);
        Call<ArrayList<DayReserve>> callday = RInode.getday(content,vakilid,userid);
        callday.enqueue(new Callback<ArrayList<DayReserve>>() {
            @Override
            public void onResponse(Call<ArrayList<DayReserve>> call, Response<ArrayList<DayReserve>> response) {
                spinnerAdapter Adapterday = new spinnerAdapter(ActivityReserve.this, android.R.layout.simple_list_item_1);
                for (int i = 0; i < response.body().size(); i++) {
                    if (response.body().get(i).getHours().length == 0) {
                    } else {
                        Adapterday.add(response.body().get(i).getDay());
                    }
//                    Log.e("arrday1", response.body().get(i).getHours()+"");
                }
                arrday = response.body();
                Adapterday.add("روز");
                spinday.setAdapter(Adapterday);
                spinday.setSelection(Adapterday.getCount());
                spinday.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // TODO Auto-generated method stub

                        if (spinday.getSelectedItem() == "روز") {

                            //Do nothing.
                        } else {
//                            id_ostan = spinostan.getSelectedItemPosition() + 1;
                             day = spinday.getSelectedItem().toString();
                            for (int j = 0; j < arrday.size(); j++) {

                                if (arrday.get(j).getDay().equals(day)) {
                                    arrtime = response.body().get(j).getHours();

                                }
                            }
                            spinnerAdapter Adaptertime = new spinnerAdapter(ActivityReserve.this, android.R.layout.simple_list_item_1);
                            for (int k = 0; k < arrtime.length; k++) {
                                Adaptertime.add(arrtime[k]);
//                                Log.e("arrday", arrtime[k]+"");
                            }

                            Adaptertime.add("ساعت");
                            spintime.setAdapter(Adaptertime);
                            spintime.setSelection(Adaptertime.getCount());
//                            thread = new Thread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    try {
//                                        Thread.sleep(5000);
//
//                                    } catch (InterruptedException e) {
//                                        e.printStackTrace();
//                                    }
//
//
//                                }
//
//
//                            });


                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });

            }

            @Override
            public void onFailure(Call<ArrayList<DayReserve>> call, Throwable t) {

            }
        });


        spintime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if (spintime.getSelectedItem() == "ساعت") {

                    //Do nothing.
                } else {
                    time = spintime.getSelectedItem().toString();
                    Log.e("time", time);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        btnreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < arrtime.length; i++) {
                    if (arrtime[i].equals(time)) {
                    } else {
                        newtime.add(arrtime[i]);
                    }
                }
//                Log.e("newtime", newtime+"");
                Call<MessageSignup> callreserve = RInode.insertreserve(content,userid,vakilid,time,day);
                callreserve.enqueue(new Callback<MessageSignup>() {
                    @Override
                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                Log.e("messagereserve", response.body().getMessage()+"");

                        Call<MessageSignup> callupday = RInode.upgradetime(content,vakilid,newtime,day,userid);
                        callupday.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                Log.e("messagetime", response.body().getMessage()+"");


                                Intent intent = new Intent(ActivityReserve.this, ActivityProfileVl.class);
                                intent.putExtra("username", vakilid);
                                ActivityReserve.this.startActivity(intent);
                                ActivityReserve.this.finish();
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<MessageSignup> call, Throwable t) {

                    }
                });




            }
        });

    }

}

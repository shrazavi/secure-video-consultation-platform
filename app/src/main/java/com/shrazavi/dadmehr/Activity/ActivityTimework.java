package com.shrazavi.dadmehr.Activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.shrazavi.dadmehr.DataClass.Date;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Nobat;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.DataClass.Week;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class ActivityTimework extends AppCompatActivity {
    int reserve = 0;
    TextView txtswitch;
    Switch swtime;
    TextInputEditText edtvisitstart, edtvisitend, edtdatevisit;
    LinearLayout laytime, laydate;
    Button btnok, btnback;
    Thread thread, thread2;
    ArrayList<String> selectedItemsList = new ArrayList<>();
    private ArrayList<Week> week = new ArrayList();
    private ArrayList<String> arrtime = new ArrayList();
    String days = "";
    Retrofitinformation RInode;
    String content;
    String enk;
    SecretKeySpec Key;
    String username = "";
    String vu = "";
    String timeserver = "";
    int dateserver = 0;
    //    public SharedPreferences preferences;
    public static MaterialProgressBar prload;
    int resstatus1 = 0;
    int resstatus2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_work);
        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(ActivityTimework.this);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content = BasicActivity.content;

        prload = findViewById(R.id.pr_time_load);
        laytime = (LinearLayout) findViewById(R.id.lay_time_hour);
        laydate = (LinearLayout) findViewById(R.id.lay_time_date);
        btnok = (Button) findViewById(R.id.btn_time_ok);
        btnback = (Button) findViewById(R.id.btn_time_back);
        swtime = (Switch) findViewById(R.id.sw_time);
        txtswitch = (TextView) findViewById(R.id.txt_time_switch);
        edtvisitstart = (TextInputEditText) findViewById(R.id.edt_setting_visit_start);
        edtvisitend = (TextInputEditText) findViewById(R.id.edt_setting_visit_end);
        edtdatevisit = (TextInputEditText) findViewById(R.id.edt_setting_visit_date);
        prload.setVisibility(View.GONE);
        btnok.setVisibility(View.VISIBLE);
        Call<Date> calldate = RInode.getdate();
        calldate.enqueue(new Callback<Date>() {
            @Override
            public void onResponse(Call<Date> call, Response<Date> response) {
                timeserver = gettime(response.body().getDate());
                dateserver = daytstoint(response.body().getDate());
            }

            @Override
            public void onFailure(Call<Date> call, Throwable t) {

            }
        });
        Call<ArrayList<Nobat>> callreserve = RInode.getreserve(content, username);
        callreserve.enqueue(new Callback<ArrayList<Nobat>>() {
            @Override
            public void onResponse(Call<ArrayList<Nobat>> call, Response<ArrayList<Nobat>> response) {
//                String time = response.body().getTime();
                String[] parttimeserver = timeserver.split(":");
//                HashMap<Integer,Integer> map=new HashMap<Integer, Integer>();

                int hserver = Integer.parseInt(parttimeserver[0]);
                int mserver = Integer.parseInt(parttimeserver[1]);
                for (int i = 0; i < response.body().size(); i++) {
                    String[] timereserve = response.body().get(i).getTime().split(":");
                    int h = Integer.parseInt(timereserve[0]);
                    int m = Integer.parseInt(timereserve[1]);
                    if (getdayofweek(response.body().get(i).getDay()) < dateserver) {
//                        map.put(i,0);
                    } else if (getdayofweek(response.body().get(i).getDay()) == dateserver) {
                        if (h < hserver) {
                            if (m < mserver) {
//                                map.put(i,0);
                            } else {
//                                map.put(i,1);
                                resstatus2++;
                            }
                        } else {
//                            map.put(i,1);
                            resstatus2++;
                        }
                    } else {
//                        map.put(i,1);
                        resstatus2++;
                    }
//                    Log.e("arrday1", response.body().get(i).getHours()+"");
                }
//                for (int j=0;j<=map.size();j++){
//                    if(map.get(j)==1){
//                        resstatus1++;
//                    }
//                }
                Log.e("resstatus1", resstatus1 + "");
                Log.e("resstatus2", resstatus2 + "");

            }

            @Override
            public void onFailure(Call<ArrayList<Nobat>> call, Throwable t) {

            }
        });
        Call<Vakil> callvakil = RInode.getvakil(content, username, username);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                edtvisitstart.setText(response.body().getStarttime());
                edtvisitend.setText(response.body().getEndtime());
                edtdatevisit.setText(response.body().getActivedate());
                reserve = response.body().getReserve();

                if (reserve == 1) {

                    swtime.setChecked(true);
                    txtswitch.setText("نیاز به نوبت دهی دارم");
                    laydate.setVisibility(View.VISIBLE);
                    laytime.setVisibility(View.VISIBLE);
                } else {
                    swtime.setChecked(false);
                    txtswitch.setText("نیاز به نوبت دهی ندارم");
                    laydate.setVisibility(View.GONE);
                    laytime.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {
                Log.e("error???", t + "");
            }
        });

        swtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    txtswitch.setText("نیازی به نوبت دهی دارم");
                    laydate.setVisibility(View.VISIBLE);
                    laytime.setVisibility(View.VISIBLE);
                    reserve = 1;
                } else {
                    txtswitch.setText("نیازی به نوبت دهی ندارم");
                    laydate.setVisibility(View.GONE);
                    laytime.setVisibility(View.GONE);
                    reserve = 0;
                }
            }
        });
        edtvisitstart.setInputType(InputType.TYPE_NULL);
        edtvisitstart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityTimework.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtvisitstart.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        edtvisitend.setInputType(InputType.TYPE_NULL);
        edtvisitend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityTimework.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        edtvisitend.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        edtdatevisit.setInputType(InputType.TYPE_NULL);
        edtdatevisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean[] checkedItems = new boolean[]{false, false, false, false, false, false, false};

                week.clear();

                String[] listItems = {"شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهارشنبه", "پنجشنبه", "جمعه"};
                for (int i = 0; i < listItems.length; i++) {
                    Week w1 = new Week();
                    w1.setWeekname(listItems[i]);
                    w1.setChecked(false);
                    week.add(w1);

                }
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTimework.this);
                builder.setTitle("روزهای فعال بودن در اپلیکیشن");

                //this will checked the items when user open the dialog
                builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        Toast.makeText(ActivitySetting.this, "Position: " + which + " Value: " + listItems[which] + " State: " + (isChecked ? "checked" : "unchecked"), Toast.LENGTH_LONG).show();


                        Week w = new Week();
                        w.setWeekname(listItems[which]);
                        w.setChecked(isChecked);
                        week.set(which, w);
//                        Log.e("cheked", isChecked + "");
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        StringBuilder result = new StringBuilder();

                        for (Week item : week) {
//                            Log.e("week", item.getWeekname() + item.isChecked);
                            if (item.isChecked) {
//                                selectedItemsList.add(item.getWeek());
//                                Log.e("weekcheked", item.getWeek() + "");

                                result.append(item.getWeekname());
                                result.append("  ");
//                                days += item.getWeek() + " ";
                            }
                        }

//                        Log.e("week", week + "");


                        edtdatevisit.setText(result);
                        days = "";
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        edtdatevisit.setText(days + "");


        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("username", username);


                Log.e("reserve=", reserve + "");
                if (resstatus2 == 0) {
                    if (edtdatevisit.toString().isEmpty() || edtvisitstart.toString().isEmpty() || edtvisitend.toString().isEmpty()) {
                        Toast.makeText(ActivityTimework.this, "لطفا زمان را مشخص کنید", Toast.LENGTH_SHORT).show();
                    } else {
                        prload.setVisibility(View.VISIBLE);
                        btnok.setVisibility(View.GONE);
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {


                                String start = edtvisitstart.getText().toString();
                                String end = edtvisitend.getText().toString();

                                String[] arrday = edtdatevisit.getText().toString().split("  ");
                                selectedItemsList = new ArrayList<String>(Arrays.asList(arrday));

                                String[] partsstart = start.split(":");
                                int h1 = Integer.parseInt(partsstart[0]);
                                int m1 = Integer.parseInt(partsstart[1]);
                                String[] partsend = end.split(":");
                                int h2 = Integer.parseInt(partsend[0]);
                                int m2 = Integer.parseInt(partsend[1]);

                                arrtime = arraytime(h1, h2, m1, m2);

                            }

                        });

                        thread.start();

                        thread2 = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    if (arrtime.isEmpty()) {
                                        Thread.sleep(5000);
                                    }
                                    Thread.sleep(5000);
                                    Log.e("vakil", username + "/n" + edtvisitstart.getText().toString() + "/n" + edtvisitend.getText().toString() + "/n" + edtdatevisit.getText().toString() + "/n" + arrtime + "/n" + selectedItemsList + "/n");
                                    Call<MessageSignup> callvakil = RInode.settimevl(content,
                                            username,
                                            reserve,
                                            edtvisitstart.getText().toString(),
                                            edtvisitend.getText().toString(),
                                            selectedItemsList,
                                            arrtime,
                                            edtdatevisit.getText().toString(),
                                            username);

                                    callvakil.enqueue(new Callback<MessageSignup>() {
                                        @Override
                                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                            Boolean status = response.body().getStatus();
                                            if (status) {
                                                Log.e("mess", response.body().getMessage());
                                                prload.setVisibility(View.GONE);
                                                btnok.setVisibility(View.VISIBLE);
                                                Intent intent = new Intent(ActivityTimework.this, ActivityOption.class);
                                                ActivityTimework.this.startActivity(intent);
                                                ActivityTimework.this.finish();
                                            } else {

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MessageSignup> call, Throwable t) {
                                            Log.e("error???", t + "");
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }


                            }

                        });

                        thread2.start();

//
                    }
                }else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(ActivityTimework.this);
                    ab.setMessage(R.string.reserve).setNeutralButton("فهمیدم", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                }
            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityTimework.this, ActivityOption.class);
                ActivityTimework.this.startActivity(intent);
                ActivityTimework.this.finish();
            }
        });

    }

    public ArrayList<String> arraytime(int h1, int h2, int m1, int m2) {
        ArrayList<String> arrtime = new ArrayList<>();


        int h = 0, m = 0;
        // console.log("m1:" + m1);
        // console.log("h1:" + h1);
        //
        // console.log("h2:" + h2);
        // console.log("m2:" + m2);

        if (m1 == 0) {
            arrtime.add(h1 + ":" + m1);
            Log.e("h1:", "" + h1);
            Log.e("m1:", "" + m1);
            //arr.add(k:m1);
        }
        if (m2 >= 30) {
            // console.log("m1:"+m1);
            for (int i = h1; i < h2; i++) {
                h = i;
                for (int j = 0; j <= 1; j++) {
                    if (j == 0) {
                        if (m1 <= 30) {
                            m = 30;
                            m1 = 30;
                            arrtime.add(h + ":" + m);
                            //arr.add(h:m);
                            Log.e("h2:", "" + h);
                            Log.e("m2:", "" + m);
                        } else {
                            m = 0;
                            m1 = 0;

                        }

                    } else {
                        if (m1 == 30) {
                            m = 0;
                            m1 = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("h3:", "" + h);
                            Log.e("m3:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            //arr.add(h:m);
                            Log.e("h4:", "" + h);
                            Log.e("m4:", "" + m);
                        }


                    }

                }

            }

        } else {
            h2--;
            for (int i = h1; i < h2; i++) {
                h = i;
                for (int j = 0; j <= 1; j++) {
                    if (j == 0) {
                        if (m1 <= 30) {
                            m = 30;
                            m1 = 30;
                            arrtime.add(h + ":" + m);
                            Log.e("h5:", "" + h);
                            Log.e("m5:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            m1 = 0;

                        }

                    } else {
                        if (m1 == 30) {
                            m = 0;
                            m1 = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("h6+:", "" + h);
                            Log.e("m6:", "" + m);
                            //arr.add(h:m);
                        } else {
                            m = 0;
                            h++;
                            arrtime.add(h + ":" + m);
                            Log.e("m7:", "" + m);
                            Log.e("h7:", "" + h);
                            //arr.add(h:m);
                        }


                    }

                }

            }
            arrtime.add(h + ":" + "30");
            Log.e("h8:", "" + h);
            Log.e("m8:", "" + "30");
            //arr.add(h:30);
        }


        return arrtime;
    }

    private String gettime(long ts) {
        PersianDate cal = new PersianDate(ts);
        cal.getHour();

        return cal.getHour() + ":" + cal.getMinute();
    }

    private int daytstoint(long ts) {
        PersianDate cal1 = new PersianDate(ts);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int day = cal1.dayOfWeek();
        return day;

    }

    private int getdayofweek(String day) {

        switch (day) {
            case "شنبه":
                return 0;
            case "یکشنبه":
                return 1;

            case "دوشنبه":
                return 2;

            case "سه شنبه":
                return 3;

            case "چهارشنبه":
                return 4;

            case "پنجشنبه":
                return 5;

            case "جمعه":
                return 6;


        }

        return 0;
    }
}


//        btnbachup = (Button) findViewById(R.id.btn_bu_backup);
//        txtpath=(TextView) findViewById(R.id.txt_bu_path);
//        txtback=(TextView) findViewById(R.id.txt_bu_backup);
//        btnbachup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String inFileName = "/data/data/com.shrazavi.darmano/databases/chat_db";
//                File dbFile = new File(inFileName);
//                FileInputStream fis = null;
//                try {
//                    fis = new FileInputStream(dbFile);
//                    String outFileName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Darmano/backup/chat_db";
////                    if (!outFileName.exists()) {
////                        outFileName.mkdirs();
////                    }
//                    Log.e("src",outFileName);
//                    // Open the empty db as the output stream
//                    OutputStream output = new FileOutputStream(outFileName);
//
//                    // Transfer bytes from the inputfile to the outputfile
//                    byte[] buffer = new byte[1024];
//                    int length;
//                    while ((length = fis.read(buffer)) > 0) {
//                        output.write(buffer, 0, length);
//                    }
//
//                    // Close the streams
//                    output.flush();
//                    output.close();
//                    fis.close();
//                    txtback.setText("فایل پشتیبانی در آدرس زیر میباشد.");
//                    txtpath.setText(outFileName);
//                    txtback.setTextColor(G.context.getResources().getColor(R.color.timemychat));
//                } catch (FileNotFoundException e) {
//                    txtback.setText("خطا در پشتیبان گیری");
//                    txtback.setTextColor(G.context.getResources().getColor(R.color.unsuccesful));
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    txtback.setText("خطا در پشتیبان گیری");
//                    txtback.setTextColor(G.context.getResources().getColor(R.color.unsuccesful));
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//
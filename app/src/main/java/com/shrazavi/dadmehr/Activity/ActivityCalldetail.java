package com.shrazavi.dadmehr.Activity;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.textfield.TextInputEditText;
import com.shrazavi.dadmehr.DataClass.CallLog;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.SolarCalendar;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.voip.CallSingleActivity;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityCalldetail extends AppCompatActivity {
    Button btncondition, btnreport, btncantinue;
    LinearLayout layreport;
    ImageView imgprof;
    CardView crdprof;
    TextView txtname, txtprof, txttype, txtdate, txthour, txtduration, txtstatus, txtcondition;
    String myuser, username = "", room = "", vu = "", callid = "", hours = "", status = "", type = "", condition = "";
    String content;
    String enk;
    long starttime = 0, endtime = 0;
//    public SharedPreferences preferences;
    Retrofitinformation RI;
    Boolean isAudioOnly = false;
public int report=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calldetail);

        txtprof = (TextView) findViewById(R.id.prof_txt_call_detail);
        txtname = (TextView) findViewById(R.id.txt_call_detail_name);
        txttype = (TextView) findViewById(R.id.txt_call_detail_type);
        txtdate = (TextView) findViewById(R.id.txt_call_detail_date);
        txthour = (TextView) findViewById(R.id.txt_call_detail_hour);
        txtduration = (TextView) findViewById(R.id.txt_call_detail_duration);
        txtstatus = (TextView) findViewById(R.id.txt_call_detail_status);
        txtcondition = (TextView) findViewById(R.id.txt_call_detail_condition);
        imgprof = (ImageView) findViewById(R.id.prof_img_call_detail);
        crdprof = (CardView) findViewById(R.id.prof_crd_call_detail);
        layreport = (LinearLayout) findViewById(R.id.lay_call_detail_report);
        btncondition = (Button) findViewById(R.id.btn_call_detail_condition);
        btnreport = (Button) findViewById(R.id.btn_call_detail_report);
        btncantinue = (Button) findViewById(R.id.btn_call_detail_cantinue);


        btncondition.setTypeface(G.face);
        btncantinue.setTypeface(G.face);
        btnreport.setTypeface(G.face);


        username = (String) getIntent().getExtras().get("username");
        room = (String) getIntent().getExtras().get("room");
        vu = (String) getIntent().getExtras().get("vu");
        callid = (String) getIntent().getExtras().get("callid");
//        starttime = (long) getIntent().getExtras().get("starttime");
//        endtime = (long) getIntent().getExtras().get("endtime");
//        hours = (String) getIntent().getExtras().get("hours");
//        status = (String) getIntent().getExtras().get("status");
//        type = (String) getIntent().getExtras().get("type");
//        condition = (String) getIntent().getExtras().get("condition");

//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        myuser = BasicActivity.userid;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myuser + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        Call<CallLog> calllog = RI.getcall(callid);
        calllog.enqueue(new Callback<CallLog>() {
            @Override
            public void onResponse(Call<CallLog> call, Response<CallLog> response) {
                starttime = response.body().getStarttime();
                endtime = response.body().getEndtime();
                hours = response.body().getHours();
                status = response.body().getStatus();
                type = response.body().getType();
                condition = response.body().getCondition();
                report=response.body().getReport();
                txthour.setText(hours);
                setdate(starttime, txtdate);
                if (type.equals("video")) {
                    txttype.setText("تصویری");
                    isAudioOnly = false;
                } else {
                    txttype.setText("صوتی");
                    isAudioOnly = true;
                }
//                if (condition.equals("continue")) {
//                    txtcondition.setText("ناتمام");
//                    txtcondition.setTextColor(Color.parseColor("#E91E63"));
//                    if (status.equals("answer")) {
//
//                    } else {
//                        btncondition.setVisibility(View.GONE);
//                        btncantinue.setVisibility(View.GONE);
//                    }
//                } else {
//                    txtcondition.setText("اتمام مشاوره");
//                    txtcondition.setTextColor(Color.parseColor("#00ff00"));
//                    btncondition.setVisibility(View.GONE);
//                    btncantinue.setVisibility(View.GONE);
//                }
//                if (status.equals("answer")) {
//                    txtstatus.setText("پاسخ داده شده");
//                    txtstatus.setTextColor(Color.parseColor("#00ff00"));
//                    setduration(starttime, endtime, txtduration);
//                } else {
//                    txtstatus.setTextColor(Color.parseColor("#E91E63"));
//                    txtstatus.setText("پاسخ داده نشده");
//
//                }
//                if (checkduration(starttime, endtime)) {
//                    txtcondition.setText("اتمام مشاوره");
//                    txtcondition.setTextColor(Color.parseColor("#00ff00"));
//                    btncondition.setVisibility(View.GONE);
//                    btncantinue.setVisibility(View.GONE);
//
//                }




                if (condition.equals("continue")) {
                    if (status.equals("answer")) {
                        txtcondition.setText("ناتمام");
                        txtstatus.setText("پاسخ داده شده");
                        txtcondition.setTextColor(Color.parseColor("#E91E63"));
                        txtstatus.setTextColor(Color.parseColor("#00ff00"));
                        setduration(starttime, endtime, txtduration);
//                        btncondition.setVisibility(View.VISIBLE);
//                        btncantinue.setVisibility(View.VISIBLE);
                    } else {
                        txtcondition.setText("ناتمام");
                        txtstatus.setText("پاسخ داده نشده");
                        txtstatus.setTextColor(Color.parseColor("#E91E63"));
                        txtcondition.setTextColor(Color.parseColor("#E91E63"));
                        btncondition.setVisibility(View.GONE);
                        btncantinue.setVisibility(View.GONE);
//                        btnreport.setVisibility(View.GONE);
                        txtduration.setText( 0 + " دقیقه " + 0 + " ثانیه ");
                    }
                } else {
                    if (status.equals("answer")) {
                        txtcondition.setText("اتمام مشاوره");
                        txtstatus.setText("پاسخ داده شده");
                        txtcondition.setTextColor(Color.parseColor("#00ff00"));
                        txtstatus.setTextColor(Color.parseColor("#00ff00"));
                        btncondition.setVisibility(View.GONE);
                        btncantinue.setVisibility(View.GONE);
                        setduration(starttime, endtime, txtduration);
                    } else {
                    }
                }

                if (checkduration(starttime, endtime)) {
                    txtcondition.setText("اتمام مشاوره");
                    txtstatus.setText("پاسخ داده شده");
                    txtcondition.setTextColor(Color.parseColor("#00ff00"));
                    txtstatus.setTextColor(Color.parseColor("#00ff00"));
                    btncondition.setVisibility(View.GONE);
                    btncantinue.setVisibility(View.GONE);
                }







                if (report==1) {
                    btnreport.setVisibility(View.GONE);
                } else {
                    btnreport.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CallLog> call, Throwable t) {

            }
        });



        if (vu.equals("vl")) {
            btncantinue.setVisibility(View.GONE);
            btncondition.setVisibility(View.VISIBLE);

            Call<User> calluser = RI.getuser(content, username, myuser);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    txtname.setText(response.body().getName());
                    if (response.body().getProfile().equals("empty")) {
                        char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                        txtprof.setText(ch1 + "");
                        crdprof.setCardBackgroundColor(color);
                    } else {
                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(imgprof);

                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            btncantinue.setVisibility(View.VISIBLE);
            btncondition.setVisibility(View.GONE);
            Call<Vakil> callvakil = RI.getvakil(content, username, myuser);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                    txtname.setText(response.body().getName());
                    if (response.body().getProfile().equals("empty")) {
                        char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                        Random rnd = new Random();
                        int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                        txtprof.setText(ch1 + "");
                        crdprof.setCardBackgroundColor(color);
                    } else {
                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(imgprof);

                    }

                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {

                }
            });
        }

        btncantinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<CallLog> calllog = RI.getcall(callid);
                calllog.enqueue(new Callback<CallLog>() {
                    @Override
                    public void onResponse(Call<CallLog> call, Response<CallLog> rescall) {
                        starttime = rescall.body().getStarttime();
                        endtime = rescall.body().getEndtime();
                        condition = rescall.body().getCondition();
                        if(condition.equals("continue")){
                            CallSingleActivity.openActivity(ActivityCalldetail.this, username, username, room, myuser, myuser, true, isAudioOnly, false, settimer(starttime, endtime) + "");
                            BasicActivity.callid = callid;
                            BasicActivity.callcondition = "continue";
                        }else {
                            Toast.makeText(ActivityCalldetail.this,"مشاوره اتمام یافته است",Toast.LENGTH_SHORT).show();
                            btncantinue.setVisibility(View.GONE);
                            txtcondition.setText("اتمام مشاوره");
                            txtcondition.setTextColor(Color.parseColor("#00ff00"));
                        }
                    }

                    @Override
                    public void onFailure(Call<CallLog> call, Throwable t) {

                    }
                });
            }
        });
        btncondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<MessageSignup> callset = RI.setconditioncall(content, callid,"compelete", myuser);
                callset.enqueue(new Callback<MessageSignup>() {
                    @Override
                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                        Boolean status = response.body().getStatus();
                        if (status) {
                            Toast.makeText(ActivityCalldetail.this,"مشاوره اتمام یافته است",Toast.LENGTH_SHORT).show();
                            btncondition.setVisibility(View.GONE);
                            txtcondition.setText("اتمام مشاوره");
                            txtcondition.setTextColor(Color.parseColor("#00ff00"));
                        }else {
                            Toast.makeText(ActivityCalldetail.this,"خطا در برقراری ارتباط با اینترنت",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<MessageSignup> call, Throwable t) {

                    }
                });
            }
        });
        btnreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuild = new AlertDialog.Builder(ActivityCalldetail.this);
                View mView = getLayoutInflater().inflate(R.layout.fragment_report, null);

                TextInputEditText txttext = (TextInputEditText) mView.findViewById(R.id.edt_report_text);
                Button btnSubmit = (Button) mView.findViewById(R.id.btnSubReport);

                mBuild.setView(mView);
                AlertDialog dialog = mBuild.create();
                dialog.show();

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (txttext.getText().toString().isEmpty()) {
                            Toast.makeText(ActivityCalldetail.this, "لطفا اطلاعات را تکمیل کنید", Toast.LENGTH_SHORT).show();
                        } else {
                            Call<MessageSignup> callreport = RI.insertreport(content, myuser,username,vu,  txttext.getText().toString(),callid, myuser);
                            callreport.enqueue(new Callback<MessageSignup>() {
                                @Override
                                public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                    Boolean status = response.body().getStatus();
                                    if (status) {
                                        dialog.dismiss();

                                    }

                                }

                                @Override
                                public void onFailure(Call<MessageSignup> call, Throwable t) {
                                    Log.e("error user", t + "");
                                }

                            });

                        }
                    }
                });
            }
        });
        crdprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void setdate(long date, TextView dateText) {
        Calendar cal1 = Calendar.getInstance();
        cal1.getTimeZone();
        cal1.setTimeInMillis(date);
//Log.e("recdate",date+"");
        SolarCalendar sc = new SolarCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), cal1.get(Calendar.DAY_OF_WEEK));
        dateText.setText(sc.getCurrentShamsidate());
    }

    private void setduration(long starttime, long endtime, TextView durationText) {
        long dur = endtime - starttime;
        String minutes = String.valueOf(((int) (dur % (60 * 60 * 1000) / (60 * 1000))));
        String seconds = String.valueOf(((int) ((dur % (60 * 60 * 1000)) % (60 * 1000) / 1000)));
        String duration = minutes + " دقیقه " + seconds + " ثانیه ";
        durationText.setText(duration);
    }

    private Boolean checkduration(long starttime, long endtime) {
        long dur = endtime - starttime;
        int minutes = ((int) (dur % (60 * 60 * 1000) / (60 * 1000)));
        int seconds = ((int) ((dur % (60 * 60 * 1000)) % (60 * 1000) / 1000));


        if (minutes == 25) {
            return true;
        } else {
            return false;
        }

    }

    private long settimer(long starttime, long endtime) {
        long dur = endtime - starttime;
        long timer = 1500000 - dur;

        return timer;
    }

}

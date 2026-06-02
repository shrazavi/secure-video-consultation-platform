package com.shrazavi.dadmehr.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shrazavi.dadmehr.Adapter.spinnerAdapter;
import com.shrazavi.dadmehr.DataClass.Cash;
import com.shrazavi.dadmehr.DataClass.Date;
import com.shrazavi.dadmehr.DataClass.DayReserve;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.Nobat;
import com.shrazavi.dadmehr.DataClass.Room;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;
import com.shrazavi.dadmehr.core.voip.CallSingleActivity;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class ActivityProfileVl extends AppCompatActivity {
    String[] arrtime;
    ArrayList<String> newtime = new ArrayList<>();

    ArrayList<DayReserve> arrday = new ArrayList<>();
    //    String[] arrcal = {"متنی", "صوتی", "تصویری", "تصویری"};
//    AutoCompleteTextView timespin, calspin;
    CardView crdvisit, crdcall, crdprof;
    //    String typecall = "";
    String content;
    String enk;
    SecretKeySpec Key;
    SecretKeySpec secretKey;
    //    LinearLayout lay_call;
    TextView txtname, txtcost, txtexperience, txtspecialty, txtdegree, txteducation, txtbase, txttype, txtbio, txtreserve, txtprof;
    Button btnvoise, btnvideo, btnchat, btnreserve, btnactivedate;
    ImageView imgprof;
    //    String time = "";
//    String call = "";
    String toid = "";
    String imgurl = "";
    String userid;
    int chattarrif = 0;
    //    String incomecash;
    String vu = "";
    String name, experience, bio;
    private Boolean isLoading = false;
    String  timeserver;
    int dateserver=0;
    //    int zarib = 0;
    int account = 0;
    //    int income = 0;
    int price = 0;
    //    int pricevl = 0;
    int timer = 1500000;
    public Handler uploadHandler;
    private static final int PER_PAGE_SIZE_100 = 100;
    private static final String ORDER_RULE = "order";
    private static final String ORDER_DESC_UPDATED = "desc date updated_at";
    public static final String TOTAL_PAGES_BUNDLE_PARAM = "total_pages";
    private Boolean hasNextPage = true;
    //    private Socket socket;
    private int currentPage = 0;
//    public SharedPreferences preferences;

    Retrofitinformation RI;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_vl);

//        socket.connect();
//        timespin = (AutoCompleteTextView) findViewById(R.id.spn_prof_vl_time);
//        calspin = (AutoCompleteTextView) findViewById(R.id.spn_prof_vl_type_call);
        txtname = (TextView) findViewById(R.id.txt_prof_vl_name);
        txtprof = (TextView) findViewById(R.id.app_bar_txt_prof_vl);
//        txtcost = (TextView) findViewById(R.id.txt_prof_vl_cost);
        txtspecialty = (TextView) findViewById(R.id.txt_prof_vl_specialty);
        txtdegree = (TextView) findViewById(R.id.txt_prof_vl_degree);
        txteducation = (TextView) findViewById(R.id.txt_prof_vl_education);
        txttype = (TextView) findViewById(R.id.txt_prof_vl_type);
        txtbase = (TextView) findViewById(R.id.txt_prof_vl_base);
        txtexperience = (TextView) findViewById(R.id.txt_prof_vl_experience);
        txtbio = (TextView) findViewById(R.id.txt_prof_vl_bio);
        txtreserve = (TextView) findViewById(R.id.txt_prof_vl_reserve);
        btnchat = (Button) findViewById(R.id.btn_prof_vl_chat);
        btnvoise = (Button) findViewById(R.id.btn_prof_vl_voise);
        btnvideo = (Button) findViewById(R.id.btn_prof_vl_video);
        btnactivedate = (Button) findViewById(R.id.btn_prof_vl_visit);
        btnreserve = (Button) findViewById(R.id.btn_prof_vl_reserve);
        crdcall = (CardView) findViewById(R.id.crd_prof_vl_call);
        crdprof = (CardView) findViewById(R.id.app_bar_crd_prof_vl);
        crdvisit = (CardView) findViewById(R.id.crd_prof_vl_visit);
        imgprof = (ImageView) findViewById(R.id.app_bar_image_prof_vl);

        toid = (String) getIntent().getExtras().get("username");
        secretKey = SymmetricAlgorithmAES.setUpSecretKey();
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        userid = BasicActivity.userid;
        vu = BasicActivity.vu;
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);

        crdcall.setVisibility(View.VISIBLE);
        crdvisit.setVisibility(View.VISIBLE);

        if (vu.equals("vl")) {
            crdcall.setVisibility(View.GONE);
            crdvisit.setVisibility(View.GONE);
            btnreserve.setVisibility(View.GONE);
            btnactivedate.setVisibility(View.GONE);
            txtreserve.setVisibility(View.GONE);
        }

//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + getResources().getString(R.string.developer), enk);
        content = BasicActivity.content;
        Log.e("content", content);

        //------------tarikh alan-------------
        Call<Date> calldate = RI.getdate();
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

        //------------etelaat vakil-------------

        Call<Vakil> callvakil = RI.getvakil(content, toid, userid);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
        //------------rooze rezerve shode-------------

                if (response.body().getReserve() == 1) {
                    crdcall.setVisibility(View.GONE);
                    crdvisit.setVisibility(View.VISIBLE);
                    Call<ArrayList<DayReserve>> callday = RI.getday(content, toid, userid);
                    callday.enqueue(new Callback<ArrayList<DayReserve>>() {
                        @Override
                        public void onResponse(Call<ArrayList<DayReserve>> call, Response<ArrayList<DayReserve>> response) {

                            arrday = response.body();

                        }

                        @Override
                        public void onFailure(Call<ArrayList<DayReserve>> call, Throwable t) {

                        }
                    });
                    Call<Nobat> callreserve = RI.getnobat(content, userid, toid, userid);
                    callreserve.enqueue(new Callback<Nobat>() {
                        @Override
                        public void onResponse(Call<Nobat> call, Response<Nobat> response) {


                            if (!response.body().getDay().isEmpty()) {
                                txtreserve.setVisibility(View.VISIBLE);
                                txtreserve.setText("زمان رزرو شما : روز " + response.body().getDay() + " ساعت " + response.body().getTime());
                                String time = response.body().getTime();
                                btnreserve.setVisibility(View.GONE);
                                String[] parttimeserver = timeserver.split(":");
                                String[] timereserve = time.split(":");
                                int h = Integer.parseInt(timereserve[0]);
                                int m = Integer.parseInt(timereserve[1]);
                                int hserver = Integer.parseInt(parttimeserver[0]);
                                int mserver = Integer.parseInt(parttimeserver[1]);
//                                Log.e("time", "dateserver=" + dateserver + "/hserver=" + hserver + "/mserver=" + mserver + "/h=" + h + "/m=" + m);
                                if (getdayint(response.body().getDay())<dateserver) {
                                    crdcall.setVisibility(View.GONE);
                                    txtreserve.setVisibility(View.GONE);
                                    btnreserve.setVisibility(View.VISIBLE);
                                }else if(getdayint(response.body().getDay())==dateserver){
                                    if (h == hserver) {
                                        if (m == 30 && mserver >= 30) {
                                            crdcall.setVisibility(View.VISIBLE);
                                            txtreserve.setVisibility(View.GONE);
                                        } else if (m == 0 && mserver >= 0 && mserver < 5) {
                                            crdcall.setVisibility(View.VISIBLE);
                                            txtreserve.setVisibility(View.GONE);
                                        } else {
                                            crdcall.setVisibility(View.GONE);
                                            txtreserve.setVisibility(View.VISIBLE);
                                        }
                                    } else {
                                        crdcall.setVisibility(View.GONE);
                                        txtreserve.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    crdcall.setVisibility(View.GONE);
                                    txtreserve.setVisibility(View.VISIBLE);
                                }
                            } else {
                                txtreserve.setVisibility(View.GONE);
                                btnreserve.setVisibility(View.VISIBLE);
                            }
                        }
                        @Override
                        public void onFailure(Call<Nobat> call, Throwable t) {
                        }
                    });
                } else {
                    crdcall.setVisibility(View.VISIBLE);
                    crdvisit.setVisibility(View.GONE);
                    if (vu.equals("vl")) {
                        crdcall.setVisibility(View.GONE);
                        crdvisit.setVisibility(View.GONE);
                        btnreserve.setVisibility(View.GONE);
                        btnactivedate.setVisibility(View.GONE);
                        txtreserve.setVisibility(View.GONE);
                    }
                }
                String blockusers = response.body().getBlockusers();
                String[] arrblock = blockusers.split(",");
                chattarrif = response.body().getChattariff();
                if (Arrays.asList(arrblock).contains(userid)) {
                    crdcall.setVisibility(View.GONE);
                    crdvisit.setVisibility(View.GONE);
                    Toast.makeText(G.context, "شما مسدود شدید", Toast.LENGTH_SHORT).show();
                } else {

                }
                name = response.body().getName();
                experience = response.body().getExperience();
                bio = response.body().getBio();
                txtname.setText(name);
                txtdegree.setText(response.body().getDegree());
                txteducation.setText(response.body().getEducation());
                txtspecialty.setText(response.body().getSpecialty());

                txtexperience.setText("با " + experience + " سال سابقه");
                txtbio.setText(bio);
//                Log.e("type vl=", response.body().getType()+ "");
                if (response.body().getChattariff() == 0) {
                    btnchat.setVisibility(View.GONE);
                } else {
                    btnchat.setVisibility(View.VISIBLE);
                }
                if (response.body().getVoisetariff() == 0) {
                    btnvoise.setVisibility(View.GONE);

                } else {
                    btnvoise.setVisibility(View.VISIBLE);

                }
                if (response.body().getVideotariff() == 0) {
                    btnvideo.setVisibility(View.GONE);
                } else {
                    btnvideo.setVisibility(View.VISIBLE);
                }


                if (response.body().getReserve() == 1) {
                    crdvisit.setVisibility(View.VISIBLE);
                } else {
                    crdvisit.setVisibility(View.GONE);
                }
                if (response.body().getType().equals("1")) {
                    txttype.setText("کانون وکلای دادگستری " + response.body().getOstan());
                    if (response.body().getBase().equals("1")) {
                        txtbase.setText("وکیل پایه یک دادگستری");
                    } else {
                        txtbase.setText("وکیل پایه دوم دادگستری");
                    }
                } else {
                    txttype.setText("مرکز مشاوران دادگستری استان " + response.body().getOstan());
                }
                imgurl = G.nodeurl + "/" + response.body().getProfile();
                if (imgurl.equals(G.nodeurl + "/" + "empty")) {
                    char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                    txtprof.setText(ch1 + "");
                    crdprof.setCardBackgroundColor(color);
                } else {
                    Picasso.with(G.context).load(imgurl).transform(new ImageProfile()).into(imgprof);

                }

            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {
                Log.e("vakilerr=", "" + t);

            }
        });
        //------------etelaat karbar-------------
        Call<User> calluser = RI.getuser(content, userid, userid);
        calluser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                G.account = Integer.parseInt(response.body().getAccount());
                account = Integer.parseInt(response.body().getAccount());
                Log.e("account", account + "");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

//        Log.e("userid", userid + "");
        imgprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgurl.isEmpty()) {
                } else {
                    Intent intent = new Intent(ActivityProfileVl.this, ActivityImageView.class);
                    intent.putExtra("imgurl", imgurl);
                    intent.putExtra("down", false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityProfileVl.this.startActivity(intent);
                }
            }
        });
        btnreserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder mBuild = new android.app.AlertDialog.Builder(ActivityProfileVl.this);
                View mView = getLayoutInflater().inflate(R.layout.fragment_reserve, null);


                AutoCompleteTextView spinday = (AutoCompleteTextView) mView.findViewById(R.id.spn_reserve_day);
                AutoCompleteTextView spintime = (AutoCompleteTextView) mView.findViewById(R.id.spn_reserve_time);
                Button btnSubmit = (Button) mView.findViewById(R.id.btn_reserve);

                spinnerAdapter Adapterday = new spinnerAdapter(ActivityProfileVl.this, android.R.layout.simple_list_item_1);
//                Adapterday.addAll(arrtype);
                for (int i = 0; i < arrday.size(); i++) {
                    if (arrday.get(i).getHours().length == 0) {
                    }else if(getdayint(arrday.get(i).getDay())<dateserver){
                    }
                    else {
                        Adapterday.add(arrday.get(i).getDay());
//                        Log.e("arrday1", arrday.get(i).getDay()+"");
                    }
//                    Log.e("arrday1", response.body().get(i).getHours()+"");
                }
                Adapterday.add("");
                spinday.setAdapter(Adapterday);

                mBuild.setView(mView);
                android.app.AlertDialog dialog = mBuild.create();
                dialog.show();
                spinday.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String day = spinday.getText().toString();
//                        Log.e("shahrname", shahrestan);
                        for (int j = 0; j < arrday.size(); j++) {

                            if (arrday.get(j).getDay().equals(day)) {
                                arrtime = arrday.get(j).getHours();

                            }
                        }
                        spinnerAdapter Adaptertime = new spinnerAdapter(ActivityProfileVl.this, android.R.layout.simple_list_item_1);
                        for (int k = 0; k < arrtime.length; k++) {
                            Adaptertime.add(arrtime[k]);
//                                Log.e("arrday", arrtime[k]+"");
                        }

                        Adaptertime.add("");
                        spintime.setAdapter(Adaptertime);
//                        spintime.setSelection(Adaptertime.getCount());

                    }
                });
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (spinday.getText().toString().isEmpty() || spintime.getText().toString().isEmpty()) {
                            Toast.makeText(ActivityProfileVl.this, "لطفا زمان مورد نظر را انتخاب کنید", Toast.LENGTH_SHORT).show();
                        } else {
                            for (int i = 0; i < arrtime.length; i++) {
                                if (arrtime[i].equals(spintime.getText().toString())) {
                                } else {
                                    newtime.add(arrtime[i]);
                                }
                            }
//                Log.e("newtime", newtime+"");
                            Call<MessageSignup> callreserve = RI.insertreserve(content, userid, toid, spintime.getText().toString(), spinday.getText().toString());
                            callreserve.enqueue(new Callback<MessageSignup>() {
                                @Override
                                public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                    Log.e("messagereserve", response.body().getMessage() + "");

                                    Call<MessageSignup> callupday = RI.upgradetime(content, toid, newtime, spinday.getText().toString(), userid);
                                    callupday.enqueue(new Callback<MessageSignup>() {
                                        @Override
                                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                            Log.e("messagetime", response.body().getMessage() + "");
                                            dialog.dismiss();
//                                            btnactivedate.setVisibility(View.GONE);
                                            txtreserve.setVisibility(View.VISIBLE);
                                            btnreserve.setVisibility(View.GONE);
                                            txtreserve.setText("زمان رزرو شما : روز " + spinday.getText().toString() + " ساعت " + spintime.getText().toString());

//                                            Intent intent = new Intent(ActivityProfileVl.this, ActivityProfileVl.class);
//                                            intent.putExtra("username", vakilid);
//                                            ActivityProfileVl.this.startActivity(intent);
//                                            ActivityProfileVl.this.finish();
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
                    }
                });
            }
        });

        btnactivedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityProfileVl.this, ActivityVisit.class);
                intent.putExtra("username", toid);
                ActivityProfileVl.this.startActivity(intent);
                ActivityProfileVl.this.finish();
            }
        });

        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                Call<Cash> callcash = RI.checkaccount(content, userid, false, true, toid);
                                callcash.enqueue(new Callback<Cash>() {
                                    @Override
                                    public void onResponse(Call<Cash> call, Response<Cash> response) {
                                        if (response.body().getStatus()) {

                                            Call<Room> callroom = RI.creatroom(content, userid, toid, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT), userid);
                                            callroom.enqueue(new Callback<Room>() {
                                                @Override
                                                public void onResponse(Call<Room> call, Response<Room> response) {


                                                    Intent intent = new Intent(ActivityProfileVl.this, ActivityChat.class);
                                                    intent.putExtra("timer", timer);
                                                    intent.putExtra("roomid", response.body().getId());
                                                    intent.putExtra("id", toid);
//
                                                    ActivityProfileVl.this.startActivity(intent);
                                                    ActivityProfileVl.this.finish();
                                                }

                                                @Override
                                                public void onFailure(Call<Room> call, Throwable t) {

                                                }
                                            });


                                        } else {
                                            Toast.makeText(G.context, "موجودی کافی نیست", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Cash> call, Throwable t) {

                                    }
                                });
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.cancel();
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(ActivityProfileVl.this);
                ab.setMessage("هزینه تماس " + chattarrif + " تومان میباشد آیا مایل به برقراری تماس هستید.").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        btnvoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Cash> callcash = RI.checkaccount(content, userid, true, false, toid);
                callcash.enqueue(new Callback<Cash>() {
                    @Override
                    public void onResponse(Call<Cash> call, Response<Cash> response) {
                        if (response.body().getStatus()) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Call<Room> callroom = RI.creatroom(content, userid, toid, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT), userid);
                                            callroom.enqueue(new Callback<Room>() {
                                                @Override
                                                public void onResponse(Call<Room> call, Response<Room> response) {
                                                    CallSingleActivity.openActivity(ActivityProfileVl.this, toid, toid, response.body().getId(), userid, userid, true, true, false, timer + "");
                                                    BasicActivity.callcondition = "start";
//                                    CallSingleActivity.openActivity(ActivityProfileVl.this, resTarget.body().getSocketId(),toid,"60e57dcab921c43ac41cf1ab",resMy.body().getSocketId(), true,userid, true, false,timer+"");
//                                    CallSingleActivity.openActivity(ActivityProfileVl.this, resTarget.body().getSocketId(), toid, response.body().getId(), resMy.body().getSocketId(), userid, true, true, false, timer + "");


                                                }

                                                @Override
                                                public void onFailure(Call<Room> call, Throwable t) {

                                                }
                                            });
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder ab = new AlertDialog.Builder(ActivityProfileVl.this);
                            ab.setMessage("هزینه تماس " + response.body().getMessage() + "تومان میباشد آیا مایل به برقراری تماس هستید.").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();

                        } else {

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Toast.makeText(G.context, "موجودی کافی نیست", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder ab = new AlertDialog.Builder(ActivityProfileVl.this);
                            ab.setMessage("هزینه تماس " + response.body().getMessage() + "تومان میباشد آیا مایل به برقراری تماس هستید.").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Cash> call, Throwable t) {
                        Log.e("casherr", "" + t);
                    }
                });

            }
        });
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Cash> callcash = RI.checkaccount(content, userid, false, false, toid);
                callcash.enqueue(new Callback<Cash>() {
                    @Override
                    public void onResponse(Call<Cash> call, Response<Cash> response) {
                        if (response.body().getStatus()) {
                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Call<Room> callroom = RI.creatroom(content, userid, toid, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT), userid);
                                            callroom.enqueue(new Callback<Room>() {
                                                @Override
                                                public void onResponse(Call<Room> call, Response<Room> response) {

//                                    CallSingleActivity.openActivity(ActivityProfileVl.this, resTarget.body().getSocketId(),toid,response.body().getId(),resMy.body().getSocketId(), true,resTarget.body().getUserId(), false, false,timer+"");
//                                    CallSingleActivity.openActivity(ActivityProfileVl.this, resTarget.body().getSocketId(), toid, response.body().getId(), resMy.body().getSocketId(), userid, true, false, false, timer + "");
                                                    CallSingleActivity.openActivity(ActivityProfileVl.this, toid, toid, response.body().getId(), userid, userid, true, false, false, timer + "");
                                                    BasicActivity.callcondition = "start";

                                                }


                                                @Override
                                                public void onFailure(Call<Room> call, Throwable t) {
                                                    Log.e("error", t + "");
                                                }
                                            });
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder ab = new AlertDialog.Builder(ActivityProfileVl.this);
                            ab.setMessage("هزینه تماس " + response.body().getMessage() + "تومان میباشد آیا مایل به برقراری تماس هستید.").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        } else {

                            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_POSITIVE:
                                            Toast.makeText(G.context, "موجودی کافی نیست", Toast.LENGTH_SHORT).show();
                                            break;

                                        case DialogInterface.BUTTON_NEGATIVE:
                                            //No button clicked
                                            dialog.cancel();
                                            break;
                                    }
                                }
                            };
                            AlertDialog.Builder ab = new AlertDialog.Builder(ActivityProfileVl.this);
                            ab.setMessage("هزینه تماس " + response.body().getMessage() + "تومان میباشد آیا مایل به برقراری تماس هستید.").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<Cash> call, Throwable t) {

                    }
                });

            }
        });
    }


    private String gettime(long ts) {
        PersianDate cal = new PersianDate(ts);
        cal.getHour();

        return cal.getHour() + ":" + cal.getMinute();
    }

    private String getdayofweek(long ts1) {
        PersianDate cal1 = new PersianDate(ts1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int day = cal1.dayOfWeek();
        switch (day) {
            case 0:
                return "شنبه";
            case 1:
                return "یکشنبه";

            case 2:
                return "دوشنبه";

            case 3:
                return "سه شنبه";

            case 4:
                return "چهارشنبه";

            case 5:
                return "پنجشنبه";

            case 6:
                return "جمعه";


        }

        return "";
    }
    private int daytstoint(long ts) {
        PersianDate cal1 = new PersianDate(ts);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        int day = cal1.dayOfWeek();
        return day;

    }

    private int getdayint(String day) {

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
    @Override
    protected void onResume() {
        super.onResume();

    }
}
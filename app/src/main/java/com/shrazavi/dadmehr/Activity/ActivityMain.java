
package com.shrazavi.dadmehr.Activity;

import static com.shrazavi.dadmehr.Fragment.LawyerListFragment.edtsearch;
import static com.shrazavi.dadmehr.Fragment.LawyerListFragment.laysearch;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.github.nkzawa.emitter.Emitter;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.shrazavi.dadmehr.ClosingService;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.Fragment.CalllogFragment;
import com.shrazavi.dadmehr.Fragment.ChatListFragment;
import com.shrazavi.dadmehr.Fragment.LawyerListFragment;
import com.shrazavi.dadmehr.Fragment.ProfileFragment;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.socket.IUserState;
import com.shrazavi.dadmehr.core.voip.Utils;
import com.shrazavi.dadmehr.core.voip.VoipReceiver;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityMain extends BasicActivity implements NavigationView.OnNavigationItemSelectedListener, IUserState {

    //    String content;
    String usernumber;
    public static MaterialProgressBar prload;
    TextView txtnumber, txtuser, txtprof;
    public static TextView txtconnection;
    //    BottomBar bottombar;
    public static BadgeDrawable badgechat;
    public static BadgeDrawable badgecall;
    ClosingService service;
    //    public static Socket socket;
//    public static SharedPreferences preferences;
    EditText edtsend;
    LinearLayout linearmessage, lay_peyment, lay_setting, lay_logout, lay_support,lay_rules,lay_help;
    LinearLayout.LayoutParams layoutParams;
    public Handler handler;
    Thread datathread;
    ImageView writeNewMessage;
    ImageView imguserprofile;
    CardView crdprofile;
    public static Handler handlerchat;
    public Handler handlercash;
    Thread thread;
    public static int coanterfrag;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public static Button btnmenu, btnnotify, btnnotifycount, btnsearch;
    String enk;
    String rating;
    int rate;
    public static Context dialogContext;
    SharedPreferences.Editor editor;
    NavigationView navigationView2;
    Retrofitinformation RI;
    SecretKeySpec secretKey;
    boolean isFromCall;
    int search = 0;
//    {
//        try {
//            socket = IO.socket(G.nodeurl);//http://192.168.1.103:8000
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }


//    public static void start(Context context) {
//        Intent intent = new Intent(context, ActivityMain.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        context.startActivity(intent);
//    }
//    protected ServiceConnection mConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder binder) {
//            service = ((DefaultBinder) binder).getService();
//
//        }
//
//        public void onServiceDisconnected(ComponentName className) {
//            service = null;
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        handler = new Handler();
//        socket.connect();
//        creatnotification(this);

//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
//        editor = preferences.edit();
//        editor.putString("login", "ok");
//        editor.commit();
        Log.e("loginam", preferences.getString("login", "not") + "");
        if (preferences.getString("login", "not").equals("not")) {
            finish();

        }
        coanterfrag = 0;
        writeNewMessage = (ImageView) findViewById(R.id.img_profil);
//        btn_search = (Button) findViewById(R.id.btn_search);
//        bottombar = (BottomBar) findViewById(R.id.bottom_bar);
        btnnotify = (Button) findViewById(R.id.btn_notify);
        btnnotifycount = (Button) findViewById(R.id.btn_notify_count);
        btnmenu = (Button) findViewById(R.id.menu);
        btnsearch = (Button) findViewById(R.id.btn_main_search);

        btnnotifycount.setTypeface(G.face);
        prload = findViewById(R.id.pr_main_load);
        linearmessage = (LinearLayout) findViewById(R.id.linearMessage1);
        lay_peyment = (LinearLayout) findViewById(R.id.lay_nav_peyment);
        lay_setting = (LinearLayout) findViewById(R.id.lay_nav_setting);
        lay_logout = (LinearLayout) findViewById(R.id.lay_nav_logout);
        lay_support = (LinearLayout) findViewById(R.id.lay_nav_backup);
        lay_rules = (LinearLayout) findViewById(R.id.lay_nav_ruls);
        lay_help = (LinearLayout) findViewById(R.id.lay_nav_help);

        navigationView2 = (NavigationView) findViewById(R.id.nav_view2);
        View headerView = navigationView2.getHeaderView(0);
        txtconnection = (TextView) findViewById(R.id.txt_connection);
        imguserprofile = (ImageView) findViewById(R.id.img_prof);
        crdprofile = (CardView) findViewById(R.id.crd_prof);
        txtnumber = (TextView) findViewById(R.id.txt_num_head);
        txtuser = (TextView) findViewById(R.id.txt_user_head);
        txtprof = (TextView) findViewById(R.id.txt_prof);
        edtsend = (EditText) findViewById(R.id.edtTextMessage);
        btnsearch.setVisibility(View.GONE);

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                JSONObject notify = new JSONObject();
                try {
                    notify.put("from", userid);
                    notify.put("content", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.emit("notify", notify);

                if (vu.equals("vl")) {
                    String call = preferences.getString("call", "not");
                    String timer = preferences.getString("timer", "not");
                    if (call.equals("not") || timer.equals("not")) {
                    } else {
                        Call<MessageSignup> calltimer = RI.checktimer(content, Long.parseLong(timer), userid);
                        calltimer.enqueue(new Callback<MessageSignup>() {
                            @Override
                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {


                                Call<User> calluser = RI.getuser(content, userid, userid);
                                calluser.enqueue(new Callback<User>() {
                                    @Override
                                    public void onResponse(Call<User> call, Response<User> resuser) {

                                        AlertDialog.Builder mBuild = new AlertDialog.Builder(ActivityMain.this);
                                        View mView = getLayoutInflater().inflate(R.layout.fragment_call_pay, null);

                                        TextView txtcallname = (TextView) mView.findViewById(R.id.txt_callpay_name);
                                        TextView txtcallprof = (TextView) mView.findViewById(R.id.txt_callpay);
                                        CardView crdcallprof = (CardView) mView.findViewById(R.id.crd_callpay);
                                        ImageView imgcall = (ImageView) mView.findViewById(R.id.img_callpay);
                                        Button btnyes = (Button) mView.findViewById(R.id.btn_callpay_yes);
                                        Button btnno = (Button) mView.findViewById(R.id.btn_callpay_no);

                                        txtcallname.setText(resuser.body().getName());
                                        if (resuser.body().getProfile().equals("empty")) {
                                            char ch1 = resuser.body().getUsername().toUpperCase().charAt(0);
                                            Random rnd = new Random();
                                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                                            txtcallprof.setText(ch1 + "");
                                            crdcallprof.setCardBackgroundColor(color);
                                        } else {
                                            Picasso.with(G.context).load(G.nodeurl + "/" + resuser.body().getProfile()).transform(new ImageProfile()).into(imgcall);
                                        }
                                        mBuild.setView(mView);
                                        AlertDialog dialog = mBuild.create();
                                        dialog.show();

                                        btnyes.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                Call<MessageSignup> callrating = RI.Rating(content, rating, rate, userid);
                                                callrating.enqueue(new Callback<MessageSignup>() {
                                                    @Override
                                                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                                        Log.e("message rating", response.body().getMessage() + "");
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("call", "not");
                                                        editor.commit();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<MessageSignup> call, Throwable t) {
                                                        Log.e("error user", t + "");
                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        });

                                        btnno.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Call<MessageSignup> callrating = RI.Rating(content, rating, rate, userid);
                                                callrating.enqueue(new Callback<MessageSignup>() {
                                                    @Override
                                                    public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                                        Log.e("message rating", response.body().getMessage() + "");
                                                        SharedPreferences.Editor editor = preferences.edit();
                                                        editor.putString("call", "not");
                                                        editor.commit();
                                                    }

                                                    @Override
                                                    public void onFailure(Call<MessageSignup> call, Throwable t) {
                                                        Log.e("error user", t + "");
                                                    }
                                                });
                                                dialog.dismiss();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<User> call, Throwable t) {
                                        Log.e("error user", t + "");
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<MessageSignup> call, Throwable t) {
                                Log.e("error vl", t + "");
                            }
                        });
                    }
                } else {
                    rating = preferences.getString("rating", "not");
                    if (rating.equals("not")) {
                    } else {
                        Call<Vakil> callvakil = RI.getvakil(content, rating, userid);
                        callvakil.enqueue(new Callback<Vakil>() {
                            @Override
                            public void onResponse(Call<Vakil> call, Response<Vakil> response) {


                                AlertDialog.Builder mBuild = new AlertDialog.Builder(ActivityMain.this);
                                View mView = getLayoutInflater().inflate(R.layout.fragment_rating, null);

                                final RatingBar ratebar = (RatingBar) mView.findViewById(R.id.ratingBar);
                                ratebar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        rate = (int) rating;
                                    }
                                });
                                TextView txtratename = (TextView) mView.findViewById(R.id.txt_rate_name);
                                TextView txtrateprof = (TextView) mView.findViewById(R.id.txt_rating);
                                CardView crdrateprof = (CardView) mView.findViewById(R.id.crd_rating);
                                ImageView imgrate = (ImageView) mView.findViewById(R.id.img_rating);
                                Button btnSubmit = (Button) mView.findViewById(R.id.btnSubRating);
                                txtratename.setText(response.body().getName());
                                if (response.body().getProfile().equals("empty")) {
                                    char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                                    Random rnd = new Random();
                                    int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                                    txtrateprof.setText(ch1 + "");
                                    crdrateprof.setCardBackgroundColor(color);
                                } else {
                                    Picasso.with(G.context).load(G.nodeurl + "/" + response.body().getProfile()).transform(new ImageProfile()).into(imgrate);
                                }
                                mBuild.setView(mView);
                                AlertDialog dialog = mBuild.create();
                                dialog.show();
                                btnSubmit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        Call<MessageSignup> callrating = RI.Rating(content, rating, rate, userid);
                                        callrating.enqueue(new Callback<MessageSignup>() {
                                            @Override
                                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                                Log.e("message rating", response.body().getMessage() + "");
                                                SharedPreferences.Editor editor = preferences.edit();
                                                editor.putString("rating", "not");
                                                editor.commit();
                                            }

                                            @Override
                                            public void onFailure(Call<MessageSignup> call, Throwable t) {
                                                Log.e("error user", t + "");
                                            }
                                        });
                                        dialog.dismiss();
                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Vakil> call, Throwable t) {
                                Log.e("error vl", t + "");
                            }
                        });
                    }
                }
            }
        }.start();
        FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new ProfileFragment());
        frm.commit();
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_bar);
        badgechat = bottomNavigationView.getOrCreateBadge(R.id.chat);
        badgecall = bottomNavigationView.getOrCreateBadge(R.id.call);


        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.call:
                    FragmentTransaction frm1 = fragmentManager.beginTransaction().replace(R.id.frmlay, new CalllogFragment());

                    frm1.commit();
                    item.setChecked(true);
                    btnsearch.setBackgroundResource(R.drawable.ic_search);
                    btnsearch.setVisibility(View.GONE);
                    break;

                case R.id.lawyer:
                    FragmentTransaction frm3 = fragmentManager.beginTransaction().replace(R.id.frmlay, new LawyerListFragment());
                    frm3.commit();
                    item.setChecked(true);
                    btnsearch.setVisibility(View.VISIBLE);
                    btnsearch.setBackgroundResource(R.drawable.ic_search);
                    break;
                case R.id.chat:
                    FragmentTransaction frm4 = fragmentManager.beginTransaction().replace(R.id.frmlay, new ChatListFragment());
                    frm4.commit();
                    item.setChecked(true);
                    btnsearch.setBackgroundResource(R.drawable.ic_search);

                    btnsearch.setVisibility(View.GONE);
                    break;
                case R.id.profile:
                    FragmentTransaction frm5 = fragmentManager.beginTransaction().replace(R.id.frmlay, new ProfileFragment());
                    frm5.commit();
                    item.setChecked(true);
                    btnsearch.setBackgroundResource(R.drawable.ic_search);
                    btnsearch.setVisibility(View.GONE);


                    break;
            }
            return false;
        });

//        progressDialog = findViewById(R.id.progressBarmain);
        dialogContext = ActivityMain.this;

//        vu = preferences.getString("type", "not");
//        userid = preferences.getString("username", "not");
//)
//        enk = preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        secretKey = new SecretKeySpec(data, 0, data.length, "AES");

//        Log.e("code", vu + "-" + userid + "-" + R.string.developer);
//        Log.e("ejrashodam","aaaaaaaaaaaaaa");
//        Log.e("ejrashodam","aaaaaaaaaaaaaa");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(secretKey, vu+"-"+userid+"-"+R.string.developer), Base64.NO_WRAP);
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + getResources().getString(R.string.developer), enk);
//        content=preferences.getString("content", "not");


        Log.e("userid", userid + "/" + vu);
//        usertype = preferences.getString("type", "not");
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        socket.on("connected", handlerConnected);
        G.getInstance().setUsername(userid);
        // 添加登录回调
        BasicActivity.addUserStateCallback(this);
        // 连接socket:登录
//        SocketManager.getInstance().connect(Urls.WS, userid, 0);
//        SocketManager.getInstance().Login(userid);

        isFromCall = getIntent().getBooleanExtra("isFromCall", false);
        LogUtils.dTag("MainActivity", "onCreate isFromCall = " + isFromCall);
        if (isFromCall) { //无权限，来电申请权限会走这里
            initCall();
        }

//        socket.on("incomingcall", handlercall);
        JSONObject connected = new JSONObject();
        try {
            connected.put("from", userid);
            connected.put("message", "Connected");
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        G.getInstance();
//        socket.emit("nickname", userid);
//        socket.emit("connected", connected);
//        JSONObject postDataForRoom = new JSONObject();
//        try {
//            postDataForRoom.put("user", userid);
//            postDataForRoom.put("vu", vu);
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        socket.emit("getroom", postDataForRoom);


//        thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(2500);
//                    handlerchat.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("ejrashodam","aaaaaaaaaaaaaa");
//                            socket.emit("getmessage", userid);
//                        }
//                    });
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        });
//
//        thread.start();

//        if (ValidationUtils.isLoginValid(ActivityLogin.this, edtuser) &&
//                ValidationUtils.isFoolNameValid(ActivityLogin.this, userFullNameEditText)) {
//            hideKeyboard();
//            userForSave = createUserWithEnteredData();
//            startSignUpNewUser(userForSave);
//        }
//        Log.e("du", du);
//        handlercash = new Handler();
        if (vu.equals("vl")) {
            lay_peyment.setVisibility(View.GONE);
            Call<Vakil> callvakil = RI.getvakil(content, userid, userid);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                    G.income = Integer.parseInt(response.body().getIncome());
                    Log.e("imgurl", G.nodeurl + "/" + response.body().getProfile());
//                    preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("imgprofile", G.nodeurl + "/" + response.body().getProfile());
                    editor.putString("number", response.body().getPhone());
                    editor.commit();
                    txtuser.setText(response.body().getName());

                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {
                    Log.e("error vl", t + "");
                }
            });

//

        } else {
            Call<User> calluser = RI.getuser(content, userid, userid);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    G.account = Integer.parseInt(response.body().getAccount());
//                    preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("number", response.body().getNumber());
                    editor.putString("imgprofile", G.nodeurl + "/" + response.body().getProfile());
                    txtuser.setText(response.body().getName());
                    editor.commit();
//                    txtuser.setText(response.body().getName());

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.e("error user", t + "");
                }
            });
//
        }
        //        new AsyncTaskGetProfileUrl(G.phpurl + "/getimageprofile.php", userid, du).execute();

        usernumber = preferences.getString("number", "not");

        String imgurl = preferences.getString("imgprofile", "not");
        Log.e("imgurl", imgurl);
        if (imgurl.equals(G.nodeurl + "/" + "empty")) {
            char ch1 = userid.toUpperCase().charAt(0);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
//                            holder.imgProf.setVisibility(View.GONE);
            txtprof.setText(ch1 + "");
            crdprofile.setCardBackgroundColor(color);
        } else {
//            Picasso.with(G.context).load(imgurl).into(imguserprofile);
            Picasso.with(G.context).load(imgurl).transform(new ImageProfile()).into(imguserprofile);
        }


        // Check network connection
//        if (Variables.isNetworkConnected){
//            // Internet Connected
//            ActivityMain.txtconnection.setText("Connected");
//        }else{
//            // Not Connected
//            ActivityMain.txtconnection.setText("Waiting For Connection");
//        }
        isConnected();
        if (isConnected()) {
            ActivityMain.txtconnection.setText("Connected");
            ActivityMain.prload.setVisibility(View.GONE);
        } else {
            ActivityMain.prload.setVisibility(View.VISIBLE);

            ActivityMain.txtconnection.setText("Waiting For Connection");
        }


//        if (usernumber.equals("not")) {
////            new AsyncaTaskGetnumber(G.phpurl + "/getnumber.php", userid, usertype).execute();
//        } else {
        lay_peyment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.context, ActivityPayment.class);
                intent.putExtra("vu", vu);
                startActivity(intent);
            }
        });
        lay_rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.context, ActivityLaw.class);
                startActivity(intent);
            }
        });
        lay_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(G.context, ActivityHelp.class);
                startActivity(intent);
            }
        });
        lay_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(G.context, ActivityOption.class);
                startActivity(intent4);
            }
        });
        lay_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(G.context, ActivitySupport.class);
                startActivity(intent4);
            }
        });
        lay_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);

            }
        });
        txtnumber.setText("" + usernumber);
//        txtuser.setText(userid);
        // }


//        socket.connect();
//        socket.on("message", holderIncominMessage);

//        writeNewMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(G.context, ActivityTest.class);
//                startActivity(intent);
//            }
//        });

//        bottombar.setOnTabReselectListener(new OnTabReselectListener() {
//            @Override
//            public void onTabReSelected(int tabId) {
//                if (tabId == R.id.profile) {
//                    //   Toast.makeText(MainActivity.this, "صفحه اصلی", Toast.LENGTH_SHORT).show();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new ProfileFragment());
//                    frm.commit();
//                }
//                if (tabId == R.id.chat) {
//                    //  Toast.makeText(MainActivity.this, "پر کامنت ترین", Toast.LENGTH_SHORT).show();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new ChatListFragment());
//                    frm.commit();
//                }
//                if (tabId == R.id.call) {
//                    // Toast.makeText(MainActivity.this, "پربازدید ترین", Toast.LENGTH_SHORT).show();
//                    coanterfrag = 0;
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new CalllogFragment());
//                    frm.commit();
//                }
//
//
//            }
//        });
//        bottombar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(int tabId) {
//                if (tabId == R.id.profile) {
//                    // Toast.makeText(MainActivity.this, "پر کامنت ترین", Toast.LENGTH_SHORT).show();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new ProfileFragment());
//                    frm.commit();
//                }
//                if (tabId == R.id.chat) {
//                    //Toast.makeText(MainActivity.this, "صفحه اصلی", Toast.LENGTH_SHORT).show();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new ChatListFragment());
//                    frm.commit();
//                }
//                if (tabId == R.id.call) {
//                    //  Toast.makeText(MainActivity.this, "پربازدید ترین", Toast.LENGTH_SHORT).show();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new CalllogFragment());
//                    frm.commit();
//                }
//
//
//            }
//        });


//        btn_search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,Search.class);
//                MainActivity.this.startActivity(intent);
//            }
//        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search == 0) {
                    edtsearch.setVisibility(View.VISIBLE);
                    laysearch.setVisibility(View.VISIBLE);
                    edtsearch.setFocusableInTouchMode(true);
                    edtsearch.requestFocus();
                    btnsearch.setBackgroundResource(R.drawable.ic_unsucces);
                    search = 1;
                } else {
                    edtsearch.setText("");
                    edtsearch.setVisibility(View.GONE);
                    laysearch.setVisibility(View.GONE);
                    btnsearch.setBackgroundResource(R.drawable.ic_search);
                    search = 0;
                }


//                edtsearch.showDropDown();
            }
        });
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConnected();
                if (isConnected()) {
                    ActivityMain.txtconnection.setText("Connected");
                    ActivityMain.prload.setVisibility(View.GONE);

                } else {
                    ActivityMain.prload.setVisibility(View.VISIBLE);

                    ActivityMain.txtconnection.setText("Waiting For Connection");
                }
                if (drawer.isDrawerOpen(GravityCompat.END)) {
                    drawer.closeDrawer(GravityCompat.END);
                } else {
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });
        btnnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivitySupport.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityMain.this.startActivity(intent);
            }
        });

        navigationView2.setNavigationItemSelectedListener(this);


    }


    @Override
    protected void onResume() {
        super.onResume();
//        bindService(new Intent(this, ActivityMain.class), mConnection,
//                Context.BIND_AUTO_CREATE);
//        socket.emit("nickname", userid);
//    loadUsers();
    }

    @Override
    protected void onStop() {
        super.onStop();

//        if (mConnection != null) {
//            try {
//                unbindService(mConnection);
//            } catch (Exception e) {}
//        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();

        int id = menuItem.getItemId();
        FragmentTransaction frm1;
        switch (id) {

            case R.id.nav_contact:
                //   frm1 = fragmentManager.beginTransaction().replace(R.id.frmlay, new Profile());
                //   frm1.commit();
//                Intent intent = new Intent(G.context, mFragmentFavorite.class);
//                startActivity(intent);
//                Intent intent = new Intent(G.context, ActivityContacts.class);
//                intent.putExtra("du", vu);
//                startActivity(intent);

                break;

//            case R.id.nav_group_add:
//                //  frm1 = fragmentManager.beginTransaction().replace(R.id.frmlay, new Categorylist());
//                //  frm1.commit();
//                Intent intent3 = new Intent(G.context, ActivityChannel.class);
//                startActivity(intent3);
//                break;
//
//
//            case R.id.nav_group:
//                Intent intent2 = new Intent(G.context, ActivityJoinChannel.class);
//                startActivity(intent2);
//                break;
            case R.id.nav_Setting:
                Intent intent4 = new Intent(G.context, ActivitySetting.class);
                startActivity(intent4);
                break;
        }
        return true;

    }


    public void open() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("How much do yo like this App?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);

                try {
                    startActivity(myAppLinkToMarket);


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public Emitter.Listener holderIncominMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jobject = (JSONObject) args[0];
                    String messsage = "";
                    try {
                        messsage = jobject.getString("message").toString();
                        TextView txtmessage = new TextView(getApplicationContext());
                        txtmessage.setText(messsage);
                        txtmessage.setTextSize(18);
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        linearmessage.addView(txtmessage);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    public Emitter.Listener handlerConnected = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String connect = "";

                    try {
                        connect = jsonObject.getString("message").toString();


                        txtconnection.setText(connect);

//                            txtIsTyping.setText("");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlercall = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.e("ejrashodam", "aaaaaaaaaaaaaa");
                    String connect = "";
                    String room = "";
                    String type = "";
                    String from = "";
                    String to = "";
                    String time = "";
                    try {
                        room = jsonObject.getString("room").toString();
                        type = jsonObject.getString("type").toString();
                        from = jsonObject.getString("from").toString();
                        to = jsonObject.getString("to").toString();
                        time = jsonObject.getString("time").toString();

                        Intent intent = new Intent(ActivityMain.this, ActivityCall.class);
//                                    intent.putExtra("imgurl", imgurl);
                        intent.putExtra("from", from);
                        intent.putExtra("to", to);
                        intent.putExtra("room", room);
                        intent.putExtra("type", type);
                        intent.putExtra("time", time);
                        intent.putExtra("call", "in");
                        ActivityMain.this.startActivity(intent);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            JSONObject disconnect = new JSONObject();
            try {
                disconnect.put("from", userid);
                disconnect.put("vu", vu);
                disconnect.put("message", "Last Seen Recently");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            socket.emit("disconnect", disconnect);
            // Handle application closing
            socket.disconnect();
//            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        return super.onKeyDown(keycode, event);
    }

    public static void restart(Context context, int delay) {
        if (delay == 0) {
            delay = 1;
        }
        Log.e("", "restarting app");
        Intent restartIntent = context.getPackageManager()
                .getLaunchIntentForPackage(context.getPackageName());
        PendingIntent intent = PendingIntent.getActivity(
                context, 0,
                restartIntent, Intent.FLAG_ACTIVITY_CLEAR_TOP);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, System.currentTimeMillis() + delay, intent);
        System.exit(2);
    }

    //
    public boolean isConnected() {
        ConnectivityManager connect = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null) {
            NetworkInfo[] information = connect.getAllNetworkInfo();
            if (information != null) {
                for (int x = 0; x < information.length; x++) {
                    if (information[x].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void initCall() {
        //在前台了，发送广播 调起权限判断弹窗
        Intent viop = new Intent();
        Intent intent = getIntent();
        viop.putExtra("room", intent.getStringExtra("room"));
        viop.putExtra("audioOnly", intent.getBooleanExtra("audioOnly", false));
        viop.putExtra("inviteId", intent.getStringExtra("inviteId"));
        viop.putExtra("inviteUserName", intent.getStringExtra("inviteUserName"));
//        viop.putExtra("msgId", intent.getLongExtra("msgId", 0));
        viop.putExtra("userList", intent.getStringExtra("userList"));
        viop.setAction(Utils.ACTION_VOIP_RECEIVER);
        viop.setComponent(new ComponentName(G.getInstance().getPackageName(), VoipReceiver.class.getName()));
        sendBroadcast(viop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject disconnect = new JSONObject();
        try {
            disconnect.put("from", userid);
            disconnect.put("vu", vu);
            disconnect.put("message", "Last Seen Recently");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("disconnec", disconnect);
        // Handle application closing
        socket.disconnect();
    }
//
//    private Socket socket;
//
//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
////        CalllogFragment settingfragment = (CalllogFragment) getSupportFragmentManager().findFragmentByTag("test");
////        settingfragment.onActivityResult(requestCode, resultCode, data);
//    }

//    private class LoginEditTextWatcher implements TextWatcher {
//        private EditText editText;
//
//        private LoginEditTextWatcher(EditText editText) {
//            this.editText = editText;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            editText.setError(null);
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//
//        }
//    }
//    private void initUI() {
//        setActionBarTitle(R.string.title_login_activity);
//        userLoginEditText = findViewById(R.id.user_login);
//        userLoginEditText.addTextChangedListener(new LoginEditTextWatcher(userLoginEditText));

//        userFullNameEditText = findViewById(R.id.user_full_name);
//        userFullNameEditText.addTextChangedListener(new LoginEditTextWatcher(userFullNameEditText));
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_login, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.menu_login_user_done:
//                if (ValidationUtils.isLoginValid(this, userLoginEditText) &&
//                        ValidationUtils.isFoolNameValid(this, userFullNameEditText)) {
//                    hideKeyboard();
//                    userForSave = createUserWithEnteredData();
//                    startSignUpNewUser(userForSave);
//                }
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

    //    private void hideKeyboard() {
//        KeyboardUtils.hideKeyboard(userLoginEditText);
//        KeyboardUtils.hideKeyboard(userFullNameEditText);
//    }
    @Override
    public void userLogin() {
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
    }

//    @Override
//    public void userLogout() {
//
//    }

    private void creatnotification(Context context) {
        int notifyId = 1001;
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifyId, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.e("ejra", "notify");

        NotificationCompat.Builder notify = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle("New Message")
                .setContentText("New Missed Call")
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notifyId, notify.build());

    }

}
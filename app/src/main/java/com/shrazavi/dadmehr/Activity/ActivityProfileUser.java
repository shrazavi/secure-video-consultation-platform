package com.shrazavi.dadmehr.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.voip.CallSingleActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfileUser extends AppCompatActivity {
    ImageView imgprof;
    CardView crdprof;
    String imgurl = "";
    String userid;
    String btn;
    String content;
    String enk;
    String vu;
    SecretKeySpec Key;
    int income = 0;
    int account = 0;
    int price = 0;
    TextView txtname, txtprof, txtemail, txtphone, txtswich, txtsickness;
    //    public SharedPreferences preferences;
    Retrofitinformation RI;
    Switch swtime;
    Button btnchat, btnvoise, btnvideo;
    private static final int PER_PAGE_SIZE_100 = 100;
    private static final String ORDER_RULE = "order";
    private static final String ORDER_DESC_UPDATED = "desc date updated_at";
    public static final String TOTAL_PAGES_BUNDLE_PARAM = "total_pages";
    private Boolean hasNextPage = true;
    //    private Socket socket;
    private int currentPage = 0;
    int time = 3610000;
    String timer = "3610000";
    String myid, name, room;
    int access = 0;
    private Boolean isLoading = false;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
//        socket.connect();
        txtname = (TextView) findViewById(R.id.txt_prof_user_name);
        txtemail = (TextView) findViewById(R.id.txt_prof_user_email);
        txtphone = (TextView) findViewById(R.id.txt_prof_user_phone);
        txtprof = (TextView) findViewById(R.id.app_bar_txt_prof_user);
        txtswich = (TextView) findViewById(R.id.txt_prof_user_switch);
        imgprof = (ImageView) findViewById(R.id.app_bar_image_prof_user);
        crdprof = (CardView) findViewById(R.id.app_bar_crd_prof_user);
        swtime = (Switch) findViewById(R.id.sw_prof_user);

        userid = (String) getIntent().getExtras().get("username");
        room = (String) getIntent().getExtras().get("room");
        btnchat = (Button) findViewById(R.id.btn_prof_user_chat);
        btnvoise = (Button) findViewById(R.id.btn_prof_user_voise);
        btnvideo = (Button) findViewById(R.id.btn_prof_user_video);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        myid = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
        content = BasicActivity.content;

//        Log.e("userid", userid);
//        Log.e("my", myid);
        swtime.setChecked(false);
        txtswich.setText("کاربر فقط در صورت پرداخت هزینه مجاز به ارسال پیام میباشد.");
        Call<MessageSignup> callaccess = RI.getaccesschat(content, room, myid);
        callaccess.enqueue(new Callback<MessageSignup>() {
            @Override
            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                if (response.body().getStatus()) {
                    access=1;
                    swtime.setChecked(true);
                    txtswich.setText("کاریر مجاز به ارسال پیام میباشد.");
                } else {
                    access=0;
                    swtime.setChecked(false);
                    txtswich.setText("کاربر فقط در صورت پرداخت هزینه مجاز به ارسال پیام میباشد.");
                }

            }

            @Override
            public void onFailure(Call<MessageSignup> call, Throwable t) {
                Log.e("accesschat", "" + t);
            }
        });
        swtime.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked) {
                    access = 1;
                    txtswich.setText("کاریر مجاز به ارسال پیام میباشد.");
                    JSONObject accesschat = new JSONObject();
                    try {
                        accesschat.put("from", myid);
                        accesschat.put("to", userid);
                        accesschat.put("room", room);
                        accesschat.put("access", true);
                        accesschat.put("content", content);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BasicActivity.socket.emit("accesschat", accesschat);
                } else {
                    access = 0;
                    txtswich.setText("کاربر فقط در صورت پرداخت هزینه مجاز به ارسال پیام میباشد.");
                    JSONObject accesschat = new JSONObject();
                    try {
                        accesschat.put("from", myid);
                        accesschat.put("to", userid);
                        accesschat.put("room", room);
                        accesschat.put("access", false);
                        accesschat.put("content", content);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    BasicActivity.socket.emit("accesschat", accesschat);

                }
            }
        });
        txtphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 22) {

                    if (ActivityCompat.checkSelfPermission(ActivityProfileUser.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(ActivityProfileUser.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                        return;
                    }
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + txtphone.getText().toString().trim()));
                    startActivity(callIntent);
                } else {

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:+" + txtphone.getText().toString().trim()));
                    startActivity(callIntent);
                }
            }
        });
        txtphone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("phone", txtphone.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ActivityProfileUser.this, "کپی شد", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        txtemail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("email", txtemail.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ActivityProfileUser.this, "کپی شد", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        imgprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imgurl.isEmpty()) {
                } else {
                    Intent intent = new Intent(ActivityProfileUser.this, ActivityImageView.class);
                    intent.putExtra("imgurl", imgurl);
                    intent.putExtra("down", false);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ActivityProfileUser.this.startActivity(intent);
                }
            }
        });
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        Call<User> calluser = RI.getuser(content, userid, myid);
        calluser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                txtname.setText(response.body().getName());
                txtemail.setText(response.body().getEmail());
                txtphone.setText(response.body().getNumber());

                Log.e("userprofile", G.nodeurl + response.body().getProfile());
                if (response.body().getProfile().equals("empty")) {
                    char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
//                  imgProf.setVisibility(View.GONE);
//                   holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);

                    txtprof.setText(ch1 + "");
                    crdprof.setCardBackgroundColor(color);
                } else {
                    imgurl = G.nodeurl + response.body().getProfile();
//                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).into(imgprof);
                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(imgprof);
                }
                account = Integer.parseInt(response.body().getAccount());
                name = response.body().getName();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("userprofile", "" + t);
            }
        });
        btnchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityProfileUser.this, ActivityChat.class);
                intent.putExtra("timer", time);
                intent.putExtra("id", userid);
                intent.putExtra("imgurl", imgurl);
                intent.putExtra("roomid", room);
                ActivityProfileUser.this.startActivity(intent);
                ActivityProfileUser.this.finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        btnvoise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CallSingleActivity.openActivity(ActivityProfileUser.this, userid, userid, "60e57dcab921c43ac41cf1ab", myid, myid, true, true, false, time + "");
                BasicActivity.callcondition = "start";
//           /
            }
        });
        btnvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("userid", userid);
                Log.e("my", myid);
//                CallSingleActivity.openActivity(ActivityProfileVl.this, toid, toid,      room, userid, userid, true, false, false, timer + "");

                CallSingleActivity.openActivity(ActivityProfileUser.this, userid, userid, "60e57dcab921c43ac41cf1ab", myid, myid, true, false, false, time + "");
                BasicActivity.callcondition = "start";
//                BasicActivity.singelton=0;
            }
        });


        Call<Vakil> callvakil = RI.getvakil(content, myid, myid);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                    G.income = Integer.parseInt(response.body().getIncome());
                income = Integer.parseInt(response.body().getIncome());

            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


}

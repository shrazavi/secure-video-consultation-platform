package com.shrazavi.dadmehr.Activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.nkzawa.emitter.Emitter;
import com.google.android.material.textfield.TextInputLayout;
import com.shrazavi.dadmehr.ActionBottomDialogFragment;
import com.shrazavi.dadmehr.Adapter.RecyclerAdapterChats;
import com.shrazavi.dadmehr.CustomEditText;
import com.shrazavi.dadmehr.DataClass.ChatsText;
import com.shrazavi.dadmehr.DataClass.Key;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageBase64;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.Utilities;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.DrawableClickListener;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import saman.zamani.persiandate.PersianDate;

public class ActivityTicketMessage extends AppCompatActivity implements ActionBottomDialogFragment.ItemClickListener {
    public static final int PICKFILE_RESULT_CODE = 1;
    MaterialProgressBar prgvideo;
    public String vi;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    private static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
    int isvoise = 0;
    int timer = 0;
    private int currentFormat = 0;
    private static final String AUDIO_RECORDER_FOLDER = "Mava/Recorder";
//    public static SharedPreferences preferences;
    static Handler threadHandler;
    public int REQUEST_OPEN_GALLERY = 1;
    Thread thread;

    String content;
    String enk;
    SecretKeySpec Key;
    static String toid = "";
    static String room = "";
    String vu = "";
    String imgurl = "";
    public static SeekBar seekvoise;
    static String myId = "";
    JSONObject stopTyping;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerViewChats;
    ArrayList<ChatsText> chatsTexts;
    Retrofitinformation RI;
    RecyclerAdapterChats recyclerAdapterChats;
    String encodedImage = "";
    long totalDuration = 0;
    long currentDuration = 0;
    public static TextView txttimerseek, txttimerchat, txttimerrec, txtUsername, txtprof, txtIsTyping;
    private boolean isRecording;
    public static MediaPlayer mediaPlayer;
    private float counter = 0;
    int length;
    Utilities utils;
    private MediaRecorder recorder;
    public static String lastFilename;
    public static String objectid = "";
    public static Boolean isPlaying;
    public static Boolean edit = false;
    public static Boolean ispusing;
    public static Button btnSend, btnVoice, btnStop, btnPlay, btnDeletRec, btnTime, btnPuse, btnscroll, btnsearch, btnmore, btnok;
    public static CustomEditText edtTextMessage;
    int search = 0;
    LinearLayout linearMessage;
    public static LinearLayout layrecord, laychat, laytoolbar;
    Handler mHandler = new Handler();
    CardView crdprof;
    ScrollView scrollview;
    ImageView imgProfilePrivateChat;
    SecretKeySpec secretKey;
    public static AutoCompleteTextView edtsearch;
    public static TextInputLayout laysearch;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_option, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JSONObject disconnect = new JSONObject();
        try {
            disconnect.put("from", myId);
            disconnect.put("to", toid);
            disconnect.put("room", room);
            disconnect.put("vu", vu);
            disconnect.put("message", "آخرین بازدید به تازگی");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        BasicActivity.socket.emit("disconnec", disconnect);
        BasicActivity.socket.emit("ticketleave", room);
        BasicActivity.socket.off("tickticket", handlerTickMessage);
        BasicActivity.socket.off("ticketmessage", handlerIncomingMessage);
        BasicActivity.socket.off("typing", handlerTyping);
        BasicActivity.socket.off("stoptyping", handlerStopTyping);
        BasicActivity.socket.off("getallticket", handlerAllChats);
        BasicActivity.socket.off("disconnec", handlerDisconnect);
        BasicActivity.socket.off("ticketjoin", handlerOnline);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        chatsTexts = new ArrayList<>();
        recyclerAdapterChats = new RecyclerAdapterChats(ActivityTicketMessage.this, chatsTexts, "ticket");
        threadHandler = new Handler();
        linearLayoutManager = new LinearLayoutManager(G.context);
        utils = new Utilities();
        mediaPlayer = new MediaPlayer();
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        scrollview = findViewById(R.id.scroll);
        edtTextMessage = findViewById(R.id.edtTextMessage);
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        seekvoise = findViewById(R.id.seekvoise);
        prgvideo = findViewById(R.id.prgvideo);
        btnsearch = findViewById(R.id.btn_search_chat);
        btnmore = findViewById(R.id.btn_more_chat);
        btnStop = findViewById(R.id.btn_stop_record);
        btnok = findViewById(R.id.btnok);
        btnTime = findViewById(R.id.btn_timer);
        btnPlay = findViewById(R.id.btn_play_record);
        btnPuse = findViewById(R.id.btn_puse);
        btnDeletRec = findViewById(R.id.btn_delet_record);
        btnscroll = findViewById(R.id.btn_scroll);
        btnSend = findViewById(R.id.btnSend);
        btnVoice = findViewById(R.id.btnVoice);
        txttimerchat = findViewById(R.id.txt_timer_toolbar);
        txttimerseek = findViewById(R.id.txt_timer_seek);
        txttimerrec = findViewById(R.id.txt_timer_record);
        txtUsername = findViewById(R.id.txtUsername);
        txtprof = findViewById(R.id.txtProfilePrivateChat);
        txtIsTyping = findViewById(R.id.txtIsTyping);
        linearMessage = findViewById(R.id.linearMessage1);
        layrecord = findViewById(R.id.lay_record);
        laytoolbar = findViewById(R.id.lay_toolbar_chat);
        laychat = findViewById(R.id.lay_chat);
        imgProfilePrivateChat = findViewById(R.id.imgProfilePrivateChat);
        crdprof = findViewById(R.id.crdProfilePrivateChat);
        laysearch = (TextInputLayout) findViewById(R.id.lay_edt_search_chat);
        edtsearch = (AutoCompleteTextView) findViewById(R.id.edt_search_chat);

        laysearch.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        laysearch.setBoxStrokeColor(getResources().getColor(R.color.white));
        edtsearch.setVisibility(View.GONE);
        laysearch.setVisibility(View.GONE);
        edtsearch.setThreshold(1);
        edtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int posi = getItemPosition(edtsearch.getText().toString());
                if (posi >= 0) {
                    recyclerViewChats.scrollToPosition(posi);
                }
            }
        });
        content=BasicActivity.content;
        toid = (String) getIntent().getExtras().get("id");
        room = (String) getIntent().getExtras().get("roomid");
        timer = (Integer) getIntent().getExtras().get("timer");
        JSONObject join = new JSONObject();
        try {
            join.put("ticket", room);
            join.put("username", myId);
            join.put("type", "user");
            join.put("content", content);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        BasicActivity.socket.emit("ticketjoin", join);
        ispusing = false;
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);

        myId = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myId + "-" + getResources().getString(R.string.developer), enk);



        btnok.setVisibility(View.GONE);
        btnSend.setVisibility(View.GONE);
        btnVoice.setVisibility(View.VISIBLE);
        layrecord.setVisibility(View.GONE);
        edtTextMessage.setVisibility(View.VISIBLE);
        txttimerrec.setVisibility(View.GONE);
        txttimerseek.setVisibility(View.GONE);
        seekvoise.setVisibility(View.GONE);
        btnscroll.setVisibility(View.GONE);
        laychat.setVisibility(View.VISIBLE);
        Picasso.with(G.context).load(G.nodeurl + "/upload/profile/support.png").transform(new ImageProfile()).into(imgProfilePrivateChat);
        txtUsername.setText("پشتیبان");
//        if (vu.equals("vl")) {
//            txttimerchat.setVisibility(View.GONE);
//            Call<User> calluser = RI.getuser(content, toid, myId);
//            calluser.enqueue(new Callback<User>() {
//                @Override
//                public void onResponse(Call<User> call, Response<User> response) {
//                    imgurl = G.phpurl + "/" + response.body().getProfile();
//                    txtIsTyping.setText(response.body().getStatus() + "");
//                    txtUsername.setText(response.body().getName());
//                    Log.e("status", response.body().getStatus() + "");
//                    if (!response.body().getProfile().equals("empty")) {
//                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(imgProfilePrivateChat);
//                    } else {
//                        char ch1 = response.body().getUsername().toUpperCase().charAt(0);
//                        Random rnd = new Random();
//                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                        txtprof.setText(ch1 + "");
//                        crdprof.setCardBackgroundColor(color);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<User> call, Throwable t) {
//
//                }
//            });
//        } else {
//            txttimerchat.setVisibility(View.VISIBLE);
//            Call<Vakil> callvakil = RI.getvakil(content, toid, myId);
//            callvakil.enqueue(new Callback<Vakil>() {
//                @Override
//                public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                    txtIsTyping.setText(response.body().getStatus() + "");
//                    txtUsername.setText(response.body().getName());
//                    String blockusers = response.body().getBlockusers();
//                    String[] arrblock = blockusers.split(",");
//                    if (Arrays.asList(arrblock).contains(myId)) {
//                        Toast.makeText(G.context, "شما مسدود شدید", Toast.LENGTH_SHORT).show();
//                    } else {
//                    }
//                    Log.e("status", response.body().getStatus() + "");
//                    imgurl = G.nodeurl + response.body().getProfile();
//                    if (!response.body().getProfile().equals("empty")) {
//
//                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(imgProfilePrivateChat);
//                        Log.e("imgurl", imgurl);
//                    } else {
//
//                        char ch1 = response.body().getUsername().toUpperCase().charAt(0);
//                        Random rnd = new Random();
//                        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
//                        txtprof.setText(ch1 + "");
//                        crdprof.setCardBackgroundColor(color);
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Vakil> call, Throwable t) {
//
//                }
//            });
//
//
//        }
        recyclerViewChats.setHasFixedSize(true);
        recyclerViewChats.setLayoutManager(linearLayoutManager);

//        new CountDownTimer(timer, 1000) {
//            public void onTick(long millisUntilFinished) {
////                timerValue.setText(millisUntilFinished/1000+"");
//                txttimerchat.setText(utils.milli_to_time(millisUntilFinished));
//            }
//
//            public void onFinish() {
////                timerValue.setText("ارسال مجدد کد");
//                txttimerchat.setVisibility(View.GONE);
//                btnSend.setVisibility(View.GONE);
//                btnVoice.setVisibility(View.GONE);
//                edtTextMessage.setVisibility(View.GONE);
//                timer = 1;
//            }
//        }.start();
        Log.e("room", room);
        Call<Key> callkey = RI.getkey(content, room, myId);
        callkey.enqueue(new Callback<Key>() {
            @Override
            public void onResponse(Call<Key> call, Response<Key> response) {
                secretKey = null;
//                Log.e("key", response.body().getKey());
                if (response.body().getKey() != null) {
                    byte[] data = Base64.decode(response.body().getKey(), Base64.DEFAULT);

                Log.e("keylength", data.length + "");
                secretKey = new SecretKeySpec(data, 0, data.length, "AES");
                JSONObject postDataForGetAllMessage = new JSONObject();
                try {
                    postDataForGetAllMessage.put("from", myId);
                    postDataForGetAllMessage.put("ticket", room);
                    postDataForGetAllMessage.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BasicActivity.socket.emit("getallticket", postDataForGetAllMessage);
                BasicActivity.socket.on("getallticket", handlerAllChats);
            }
            }

            @Override
            public void onFailure(Call<Key> call, Throwable t) {
                Log.e("errorkey", t + "");
            }
        });


        BasicActivity.socket.on("tickticket", handlerTickMessage);
        BasicActivity.socket.on("ticketmessage", handlerIncomingMessage);
        BasicActivity.socket.on("typing", handlerTyping);
        BasicActivity.socket.on("stoptyping", handlerStopTyping);
        BasicActivity.socket.on("disconnec", handlerDisconnect);
        BasicActivity.socket.on("ticketjoin", handlerOnline);
//        JSONObject online = new JSONObject();
//        try {
//            Log.e("toIdon", toid);
//            online.put("from", myId);
//            online.put("to", toid);
//            online.put("room", room);
//            online.put("vu", vu);
//            online.put("message", "آنلاین");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        JSONObject postDataForTickMessage = new JSONObject();
        try {
            postDataForTickMessage.put("from", myId);
            postDataForTickMessage.put("type", "user");
            postDataForTickMessage.put("ticket", room);
            postDataForTickMessage.put("vu", vu);
            postDataForTickMessage.put("content", content);


        } catch (JSONException e) {
            e.printStackTrace();
        }


//        G.getInstance();
//        BasicActivity.socket.emit("nickname", nickname);

        BasicActivity.socket.emit("readticket", postDataForTickMessage);
//        BasicActivity.socket.emit("online", online);


        recyclerViewChats.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//


            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                btnscroll.setVisibility(View.VISIBLE);
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();
                int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    //End of list
                    btnscroll.setVisibility(View.GONE);
                }
            }
        });

        seekvoise.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {


            @Override
            public void onProgressChanged(final SeekBar seekBar, int progress, boolean fromUser) {

//
            }


            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdate);

                int totalDuration = mediaPlayer.getDuration();
                int currentDuration = utils.progressToTime(seekBar.getProgress(), totalDuration);

                mediaPlayer.seekTo(currentDuration);

                updateProgress();


            }
        });
        laytoolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
//
                                if (vu.equals("user")) {

                                    Intent intent = new Intent(ActivityTicketMessage.this, ActivityProfileVl.class);
//                                    intent.putExtra("imgurl", imgurl);
                                    intent.putExtra("username", toid);
                                    ActivityTicketMessage.this.startActivity(intent);
                                    ActivityTicketMessage.this.finish();

//                                    android.os.Process.killProcess(android.os.Process.myPid());
                                } else {
                                    Intent intent = new Intent(ActivityTicketMessage.this, ActivityProfileUser.class);
//                                    intent.putExtra("imgurl", imgurl);
                                    intent.putExtra("username", toid);
                                    intent.putExtra("room", room);
                                    intent.putExtra("btn", "1");
                                    ActivityTicketMessage.this.startActivity(intent);
                                    ActivityTicketMessage.this.finish();
//                                    android.os.Process.killProcess(android.os.Process.myPid());

                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityTicketMessage.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                btnPlay.setVisibility(View.VISIBLE);
                btnDeletRec.setVisibility(View.VISIBLE);
                btnPuse.setVisibility(View.GONE);
                seekvoise.setVisibility(View.GONE);
                mediaPlayer.reset();
                isPlaying = false;
                ispusing = false;
                txttimerseek.setVisibility(View.GONE);
            }
        });
        btnDeletRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSend.setVisibility(View.GONE);
                btnVoice.setVisibility(View.VISIBLE);
                edtTextMessage.setVisibility(View.VISIBLE);
                layrecord.setVisibility(View.GONE);
                btnDeletRec.setVisibility(View.GONE);
                seekvoise.setVisibility(View.GONE);
                mediaPlayer.reset();
                txttimerrec.setVisibility(View.GONE);
                btnVoice.setVisibility(View.VISIBLE);
                isvoise = 0;
                new File(lastFilename).delete();
            }
        });
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });
        btnmore.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
//                if (vu.equals("vl")) {
//                    Call<Vakil> callblock = RI.getvakil(content, myId, myId);
//                    callblock.enqueue(new Callback<Vakil>() {
//                        @Override
//                        public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                            String blockusers = response.body().getBlockusers();
//                            String[] arrblock = blockusers.split(",");
////                            Log.e("","");
//                            if (Arrays.asList(arrblock).contains(toid)) {
//                                PopupMenu popup = new PopupMenu(ActivityTicketMessage.this, view);
//                                popup.getMenuInflater().inflate(R.menu.pop_up_unblock, popup.getMenu());
//                                try {
//                                    Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
//                                    mFieldPopup.setAccessible(true);
//                                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
//                                    mPopup.setForceShowIcon(true);
//                                } catch (Exception e) {
//                                }
//                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                    public boolean onMenuItemClick(MenuItem item) {
//                                        switch (item.getItemId()) {
//                                            case R.id.unblock:
//                                                //handle menu1 click
//                                                Call<Vakil> callvakil = RI.getvakil(content, myId, myId);
//                                                callvakil.enqueue(new Callback<Vakil>() {
//                                                    @Override
//                                                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                                                        String blockuser = response.body().getBlockusers();
//                                                        StringBuilder result = new StringBuilder();
//
//                                                        String[] arrblock = blockuser.split(",");
//                                                        ArrayList<String> blackList = new ArrayList<String>(Arrays.asList(arrblock));
//                                                        blackList.remove(toid);
//                                                        for (int i = 0; i < blackList.size(); i++) {
//                                                            result.append(blackList.get(i));
//                                                            result.append(",");
//                                                        }
//
//
//                                                        Call<MessageSignup> callvakil = RI.upgradeblack(content,
//                                                                result.toString(),
//                                                                myId);
//                                                        callvakil.enqueue(new Callback<MessageSignup>() {
//                                                            @Override
//                                                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
//                                                                Boolean status = response.body().getStatus();
//                                                                if (status) {
//                                                                    Toast.makeText(G.context, "مسدودی کاربر رفع شد", Toast.LENGTH_SHORT).show();
//
//                                                                } else {
//
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(Call<MessageSignup> call, Throwable t) {
//                                                                Log.e("error???", t + "");
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<Vakil> call, Throwable t) {
//                                                        Log.e("error???", t + "");
//                                                    }
//                                                });
//
//                                                break;
//                                        }
//                                        return true;
//                                    }
//                                });
//                                popup.show();
//                            } else {
//                                PopupMenu popup = new PopupMenu(ActivityTicketMessage.this, view);
//                                popup.getMenuInflater().inflate(R.menu.pop_up_block, popup.getMenu());
//                                try {
//                                    Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
//                                    mFieldPopup.setAccessible(true);
//                                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
//                                    mPopup.setForceShowIcon(true);
//                                } catch (Exception e) {
//                                }
//                                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                    public boolean onMenuItemClick(MenuItem item) {
//                                        switch (item.getItemId()) {
//                                            case R.id.block:
//
//                                                Call<Vakil> callvakil = RI.getvakil(content, myId, myId);
//                                                callvakil.enqueue(new Callback<Vakil>() {
//                                                    @Override
//                                                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                                                        String blockuser = response.body().getBlockusers();
//                                                        StringBuilder result = new StringBuilder();
//                                                        if (blockuser.isEmpty()) {
//                                                            result.append(toid);
//                                                            result.append(",");
//                                                        } else {
//                                                            String[] arrblock = blockuser.split(",");
//                                                            ArrayList<String> blackList = new ArrayList<String>(Arrays.asList(arrblock));
//                                                            blackList.add(toid);
//                                                            for (int i = 0; i < blackList.size(); i++) {
//                                                                result.append(blackList.get(i));
//                                                                result.append(",");
//                                                            }
//                                                        }
//
//                                                        Call<MessageSignup> callvakil = RI.upgradeblack(content,
//                                                                result.toString(),
//                                                                myId);
//                                                        callvakil.enqueue(new Callback<MessageSignup>() {
//                                                            @Override
//                                                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
//                                                                Boolean status = response.body().getStatus();
//                                                                if (status) {
//                                                                    Toast.makeText(G.context, "کاربر مسدود شد", Toast.LENGTH_SHORT).show();
//                                                                } else {
//
//                                                                }
//                                                            }
//
//                                                            @Override
//                                                            public void onFailure(Call<MessageSignup> call, Throwable t) {
//                                                                Log.e("error???", t + "");
//                                                            }
//                                                        });
//                                                    }
//
//                                                    @Override
//                                                    public void onFailure(Call<Vakil> call, Throwable t) {
//                                                        Log.e("error???", t + "");
//                                                    }
//                                                });
//
//                                                break;
//
//                                        }
//                                        return true;
//                                    }
//                                });
//                                popup.show();
//
//                            }
//
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Vakil> call, Throwable t) {
//                            Log.e("error???", t + "");
//                        }
//                    });
//
//
//                } else {
//
//
//                }
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtTextMessage.getText().toString();
                sendMessage(message);
                edtTextMessage.setVisibility(View.VISIBLE);
                layrecord.setVisibility(View.GONE);
                seekvoise.setVisibility(View.GONE);
                mediaPlayer.reset();
                txttimerrec.setVisibility(View.GONE);
                btnVoice.setVisibility(View.VISIBLE);
                isvoise = 0;
            }
        });
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = edtTextMessage.getText().toString();
                if (!message.isEmpty()) {
                    editMessage(message, objectid);
                }
                edtTextMessage.setVisibility(View.VISIBLE);
                layrecord.setVisibility(View.GONE);
                seekvoise.setVisibility(View.GONE);
                mediaPlayer.reset();
                txttimerrec.setVisibility(View.GONE);
                edit = false;
                edtTextMessage.setText("");
                btnok.setVisibility(View.GONE);
                btnVoice.setVisibility(View.VISIBLE);
                isvoise = 0;
            }
        });
        btnVoice.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                edtTextMessage.setVisibility(View.GONE);
                txttimerseek.setVisibility(View.GONE);
                txttimerrec.setVisibility(View.VISIBLE);
                seekvoise.setVisibility(View.GONE);
                layrecord.setVisibility(View.VISIBLE);
                btnPuse.setVisibility(View.GONE);
                btnDeletRec.setVisibility(View.GONE);
                btnPlay.setVisibility(View.GONE);
                toggleRecording();
                isPlaying = false;
            }
        });
        btnPuse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pausePlaying();
                btnPuse.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
                ispusing = true;
                isPlaying = false;
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePlaying();
                btnTime.setText("");
                btnPlay.setVisibility(View.GONE);
                btnDeletRec.setVisibility(View.GONE);
                btnPuse.setVisibility(View.VISIBLE);
                txttimerseek.setVisibility(View.VISIBLE);
                seekvoise.setVisibility(View.VISIBLE);
                txttimerrec.setVisibility(View.GONE);
                isPlaying = true;
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecording) {
                    toggleRecording();
                    btnSend.setVisibility(View.VISIBLE);
                    btnPlay.setVisibility(View.VISIBLE);
                    btnDeletRec.setVisibility(View.VISIBLE);
                    btnStop.setVisibility(View.GONE);
                } else {
                    isPlaying = true;
                    togglePlaying();
                    Log.e("timer", "" + timer);
                    if (timer == 1) {
                        btnVoice.setVisibility(View.GONE);
                        btnStop.setVisibility(View.GONE);
                        txttimerseek.setVisibility(View.GONE);
                        seekvoise.setVisibility(View.GONE);
                        layrecord.setVisibility(View.GONE);
                        edtTextMessage.setVisibility(View.GONE);
                    } else {
                        btnVoice.setVisibility(View.VISIBLE);
                        btnStop.setVisibility(View.GONE);
                        txttimerseek.setVisibility(View.GONE);
                        seekvoise.setVisibility(View.GONE);
                        layrecord.setVisibility(View.GONE);
                        edtTextMessage.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
        btnscroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToBottom(recyclerViewChats);
                btnscroll.setVisibility(View.GONE);
            }
        });


        edtTextMessage.setDrawableClickListener(new DrawableClickListener() {


            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        showBottomSheet();
                        break;
                }
            }

        });
        edtTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 0) {
                    if (edit) {
                        btnSend.setVisibility(View.GONE);
                        btnVoice.setVisibility(View.GONE);
                        btnok.setVisibility(View.VISIBLE);
                    } else {
                        btnSend.setVisibility(View.GONE);
                        btnVoice.setVisibility(View.VISIBLE);
                        btnok.setVisibility(View.GONE);
                    }
                } else {
                    if (edit) {
                        btnok.setVisibility(View.VISIBLE);
                        btnSend.setVisibility(View.GONE);
                        btnVoice.setVisibility(View.GONE);
                    } else {
                        btnSend.setVisibility(View.VISIBLE);
                        btnVoice.setVisibility(View.GONE);
                        btnok.setVisibility(View.GONE);
                    }
                }

                JSONObject typing = new JSONObject();
                try {
                    typing.put("from", myId);
                    typing.put("to", toid);
                    typing.put("room", room);
                    typing.put("message", "در حال نوشتن ...");
                    typing.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BasicActivity.socket.emit("typing", typing);


            }

            @Override
            public void afterTextChanged(Editable s) {


                stopTyping = new JSONObject();
                try {
                    stopTyping.put("from", myId);
                    stopTyping.put("to", toid);
                    stopTyping.put("room", room);
                    stopTyping.put("message", "");
                    stopTyping.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Thread.sleep(1500);
                            ActivityTicketMessage.threadHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    BasicActivity.socket.emit("stoptyping", stopTyping);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                });

                thread.start();

            }
        });

    }


    Runnable mUpdate = new Runnable() {
        @Override
        public void run() {

            try {
                utils = new Utilities();
                long totalDuration = mediaPlayer.getDuration();
                long currentDuration = mediaPlayer.getCurrentPosition();
                txttimerseek.setText(utils.milli_to_time(currentDuration));
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                seekvoise.setProgress(progress);
                mHandler.postDelayed(this, 100);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }


    };


    @Override
    protected void onResume() {
        super.onResume();
        JSONObject online = new JSONObject();
        try {
            online.put("from", myId);
            online.put("to", toid);
            online.put("room", room);
            online.put("message", "آنلاین");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Emitter.Listener handlerOnline = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String typing = "";
                    String roo = "";
                    try {
                        roo = jsonObject.getString("ticket").toString();
                        if (roo.equals(room)) {
                            txtIsTyping.setText("آنلاین");
                        } else {
//                            txtIsTyping.setText("");
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerTyping = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String typing = "";
                    String roomtyping = "";
                    try {
                        typing = jsonObject.getString("message").toString();
                        roomtyping = jsonObject.getString("room").toString();
                        if (roomtyping.equals(room)) {
                            txtIsTyping.setText(typing);
                        } else {
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerDisconnect = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String stopTyping = "";
                    String roomstoptyping = "";
                    try {

                        stopTyping = jsonObject.getString("message").toString();
                        roomstoptyping = jsonObject.getString("room").toString();
                        if (roomstoptyping.equals(room)) {
                            txtIsTyping.setText(stopTyping);
                        } else {
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerStopTyping = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String stopTyping = "";
                    String room = "";
                    try {

                        stopTyping = jsonObject.getString("message").toString();
                        room = jsonObject.getString("room").toString();
//                        txtIsTyping.setText(stopTyping);
                        if (room.equals(room)) {
                            txtIsTyping.setText("آنلاین");
                        } else {
//                            txtIsTyping.setText("");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerIncomingMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
//                    Log.e("ejrashodam", "ok" + jsonObject);
                    String message = "";
                    String picurl = "";
                    String vidurl = "";
                    String pdfurl = "";
                    String fromuser = "";
                    String roommessage;
                    String voise = "";
                    String history = "";

                    try {
//                        Log.e("ejrashodam", "ok");
                        history = jsonObject.getString("history");
                        Log.e("historyres", history);
                        message = jsonObject.getString("text").toString();
                        roommessage = jsonObject.getString("ticket").toString();
                        fromuser = jsonObject.getString("from").toString();
//                        Log.e("from1", fromuser);
//                        Log.e("to1",touser);
                        Log.e("messageincom", message);
//                        byte[] encryptedMessage = Base64.decode(message, Base64.DEFAULT);
                        picurl = jsonObject.getString("picurl").toString();
                        vidurl = jsonObject.getString("vidurl").toString();
                        pdfurl = jsonObject.getString("pdfurl").toString();
                        voise = jsonObject.getString("voise").toString();
                        if (roommessage.equals(room)) {
                            if (fromuser.equals(myId)) {
//                                Log.e("from2", fromuser);
//                            Log.e("to2",touser);


                                prgvideo.setVisibility(View.GONE);
                                btnVoice.setVisibility(View.VISIBLE);
                                edtTextMessage.setVisibility(View.VISIBLE);
                                ChatsText chatsText = new ChatsText();
                                chatsText.setId(jsonObject.getString("_id"));
                                chatsText.setDate(jsonObject.getLong("date"));
                                chatsText.setDeleted(jsonObject.getBoolean("deleted"));
                                chatsText.setVidurl(vidurl);
                                chatsText.setPdf(pdfurl);
                                chatsText.setImgurl(picurl);
                                chatsText.setVoise(voise);
                                if (jsonObject.getString("pdfurl").isEmpty()) {
                                    if (message.isEmpty()) {
                                        chatsText.setText("");
                                    } else {

                                        byte[] encryptedMessage = Base64.decode(message + "", Base64.DEFAULT);
                                        chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                    }
                                } else {
                                    chatsText.setText(message);
                                }
//                                chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                chatsText.setFrom("me");
                                chatsText.setTime(currentTime());
                                chatsText.setRead(false);
                                chatsTexts.add(chatsText);
//                                ChatDatabase chatDatabase = new ChatDatabase(G.context);

//                                chatDatabase.addChat(fromuser, toid, toid + fromuser, currentTime(), jsonObject.getLong("date"), picurl, vidurl, voise, 0, 1, message);
                                recyclerAdapterChats.notifyDataSetChanged();
//                                Log.e("vidchatme", G.nodeurl + chatsText.vidurl);

                            } else {

                                JSONObject postDataForTickMessage = new JSONObject();
                                try {
                                    postDataForTickMessage.put("from", myId);
                                    postDataForTickMessage.put("type", "user");
                                    postDataForTickMessage.put("ticket", room);
                                    postDataForTickMessage.put("vu", vu);
                                    postDataForTickMessage.put("content", content);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

//                                Log.e("from3", fromuser);
//                            Log.e("to3",touser);

                                ChatsText chatsText = new ChatsText();
//                                Log.e("vidchatyou", G.nodeurl + chatsText.vidurl);
                                chatsText.setId(jsonObject.getString("_id"));
                                if (message.isEmpty()) {
                                    chatsText.setText("");
                                } else {

                                    byte[] encryptedMessage = Base64.decode(message + "", Base64.DEFAULT);
                                    chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                }
//                                chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                chatsText.setImgurl(picurl);
                                chatsText.setVidurl(vidurl);
                                chatsText.setDate(jsonObject.getLong("date"));
                                chatsText.setDeleted(jsonObject.getBoolean("deleted"));
                                chatsText.setVoise(voise);
                                chatsText.setFrom("you");
                                chatsText.setTime(currentTime());
                                chatsText.setRead(true);
                                chatsTexts.add(chatsText);
                                recyclerAdapterChats.notifyDataSetChanged();
                                BasicActivity.socket.emit("readticket", postDataForTickMessage);

                            }
                        }
                        recyclerViewChats.setAdapter(new RecyclerAdapterChats(ActivityTicketMessage.this, chatsTexts, "ticket"));
                        scrollToBottom(recyclerViewChats);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String encoded = "";
        if (requestCode == REQUEST_OPEN_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();


            if (vi.equals("video")) {
                String[] info = {MediaStore.Video.Media.DATA};
                Cursor cursor = getContentResolver().query(uri, info, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(info[0]);
                final String filePath = cursor.getString(columnIndex);
                InputStream inputStream = null;//You can get an inputStream using any IO API
                File file = new File(filePath);


                if (file.length() > 40000000) {
                    Toast.makeText(G.context, "حجم فیلم بیش از حد مجاز است", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        inputStream = new FileInputStream(filePath);


                        byte[] buffer = new byte[(int) file.length()];
//                        Log.e("Filesize", (int) file.length() + "");
                        int bytesRead;
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
                        prgvideo.setVisibility(View.VISIBLE);
                        btnVoice.setVisibility(View.GONE);
                        edtTextMessage.setVisibility(View.GONE);
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            output64.write(buffer, 0, bytesRead);

                        }

                        output64.close();
                        encoded = output.toString();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject postDatavid = new JSONObject();
                try {
                    postDatavid.put("from", myId);
                    postDatavid.put("to", toid);
                    postDatavid.put("pic", "");
                    postDatavid.put("room", room);
                    postDatavid.put("vid", encoded);
                    postDatavid.put("time", currentTime());
                    postDatavid.put("date", 0);
                    postDatavid.put("history", currenthistory());
                    postDatavid.put("vu", vu);
                    postDatavid.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BasicActivity.socket.emit("videoticket", postDatavid);

            } else if (vi.equals("pdf")) {


                final String filePath = data.getData().getPath();

                File file = new File(filePath);
//                File fl = new File(uri.toString());
//                path = fl.getAbsolutePath();
//                Log.e("file name", file.getName() + "");
                if (file.length() > 40000000) {
                    Toast.makeText(G.context, "حجم فایل بیش از حد مجاز است", Toast.LENGTH_SHORT).show();
                } else {
//                    Log.e("path", fl.getAbsolutePath() + "");
//                    Log.e("uri", uri + "");
//                    Log.e("uristring", uri.toString() + "");
//
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setDataAndType(Uri.parse("content://com.android.externalstorage.documents/document/primary%3AVakil%2FDocument%2FDocument2021_7_9_0ae50c15-895a-45b9-84a1-eda5dfc0c7ec.pdf"), "application/pdf");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//                    Intent intent1 = Intent.createChooser(intent, "Open With");
//                    try {
//                        startActivity(intent1);
//                    } catch (ActivityNotFoundException e) {
//                        // Instruct the user to install a PDF reader here, or something
//                    }
//                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +"/"+ filename);
//                    Intent target = new Intent(Intent.ACTION_VIEW);
//                    target.setDataAndType(uri,"application/pdf");
//                    target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//
//                    Intent intent = Intent.createChooser(target, "Open File");
//                    try {
//                        startActivity(intent);
//                    } catch (ActivityNotFoundException e) {
//                        // Instruct the user to install a PDF reader here, or something
//                    }


                    InputStream inputStream = null;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        inputStream = getContentResolver().openInputStream(uri);

                        byte[] buffer = new byte[1024];
                        byteArrayOutputStream = new ByteArrayOutputStream();

                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

                    encoded = Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
                    prgvideo.setVisibility(View.VISIBLE);
                    btnVoice.setVisibility(View.GONE);
                    edtTextMessage.setVisibility(View.GONE);

                }

                JSONObject postDatapdf = new JSONObject();
                try {
                    postDatapdf.put("from", myId);
                    postDatapdf.put("to", toid);
                    postDatapdf.put("pic", "");
                    postDatapdf.put("message", file.getName());

                    postDatapdf.put("room", room);
                    postDatapdf.put("pdf", encoded);
                    postDatapdf.put("time", currentTime());
                    postDatapdf.put("date", 0);
                    postDatapdf.put("history", currenthistory());
                    postDatapdf.put("vu", vu);
                    postDatapdf.put("content", content);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BasicActivity.socket.emit("pdfticket", postDatapdf);


            } else if (vi.equals("audio")) {

                final String filePath = data.getData().getPath();
                File file = new File(filePath);

                if (file.length() > 40000000) {
                    Toast.makeText(G.context, "حجم فایل بیش از حد مجاز است", Toast.LENGTH_SHORT).show();
                } else {


                    InputStream inputStream = null;
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    try {
                        inputStream = getContentResolver().openInputStream(uri);

                        byte[] buffer = new byte[1024];
                        byteArrayOutputStream = new ByteArrayOutputStream();

                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            byteArrayOutputStream.write(buffer, 0, bytesRead);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

                    encoded = Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
//                    Log.e("encoded", encoded+ "");

                    prgvideo.setVisibility(View.VISIBLE);
                    btnVoice.setVisibility(View.GONE);
                    edtTextMessage.setVisibility(View.GONE);

                }

                JSONObject postDataAudio = new JSONObject();
                try {
                    postDataAudio.put("from", myId);
                    postDataAudio.put("to", toid);
                    postDataAudio.put("pic", "");
                    postDataAudio.put("room", room);
                    postDataAudio.put("audio", encoded);
                    postDataAudio.put("time", currentTime());
                    postDataAudio.put("date", 0);
                    postDataAudio.put("history", currenthistory());
                    postDataAudio.put("vu", vu);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                BasicActivity.socket.emit("audioticket", postDataAudio);
            } else {
                try {
                    String[] info = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, info, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(info[0]);
                    final String filePath = cursor.getString(columnIndex);

                    encoded = ImageBase64
                            .with(getApplicationContext())
                            .requestSize(1024, 1024) // default size
                            .encodeFile(filePath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                JSONObject postDatapic = new JSONObject();
                try {
                    postDatapic.put("from", myId);
                    postDatapic.put("to", toid);
                    postDatapic.put("room", room);
                    postDatapic.put("pic", encoded);
                    postDatapic.put("vid", "");
                    postDatapic.put("time", currentTime());
                    postDatapic.put("date", 0);
                    postDatapic.put("history", currenthistory());
                    postDatapic.put("vu", vu);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                BasicActivity.socket.emit("pictureticket", postDatapic);

            }


        }

    }

    private void editMessage(String message, String id) {
        String encryptedMessage = Base64.encodeToString(SymmetricAlgorithmAES.encryption(secretKey, message), Base64.DEFAULT);
        JSONObject editchat = new JSONObject();
        try {
            editchat.put("from", myId);
            editchat.put("to", toid);
            editchat.put("id", id);
            editchat.put("room", room);

            editchat.put("message", encryptedMessage);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BasicActivity.socket.emit("editticket", editchat);

    }

    private void sendMessage(String message) {

        if (isvoise == 0) {


            String encryptedMessage = Base64.encodeToString(SymmetricAlgorithmAES.encryption(secretKey, message), Base64.DEFAULT);
            JSONObject postchat = new JSONObject();
            try {
                postchat.put("from", myId);
//                postchat.put("to", toid);
                postchat.put("pic", "");
                postchat.put("ticket", room);
                postchat.put("message", encryptedMessage);
                postchat.put("time", currentTime());
                postchat.put("date", 0);
                postchat.put("history", currenthistory());
                postchat.put("vu", vu);
                postchat.put("type", "user");
//                Log.e("historysend", currenthistory());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BasicActivity.socket.emit("ticketmessage", postchat);

        } else {
            String voiseencoded = "";
            InputStream inputStream = null;//You can get an inputStream using any IO API

            try {
                inputStream = new FileInputStream(lastFilename);

                File file = new File(lastFilename);

//                new byte[8192];
                byte[] buffer = new byte[(int) file.length()];
                int bytesRead;
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }

                output64.close();
                voiseencoded = output.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject postvoise = new JSONObject();
            try {
                postvoise.put("from", myId);
                postvoise.put("to", toid);
                postvoise.put("voise", voiseencoded);
                postvoise.put("ticket", room);
                postvoise.put("message", "");
                postvoise.put("time", currentTime());
                postvoise.put("date", 0);
                postvoise.put("history", currenthistory());
                postvoise.put("vu", vu);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            BasicActivity.socket.emit("voiseticket", postvoise);
            new File(lastFilename).delete();
        }
        edtTextMessage.setText("");
        btnSend.setVisibility(View.GONE);
        btnVoice.setVisibility(View.VISIBLE);


    }

    public Emitter.Listener handlerAllChats = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("key", secretKey + "");
                    JSONArray jsonArray = (JSONArray) args[0];
                    String message = "";
                    chatsTexts.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        message = "";
                        try {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ChatsText chatsText = new ChatsText();
//                            ChatDatabase chatDatabase = new ChatDatabase(G.context);

                            message = jsonObject.getString("text");
                            Log.e("message", message + "");

                            if (jsonObject.getString("pdfurl").isEmpty()) {
                                if (message.isEmpty()) {
                                    chatsText.setText("");
                                } else {

                                    byte[] encryptedMessage = Base64.decode(message + "", Base64.DEFAULT);
                                    chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                                }
                            } else {
                                chatsText.setText(message);
                            }


//                            Log.e("messageall",jsonObject.getString("text")+"");

                            chatsText.setId(jsonObject.getString("_id"));
                            chatsText.setImgurl(jsonObject.getString("picurl"));
                            chatsText.setVidurl(jsonObject.getString("vidurl"));
                            chatsText.setPdf(jsonObject.getString("pdfurl"));
                            chatsText.setAudio(jsonObject.getString("audio"));
                            chatsText.setVoise(jsonObject.getString("voise"));
//                            chatsText.setText(new String(SymmetricAlgorithmAES.decryption(secretKey, encryptedMessage)));
                            chatsText.setDate(jsonObject.getLong("date"));
                            chatsText.setDeleted(jsonObject.getBoolean("deleted"));
                            if (jsonObject.getString("from").equals(myId)) {
                                chatsText.setFrom("me");
                            } else {
                                chatsText.setFrom("you");
                            }
                            if (jsonObject.getString("read").equals("unread")) {
                                chatsText.setRead(false);
                            } else {
                                chatsText.setRead(true);
                            }
                            chatsText.setTime(jsonObject.getString("time"));

                            chatsTexts.add(chatsText);
//                            chatDatabase.addChat(jsonObject.getString("from"), jsonObject.getString("to"), jsonObject.getString("room"), jsonObject.getString("time"), jsonObject.getLong("date"), jsonObject.getString("picurl"), jsonObject.getString("vidurl"), chatsText.voise = jsonObject.getString("voise"), 1, 1, jsonObject.getString("text"));
                            recyclerAdapterChats.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    recyclerViewChats.setAdapter(new RecyclerAdapterChats(ActivityTicketMessage.this, chatsTexts, "ticket"));
                    scrollToBottom(recyclerViewChats);
//                    Log.i("GET", jsonArray.toString());

                }
            });
        }
    };

    public Emitter.Listener handlerTickMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            BasicActivity.handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    try {
                        String userName = jsonObject.getString("read");

                        ChatsText chatsText = new ChatsText();

                        for (int i = 0; i < chatsTexts.size(); i++) {
                            chatsText = chatsTexts.get(i);
                            chatsText.setRead(true);

                            recyclerAdapterChats.notifyDataSetChanged();


                        }

//                        ChatDatabase chatDatabase = new ChatDatabase(G.context);
//                        chatDatabase.updateread();

                        recyclerViewChats.setAdapter(new RecyclerAdapterChats(ActivityTicketMessage.this, chatsTexts, "ticket"));
                        scrollToBottom(recyclerViewChats);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    public String currentTime() {
        Calendar c = Calendar.getInstance();
        c.getTimeZone();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;


    }


    public String currenthistory() {
        Calendar c = Calendar.getInstance();
        c.getTimeZone();
//        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yyyy");

        PersianDate today = new PersianDate(c.getTimeInMillis());
        return today.getShDay() + "/" + today.getShMonth() + "/" + today.getShYear();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                AlertDialog.Builder ab = new AlertDialog.Builder(ActivityTicketMessage.this);
                ab.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }

        return super.onKeyDown(keyCode, event);
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
//                    JSONObject disconnect = new JSONObject();
//                    try {
//                        disconnect.put("from", myId);
//                        disconnect.put("to", toid);
//                        disconnect.put("room", room);
//                        disconnect.put("message", "Last Seen Recently");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    BasicActivity.socket.emit("disconnect", disconnect);
//
//                    BasicActivity.socket.off("tickmessage", handlerTickMessage);
//                    BasicActivity.socket.off("message", handlerIncomingMessage);
//                    BasicActivity.socket.off("typing", handlerTyping);
//                    BasicActivity.socket.off("stoptyping", handlerStopTyping);

//                    secretKey = null;
//                    BasicActivity.socket.disconnect();
//                    socket.close();
                    Intent intent = new Intent(ActivityTicketMessage.this, ActivityMain.class);
//                    intent.putExtra("imgurl", imgurl);
//                    intent.putExtra("id", toid);
//                    intent.putExtra("userid", tousername);

                    ActivityTicketMessage.this.startActivity(intent);
                    ActivityTicketMessage.this.finish();
//                    ActivityChat.this.onDestroy();
//                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.cancel();
                    break;
            }
        }
    };

    private void scrollToBottom(final RecyclerView recyclerView) {
        // scroll to last item to get the view of last item
        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        final RecyclerView.Adapter adapter = recyclerView.getAdapter();
        final int lastItemPosition = adapter.getItemCount() - 1;

        layoutManager.scrollToPositionWithOffset(lastItemPosition, 0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // then scroll to specific offset
                View target = layoutManager.findViewByPosition(lastItemPosition);
                if (target != null) {
                    int offset = recyclerView.getMeasuredHeight() - target.getMeasuredHeight();
                    layoutManager.scrollToPositionWithOffset(lastItemPosition, offset);

                }
            }
        });
    }

    public void showBottomSheet() {
        ActionBottomDialogFragment addPhotoBottomDialogFragment =
                ActionBottomDialogFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ActionBottomDialogFragment.TAG);
    }

    @Override
    public void onItemClick(String item) {
        if (item.equals("Photo")) {
//            Toast.makeText(ActivityChat.this, "Photo", Toast.LENGTH_SHORT).show();
            chooseImage();
        } else if (item.equals("PDF")) {
//            Toast.makeText(ActivityChat.this, "Video", Toast.LENGTH_SHORT).show();
            choosePdf();
//            prgvideo.setVisibility(View.VISIBLE);
//            btnVoice.setVisibility(View.GONE);
        } else if (item.equals("Audio")) {
            chooseAudio();

        }
//        Log.e("item", item);

    }

//    public String geturlimageprofile(final String userid) {
//        new AsyncTaskGetProfileImageChatList(new AsyncTaskGetProfileImageChatList.OnResponseRecieve() {
//            @Override
//            public String onRecieve(String response) {
//                ChatDatabase chatDatabase = new ChatDatabase(G.context);
//                chatDatabase.upuser(userid, imgurl);
//                Log.i("response", response);
//                return response;
//            }
//        }, G.phpurl + "/getimageprofile.php", userid,du).execute();
//        return "";
//    }

    private class CounterThread extends Thread {

        @Override
        public void run() {
            while (isRecording) {

                try {
                    Thread.sleep(100);
                    counter += 100;

                    G.HANDLER1.post(new Runnable() {

                        @Override
                        public void run() {
                            txttimerrec.setText(convertToTime(counter));
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    private String convertToTime(float counter) {
        int ms = (int) counter % 1000;
        counter = (int) counter / 1000;
        int min = (int) counter / 60;
        int sec = (int) counter - min * 60;

        String minPrefix = "";
        String secPrefix = "";
        String msPrefix = "";

        if (min < 10) {
            minPrefix = "0";
        }

        if (sec < 10) {
            secPrefix = "0";
        }

        if (ms < 10) {
            msPrefix = "00";
        } else if (ms < 100) {
            msPrefix = "0";
        }

        return minPrefix + min + ":" + secPrefix + sec + ":" + msPrefix + ms;
    }


    private void choosePdf() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/pdf");
        vi = "pdf";
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");

        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//        startActivityForResult(intent, PICKFILE_RESULT_CODE);

    }

    private void chooseAudio() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("audio/*");
        vi = "audio";
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");

        startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
//        startActivityForResult(intent, PICKFILE_RESULT_CODE);

    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        vi = "image";
        startActivityForResult(intent, REQUEST_OPEN_GALLERY);

    }

    private String getFilename() {
        if (Build.VERSION.SDK_INT < 30) {
            String filepath = Environment.getExternalStorageDirectory().getPath();
            File file = new File(filepath, AUDIO_RECORDER_FOLDER);

            if (!file.exists()) {
                file.mkdirs();
            }

            return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
        } else {
            String fileName = getExternalCacheDir().getAbsolutePath();
            fileName += "/" + System.currentTimeMillis() + file_exts[currentFormat];


            return (fileName);
        }
    }


    private void startRecording() {
        btnVoice.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);
        btnSend.setVisibility(View.GONE);
        txttimerseek.setVisibility(View.GONE);
        txttimerrec.setVisibility(View.VISIBLE);
        btnTime.setText("");
        counter = 0;
        isRecording = true;


        CounterThread thread = new CounterThread();
        thread.start();

        try {
            lastFilename = getFilename();
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(output_formats[currentFormat]);
            recorder.setOutputFile(lastFilename);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteitem(String id, Context context) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        JSONObject delete = new JSONObject();
                        try {
//                            Log.e("fromdelete", myusername);

                            delete.put("id", id);
                            delete.put("room", room);
                            delete.put("from", myId);
                            delete.put("to", toid);
                            delete.put("content", BasicActivity.content);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BasicActivity.socket.emit("deleteticket", delete);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        dialog.cancel();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }

    public void updateProgress() {

        mHandler.postDelayed(mUpdate, 100);

    }

    private void togglePlaying() {
        if (isPlaying) {
            stopPlaying();
        } else {
            startplaying();
        }
    }

    private void toggleRecording() {
        if (isRecording) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startplaying() {
//        txttimer.setVisibility(View.GONE);
        if (ispusing) {
            resumeplaying();
        } else {


            if (lastFilename == null || lastFilename.length() == 0) {
                Toast.makeText(G.context, "No Data Found!", Toast.LENGTH_SHORT).show();
            }

            try {


//                btnTime.setText("");
//                Log.e("lastFilename", lastFilename);
                mediaPlayer.setDataSource(lastFilename);
                mediaPlayer.prepare();
                mediaPlayer.start();
//                seekbar();
                seekvoise.setMax(100);
                seekvoise.setProgress(0);


                updateProgress();
                isPlaying = true;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void seekbar() {
//        final Handler mHandler = new Handler();
////Make sure you update Seekbar on UI thread
//        ActivityChat.this.runOnUiThread(new Runnable() {
//
//            @Override
//            public void run() {
//                seekvoise.setMax(mediaPlayer.getDuration());
//                seekvoise.setProgress(mediaPlayer.getCurrentPosition());
//                txttimerseek.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
////            btnTime.setText(milliSecondsToTimer(mediaPlayer.getCurrentPosition()));
////            btnTime.setText("");
//                mHandler.postDelayed(this, 50);
////                        mHandler.postDelayed(this, 1000);
//            }
//        });
//    }

    private void pausePlaying() {
        mediaPlayer.pause();
        length = mediaPlayer.getCurrentPosition();
        isPlaying = true;
    }

    private void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.reset();
        isPlaying = false;
        ispusing = false;

        mHandler.removeCallbacksAndMessages(mUpdate);

    }

    private void resumeplaying() {

        mediaPlayer.seekTo(length);
        mediaPlayer.start();
        ispusing = false;
    }

    private void stopRecording() {
        btnStop.setVisibility(View.GONE);
        btnSend.setVisibility(View.VISIBLE);

        isRecording = false;
//        CounterThread thread = new CounterThread();

        recorder.stop();
        recorder.reset();
        recorder.release();

        isvoise = 1;
    }

    public int getItemPosition(String eventId) {
        for (int i = 0; i < chatsTexts.size(); i++) {
            if (chatsTexts.get(i).getText().equals(eventId)) {
                return i;
            }
        }
        return -1;
    }
}
package com.shrazavi.dadmehr.Fragment;

import static com.shrazavi.dadmehr.Activity.ActivityCall.handlercall;
import static com.shrazavi.dadmehr.core.base.BasicActivity.userid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.github.nkzawa.emitter.Emitter;
import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.Adapter.RecyclerAdapterRoom;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.voip.AsyncPlayer;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OutcomingCallFragment extends Fragment {

    Button btncancel;
    ImageView profile;
//    private Socket socket;
    TextView txtname;
    TextView txttime;
//    TextView txtStartChannel;
//    TextView txtSetting;
ImageView imgback;
    CountDownTimer countcancel;
    Retrofitinformation RI;
    public String id = "";
    public String room = "";
    public String from = "";
    public String to = "";
    public String vu = "";
    public String myid = "";
    public String type = "";
    public static Context context;
    Handler threadHandler;
    int callstatus = 0;
    public int timer = 0;
//    public static SharedPreferences sharedPreferences;
    private AsyncPlayer ringPlayer;
    public RecyclerAdapterRoom recyclerAdapterroom;
    String content;
    String enk;
    SecretKeySpec Key;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    LinearLayoutManager linearLayoutManager;

    public OutcomingCallFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString("result");

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (callstatus == 0) {

        } else {
            Intent intent = new Intent(G.context, ActivityMain.class);
            startActivity(intent);
        }
//        Intent intent = new Intent(G.context, ActivityMain.class);
//        G.context.startActivity(intent);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_outcomingcall, container, false);
        ringPlayer = new AsyncPlayer(null);
        Uri uri = Uri.parse("android.resource://" + G.getInstance().getPackageName() + "/" + R.raw.wr_ringback);
        ringPlayer.play(G.getInstance(), uri, true, AudioManager.STREAM_RING);
        profile = (ImageView) myFragmentView.findViewById(R.id.img_out_call_prof);
        txtname = (TextView) myFragmentView.findViewById(R.id.txt_out_call_name);
        imgback = (ImageView) myFragmentView.findViewById(R.id.img_out_call_back);
        btncancel = (Button) myFragmentView.findViewById(R.id.btn_out_call_cancel);
//        txttime = (TextView) myFragmentView.findViewById(R.id.txt_out_call_time);
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = BasicActivity.vu;
        userid = BasicActivity.userid;
//        enk = sharedPreferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key=new SecretKeySpec(data, 0, data.length, "AES");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+myid+"-"+R.string.developer), Base64.NO_WRAP);
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        int DrawableImage[] = {R.drawable.callpage1, R.drawable.callpage2,R.drawable.callpage3, R.drawable.callpage4};

        final Handler handler = new Handler();
        final int[] i = {0};
        final int[] j = {1};
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Resources res = G.context.getResources();
                        TransitionDrawable out = new TransitionDrawable(new Drawable[]{res.getDrawable(DrawableImage[i[0]]), res.getDrawable(DrawableImage[j[0]])});
                        out.setCrossFadeEnabled(true);

                        imgback.setBackgroundDrawable(out);
                        out.startTransition(2800);
                        i[0]++;
                        j[0]++;
                        if (j[0] == DrawableImage.length) {
                            j[0] = 0;
                        }
                        if (i[0] == DrawableImage.length) {
                            i[0] = 0;
                        }
                        handler.postDelayed(this, 3000);
                    }
                });
            }
        }, 0);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        handlercall = new Handler();
        Bundle arguments = this.getArguments();
        id = arguments.getString("id");
        from = arguments.getString("from");
        room = arguments.getString("room");
        type = arguments.getString("type");
        timer = arguments.getInt("timer");
        to = arguments.getString("to");
        if(type.equals("voise")){


        }else {


        }
        //        time = arguments.getString("time");

//        BasicActivity.socket.connect();
//        BasicActivity.socket.emit("nickname", myid);
        if (vu.equals("vl")) {
            Call<User> calluser = RI.getuser(content,to,myid);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (!response.body().getProfile().equals("empty")) {
                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(profile);

                    }else {
                        Picasso.with(G.context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(profile);

                    }

                    txtname.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            Call<Vakil> callvakil = RI.getvakil(content,to,myid);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {

                    if (!response.body().getProfile().equals("empty")) {
                        Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(profile);

                    }else {
                        Picasso.with(G.context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(profile);

                    }
                    txtname.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {

                }
            });

        }

//        BasicActivity.socket.on("accept", handleraccepted);
        JSONObject postDataForcancel = new JSONObject();
        try {
            postDataForcancel.put("id", id);
            postDataForcancel.put("to", to);
            postDataForcancel.put("room", room);
            postDataForcancel.put("from", from);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringPlayer.stop();
//                BasicActivity.socket.emit("cancelcall", postDataForcancel);
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
            }
        });

        context = G.context;

        countcancel=new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                ringPlayer.stop();
//                BasicActivity.socket.emit("cancelcall", postDataForcancel);
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
                timer = 1;
            }
        }.start();



        return myFragmentView;
    }


    public void JoinRoomSingleVideo(String room,int timer) {
//        WebrtcUtil.callSingle(getActivity(),timer,to,
//                G.wss,
//                room,
//                true);
    }

    public void JoinRoomSingleAudio(String room,int timer) {
//        WebrtcUtil.callSingle(getActivity(),timer,to,
//                G.wss,
//                room,
//                false);
    }


    public Emitter.Listener handleraccepted = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handlercall.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    Log.e("ejrashodam", "oooooooooo");
                    String room = "";
                    String status = "";
                    try {
                        ringPlayer.stop();
                        room = jsonObject.getString("room").toString();
                        status = jsonObject.getString("status").toString();
                        if (status.equals("yes")) {
                            if (type.equals("voise")) {
//                            getActivity().finish();
                                callstatus = 1;
                                countcancel.cancel();
                                JoinRoomSingleAudio(room,timer);

                            } else {
//                            getActivity().finish();
                                callstatus = 1;
                                countcancel.cancel();
                                JoinRoomSingleVideo(room,timer);
//                               WebrtcUtil.testWs("wss://192.168.1.5/wss");

                            }

                        } else {
                            Intent intent = new Intent(G.context, ActivityMain.class);
                            startActivity(intent);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };



}

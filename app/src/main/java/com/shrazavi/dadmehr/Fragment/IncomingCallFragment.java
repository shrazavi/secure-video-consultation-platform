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
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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


public class IncomingCallFragment extends Fragment {


    ImageView profile;
//    private Socket socket;
    TextView txtname;
    TextView txttime;
    //    TextView txtStartChannel;
//    TextView txtSetting;
    Button btnaccept, btncancel;
    int callstatus = 0;
    Retrofitinformation RI;
    String content;
    String enk;
    SecretKeySpec Key;
    public String room = "";
    public String id = "";
    public String from = "";
    public String to = "";
    public int timer =0;
    public String vu = "";
    public String type = "";
    public static Context context;
    Handler threadHandler;
    public String myid = "";
//    public static SharedPreferences sharedPreferences;
    private AsyncPlayer ringPlayer;
    public RecyclerAdapterRoom recyclerAdapterroom;
    ImageView imgback;

//    {
//        try {
//            socket = IO.socket(G.nodeurl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }

    LinearLayoutManager linearLayoutManager;

    public IncomingCallFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK) {
            String result = data.getExtras().getString("result");

        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        Intent intent = new Intent(G.context, ActivityMain.class);
//        G.context.startActivity(intent);
        if (callstatus == 0) {

        } else {
            Intent intent = new Intent(G.context, ActivityMain.class);
            startActivity(intent);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_incomingcall, container, false);
        btnaccept = (Button) myFragmentView.findViewById(R.id.btn_in_call_accept);
        btncancel = (Button) myFragmentView.findViewById(R.id.btn_in_call_cancel);
        profile = (ImageView) myFragmentView.findViewById(R.id.img_in_call_prof);
        txtname = (TextView) myFragmentView.findViewById(R.id.txt_in_call_name);
//        txttime = (TextView) myFragmentView.findViewById(R.id.txt_in_call_time);
        ringPlayer = new AsyncPlayer(null);
//        Uri uri = Uri.parse("android.resource://" + G.getInstance().getPackageName() + "/" + R.raw.incoming_call_ring);
        imgback = (ImageView) myFragmentView.findViewById(R.id.img_in_call_back);
        ringPlayer.play(G.getInstance(), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), true, AudioManager.STREAM_RING);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = BasicActivity.vu;
        userid = BasicActivity.userid;
//        enk = sharedPreferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key=new SecretKeySpec(data, 0, data.length, "AES");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+myid+"-"+R.string.developer), Base64.NO_WRAP);
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
//        BasicActivity.socket.on("cancel", handlercancel);
        content=BasicActivity.content;

        Bundle arguments = this.getArguments();
        id = arguments.getString("id");
        from = arguments.getString("from");
        to = arguments.getString("to");
        room = arguments.getString("room");
        type = arguments.getString("type");
        timer = arguments.getInt("timer");
        int DrawableImage[] =  {R.drawable.callpage1, R.drawable.callpage2,R.drawable.callpage3, R.drawable.callpage4};

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
        Log.e("callid",id+"");
        if(type.equals("voise")){
            btnaccept.setBackgroundResource(R.drawable.voiseincome);

        }else {
            btnaccept.setBackgroundResource(R.drawable.videoincome);

        }

        handlercall = new Handler();
        if (vu.equals("vl")) {
            Call<User> calluser = RI.getuser(content,from,myid);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(profile);
                    txtname.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            Call<Vakil> callvakil = RI.getvakil(content,from,myid);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {

                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(profile);
                    txtname.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {

                }
            });

        }

//        socket.connect();
        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringPlayer.stop();
                JSONObject postDataForaccept = new JSONObject();
                try {
                    postDataForaccept.put("id", id);
                    postDataForaccept.put("to", from);
                    postDataForaccept.put("room", room);
                    postDataForaccept.put("from", to);
                    postDataForaccept.put("status", "yes");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                BasicActivity.socket.emit("acceptcall", postDataForaccept);
                if (type.equals("voise")) {
//                    getActivity().finish();
                    callstatus = 1;
                    JoinRoomSingleAudio(room);
                } else {
                    callstatus = 1;
//                    getActivity().finish();
                    JoinRoomSingleVideo(room);

                }

            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringPlayer.stop();
                JSONObject postDataForaccept = new JSONObject();
                try {
                    postDataForaccept.put("to", from);
                    postDataForaccept.put("room", room);
                    postDataForaccept.put("from", to);
                    postDataForaccept.put("status", "no");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                BasicActivity.socket.emit("acceptcall", postDataForaccept);
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
            }
        });

        context = G.context;


        return myFragmentView;
    }


    public void JoinRoomSingleVideo(String room) {
//        WebrtcUtil.callSingle(getActivity(),timer,"not",
//                G.wss,
//                room,
//                true);
    }

    public void JoinRoomSingleAudio(String room) {
//        WebrtcUtil.callSingle(getActivity(),timer,"not",
//                G.wss,
//                room,
//                false);
    }

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
    public Emitter.Listener handlercancel = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handlercall.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
//                    Log.e("from", from);
                    String fromcall = "";
                
                    try {

                        fromcall = jsonObject.getString("from").toString();
//                        Log.e("fromcall", fromcall);
                        if (fromcall.equals(from)) {

                            ringPlayer.stop();
                            Intent intent = new Intent(G.context, ActivityMain.class);
                            startActivity(intent);
                        } else {


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
}

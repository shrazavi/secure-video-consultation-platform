package com.shrazavi.dadmehr.core.voip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.Utilities;
import com.shrazavi.dadmehr.core.CallSession;
import com.shrazavi.dadmehr.core.EnumType;
import com.shrazavi.dadmehr.core.SkyEngineKit;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.ui.event.MsgEvent;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * <pre>
 *     author : Jasper
 *     e-mail : 229605030@qq.com
 *     time   : 2021/02/01
 *     desc   :
 * </pre>
 */
public abstract class SingleCallFragment extends Fragment {
    private static final String TAG = "SingleCallFragment";
    ImageView minimizeImageView;
    ImageView portraitImageView;// 用户头像
    TextView nameTextView; // 用户昵称
    public static TextView descTextView;  // 状态提示用语
    Chronometer durationTextView; // 通话时长

    ImageView outgoingHangupImageView;
    ImageView incomingHangupImageView;
    ImageView acceptImageView;
    TextView tvStatus;
    View outgoingActionContainer;
    View incomingActionContainer;
    View connectedActionContainer;

    View lytParent;

    public static String content;
    public static String enk;
    public static String timing;
    public static SecretKeySpec Key;
    public static String username = "";
    public static String myid = "";
    public static String vu = "";
    public static int timer = 0;
    boolean isOutgoing = false;
    Utilities utils;
    SkyEngineKit gEngineKit;

    public SharedPreferences sharedPreferences;
    Retrofitinformation RI;
    CallSingleActivity callSingleActivity;

    CallHandler handler;
    boolean endWithNoAnswerFlag = false;
    boolean isConnectionClosed = false;
    public static CountDownTimer cdt;
    public static final int WHAT_DELAY_END_CALL = 0x01;

    public static final int WHAT_NO_NET_WORK_END_CALL = 0x02;

    EnumType.CallState currentState;
    HeadsetPlugReceiver headsetPlugReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        handler = new CallHandler();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        Bundle arguments = this.getArguments();
        username = arguments.getString("username");
        timer = Integer.parseInt(arguments.getString("timer"));
        Boolean isfloating = arguments.getBoolean("isfloating");
//        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = BasicActivity.vu;
        myid=BasicActivity.userid;
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        enk = sharedPreferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        cdt = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
//                timerValue.setText(millisUntilFinished/1000+"");
//                Log.e("txttime", utils.milli_to_time(millisUntilFinished) + "");
                tvStatus.setText(utils.milli_to_time(millisUntilFinished));
                timing = millisUntilFinished + "";
                if (millisUntilFinished <= 60000) {
                    tvStatus.setTextColor(Color.parseColor("#FF0000"));

                }
//                Log.e("timer ===", millisUntilFinished + "");

            }

            public void onFinish() {

                SkyEngineKit.Instance().endCall();
                timer = 1;
            }
        };
        new CountDownTimer(12000, 1000) {
            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {

                SharedPreferences.Editor editor = BasicActivity.preferences.edit();
                editor.putString("call", username);
                editor.commit();

            }
        }.start();
        if (isfloating) {
            cdt.start();
        }

        initView(view);
        init();
        return view;
    }

    @Override
    public void onDestroyView() {
//        if (durationTextView != null)
//            durationTextView.stop();
        refreshMessage(true);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }


    abstract int getLayout();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MsgEvent<Object> messageEvent) {
        int code = messageEvent.getCode();
        Log.d(TAG, "onEvent code = $code; endWithNoAnswerFlag = $endWithNoAnswerFlag");
        if (code == MsgEvent.CODE_ON_CALL_ENDED) {
            if (endWithNoAnswerFlag) {
                didCallEndWithReason(EnumType.CallEndReason.Timeout);
            } else if (isConnectionClosed) {
                didCallEndWithReason(EnumType.CallEndReason.SignalError);
            } else {
                if (callSingleActivity != null) {
                    callSingleActivity.finish();
                }
            }
        } else if (code == MsgEvent.CODE_ON_REMOTE_RING) {
            descTextView.setText("درحال زنگ خوردن");
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callSingleActivity = (CallSingleActivity) getActivity();
        if (callSingleActivity != null) {
            isOutgoing = callSingleActivity.isOutgoing();
            gEngineKit = callSingleActivity.getEngineKit();
            headsetPlugReceiver = new HeadsetPlugReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_HEADSET_PLUG);
            callSingleActivity.registerReceiver(headsetPlugReceiver, filter);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callSingleActivity.unregisterReceiver(headsetPlugReceiver);  //注销监听
        callSingleActivity = null;
    }


    public void initView(View view) {
        utils = new Utilities();
        lytParent = view.findViewById(R.id.lytParent);
        minimizeImageView = view.findViewById(R.id.minimizeImageView);
        portraitImageView = view.findViewById(R.id.portraitImageView);
        nameTextView = view.findViewById(R.id.nameTextView);
        descTextView = view.findViewById(R.id.descTextView);
        durationTextView = view.findViewById(R.id.durationTextView);
        outgoingHangupImageView = view.findViewById(R.id.outgoingHangupImageView);
        incomingHangupImageView = view.findViewById(R.id.incomingHangupImageView);
        acceptImageView = view.findViewById(R.id.acceptImageView);
        tvStatus = view.findViewById(R.id.tvStatus);
        outgoingActionContainer = view.findViewById(R.id.outgoingActionContainer);
        incomingActionContainer = view.findViewById(R.id.incomingActionContainer);
        connectedActionContainer = view.findViewById(R.id.connectedActionContainer);

        durationTextView.setVisibility(View.GONE);

        if (vu.equals("vl")) {
            Call<User> calluser = RI.getuser(content, username, myid);
            calluser.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(portraitImageView);
                    nameTextView.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        } else {
            Call<Vakil> callvakil = RI.getvakil(content, username, myid);
            callvakil.enqueue(new Callback<Vakil>() {
                @Override
                public void onResponse(Call<Vakil> call, Response<Vakil> response) {

                    Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(portraitImageView);
                    nameTextView.setText(response.body().getName());
                }

                @Override
                public void onFailure(Call<Vakil> call, Throwable t) {

                }
            });

        }


//        nameTextView.setText();
//        portraitImageView.setImageResource(R.mipmap.icon_default_header);
        if (isOutgoing) {
            handler.sendEmptyMessageDelayed(WHAT_DELAY_END_CALL, 60 * 1000);//1分钟之后未接通，则挂断电话
        }
    }

    public void init() {
    }

    // ======================================界面回调================================
    public void didCallEndWithReason(EnumType.CallEndReason callEndReason) {
        switch (callEndReason) {
            case Busy: {
                tvStatus.setText("طرف مقابل مشغول است");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case SignalError: {
                tvStatus.setText("قطع شدن");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case RemoteSignalError: {
                tvStatus.setText("شبکه طرف مقابل قطع شده است");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
                //                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case Hangup: {
                tvStatus.setText("قطع شد");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case MediaError: {
                tvStatus.setText("خطای رسانه");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case RemoteHangup: {
                tvStatus.setText("طرف مقابل تماس را قطع کرده است");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case OpenCameraFailure: {
                tvStatus.setText("خطا در باز کردن دوربین");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case Timeout: {
                tvStatus.setText("طرف مقابل جواب نداد");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
            case AcceptByOtherClient: {
                tvStatus.setText("در دستگاه های دیگر پاسخ دهید");
                tvStatus.setTextColor(Color.parseColor("#ffffff"));
//                cdt.onFinish();
                cdt.cancel();
//                timer=1;
                break;
            }
        }
        incomingActionContainer.setVisibility(View.GONE);
        outgoingActionContainer.setVisibility(View.GONE);
        if (connectedActionContainer != null)
            connectedActionContainer.setVisibility(View.GONE);
        refreshMessage(false);
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (callSingleActivity != null) {
                callSingleActivity.finish();
            }

        }, 1500);
    }

    public void didChangeState(EnumType.CallState state) {

    }

    public void didChangeMode(Boolean isAudio) {
    }

    public void didCreateLocalVideoTrack() {
    }

    public void didReceiveRemoteVideoTrack(String userId) {
    }

    public void didUserLeave(String userId) {
    }

    public void didError(String error) {
    }

    public void didDisconnected(String error) {
        handler.sendEmptyMessage(WHAT_NO_NET_WORK_END_CALL);
    }

    private void refreshMessage(Boolean isForCallTime) {
        if (callSingleActivity == null) {
            return;
        }
        // 刷新消息; demo中没有消息，不用处理这儿快逻辑
    }

//    public void startRefreshTime() {
//        CallSession session = SkyEngineKit.Instance().getCurrentSession();
//        if (session == null) return;
//        if (durationTextView != null) {
//            durationTextView.setVisibility(View.VISIBLE);
//            durationTextView.setBase(SystemClock.elapsedRealtime() - (System.currentTimeMillis() - session.getStartTime()));
//            durationTextView.start();
//        }
//    }

    void runOnUiThread(Runnable runnable) {
        if (callSingleActivity != null) {
            callSingleActivity.runOnUiThread(runnable);
        }
    }

    class CallHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == WHAT_DELAY_END_CALL) {
                if (currentState != EnumType.CallState.Connected) {
                    endWithNoAnswerFlag = true;
                    if (callSingleActivity != null) {
                        SkyEngineKit.Instance().endCall();
                        timer = 1;
                    }
                }
            } else if (msg.what == WHAT_NO_NET_WORK_END_CALL) {

                isConnectionClosed = true;
                if (callSingleActivity != null) {
//                    SkyEngineKit.Instance().endCall();
                    timer = 1;
                }
            }
        }

    }


    class HeadsetPlugReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                CallSession session = SkyEngineKit.Instance().getCurrentSession();
                if (session == null) {
                    return;
                }
                if (intent.getIntExtra("state", 0) == 0) { //拔出耳机
                    session.toggleHeadset(false);
                } else if (intent.getIntExtra("state", 0) == 1) { //插入耳机
                    session.toggleHeadset(true);
                }
            }
        }
    }
}

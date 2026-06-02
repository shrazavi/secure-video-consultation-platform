package com.shrazavi.dadmehr.core.voip;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;


import com.shrazavi.dadmehr.core.CallSession;
import com.shrazavi.dadmehr.core.EnumType;
import com.shrazavi.dadmehr.core.SkyEngineKit;
import com.shrazavi.dadmehr.core.except.NotInitializedException;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BaseActivity;
import com.shrazavi.dadmehr.permission.Permissions;


/**
 * Created by dds on 2018/7/26.
 * 单人通话界面
 */
public class CallSingleActivity extends BaseActivity implements CallSession.CallSessionCallback {


    public static final String EXTRA_TARGETID = "targetId";
    public static final String EXTRA_TARGETUSER = "targetUser";
    public static final String EXTRA_ROOM = "room";
    public static final String EXTRA_CALLERID = "callerId";
    public static final String EXTRA_CALLERUSER = "callerUser";
    public static final String EXTRA_MO = "isOutGoing";
    public static final String EXTRA_AUDIO_ONLY = "audioOnly";
    public static final String EXTRA_FROM_FLOATING_VIEW = "fromFloatingView";
    public static final String EXTRA_TIMER = "timer";
    private static final String TAG = "CallSingleActivity";
    private Handler handler = new Handler(Looper.getMainLooper());
    private boolean isOutgoing;
    private String targetId,targetUser;
    boolean isAudioOnly;
    private boolean isFromFloatingView;

    private SkyEngineKit gEngineKit;

    private SingleCallFragment currentFragment;
    private String room,callerId,callerUser,timer;

    public static Intent getCallIntent(Context context, String targetId,String targetUser, String room, String callerId,String callerUser, boolean isOutgoing,
                                       boolean isAudioOnly, boolean isClearTop,String timer) {
        Intent voip = new Intent(context, CallSingleActivity.class);
        voip.putExtra(CallSingleActivity.EXTRA_MO, isOutgoing);
        voip.putExtra(CallSingleActivity.EXTRA_TARGETID, targetId);
        voip.putExtra(CallSingleActivity.EXTRA_TARGETUSER, targetUser);
        voip.putExtra(CallSingleActivity.EXTRA_ROOM, room);
        voip.putExtra(CallSingleActivity.EXTRA_CALLERID, callerId);
        voip.putExtra(CallSingleActivity.EXTRA_CALLERUSER, callerUser);
        voip.putExtra(CallSingleActivity.EXTRA_AUDIO_ONLY, isAudioOnly);
        voip.putExtra(CallSingleActivity.EXTRA_TIMER, timer);
        voip.putExtra(CallSingleActivity.EXTRA_FROM_FLOATING_VIEW, false);
        if (isClearTop) {
            voip.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        return voip;
    }


    public static void openActivity(Context context, String targetId,String targetUser, String room, String callerId,String callerUSer, boolean isOutgoing,
                                    boolean isAudioOnly, boolean isClearTop,String timer) {

        Intent intent = getCallIntent(context, targetId,targetUser,room,callerId,callerUSer, isOutgoing, isAudioOnly, isClearTop,timer);
        if (context instanceof Activity) {
            context.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarOrScreenStatus(this);
        setContentView(R.layout.activity_single_call);

        try {
            gEngineKit = SkyEngineKit.Instance();
        } catch (NotInitializedException e) {
            SkyEngineKit.init(new VoipEvent()); //重新初始化
            try {
                gEngineKit = SkyEngineKit.Instance();
            } catch (NotInitializedException ex) {
                finish();
            }
        }
        final Intent intent = getIntent();
        timer = intent.getStringExtra(EXTRA_TIMER);
        targetId = intent.getStringExtra(EXTRA_TARGETID);
        targetUser = intent.getStringExtra(EXTRA_TARGETUSER);
        room = intent.getStringExtra(EXTRA_ROOM);
        callerId = intent.getStringExtra(EXTRA_CALLERID);
        callerUser = intent.getStringExtra(EXTRA_CALLERUSER);
        isFromFloatingView = intent.getBooleanExtra(EXTRA_FROM_FLOATING_VIEW, false);
        isOutgoing = intent.getBooleanExtra(EXTRA_MO, false);
        isAudioOnly = intent.getBooleanExtra(EXTRA_AUDIO_ONLY, false);

        if (isFromFloatingView) {
            Intent serviceIntent = new Intent(this, FloatingVoipService.class);
            stopService(serviceIntent);
            init(targetId,targetUser, room,callerId,callerUser,false, isAudioOnly, false,timer);
        } else {

            String[] per;
            if (isAudioOnly) {
                per = new String[]{Manifest.permission.RECORD_AUDIO};
            } else {
                per = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
            }
            Permissions.request(this, per, integer -> {
                Log.d(TAG, "Permissions.request integer = " + integer);
                if (integer == 0) {

                    init(targetId,targetUser,room,callerId,callerUser, isOutgoing, isAudioOnly, false,timer);
                } else {
                    Toast.makeText(this, "مجوز رد شد", Toast.LENGTH_SHORT).show();

                    finish();
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        //通话时不能按返回键，跟微信同现象，只能挂断结束或者接听
//        super.onBackPressed();
//        if (currentFragment != null) {
//            if (currentFragment instanceof FragmentAudio) {
//                ((FragmentAudio) currentFragment).onBackPressed();
//            } else if (currentFragment instanceof FragmentVideo) {
//                ((FragmentVideo) currentFragment).onBackPressed();
//            }
//        }

    }

    private void init(String targetId,String targetUser, String room, String callerId,String callerUser, boolean outgoing, boolean audioOnly, boolean isReplace,String timer) {
        SingleCallFragment fragment;
        Bundle arguments = new Bundle();
        arguments.putString( "username" , targetUser);
        arguments.putString( "timer" , timer);
        arguments.putBoolean( "isfloating" , isFromFloatingView);
        if (audioOnly) {
            fragment = new FragmentAudio();
        } else {
            fragment = new FragmentVideo();

        }
        fragment.setArguments(arguments);
        FragmentManager fragmentManager = getSupportFragmentManager();
        currentFragment = fragment;
        if (isReplace) {
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .commit();

        } else {
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .commit();

        }
        if (outgoing && !isReplace) {
            // 创建会话
//            room = UUID.randomUUID().toString() + System.currentTimeMillis();
            boolean b = gEngineKit.startOutCall(getApplicationContext(), room,callerId, targetId,targetUser, audioOnly,timer);
            if (!b) {
                finish();
                return;
            }
            G.getInstance().setRoomId(room);
            G.getInstance().setOtherUserId(targetId);
            CallSession session = gEngineKit.getCurrentSession();
            if (session == null) {
                finish();
            } else {
                session.setSessionCallback(this);
            }
        } else {
            CallSession session = gEngineKit.getCurrentSession();
            if (session == null) {
                finish();
            } else {
                if (session.isAudioOnly() && !audioOnly) { //这种情况是，对方切换成音频的时候，activity还没启动，这里启动后需要切换一下
                    isAudioOnly = session.isAudioOnly();
                    fragment.didChangeMode(true);
                }
                session.setSessionCallback(this);
            }
        }

    }

    public SkyEngineKit getEngineKit() {
        return gEngineKit;
    }

    public boolean isOutgoing() {
        return isOutgoing;
    }


    public boolean isFromFloatingView() {
        return isFromFloatingView;
    }

    // 显示小窗
    public void showFloatingView() {
        if (!checkOverlayPermission()) {
            return;
        }
        Intent intent = new Intent(this, FloatingVoipService.class);
        intent.putExtra(EXTRA_TARGETID, targetId);
        intent.putExtra(EXTRA_TARGETUSER, targetUser);
        intent.putExtra(EXTRA_ROOM, room);
        intent.putExtra(EXTRA_CALLERID, callerId);
        intent.putExtra(EXTRA_CALLERUSER, callerUser);
        intent.putExtra(EXTRA_AUDIO_ONLY, isAudioOnly);
        intent.putExtra(EXTRA_MO, isOutgoing);
        intent.putExtra(EXTRA_TIMER, SingleCallFragment.timing);
        startService(intent);
        finish();
    }

    // 切换到语音通话
    public void switchAudio() {
        init(targetId,targetUser,room,callerId,callerUser, isOutgoing, true, true,timer);
    }

    public String getRoomId() {
        return room;
    }

    // ======================================界面回调================================
    @Override
    public void didCallEndWithReason(EnumType.CallEndReason reason) {
        G.getInstance().setOtherUserId("0");
        //交给fragment去finish
//        finish();
        handler.post(() -> currentFragment.didCallEndWithReason(reason));
    }

    @Override
    public void didChangeState(EnumType.CallState callState) {
        if (callState == EnumType.CallState.Connected) {
            isOutgoing = false;
        }
        handler.post(() -> currentFragment.didChangeState(callState));
    }

    @Override
    public void didChangeMode(boolean var1) {
        handler.post(() -> currentFragment.didChangeMode(var1));
    }

    @Override
    public void didCreateLocalVideoTrack() {
        handler.post(() -> currentFragment.didCreateLocalVideoTrack());
    }

    @Override
    public void didReceiveRemoteVideoTrack(String userId) {
        handler.post(() -> currentFragment.didReceiveRemoteVideoTrack(userId));
    }

    @Override
    public void didUserLeave(String userId) {
        handler.post(() -> currentFragment.didUserLeave(userId));
    }

    @Override
    public void didError(String var1) {
        handler.post(() -> currentFragment.didError(var1));
//        finish();
    }

    @Override
    public void didDisconnected(String userId) {
        handler.post(() -> currentFragment.didDisconnected(userId));
    }


    // ========================================================================================

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SettingsCompat.setDrawOverlays(this, true);
            if (!SettingsCompat.canDrawOverlays(this)) {
                Toast.makeText(this, "Need permission for floating window", Toast.LENGTH_LONG).show();
                SettingsCompat.manageDrawOverlays(this);
                return false;
            }
        }
        return true;
    }


    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return flags;
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    public void setStatusBarOrScreenStatus(Activity activity) {
        Window window = activity.getWindow();
        //全屏+锁屏+常亮显示
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.getDecorView().setSystemUiVisibility(getSystemUiVisibility());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
            layoutParams.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(layoutParams);
        }
        // 5.0以上系统状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //清除透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //设置状态栏颜色必须添加
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//设置透明
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //19
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

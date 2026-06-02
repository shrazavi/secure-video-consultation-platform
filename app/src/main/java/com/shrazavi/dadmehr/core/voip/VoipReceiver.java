package com.shrazavi.dadmehr.core.voip;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.shrazavi.dadmehr.core.SkyEngineKit;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BaseActivity;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.ActivityStackManager;
import com.shrazavi.dadmehr.permission.Permissions;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;

/**
 * Created by dds on 2019/8/25.
 * android_shuai@163.com
 */
public class VoipReceiver extends BroadcastReceiver {
    private static final String TAG = "VoipReceiver";
    private AsyncPlayer ringPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Utils.ACTION_VOIP_RECEIVER.equals(action)) {
            String room = intent.getStringExtra("room");
            boolean audioOnly = intent.getBooleanExtra("audioOnly", true);
            String callerId = intent.getStringExtra("callerId");
            String callerUser = intent.getStringExtra("callerUser");
            String targetId = intent.getStringExtra("targetId");
            String targetUser = intent.getStringExtra("targetUser");
            String userList = intent.getStringExtra("userList");
            String timer = intent.getStringExtra("timer");
            String[] list = userList.split(",");
            SkyEngineKit.init(new VoipEvent());
            //todo 处理邀请人名称
            if (callerUser == null) {
                callerUser = "p2pChat";
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (com.shrazavi.dadmehr.core.util.Utils.isAppRunningForeground()) {
                    onForegroundOrBeforeVersionO(targetId,targetUser,room, userList, callerId,callerUser, audioOnly, true,timer);
                } else {
                    onBackgroundAfterVersionO(targetId,targetUser,room, userList, callerId,callerUser, audioOnly,timer);
                }
            } else {
                onForegroundOrBeforeVersionO(
                        targetId,
                        targetUser,
                        room,
                        userList,
                        callerId,
                        callerUser,
                        audioOnly,
                        com.shrazavi.dadmehr.core.util.Utils.isAppRunningForeground(),timer
                );
            }
        }
    }

    private void onBackgroundAfterVersionO(
           String targetId,String targetUser, String room, String userList,
            String callerId, String callerUser, Boolean audioOnly,String timer
    ) {
        String[] strArr = userList.split(",");
        ArrayList<String> list = new ArrayList<>();
        for (String str : strArr)
            list.add(str);
        SkyEngineKit.init(new VoipEvent());
        BaseActivity activity = (BaseActivity) ActivityStackManager.getInstance().getTopActivity();
        // 权限检测
        String[] per;
        if (audioOnly) {
            per = new String[]{Manifest.permission.RECORD_AUDIO};
        } else {
            per = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        }
        boolean hasPermission = Permissions.has(activity, per);
        if (hasPermission) {
            onBackgroundHasPermission(activity,targetId,targetUser, room, list, callerId,callerUser, audioOnly,timer);
        } else {
            CallForegroundNotification notification = new CallForegroundNotification(G.getInstance());
            notification.sendRequestIncomingPermissionsNotification(
                    activity,
                    room,
                    userList,
                    callerId,
                    callerUser,
                    audioOnly
            );
        }
    }

    private void onBackgroundHasPermission(
            Context context,String targetId,String targetUser, String room, ArrayList<String> list,
            String callerId, String callerUser, Boolean audioOnly,String timer) {
        boolean b = SkyEngineKit.Instance().startInCall(G.getInstance(), room, callerId,callerUser, audioOnly,timer);
        LogUtils.dTag(TAG, "onBackgroundHasPermission b = " + b );
        if (b) {
            G.getInstance().setOtherUserId(callerId);
            if (list.size() == 1) {
                CallForegroundNotification notification = new CallForegroundNotification(G.getInstance());
                notification.sendIncomingCallNotification(
                        G.getInstance(),
                        callerId,
                        callerUser,
                        room,
                        targetId,
                        targetUser,
                        false,
                        audioOnly,
                        true,
                        timer
                );
            }
        }
    }

    private void onForegroundOrBeforeVersionO(
           String targetId,String targetUser, String room, String userList,
            String callerId,String callerUser, Boolean audioOnly, Boolean isForeGround,String timer
    ) {
        String[] strArr = userList.split(",");
        ArrayList<String> list = new ArrayList<>();
        for (String str : strArr)
            list.add(str);
        SkyEngineKit.init(new VoipEvent());
        BasicActivity activity = (BasicActivity) ActivityStackManager.getInstance().getTopActivity();
        // 权限检测
        String[] per;
        if (audioOnly) {
            per = new String[]{Manifest.permission.RECORD_AUDIO};
        } else {
            per = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        }
        boolean hasPermission = Permissions.has(activity, per);
        LogUtils.dTag(TAG, "onForegroundOrBeforeVersionO hasPermission = " + hasPermission + ", isForeGround = " + isForeGround);
        if (hasPermission) {
            onHasPermission(activity,targetId,targetUser, room, list, callerId,callerUser, audioOnly,timer);
        } else {

            ringPlayer = new AsyncPlayer(null);
            shouldStartRing(true); //来电先响铃
            if (isForeGround) {
                Alerter.create(activity).setTitle("اطلاع رسانی تماس")
                        .setText(
                                "شما درحال دریافت تماس از" + callerUser + "هستید لطفا مجوز های لازم را صادر نمایید"
                                        + (audioOnly ? "ضبط" :
                                        "میکروفن و دوربین") + "مجوز های تماس : "
                        )
                        .enableSwipeToDismiss()
                        .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
                        .setDuration(60 * 1000)
                        .addButton("پاسخ", R.style.AlertButtonBgWhite, v -> {
                            Permissions.request(activity, per, integer -> {
                                shouldStopRing();
                                Log.d(TAG, "Permissions.request integer = " + integer);
                                if (integer == 0) { //权限同意
                                    onHasPermission(activity, targetId,targetUser,room, list, callerId,callerUser, audioOnly,timer);
                                } else {
                                    onPermissionDenied(room, callerId);
                                }
                                Alerter.hide();
                            });
                        })
                        .addButton("لغو تماس", R.style.AlertButtonBgWhite, v -> {
                            shouldStopRing();
                            onPermissionDenied(room, callerId);
                            Alerter.hide();
                        }).show();
            } else {
                CallForegroundNotification notification = new CallForegroundNotification(G.getInstance());
                notification.sendRequestIncomingPermissionsNotification(
                        activity,
                        room,
                        userList,
                        callerId,
                        callerUser,
                        audioOnly
                );
            }

        }
    }

    private void onHasPermission(
            Context context,String targetId,String targetUser, String room, ArrayList<String> list,
            String callerId,String calerUser, Boolean audioOnly,String timer
    ) {
        boolean b = SkyEngineKit.Instance().startInCall(G.getInstance(), room, callerId,calerUser, audioOnly,timer);
        LogUtils.dTag(TAG, "onHasPermission b = " + b);
        if (b) {
            G.getInstance().setOtherUserId(callerId);
            LogUtils.dTag(TAG, "onHasPermission list.size() = " + list.size());
            if (list.size() == 1) {
                //以视频电话拨打，切换到音频或重走这里，结束掉上一个，防止对方挂断后，下边还有一个通话界面
                if (context instanceof CallSingleActivity) {
                    ((CallSingleActivity) context).finish();
                }
                CallSingleActivity.openActivity(context, callerId,calerUser,room, targetId,targetUser, false, audioOnly, true,timer);
            } else {
                // 群聊
            }
        } else {
            Activity activity = ActivityStackManager.getInstance().getTopActivity();
            activity.finish(); //销毁掉刚才拉起的
        }
    }

    // 权限拒绝
    private void onPermissionDenied(String room, String callerId) {
        SkyEngineKit.Instance().sendRefuseOnPermissionDenied(room, callerId);//通知对方结束
        Toast.makeText(G.getInstance(), "اجازه رد شد، امکان تماس وجود ندارد", Toast.LENGTH_SHORT).show();
    }

    private void shouldStartRing(boolean isComing) {
        if (isComing) {
            Uri uri = Uri.parse("android.resource://" + G.getInstance().getPackageName() + "/" + R.raw.incoming_call_ring);
            ringPlayer.play(G.getInstance(), uri, true, AudioManager.STREAM_RING);
        } else {
            Uri uri = Uri.parse("android.resource://" + G.getInstance().getPackageName() + "/" + R.raw.wr_ringback);
            ringPlayer.play(G.getInstance(), uri, true, AudioManager.STREAM_RING);
        }
    }

    private void shouldStopRing() {
        Log.d(TAG, "shouldStopRing begin");
        ringPlayer.stop();
    }
}

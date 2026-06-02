package com.shrazavi.dadmehr.core.voip;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.util.ActivityStackManager;

import java.util.Random;

/**
 * <pre>
 *     author : Jasper
 *     e-mail : 229605030@qq.com
 *     time   : 2021/02/01
 *     desc   :
 * </pre>
 */
public class CallForegroundNotification extends ContextWrapper {
    private static final String TAG = "CallForegroundNotificat";
    private static final String id = "channel1";
    private static final String name = "voip";
    private NotificationManager manager;

    public CallForegroundNotification(Context base) {
        super(base);
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = 26)
    public void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
        channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
        manager.createNotificationChannel(channel);
    }

    public void sendRequestIncomingPermissionsNotification(
            Context context, String room, String userList, String callerId, String callerUser, Boolean isAudioOnly
    ) {
        clearAllNotification();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        //发送广播，调起接听界面
        Intent intent = new Intent(context, ActivityStackManager.getInstance().getBottomActivity().getClass()); //栈底是MainActivity
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("room", room);
        intent.putExtra("isFromCall", true);
        intent.putExtra("audioOnly", isAudioOnly);
        intent.putExtra("callerUser", callerUser);
        intent.putExtra("callerId", callerId);
        intent.putExtra("userList", userList);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, new Random().nextInt(100), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(
                        "شما درحال دریافت تماس از" + callerUser + "هستید لطفا مجوز های لازم را صادر نمایید"
                                + (isAudioOnly ? "ضبط" :
                                "میکروفن و دوربین") + "مجوز های تماس : "
                )
                .setContentText("شما درحال دریافت تماس از" + callerUser + "هستید لطفا مجوز های لازم را صادر نمایید"
                        + (isAudioOnly ? "ضبط" :
                        "میکروفن و دوربین") + "مجوز های تماس : "
                )
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_CALL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true);
        } else {
            notificationBuilder.setContentIntent(fullScreenPendingIntent);
        }
//
        manager.notify(10086, notificationBuilder.build());
    }

    public void sendIncomingCallNotification(
            Context context, String targetId,String targetUser, String room,String callerId,String callerUser, Boolean isOutgoing,
            Boolean isAudioOnly, Boolean isClearTop,String timer
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            clearAllNotification();
            createNotificationChannel();
            Notification notification = getChannelNotificationQ(context, targetId,targetUser,room,callerId,callerUser, isOutgoing,  isAudioOnly, isClearTop,timer);
            manager.notify(10086, notification);
        }
    }

    private void clearAllNotification() {
        manager.cancelAll();
    }

    private Notification getChannelNotificationQ(
            Context context, String targetId,String targetUser,String room, String callerId, String callerUser, Boolean isOutgoing,
            Boolean isAudioOnly, Boolean isClearTop,String timer
    ) {
        Intent fullScreenIntent = CallSingleActivity.getCallIntent(context, targetId,targetUser,room, callerId,callerUser, isOutgoing, isAudioOnly, isClearTop,timer);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, new Random().nextInt(100), fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, id)
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setTicker(callerUser + "تماس ورودی")
                .setContentText(callerUser + "تماس ورودی")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(Notification.CATEGORY_CALL)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        Log.d(TAG, "getChannelNotificationQ");
        return notificationBuilder.build();
    }


}

package com.shrazavi.dadmehr.core.base;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.CallSession;
import com.shrazavi.dadmehr.core.EnumType;
import com.shrazavi.dadmehr.core.SkyEngineKit;
import com.shrazavi.dadmehr.core.consts.Urls;
import com.shrazavi.dadmehr.core.socket.IUserState;
import com.shrazavi.dadmehr.core.util.ActivityStackManager;
import com.shrazavi.dadmehr.core.util.MasterKeys;
import com.shrazavi.dadmehr.core.util.MyReceiver;
import com.shrazavi.dadmehr.core.util.StringUtil;
import com.shrazavi.dadmehr.core.voip.SingleCallFragment;
import com.shrazavi.dadmehr.core.voip.Utils;
import com.shrazavi.dadmehr.core.voip.VoipReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;


public class BasicActivity extends AppCompatActivity implements MyReceiver.ConnectivityReceiverListener {
    public static Socket socket;
    public static Handler handler;
    private static int userState;
    public static String myId;
    public static int ejra = 0;
    public static SharedPreferences preferences;
    public static String userid;
    public static String vu;
    private boolean isConnected;
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    public static String callid = "";
    public static String callcondition = "";
    public static String content = "";
    public static String imgprofile;

    MyReceiver myReceiver;

    {
        try {
            socket = IO.socket(Urls.nodeurl);//http://192.168.1.103:8000
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        // 添加Activity到堆栈
        ActivityStackManager.getInstance().onCreated(this);
        super.onCreate(savedInstanceState);
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            preferences = EncryptedSharedPreferences.create(
                    "secret_shared_prefs",
                    masterKeyAlias,
                    G.context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket.connect();
//        socket.emit("nickname", G.getInstance().getUsername());
        imgprofile = BasicActivity.preferences.getString("imgprofile", "not");
        socket.on("connected", handlerConnected);
        socket.on("peers", handlerPeers);
        socket.on("new_peer", handlerNew_Peer);
        socket.on("ice_candidate", handlerIce_Candidate);
        socket.on("remove_peer", handlerRemove_Peer);
        socket.on("offer", handlerOffer);
        socket.on("answer", handlerAnswer);
        socket.on("login_success", handlerLogin_Success);
        socket.on("invite", handlerInvite);
        socket.on("cancel", handlerCancel);
        socket.on("ring", handlerRing);
        socket.on("reject", handlerReject);
        socket.on("leave", handlerLeave);
        socket.on("audio", handlerAudio);
        socket.on("disconnected", handlerDisconnect);
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, filter);
        G.getInstance().setConnectivityListener(this);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = preferences.getString("type", "not");
        userid = preferences.getString("username", "not");
        content = preferences.getString("content", "not");

        Log.e("timer ==", preferences.getString("timer", "not") + "");

        handler = new Handler();
        JSONObject connected = new JSONObject();
        try {
            connected.put("from", userid);
            connected.put("message", "Connected");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject nickname = new JSONObject();
        try {
            nickname.put("userid", userid);
            nickname.put("vu", vu);
            nickname.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONObject notify = new JSONObject();
        try {
            notify.put("from", userid);
            notify.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("connected", connected);
        socket.emit("nickname", nickname);
    }


    public Emitter.Listener handlerPeers = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {

                    JSONObject jsonObject = (JSONObject) args[0];
                    String myuserId = "";
//                    JSONArray arr;
                    try {
//                        Log.e("Peers=",  jsonObject.getString("cont"));
                        myuserId = jsonObject.getString("you").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        handler.removeCallbacks(this);
                        if (currentSession != null) {
//                            arr = jsonObject.getJSONArray("connections");
//                            String js = com.alibaba.fastjson.JSONObject.toJSONString(arr, SerializerFeature.WriteClassName);
//                            String myId = (String) data.get("you");
//                            List<String> connections = (List<String>) com.alibaba.fastjson.JSONObject.parseArray(js, String.class);
                            String connections = jsonObject.getString("connections");
//            int roomSize = (int) data.get("roomSize");
                            currentSession.onJoinHome(myuserId, connections);
//                            if (ejra == 1) {
//                                ejra++;
//
//                            } else if (ejra==0) {
//                                ejra++;
//                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerNew_Peer = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userId = "";

                    try {
                        userId = jsonObject.getString("userid").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
//                this.userId=userId;
                            currentSession.newPeer(userId);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerIce_Candidate = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userId = "";
                    String id = "";
                    int label;
                    String candidate = "";

                    try {
                        userId = jsonObject.getString("fromID").toString();
                        id = jsonObject.getString("id").toString();
                        label = jsonObject.getInt("label");
                        candidate = jsonObject.getString("candidate").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onRemoteIceCandidate(userId, id, label, candidate);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerRemove_Peer = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String connect = "";

                    try {
                        connect = jsonObject.getString("message").toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerOffer = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String sdp = "";
                    String userId = "";
                    try {
                        sdp = jsonObject.getString("sdp").toString();
                        userId = jsonObject.getString("fromID").toString();

                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onReceiveOffer(userId, sdp);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerAnswer = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String sdp = "";
                    String userId = "";
                    try {
                        SingleCallFragment.cdt.start();
                        sdp = jsonObject.getString("sdp").toString();
                        userId = jsonObject.getString("fromID").toString();
                        callid = jsonObject.getString("callid").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("callid", callid);
                        editor.commit();

                        if (currentSession != null) {
                            currentSession.onReceiverAnswer(userId, sdp);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerLogin_Success = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];


//                        myId = jsonObject.getString("socketId").toString();
                    myId = userid;
                    userState = 1;
                    if (iUserState != null && iUserState.get() != null) {
                        iUserState.get().userLogin();
                    }


                }
            });
        }
    };
    public Emitter.Listener handlerInvite = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];

                    try {

                        String room = jsonObject.getString("room");
                        boolean audioOnly = jsonObject.getBoolean("audioOnly");
                        String callerId = jsonObject.getString("callerId");
                        String callerUser = jsonObject.getString("callerUser");
                        String targetId = jsonObject.getString("targetId");
                        String targetUser = jsonObject.getString("targetUser");
                        String userList = jsonObject.getString("userList");
                        String timer = jsonObject.getString("timer");
                        callid = jsonObject.getString("callid");
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("callid", callid);
                        editor.commit();
//                        String join = StringUtil.listToString(userList);
                        Intent intent = new Intent();
                        intent.putExtra("room", room);
                        intent.putExtra("audioOnly", audioOnly);
                        intent.putExtra("callerId", callerId);
                        intent.putExtra("callerUser", callerUser);
                        intent.putExtra("targetId", targetId);
                        intent.putExtra("targetUser", targetUser);
                        intent.putExtra("userList", userList);
                        intent.putExtra("timer", timer);
                        intent.setAction(Utils.ACTION_VOIP_RECEIVER);
                        intent.setComponent(new ComponentName(G.getInstance().getPackageName(), VoipReceiver.class.getName()));

                        G.getInstance().sendBroadcast(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerCancel = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String inviteId = "";

                    try {
                        ejra = 0;
                        inviteId = jsonObject.getString("inviteID").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onCancel(inviteId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerRing = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String fromID = "";

                    try {
                        fromID = jsonObject.getString("fromID").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onRingBack(fromID);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerReject = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userId = "";
                    int type;
                    try {
                        ejra = 0;
                        userId = jsonObject.getString("fromID").toString();
                        type = jsonObject.getInt("refuseType");
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onRefuse(userId, type);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerLeave = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userId = "";

                    try {
                        ejra = 0;
                        userId = jsonObject.getString("fromID").toString();

                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onLeave(userId);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlerAudio = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    String userId = "";

                    try {
                        userId = jsonObject.getString("fromID").toString();
                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onTransAudio(userId);
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
            handler.post(new Runnable() {
                @Override
                public void run() {


                    String userId = "";

                    try {
                        JSONObject jsonObject = (JSONObject) args[0];
                        userId = jsonObject.getString("fromID").toString();

                        CallSession currentSession = SkyEngineKit.Instance().getCurrentSession();
                        if (currentSession != null) {
                            currentSession.onDisConnect(userId, EnumType.CallEndReason.RemoteSignalError);
                        }
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


                        ActivityMain.txtconnection.setText(connect);
                        ActivityMain.prload.setVisibility(View.GONE);

//                            txtIsTyping.setText("");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlernotify = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    JSONObject jsonObject = (JSONObject) args[0];
                    int message = 0;
                    int call = 0;
                    int ticket = 0;
                    try {
                        message = jsonObject.getInt("message");
                        call = jsonObject.getInt("call");
                        ticket = jsonObject.getInt("ticket");

                        Log.e("notyfi","message="+message+"/"+"call="+call+"/"+"ticket="+ticket);
                        if (message != 0 || call != 0) {
                            creatnotification(BasicActivity.this, message, call);
                        }
//                            txtIsTyping.setText("");
                        if (ticket != 0) {
                            ActivityMain.btnnotifycount.setText(ticket + "");
                            ActivityMain.btnnotifycount.setVisibility(View.VISIBLE);
                        } else {
                            ActivityMain.btnnotifycount.setVisibility(View.GONE);
//                            ActivityMain.btnnotifycount.clearNumber();
                        }
                        if (message != 0) {
                            ActivityMain.badgechat.setNumber(message);
                            ActivityMain.badgechat.setVisible(true);
                        } else {
                            ActivityMain.badgechat.setVisible(false);
                            ActivityMain.badgechat.clearNumber();
                        }
                        if (call != 0) {
                            ActivityMain.badgecall.setNumber(call);
                            ActivityMain.badgecall.setVisible(true);
                        } else {
                            ActivityMain.badgecall.setVisible(false);
                            ActivityMain.badgecall.clearNumber();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };
    public Emitter.Listener handlermessagenotify = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    Log.e("count1=", count + "");
//                    count++;
//                    Log.e("count2=", count + "");
                    JSONObject jsonObject = (JSONObject) args[0];

                    int message = 0;
                    String from = "";
                    try {

                        message = jsonObject.getInt("message");
                        from = jsonObject.getString("from");

                        if (message != 0) {
                            messagenotification(BasicActivity.this, message, from);
                        }
//                            txtIsTyping.setText("");
                        ActivityMain.badgechat.setVisible(true);
                        ActivityMain.badgechat.setNumber(message);
//                        if(message!=0){
////                            ActivityMain.badgechat.setNumber(message);
//                        }else {
//                            ActivityMain.badgechat.setVisible(false);
//                            ActivityMain.badgechat.clearNumber();
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        checkConnection();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    private void checkConnection() {
        isConnected = MyReceiver.isConnected();
        if (isConnected) {
            socket.connect();
            socket.on("connected", handlerConnected);
            socket.on("notify", handlernotify);
            socket.on("messagenotify", handlermessagenotify);
            JSONObject connected = new JSONObject();
            try {
                connected.put("from", userid);
                connected.put("message", "Connected");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject nickname = new JSONObject();
            try {
                nickname.put("userid", userid);
                nickname.put("vu", vu);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject notify = new JSONObject();
            try {
                notify.put("from", userid);
                notify.put("content", content);
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        G.getInstance();
            socket.emit("nickname", nickname);
            socket.emit("notify", notify);
            socket.emit("connected", connected);


//            linearConnectionAvailable.setVisibility(View.VISIBLE);
//            linearNoConnectionAvailable.setVisibility(View.GONE);
//            Toast.makeText(this, "WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();

            ActivityMain.txtconnection.setText("Connected");
            ActivityMain.prload.setVisibility(View.GONE);


        } else {
            ActivityMain.txtconnection.setText("Waiting For Connection");
            ActivityMain.prload.setVisibility(View.VISIBLE);

//            socket.disconnect();
//            JSONObject disconnect = new JSONObject();
//            try {
//                disconnect.put("from", myId);
//                disconnect.put("to", toid);
//                disconnect.put("room", room);
//                disconnect.put("vu", vu);
//                disconnect.put("message", "Last Seen Recently");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            socket.emit("disconnect", disconnect);
//            linearConnectionAvailable.setVisibility(View.GONE);
//            linearNoConnectionAvailable.setVisibility(View.VISIBLE);
//            Toast.makeText(this, "Ooops! No WiFi/Mobile Networks Connected!", Toast.LENGTH_SHORT).show();

        }
    }


    private void messagenotification(Context context, int count, String from) {
        Intent intent = new Intent(context, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setShowBadge(false);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(count + " New Message")
                .setContentText(from)
                .setContentIntent(contentIntent)
                .setLights(0xff00ff00, 1000, 2000)
                .setAutoCancel(true)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setNumber(count);
        notificationManager.notify(1002, builder.build());

    }

    private void creatnotification(Context context, int message, int call) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, ActivityMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setVibrate(new long[]{0, 100, 100, 100, 100, 100})
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.logo)
                .setContentTitle(message + " New Message")
                .setContentText(call + " Missed Call")
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    //=====================================================================================================


    public static void createRoom(String room, String myId, int roomSize) {

        JSONObject create = new JSONObject();
        try {
            create.put("room", room);
            create.put("roomSize", roomSize);
            create.put("user", myId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("create", create);

    }


    public static void sendInvite(String room, String targetId, String targetUser, List<String> users, boolean audioOnly, String timer) {

        String join = StringUtil.listToString(users);


        JSONObject invite = new JSONObject();
        try {
            if (callcondition.equals("continue")) {
                invite.put("callid", callid);
                invite.put("condition", callcondition);
            }
            invite.put("room", room);
            invite.put("audioOnly", audioOnly);
            invite.put("callerId", myId);
            invite.put("callerUser", userid);
            invite.put("targetId", targetId);
            invite.put("targetUser", targetUser);
            invite.put("userlist", join);
            invite.put("timer", timer);
            invite.put("vu", vu);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("invite", invite);


    }


    public static void sendCancel(String mRoomId, List<String> users) {

        String join = StringUtil.listToString(users);


        JSONObject cancel = new JSONObject();
        try {
            cancel.put("room", mRoomId);
            cancel.put("inviteID", myId);
            cancel.put("userList", join);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("cancel", cancel);


    }


    public static void sendRing(String toId, String room) {


        JSONObject ring = new JSONObject();
        try {
            ring.put("room", room);
            ring.put("fromID", myId);
            ring.put("userID", toId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("ring", ring);


    }


    public static void sendJoin(String room) {


        JSONObject join = new JSONObject();
        try {
            join.put("room", room);
            join.put("user", myId);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("join", join);


    }


    public static void sendRefuse(String room, String inviteID, int refuseType) {

        JSONObject reject = new JSONObject();
        try {
            reject.put("room", room);
            reject.put("fromID", myId);
            reject.put("userID", inviteID);
            reject.put("refuseType", refuseType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("reject", reject);

    }


    public static void sendLeave(String room, String myid, String userId, String timer) {


        JSONObject leave = new JSONObject();
        try {
            leave.put("room", room);
            leave.put("fromID", myId);
            leave.put("userID", userId);
            leave.put("callid", callid);
            leave.put("timer", Long.parseLong(timer));
            leave.put("condition", callcondition);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("leave", leave);
    }

    public static void sendOffer(String userId, String sdp) {


        JSONObject offer = new JSONObject();
        try {
            offer.put("sdp", sdp);
            offer.put("fromID", myId);
            offer.put("userID", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("offer", offer);

    }


    public static void sendAnswer(String userId, String sdp) {
        SingleCallFragment.cdt.start();
        JSONObject answer = new JSONObject();
        try {
            answer.put("sdp", sdp);
            answer.put("fromID", myId);
            answer.put("userID", userId);
            answer.put("callid", callid);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("answer", answer);


    }

    public static void sendIceCandidate(String userId, String id, int label, String candidate) {


        JSONObject ice_candidate = new JSONObject();
        try {
            ice_candidate.put("userID", userId);
            ice_candidate.put("fromID", myId);
            ice_candidate.put("id", id);
            ice_candidate.put("label", label);
            ice_candidate.put("candidate", candidate);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("ice_candidate", ice_candidate);
    }


    public static void sendTransAudio(String userId) {

        JSONObject audio = new JSONObject();
        try {
            audio.put("fromID", myId);
            audio.put("userID", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("audio", audio);

    }


    public static void sendDisconnect(String room, String userId) {


        JSONObject disconnect = new JSONObject();
        try {
            disconnect.put("room", room);
            disconnect.put("fromID", myId);
            disconnect.put("userID", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("disconnected", disconnect);


    }

    public static void sendLogin(String userId) {

        JSONObject login = new JSONObject();
        try {

            login.put("userID", userId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("login", login);


    }

    //=====================================================================================================
    public static int getUserState() {
        return userState;
    }

    private static WeakReference<IUserState> iUserState;

    public static void addUserStateCallback(IUserState userState) {
        iUserState = new WeakReference<>(userState);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Toast.makeText(G.context, "on pause shode", Toast.LENGTH_SHORT).show();
        JSONObject disconnect = new JSONObject();
        try {
            disconnect.put("from", userid);
            disconnect.put("vu", vu);
            disconnect.put("message", "Last Seen Recently");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("disconnec", disconnect);
    }

    @Override
    protected void onDestroy() {
        ActivityStackManager.getInstance().onDestroyed(this);
        super.onDestroy();
//        Toast.makeText(G.context, "on destory shode", Toast.LENGTH_SHORT).show();
//        socket.off("connected", handlerConnected);
//        socket.off("peers", handlerPeers);
//        socket.off("new_peer", handlerNew_Peer);
//        socket.off("ice_candidate", handlerIce_Candidate);
//        socket.off("remove_peer", handlerRemove_Peer);
//        socket.off("offer", handlerOffer);
//        socket.off("answer", handlerAnswer);
//        socket.off("login_success", handlerLogin_Success);
//        socket.off("invite", handlerInvite);
//        socket.off("cancel", handlerCancel);
//        socket.off("ring", handlerRing);
//        socket.off("reject", handlerReject);
//        socket.off("leave", handlerLeave);
//        socket.off("audio", handlerAudio);
//        socket.off("disconnect", handlerDisconnect);

    }
}

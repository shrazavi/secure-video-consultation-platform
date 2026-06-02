package com.shrazavi.dadmehr;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.shrazavi.dadmehr.Activity.ActivityMain;

import org.json.JSONException;
import org.json.JSONObject;

public class ClosingService extends Service {
    private DefaultBinder mBinder;




    public void onCreate() {

        mBinder = new DefaultBinder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onTaskRemoved (Intent rootIntent){
//        alarmManager.cancel(alarmIntent);
        JSONObject disconnect = new JSONObject();
        try {
            disconnect.put("from", ActivityMain.userid);
            disconnect.put("vu", ActivityMain.vu);
            disconnect.put("message", "Last Seen Recently");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ActivityMain.socket.emit("disconnec", disconnect);
        // Handle application closing
        ActivityMain.socket.disconnect();
        Log.e("close","ok");
        this.stopSelf();
    }






}


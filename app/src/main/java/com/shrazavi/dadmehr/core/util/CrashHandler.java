package com.shrazavi.dadmehr.core.util;

import android.content.Intent;
import android.util.Log;

import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.core.CallSession;
import com.shrazavi.dadmehr.core.SkyEngineKit;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "MyUncaughtExceptionHand";

    @Override
    public void uncaughtException(@NotNull Thread thread, @NotNull Throwable ex) {
        SkyEngineKit gEngineKit = SkyEngineKit.Instance();
        CallSession session = gEngineKit.getCurrentSession();


        Log.e(TAG, "uncaughtException session = " + session);
        if (session != null) {
            gEngineKit.endCall();
        } else {
            Log.e("disconnect", "reason= " + "crash");
            gEngineKit.sendDisconnected(G.getInstance().getRoomId(), G.getInstance().getOtherUserId(),true);
        }
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        //如果异常时在AsyncTask里面的后台线程抛出的
        //那么实际的异常仍然可以通过getCause获得
        Throwable cause = ex;
        while (null != cause) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        //stacktraceAsString就是获取的carsh堆栈信息
        final String stacktraceAsString = result.toString();
        printWriter.close();
        restartApp();
    }

    private void restartApp() {
        Intent i = new Intent(G.getInstance(), ActivityMain.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        G.getInstance().startActivity(i);
    }


}
package com.shrazavi.dadmehr.net;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.security.crypto.EncryptedSharedPreferences;

import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.Activity.ActivitySignup;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.core.util.MasterKeys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;

public class AsyncaTaskSendCode extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    public String link = "";
    public String code;
    public String number;
    public String username;
    StringBuilder builder;
    public static SharedPreferences preferences;
    public Boolean isconected;
    public AsyncaTaskSendCode(String link, String number, String code,String username) {
        this.link = link;
        this.code = code;
        this.number = number;
        this.username = username;
    }

    @Override
    protected String doInBackground(String[] params) {
        try {
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            isconected=true;
            String data = URLEncoder.encode("number", "UTF8") + "=" + URLEncoder.encode(number, "UTF8");
            data += "&" + URLEncoder.encode("code", "UTF8") + "=" + URLEncoder.encode(code, "UTF8");
            URL url = new URL(link);
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);

            }
            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            isconected=false;

        }
        return "";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(ActivitySignup.dialogContext);
        progressDialog.setTitle("please wait");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(String code) {
        progressDialog.dismiss();
        Log.e("validcode",code);
        if(isconected==false){
            Toast.makeText(G.context, "لطفا اتصال دستگاه خود را به اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }else {
            if (!code.equals("ok")) {
                Toast.makeText(G.context, "اطلاعات وارد شده صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
            } else {
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

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("login", "ok");
                editor.putString("number", number);
                editor.putString("username", username);
                editor.commit();

                Intent intent = new Intent(G.context, ActivityMain.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                G.context.startActivity(intent);
                ActivityMain.restart(G.context,0);
            }

        }
    }
}

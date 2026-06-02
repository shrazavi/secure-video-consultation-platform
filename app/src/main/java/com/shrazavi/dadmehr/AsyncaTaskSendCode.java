package com.shrazavi.dadmehr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shrazavi.dadmehr.Activity.ActivityLaw;
import com.shrazavi.dadmehr.Activity.ActivitySignup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class AsyncaTaskSendCode extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    public String link = "";
    public String code;
    public String number;
    public String username;
    StringBuilder builder;
//    public static SharedPreferences preferences;
    public Boolean isconected;
    String name, vu, melli, ostan, shahr, parvane, experience, pass;
    int type, base;

    public AsyncaTaskSendCode(String link,
                              String number,
                              String code,
                              String username,
                              String name,
                              String vu,
                              String melli,
                              String ostan,
                              String shahr,
                              String parvane,
                              String experience,
                              int type,
                              int base,
                              String pass) {

        this.link = link;
        this.number = number;
        this.code = code;
        this.username = username;
        this.name = name;
        this.vu = vu;
        this.melli = melli;
        this.ostan = ostan;
        this.shahr = shahr;
        this.parvane = parvane;
        this.experience = experience;
        this.type = type;
        this.base = base;
        this.pass = pass;
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
            isconected = true;
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
            isconected = false;

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
        Log.e("validcode", code);
        if (isconected == false) {
            Toast.makeText(G.context, "لطفا اتصال دستگاه خود را به اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        } else {
            if (!code.equals("ok")) {
                Toast.makeText(G.context, "اطلاعات وارد شده صحیح نمی باشد.", Toast.LENGTH_SHORT).show();
            } else {


                Intent intent = new Intent(G.context, ActivityLaw.class);
                intent.putExtra("status", "sign");
                intent.putExtra("name", name);
                intent.putExtra("vu", "vl");
                intent.putExtra("melli", melli);
                intent.putExtra("ostan", ostan);
                intent.putExtra("shahr", shahr);
                intent.putExtra("parvane", parvane);
                intent.putExtra("experience", experience);
                intent.putExtra("type", type);
                intent.putExtra("base", base);
                intent.putExtra("pass", pass);
                intent.putExtra("number", number);
                intent.putExtra("username", username);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                G.context.startActivity(intent);

            }

        }
    }
}

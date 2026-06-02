package com.shrazavi.dadmehr.net;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.shrazavi.dadmehr.Activity.ActivitySignup;
import com.shrazavi.dadmehr.G;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

/**
 * Created by General on 6/23/2017.
 */

public class AsyncaTaskSendNumber extends AsyncTask<String, Void, String> {
    ProgressDialog progressDialog;
    public Boolean isconected;
    public String link="";
    public String number;
    StringBuilder builder;

    public AsyncaTaskSendNumber(String link, String number){

        this.link=link;
        this.number=number;


    }


    @Override
    protected String doInBackground(String[] params) {
        try{
            Socket sock = new Socket();
            sock.connect(new InetSocketAddress("8.8.8.8", 53), 1500);
            sock.close();
            isconected=true;
            String data= URLEncoder.encode("number","UTF8")+"="+ URLEncoder.encode(number,"UTF8");


            URL url=new URL(link);
            Log.e("link",link);
            URLConnection connection=url.openConnection();

            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            writer.write(data);
            writer.flush();

            Log.e("number",number);


            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            builder=new StringBuilder();

            String line=null;

            while((line=reader.readLine())!=null){
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
        progressDialog=new ProgressDialog(ActivitySignup.dialogContext);
        progressDialog.setTitle("please wait");
      //  progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("num",number);
    }

    @Override
    protected void onPostExecute(String code) {
        if(isconected==false){
            Toast.makeText(G.context, "لطفا اتصال دستگاه خود را به اینترنت بررسی کنید", Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }
}

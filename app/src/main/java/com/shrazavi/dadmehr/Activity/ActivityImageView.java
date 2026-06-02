package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.DownloadManager;
import com.shrazavi.dadmehr.FullImage;
import com.shrazavi.dadmehr.R;
import com.squareup.picasso.Picasso;

public class ActivityImageView extends AppCompatActivity {
    FullImage imgchatview;
    String imgurl;
    Button btndownload;
    String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageview);
        imgchatview=(FullImage) findViewById(R.id.img_chat_view);
        imgurl = (String) getIntent().getExtras().get("imgurl");

        Log.e("imgurl",imgurl);
        btndownload=(Button) findViewById(R.id.btn_iv_download);
        if((Boolean) getIntent().getExtras().get("down")){
            btndownload.setVisibility(View.VISIBLE);
        }else {
            btndownload.setVisibility(View.GONE);
        }
        Picasso.with(ActivityImageView.this).load(imgurl).into(imgchatview);
//        Glide.with(ActivityImageView.this).load(imgurl).placeholder(R.drawable.backdown).error(R.drawable.backdown).into(imgchatview);
        path= Environment.getExternalStorageDirectory() + "/Darmano/Image/";
        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DownloadManager(ActivityImageView.this, imgurl,path);
            }
        });
    }
}

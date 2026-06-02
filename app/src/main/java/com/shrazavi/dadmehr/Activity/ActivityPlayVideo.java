package com.shrazavi.dadmehr.Activity;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.DownloadManager;
import com.shrazavi.dadmehr.R;

public class ActivityPlayVideo extends AppCompatActivity {
    Button btndownload;
    VideoView videoview;
    int pause;
    String vidurl,path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);
        videoview =(VideoView)findViewById(R.id.vidopen);
        vidurl = (String) getIntent().getExtras().get("vidurl");

        Log.e("vidopenurl",vidurl);
        videoview.setMediaController(new MediaController(this));
        videoview.requestFocus();
        Uri video = Uri.parse(vidurl);
        videoview.setVideoURI(video);
        videoview.start();
//        Environment.getExternalStorageDirectory() + "/CodePlayon/"
        path= Environment.getExternalStorageDirectory() + "/Darmano/Video/";




        btndownload=(Button) findViewById(R.id.btn_pv_download);
//        btnpause=(Button) findViewById(R.id.btn_pause);
//
//        btnpause.setVisibility(View.GONE);
//        pause=0;
//        if(pause==1){
//            btnpause.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    btnplay.setVisibility(View.VISIBLE);
//                    btnpause.setVisibility(View.GONE);
//                    videoview.pause();
//                    pause=0;
//                }
//            });}else{
        btndownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new DownloadManager(ActivityPlayVideo.this, vidurl,path);
            }
        });




        }



    }


package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.R;

public class ActivityWebView extends AppCompatActivity {
    WebView wv;
    String pdf_url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        pdf_url = (String) getIntent().getExtras().get("pdfurl");
        wv = (WebView) findViewById(R.id.webview);
        Log.e("pdfurl",pdf_url);
//        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdf_url);
    }


}

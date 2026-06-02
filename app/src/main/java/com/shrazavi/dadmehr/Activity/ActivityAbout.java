package com.shrazavi.dadmehr.Activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;

public class ActivityAbout extends AppCompatActivity {
    ImageView imgshamed;
    TextView txtemail, txttelegram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtemail = (TextView) findViewById(R.id.txt_about_email);
        txttelegram = (TextView) findViewById(R.id.txt_about_telegram);
        imgshamed = (ImageView) findViewById(R.id.img_about_shamed);

        txttelegram.setText("@shrazavi510");
        txttelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://t.me/Shrazavi510"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        imgshamed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://logo.saramad.ir/verify.aspx?CodeShamad=1-1-869641-63-0-1"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        txtemail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("dadmehr_email", txtemail.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ActivityAbout.this, "کپی شد", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }
}

package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BasicActivity;

public class ActivityOption extends AppCompatActivity {
    LinearLayout laysetting,laytime,laylokation,laytariff,laypassword,layblockuser,laylaw,layhelp,layabout,laylogout;
    SharedPreferences.Editor editor;
//    public SharedPreferences preferences;
    CardView crdvl;
    String vu="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        laysetting= (LinearLayout) findViewById(R.id.lay_op_setting);
        laytime=    (LinearLayout) findViewById(R.id.lay_op_time_work);
        laylokation=(LinearLayout) findViewById(R.id.lay_op_location);
        laytariff=(LinearLayout) findViewById(R.id.lay_op_tariff);
        laypassword=(LinearLayout) findViewById(R.id.lay_op_password);
        layblockuser= (LinearLayout) findViewById(R.id.lay_op_blockuser);
        laylaw=    (LinearLayout) findViewById(R.id.lay_op_law);
        layhelp=    (LinearLayout) findViewById(R.id.lay_op_help);
        layabout=(LinearLayout) findViewById(R.id.lay_op_about);
        laylogout=(LinearLayout) findViewById(R.id.lay_op_logout);
        crdvl=(CardView) findViewById(R.id.crd_op_vl);
//        preferences = PreferenceManager.getDefaultSharedPreferences(ActivityOption.this);
        editor = BasicActivity.preferences.edit();
        vu = BasicActivity.vu;
        if(vu.equals("vl")){
            crdvl.setVisibility(View.VISIBLE);
        }else {
            crdvl.setVisibility(View.GONE);
        }
        laysetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityOption.this, ActivitySetting.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                ActivityOption.this.finish();
            }
        });
        laytime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ActivityOption.this, ActivityTimework.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                ActivityOption.this.finish();
            }
        });
        laylokation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ActivityOption.this, ActivityLocation.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                ActivityOption.this.finish();
            }
        });
        laytariff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ActivityOption.this, ActivityTariff.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                ActivityOption.this.finish();
            }
        });
        laypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(ActivityOption.this, ActivityPassword.class);
                intent4.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent4.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOption.this.startActivity(intent4);
                ActivityOption.this.finish();
            }
        });
        layblockuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ActivityOption.this, ActivityBlockuser.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent1);
                ActivityOption.this.finish();
            }
        });
        laylaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ActivityOption.this, ActivityLaw.class);
                intent2.putExtra("status", "option");
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                ActivityOption.this.finish();
            }
        });
        layhelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ActivityOption.this, ActivityHelp.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent2);
                ActivityOption.this.finish();
            }
        });
        layabout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(ActivityOption.this, ActivityAbout.class);
                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent3);
                ActivityOption.this.finish();
            }
        });
        laylogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.remove("login");
                editor.apply();
                editor.commit();
                Log.e("loginop", BasicActivity.preferences.getString("login", "not") + "");
                Intent intent6 = new Intent(ActivityOption.this, ActivityStart.class);
                intent6.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent6);
                android.os.Process.killProcess(android.os.Process.myPid());
                ActivityOption.this.finish();

//                finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);
            }
        });





    }




}

package com.shrazavi.dadmehr.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.util.MasterKeys;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;


public class ActivityStart extends AppCompatActivity {
    SecretKeySpec secretKey1, secretKey2, secretKey3, secretKey4, secretKey5;
    Button btnstart;
    public SharedPreferences preferences;
    public final int MY_REQUEST_CODE = 1;
    private static final int PERMISSIONS_REQUEST_READ_PHONE_STATE = 999;
    String login;
    private TelephonyManager mTelephonyManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //  txtStart = (TextView) findViewById(R.id.txtStart);
//        if (Build.VERSION.SDK_INT in 21..29) {
//            window.statusBarColor = Color.TRANSPARENT
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.decorView.systemUiVisibility =
//                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
//
//        } else if (Build.VERSION.SDK_INT >= 30) {
//            window.statusBarColor = Color.TRANSPARENT
//            // Making status bar overlaps with the activity
//            WindowCompat.setDecorFitsSystemWindows(window, false)
//        }
        btnstart = (Button) findViewById(R.id.btn_start);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
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
        login = preferences.getString("login", "not");
        Log.e("login", login + "");
        if (isRooted()) {
//            Toast.makeText(ActivityStart.this,"YOUR OS IS Root",Toast.LENGTH_SHORT).show();

        }
        secretKey1 = SymmetricAlgorithmAES.setUpSecretKey();
        secretKey2 = SymmetricAlgorithmAES.setUpSecretKey();
        secretKey3 = SymmetricAlgorithmAES.setUpSecretKey();
        secretKey4 = SymmetricAlgorithmAES.setUpSecretKey();
        secretKey5 = SymmetricAlgorithmAES.setUpSecretKey();
        btnstart.setTypeface(G.face);

        Log.e("k5", preferences.getString("k5", "not"));
        //   String img=getIntent().getExtras().getString("img");
        //   Toast.makeText(this, img, Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= 23) {
            checkMyPermissions();
            //Toast.makeText(ActivityStart.this,"YOUR OS IS Upper THEN 23",Toast.LENGTH_SHORT).show();
        } else {

            btnstart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("k1", Base64.encodeToString(secretKey1.getEncoded(), Base64.NO_WRAP));
                    editor.putString("k2", Base64.encodeToString(secretKey2.getEncoded(), Base64.NO_WRAP));
                    editor.putString("k3", Base64.encodeToString(secretKey3.getEncoded(), Base64.NO_WRAP));
                    editor.putString("k4", Base64.encodeToString(secretKey4.getEncoded(), Base64.NO_WRAP));
                    editor.putString("k5", Base64.encodeToString(secretKey5.getEncoded(), Base64.NO_WRAP));
                    editor.putString("IM", getDeviceImei());
                    editor.commit();

                    Intent intent = new Intent(ActivityStart.this, ActivityLogin.class);
                    startActivity(intent);
                    ActivityStart.this.finish();
                }
            });


            String id = preferences.getString("login", "not");
            if (id.equals("not")) {

                btnstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("k1", Base64.encodeToString(secretKey1.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k2", Base64.encodeToString(secretKey2.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k3", Base64.encodeToString(secretKey3.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k4", Base64.encodeToString(secretKey4.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k5", Base64.encodeToString(secretKey5.getEncoded(), Base64.NO_WRAP));
                        editor.putString("IM", getDeviceImei());
                        editor.commit();
                        Intent intent = new Intent(G.context, ActivityLogin.class);
                        startActivity(intent);
                        ActivityStart.this.finish();
                    }
                });


            } else {
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
                ActivityStart.this.finish();
            }

            //Toast.makeText(ActivityStart.this,"YOUR OS IS LESS THEN 23",Toast.LENGTH_SHORT).show();

        }


    }

    private void checkMyPermissions() {
        if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);

        } else if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);

        } else if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);


        } else if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);


        } else if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        } else if (ContextCompat.checkSelfPermission(ActivityStart.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ActivityStart.this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA}, MY_REQUEST_CODE);
        } else {
            //Toast.makeText(MainActivity.this,"You accepted before",Toast.LENGTH_SHORT).show();
            String id = preferences.getString("login", "not");
            if (id.equals("not")) {
                btnstart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("k1", Base64.encodeToString(secretKey1.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k2", Base64.encodeToString(secretKey2.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k3", Base64.encodeToString(secretKey3.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k4", Base64.encodeToString(secretKey4.getEncoded(), Base64.NO_WRAP));
                        editor.putString("k5", Base64.encodeToString(secretKey5.getEncoded(), Base64.NO_WRAP));
                        editor.putString("IM", getDeviceImei());
                        editor.commit();
                        Intent intent = new Intent(G.context, ActivityLogin.class);
                        startActivity(intent);
                        finish();
                    }
                });
            } else {
                Intent intent = new Intent(G.context, ActivityMain.class);
                startActivity(intent);
                ActivityStart.this.finish();

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ActivityStart.this, "Thanks for your permission", Toast.LENGTH_SHORT).show();


                    String id = preferences.getString("login", "not");
                    if (id.equals("not")) {
                        btnstart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("k1", Base64.encodeToString(secretKey1.getEncoded(), Base64.NO_WRAP));
                                editor.putString("k2", Base64.encodeToString(secretKey2.getEncoded(), Base64.NO_WRAP));
                                editor.putString("k3", Base64.encodeToString(secretKey3.getEncoded(), Base64.NO_WRAP));
                                editor.putString("k4", Base64.encodeToString(secretKey4.getEncoded(), Base64.NO_WRAP));
                                editor.putString("k5", Base64.encodeToString(secretKey5.getEncoded(), Base64.NO_WRAP));
                                editor.putString("IM", getDeviceImei());

                                editor.commit();
                                Intent intent = new Intent(G.context, ActivityLogin.class);
                                startActivity(intent);
                                ActivityStart.this.finish();
                            }
                        });
                    } else {
                        Intent intent = new Intent(G.context, ActivityMain.class);
                        startActivity(intent);
                        ActivityStart.this.finish();
                    }

                } else {


                    Toast.makeText(ActivityStart.this, "access dinied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getDeviceImei() {
        if (Build.VERSION.SDK_INT >= 29) {
            int min1 = 100000000;
            int max1 = 999999999;
            int min2 = 100000;
            int max2 = 999999;
            int rand1 = new Random().nextInt((max1 - min1) + 1) + min1;
            int rand2 = new Random().nextInt((max2 - min2) + 1) + min2;
            return rand1 + "" + rand2;
        } else {
            mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String deviceid = mTelephonyManager.getDeviceId();
            return deviceid;
        }
    }

    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }
}

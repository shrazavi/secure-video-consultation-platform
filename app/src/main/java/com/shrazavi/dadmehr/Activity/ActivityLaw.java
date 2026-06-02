package com.shrazavi.dadmehr.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.security.crypto.EncryptedSharedPreferences;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterLaw;
import com.shrazavi.dadmehr.DataClass.Lawmore;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.shrazavi.dadmehr.core.util.MasterKeys;
import com.shrazavi.dadmehr.core.util.SymmetricAlgorithmAES;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityLaw extends AppCompatActivity {
    static Retrofitinformation RI;
        public SharedPreferences preferences;
    static ArrayList<Lawmore> title = new ArrayList<>();
    Button btnok;
    String myid;
    static String content;
    String enk;
    String vu;
    SecretKeySpec Key;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAdapterLaw recyclerAdapterLaw;
    TextView txtcheck;
    CheckBox checklaw;
    String name = "", melli = "", ostan = "", shahr = "", parvane = "", experience = "", pass = "", number = "",data="";
    int base = 0, type = 0;
    public String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        title.clear();
        title.clone();
        title.add(new Lawmore("قوانین وکلا", 0, "lawyer"));
        title.add(new Lawmore("قوانین کاربران", 0, "user"));
        title.add(new Lawmore("قوانین پرداخت", 0, "pay"));
        title.add(new Lawmore("قوانین تعیین وقت قبلی", 0, "reserve"));
        String masterKeyAlias = null;
        btnok = (Button) findViewById(R.id.btn_law_ok);
        checklaw = (CheckBox) findViewById(R.id.check_law);
        txtcheck = (TextView) findViewById(R.id.txt_law_check);
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
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//       preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
//        myid = preferences.getString("username", "empty");
//        vu = preferences.getString("type", "empty");
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
        Log.e("info vu=", vu + "");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
//        content = SymmetricAlgorithmAES.encrypt(preferences.getString("IM", "not"), preferences.getString("k5", "not"));


        if (status.equals("sign")) {
            myid = (String) getIntent().getExtras().get("username");
            name = (String) getIntent().getExtras().get("name");
            vu = (String) getIntent().getExtras().get("vu");
            melli = (String) getIntent().getExtras().get("melli");
            ostan = (String) getIntent().getExtras().get("ostan");
            shahr = (String) getIntent().getExtras().get("shahr");
            parvane = (String) getIntent().getExtras().get("parvane");
            experience = (String) getIntent().getExtras().get("experienc");
            type = (Integer) getIntent().getExtras().get("type");
            base = (Integer) getIntent().getExtras().get("base");
            pass = (String) getIntent().getExtras().get("pass");
            number = (String) getIntent().getExtras().get("number");
        } else {
            checklaw.setVisibility(View.GONE);
            txtcheck.setVisibility(View.GONE);
        }


        btnok.setVisibility(View.GONE);
        checklaw.setChecked(false);
        checklaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {
                                                        btnok.setVisibility(View.VISIBLE);
                                                    } else {
                                                        btnok.setVisibility(View.GONE);
                                                    }

                                                }
                                            }
        );
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vu.equals("vl")) {
                    data = SymmetricAlgorithmAES.encrypt(myid+
                            "/"+name+
                            "/"+melli+
                            "/"+ostan+
                            "/"+shahr+
                            "/"+parvane+
                            "/"+experience+
                            "/"+type+
                            "/"+base+
                            "/"+pass+
                            "/"+number, preferences.getString("k5", "not"));
                    Call<MessageSignup> callvl = RI.signupvl(BasicActivity.content,data);
                    callvl.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Log.e("signupvl", response.body().getMessage());
                            Boolean status = response.body().getStatus();
                            if (status) {
//                             preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("login", "ok");
                                editor.putString("number", number);
                                editor.putString("username", myid);
                                editor.putString("type", vu);
                                editor.putString("content", response.body().getContent());
                                editor.commit();
                                Intent intent = new Intent(G.context, ActivityMain.class);
                                startActivity(intent);
                                ActivityLaw.this.finish();
                                ActivityMain.restart(G.context, 0);
                            } else {

                            }
                        }

                        @Override
                        public void onFailure(Call<MessageSignup> call, Throwable t) {

                        }

                    });
                } else {
                    data = SymmetricAlgorithmAES.encrypt(myid+
                            "/"+name+
                            "/"+melli+
                            "/"+pass+
                            "/"+number, preferences.getString("k5", "not"));
                    Call<MessageSignup> calluser = RI.signupuser(content, data);
                    calluser.enqueue(new Callback<MessageSignup>() {
                        @Override
                        public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                            Log.e("signupvl", response.body().getMessage());
                            Boolean status = response.body().getStatus();
                            if (status) {
//                                preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("login", "ok");
                                editor.putString("number", number);
                                editor.putString("username", myid);
                                editor.putString("type", vu);
                                editor.putString("content", response.body().getContent());
                                editor.commit();
                                Intent intent = new Intent(G.context, ActivityMain.class);
                                startActivity(intent);
                                ActivityLaw.this.finish();
                                ActivityMain.restart(G.context, 0);

                            } else {


                            }
                        }

                        @Override
                        public void onFailure(Call<MessageSignup> call, Throwable t) {

                        }

                    });

                }


            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.rec_law_lawyer);
        linearLayoutManager = new LinearLayoutManager(G.context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapterLaw = new RecyclerAdapterLaw(ActivityLaw.this, title));

    }


}

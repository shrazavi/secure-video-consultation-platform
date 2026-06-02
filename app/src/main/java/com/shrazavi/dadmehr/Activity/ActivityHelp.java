package com.shrazavi.dadmehr.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterHelp;
import com.shrazavi.dadmehr.DataClass.Helpmore;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

public class ActivityHelp extends AppCompatActivity {
    static Retrofitinformation RI;
//    public SharedPreferences preferences;
    static ArrayList<Helpmore> title = new ArrayList<>();
    Button btnok;
    String myid;
    static String content;
    String enk;
    String vu;
    SecretKeySpec Key;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerAdapterHelp recyclerAdapterHelp;
    CheckBox checkhelp;
TextView txtckeck,txttitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_law);
        title.clear();
        title.clone();
        title.add(new Helpmore("راهنمای ثبت نام", 0, "signup"));
        title.add(new Helpmore("راهنمای ورود", 0, "login"));
        title.add(new Helpmore("راهنمای کاربران", 0, "user"));
        title.add(new Helpmore("راهنمای وکلا", 0, "lawyer"));

        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        myid = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
        Log.e("info vu=", vu + "");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + myid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        btnok = (Button) findViewById(R.id.btn_law_ok);
        checkhelp = (CheckBox) findViewById(R.id.check_law);
        txtckeck = (TextView) findViewById(R.id.txt_law_check);
        txttitle = (TextView) findViewById(R.id.txt_law_title);

        txttitle.setText("راهنمای دادمهر");

        txtckeck.setVisibility(View.GONE);
        btnok.setVisibility(View.GONE);
        checkhelp.setVisibility(View.GONE);
//        checklaw.setChecked(false);
//        checklaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//             @Override
//             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                 if (isChecked) {
//                     btnok.setVisibility(View.VISIBLE);
//                 } else {
//                     btnok.setVisibility(View.GONE);
//                 }
//
//             }
//         }
//        );
//        btnok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(G.context, ActivityMain.class);
//                startActivity(intent);
//                ActivityHelp.this.finish();
//                ActivityMain.restart(G.context,0);
//            }
//        });
        recyclerView = (RecyclerView) findViewById(R.id.rec_law_lawyer);
        linearLayoutManager = new LinearLayoutManager(G.context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerAdapterHelp = new RecyclerAdapterHelp(ActivityHelp.this, title));

    }


}

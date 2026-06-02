package com.shrazavi.dadmehr.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterBlackList;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityBlockuser extends AppCompatActivity {
    static ArrayList<String> userlist;
    //    Retrofitinformation RInode;
    String content;
    String enk;
    SecretKeySpec Key;
    String username = "";
    String vu = "";
   // public SharedPreferences preferences;
    static RecyclerView recblock;
    public static TextView txtblock;
    public static RecyclerAdapterBlackList recyclerAdapterBlackList;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockuser);


//        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
       // BasicActivity.preferences = PreferenceManager.getDefaultSharedPreferences(ActivityBlockuser.this);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        recblock = (RecyclerView) findViewById(R.id.rec_blocklist);
        txtblock = (TextView) findViewById(R.id.txt_block_no);
        txtblock.setVisibility(View.GONE);
        userlist = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(G.context);
        recblock.setHasFixedSize(true);
        recblock.setLayoutManager(linearLayoutManager);
        setdata(content, username, ActivityBlockuser.this);


    }

    public static void setdata(String content, String username, Context context) {

        Retrofitinformation RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        Call<Vakil> callvakil = RInode.getvakil(content, username, username);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                String blockusers = response.body().getBlockusers();
                if (blockusers.isEmpty()) {
                    txtblock.setVisibility(View.VISIBLE);
                    recblock.setVisibility(View.GONE);
                } else {
                    recblock.setVisibility(View.VISIBLE);
                    txtblock.setVisibility(View.GONE);
                    String[] arrblock = blockusers.split(",");
                    userlist = new ArrayList<String>(Arrays.asList(arrblock));
                    recblock.setAdapter(recyclerAdapterBlackList = new RecyclerAdapterBlackList(userlist, context, content));
                    recyclerAdapterBlackList.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {
                Log.e("error???", t + "");
            }
        });


    }

}

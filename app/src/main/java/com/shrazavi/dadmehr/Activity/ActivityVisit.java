package com.shrazavi.dadmehr.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterVisit;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.DataClass.Visitday;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityVisit extends AppCompatActivity {
    static ArrayList<Visitday> daylist;
    Retrofitinformation RInode;
    String content;
    String enk;
    SecretKeySpec Key;
    String username = "";
    String vu = "";
    String toid = "";
    String imgurl = "";
    TextView txtname, txtexperience, txtbase, txttype, txtprof;
    ImageView imgprof;
    CardView crdprof;
//    public SharedPreferences preferences;
    static RecyclerView recvisit;
    public static TextView txtblock;
    public static RecyclerAdapterVisit recyclerAdapterVisit;
    LinearLayoutManager linearLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit);

        RInode = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
//        preferences = PreferenceManager.getDefaultSharedPreferences(ActivityVisit.this);
        username = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        byte[] data = Base64.decode(enk, Base64.DEFAULT);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + username + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;

        toid = (String) getIntent().getExtras().get("username");
        crdprof = (CardView) findViewById(R.id.crd_color_prof);
        txtprof = (TextView) findViewById(R.id.txt_color_prof);
        recvisit = (RecyclerView) findViewById(R.id.rec_visit);
        txtname = (TextView) findViewById(R.id.txt_visit_name);
        txttype = (TextView) findViewById(R.id.txt_visit_type);
        txtbase = (TextView) findViewById(R.id.txt_visit_base);
        txtexperience = (TextView) findViewById(R.id.txt_visit_experience);
        imgprof = (ImageView) findViewById(R.id.app_bar_image_visit);
        daylist = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(ActivityVisit.this);
        recvisit.setHasFixedSize(true);
        recvisit.setLayoutManager(linearLayoutManager);

        Call<Vakil> callvakil = RInode.getvakil(content, toid, username);
        callvakil.enqueue(new Callback<Vakil>() {
            @Override
            public void onResponse(Call<Vakil> call, Response<Vakil> response) {
//                    G.income = Integer.parseInt(response.body().getIncome());
//                income = Integer.parseInt(response.body().getIncome());
                String day = response.body().getActivedate();
                String[] arrday = day.split("  ");
//                            Log.e("","");
                ArrayList<String> visitday = new ArrayList<String>(Arrays.asList(arrday));
//                Log.e("dayarr",response.body().getVisitdate()+"");
                for (int i = 0; i < visitday.size(); i++) {
                    Visitday vs = new Visitday();
                    vs.setStarttime(response.body().getStarttime());
                    vs.setEndtime(response.body().getEndtime());
//                    Log.e("dayarr",visitday.get(i)+"");
                    vs.setVisitday(visitday.get(i));
                    daylist.add(vs);
                }

                recvisit.setAdapter(recyclerAdapterVisit = new RecyclerAdapterVisit(daylist, ActivityVisit.this));
                recyclerAdapterVisit.notifyDataSetChanged();
                txtname.setText(response.body().getName());
                txtexperience.setText("با " + response.body().getExperience() + " سال سابقه");

//                Log.e("type vl=", response.body().getType()+ "");
                if (response.body().getType().equals("1")) {
                    txttype.setText("کانون وکلای دادگستری " + response.body().getOstan());
                    if (response.body().getBase().equals("1")) {
                        txtbase.setText("وکیل پایه یک دادگستری");
                    } else {
                        txtbase.setText("وکیل پایه دوم دادگستری");
                    }
                } else {
                    txttype.setText("مرکز مشاوران دادگستری استان " + response.body().getOstan());
                }
                imgurl = G.nodeurl + "/" + response.body().getProfile();
                if (imgurl.equals(G.nodeurl + "/" + "empty")) {
                    char ch1 = toid.toUpperCase().charAt(0);
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(250), rnd.nextInt(250), rnd.nextInt(250));
//                            holder.imgProf.setVisibility(View.GONE);
                    txtprof.setText(ch1+"");
                    crdprof.setCardBackgroundColor(color);
                } else {
                    Picasso.with(G.context).load(imgurl).transform(new ImageProfile()).into(imgprof);

                }

            }

            @Override
            public void onFailure(Call<Vakil> call, Throwable t) {

            }
        });


    }


}

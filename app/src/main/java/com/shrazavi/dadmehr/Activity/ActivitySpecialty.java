package com.shrazavi.dadmehr.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Adapter.RecyclerAdapterlawyer;
import com.shrazavi.dadmehr.DataClass.Lawyer;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivitySpecialty extends AppCompatActivity {

    RecyclerView recyclerView;
    String url = G.phpurl + "/getimageprofile.php";
    String company,specialty;
    Retrofitinformation RI;
    String content;
    String enk;
    SecretKeySpec Key;
    ArrayList<Lawyer> infos;
//    public SharedPreferences preferences;
    public String type = "";
    public static Context context;
    String vu;

    public String userid = "";
    public static SharedPreferences sharedPreferences;
    public RecyclerAdapterlawyer recyclerAdapterlawyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialty);

        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        recyclerView=(RecyclerView) findViewById(R.id.recycler_ex_lawyer);
        recyclerView.setLayoutManager(new GridLayoutManager(G.context, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        infos = new ArrayList<>();
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        userid = BasicActivity.userid;
        vu = BasicActivity.vu;
//        enk = BasicActivity.preferences.getString("k5", "not");
//        company = getResources().getString(R.string.developer);
//        byte[] data = Base64.decode(enk, Base64.NO_WRAP);
//        Key=new SecretKeySpec(data, 0, data.length, "AES");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+userid+"-"+R.string.developer), Base64.NO_WRAP);
        specialty = (String) getIntent().getExtras().get("specialty");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + company, enk);
        content=BasicActivity.content;

//        Log.e("enk1",enk);
//        Log.e("content1",content+"");
        Call<ArrayList<Lawyer>> callexperience = RI.getspecialty(content,specialty,userid);
        callexperience.enqueue(new Callback<ArrayList<Lawyer>>() {
            @Override
            public void onResponse(Call<ArrayList<Lawyer>> call, Response<ArrayList<Lawyer>> response) {

                infos=response.body();
                recyclerView.setAdapter(recyclerAdapterlawyer = new RecyclerAdapterlawyer(infos, G.context));
                recyclerAdapterlawyer.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Lawyer>> call, Throwable t) {

            }
        });


    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }



    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}

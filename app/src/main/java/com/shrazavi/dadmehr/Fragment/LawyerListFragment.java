package com.shrazavi.dadmehr.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
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


public class LawyerListFragment extends Fragment {

    String[] lawyerlist;
    public static AutoCompleteTextView edtsearch;
    RecyclerView recyclerView;
    String url = G.phpurl + "/getimageprofile.php";
    String company;
    Retrofitinformation RI;
    String content;
    String enk;
    SecretKeySpec Key;
    ArrayList<Lawyer> infos;
//    public SharedPreferences preferences;
    public String type = "";
    public static Context context;
    String vu;
    public static TextInputLayout laysearch;
    public String userid = "";
    public static SharedPreferences sharedPreferences;
    public RecyclerAdapterlawyer recyclerAdapterlawyer;


    LinearLayoutManager linearLayoutManager;

    public LawyerListFragment() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        View myFragmentView = inflater.inflate(R.layout.fragment_lawyer, container, false);
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recycler_lawyer);
        laysearch = (TextInputLayout) myFragmentView.findViewById(R.id.lay_edt_search);
        edtsearch = (AutoCompleteTextView) myFragmentView.findViewById(R.id.edt_search);
        recyclerView.setLayoutManager(new GridLayoutManager(G.context, 2));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(10, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        infos = new ArrayList<>();
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        vu = BasicActivity.vu;
        userid = BasicActivity.userid;
//        enk = preferences.getString("k5", "not");
//        company = getResources().getString(R.string.developer);
//        byte[] data = Base64.decode(enk, Base64.NO_WRAP);
//        Key = new SecretKeySpec(data, 0, data.length, "AES");
//        content = Base64.encodeToString(SymmetricAlgorithmAES.encryption(Key, vu+"-"+userid+"-"+R.string.developer), Base64.NO_WRAP);
        laysearch.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        laysearch.setBoxStrokeColor(getResources().getColor(R.color.white));
        edtsearch.setVisibility(View.GONE);
        laysearch.setVisibility(View.GONE);
        edtsearch.setThreshold(1);
        edtsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int posi = getItemPosition(edtsearch.getText().toString());
                if (posi >= 0) {
                    recyclerView.scrollToPosition(posi);
                }
            }
        });
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + company, enk);
        content=BasicActivity.content;

//        Log.e("enk1", enk);
        Log.e("content1", content + "");
        Call<ArrayList<Lawyer>> callvokala = RI.getvokala(content, userid);
        callvokala.enqueue(new Callback<ArrayList<Lawyer>>() {
            @Override
            public void onResponse(Call<ArrayList<Lawyer>> call, Response<ArrayList<Lawyer>> response) {

                infos = response.body();
                recyclerView.setAdapter(recyclerAdapterlawyer = new RecyclerAdapterlawyer(infos, G.context));
                recyclerAdapterlawyer.notifyDataSetChanged();
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line);
                for (int i = 0; i < response.body().size(); i++) {
                    adapter.add(response.body().get(i).getName());
                }
                adapter.add("");
                edtsearch.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Lawyer>> call, Throwable t) {

            }
        });


//        recyclerView.setAdapter(recyclerAdapterlawyer = new RecyclerAdapterlawyer(infos, G.context));
        return myFragmentView;
//        Log.e("sqluser",indfos+"");
    }

    public boolean isConnected() {
        ConnectivityManager connect = (ConnectivityManager) G.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null) {
            NetworkInfo[] information = connect.getAllNetworkInfo();
            if (information != null) {
                for (int x = 0; x < information.length; x++) {
                    if (information[x].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
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

    public int getItemPosition(String eventId) {
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).getName().equals(eventId)) {
                return i;
            }
        }
        return -1;
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

package com.shrazavi.dadmehr.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityMain;
import com.shrazavi.dadmehr.Adapter.RecyclerAdapterCalllog;
import com.shrazavi.dadmehr.DataClass.CallLog;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import javax.crypto.spec.SecretKeySpec;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalllogFragment extends Fragment {
    ArrayList<CallLog> infos;
    public RecyclerAdapterCalllog recyclerAdapterCalllog;
    RecyclerView recyclerView;
    TextView txtnocontent;
    Retrofitinformation RI;
    String userid = "";
    String vu;
    public static int timer = 0;
//    public static SharedPreferences preferences;
    LinearLayoutManager linearLayoutManager;
    Thread uploadThread;
    Thread dataThread;
    String counter = "";
    private Activity mActivity;
    String content;
    String enk;
    SecretKeySpec Key;

    public static MaterialProgressBar progresslogin;

    public CalllogFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_calllog, container, false);
        recyclerView = (RecyclerView) myFragmentView.findViewById(R.id.recycler_call_log);
        txtnocontent = (TextView) myFragmentView.findViewById(R.id.txt_call_nocontent);


        linearLayoutManager = new LinearLayoutManager(G.context);
        progresslogin = myFragmentView.findViewById(R.id.calllogprogress);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        infos = new ArrayList<>();
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        txtnocontent.setVisibility(View.GONE);

        vu = BasicActivity.vu;
        userid = BasicActivity.userid;
//        enk = preferences.getString("k5", "not");
//        content = SymmetricAlgorithmAES.encrypt(vu + "-" + userid + "-" + getResources().getString(R.string.developer), enk);
        content=BasicActivity.content;
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        ActivityMain.badgecall.setVisible(false);
        ActivityMain.badgecall.clearNumber();
//        Log.e("typef",type);
//        if (typeuser.equals("vl")) {
//            du = "user";
//        } else {
//            du = "dr";
//        }
        Log.e("calllogcontent", content);
        Log.e("callloguserid", userid);

        Call<ArrayList<CallLog>> calllog = RI.getcalllog(content, userid, userid);
        calllog.enqueue(new Callback<ArrayList<CallLog>>() {
            @Override
            public void onResponse(Call<ArrayList<CallLog>> call, Response<ArrayList<CallLog>> response) {
                infos = response.body();
                recyclerView.setAdapter(recyclerAdapterCalllog = new RecyclerAdapterCalllog(infos, G.context, content));


                recyclerAdapterCalllog.notifyDataSetChanged();
                Log.e("info size=",infos.size()+"");
                if (infos.size() == 0) {
                    txtnocontent.setVisibility(View.VISIBLE);

                } else {
                    txtnocontent.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<CallLog>> call, Throwable t) {
                Log.e("calllogerror", t + "");
            }
        });
        return myFragmentView;
    }

    public void getdate() {


    }
}

//        new CountDownTimer(2000, 1000) {
//            public void onTick(long millisUntilFinished) {
////                timerValue.setText(millisUntilFinished/1000+"");
//
//            }
//
//            public void onFinish() {
//                if (getActivity() != null&& ActivityMain.coanterfrag==0) {
////your code
//
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction frm = fragmentManager.beginTransaction().replace(R.id.frmlay, new CalllogFragment());
//                    frm.commit();
//                    ActivityMain.coanterfrag=1;
//                }
//            }
//        }.start();
//        ChatDatabase chatDatabase = new ChatDatabase(G.context);
//        Cursor cursor = chatDatabase.getCalllog();
//
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    progresslogin.setVisibility(View.VISIBLE);
//                    if (cursor.getString(1).isEmpty()) {
//                        if (du.equals("dr")) {
//
//                            retrofit2.Call<Vakil> callvakil = RI.getvakil(cursor.getString(1));
//                            callvakil.enqueue(new Callback<Vakil>() {
//                                @Override
//                                public void onResponse(retrofit2.Call<Vakil> call, Response<Vakil> response) {
//
////                                    chatDatabase.upcall(response.body().getProfile(), response.body().getUserid(), response.body().getQbid());
//
//                                }
//
//                                @Override
//                                public void onFailure(retrofit2.Call<Vakil> call, Throwable t) {
//
//                                }
//                            });
//                        } else {
//                            retrofit2.Call<User> calluser = RI.getuser(cursor.getString(1));
//                            calluser.enqueue(new Callback<User>() {
//                                @Override
//                                public void onResponse(retrofit2.Call<User> call, Response<User> response) {
//
////                                    chatDatabase.upcall(response.body().getProfile(), response.body().getUserid(), response.body().getQbid());
//
//                                }
//
//                                @Override
//                                public void onFailure(retrofit2.Call<User> call, Throwable t) {
//
//                                }
//                            });
//                        }
//                    }
//
//                }
//                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                    final String username = cursor.getString(1);
//                    final String time = cursor.getString(3);
//                    final long date = cursor.getLong(4);
//                    final String status = cursor.getString(5);
//                    final String type = cursor.getString(6);
//                    final String imgProfile = cursor.getString(7);


//                    CallLog callLogInfo = new CallLog();
//                    callLogInfo.username = username;
//                    callLogInfo.time = time;
//                    callLogInfo.date = date;
//                    callLogInfo.status = status;
//                    callLogInfo.type = type;
//                    callLogInfo.imgurl = imgProfile;
//                    infos.add(callLogInfo);


//                }


//
//        progresslogin.setVisibility(View.GONE);
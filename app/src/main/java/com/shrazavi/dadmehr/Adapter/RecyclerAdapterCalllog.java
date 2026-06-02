package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityCalldetail;
import com.shrazavi.dadmehr.DataClass.CallLog;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.SolarCalendar;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterCalllog extends RecyclerView.Adapter<RecyclerAdapterCalllog.CallViewHolder> {
    String username = "";
//    public SharedPreferences preferences;
    Retrofitinformation RI;
    String vu = "";
String targertuser="";
    String content;
    public ArrayList<CallLog> callLogInfos = new ArrayList<>();
    Context context;

    public RecyclerAdapterCalllog(ArrayList<CallLog> callLogInfos, Context context, String content) {
        this.context = context;
        this.content = content;
        this.callLogInfos = callLogInfos;
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_item, parent, false);
        return new CallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CallViewHolder holder, final int position) {
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        vu = BasicActivity.vu;
        username = BasicActivity.userid;

        holder.txtUsername.setTypeface(G.face);
        holder.txttime.setTypeface(G.face);
        holder.txtdate.setTypeface(G.face);

//        holder.layreport.setVisibility(View.GONE);
        final CallLog callLogInfo = callLogInfos.get(position);
        Log.e("from", callLogInfo.from + "");

        if (callLogInfo.from.equals(username)) {
            if (callLogInfo.status.equals("Missed")) {
                holder.imgstatus.setBackgroundResource(R.drawable.ic_block);
            } else {
                holder.imgstatus.setBackgroundResource(R.drawable.ic_outcall);
            }
            targertuser=callLogInfo.to;
//            holder.txtUsername.setText(callLogInfo.to);
            if (vu.equals("user")) {
                Call<Vakil> callvakil = RI.getvakil(content, callLogInfo.to, username);
                callvakil.enqueue(new Callback<Vakil>() {
                    @Override
                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                        holder.txtUsername.setText(response.body().getName());
//                        Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");
                        if (!response.body().getProfile().equals("empty")) {
                            holder.imgProf.setVisibility(View.VISIBLE);
                            Picasso.with(context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);

                        } else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
//                            holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);
                            holder.txtprof.setText(ch1 + "");
                            holder.imgProf.setVisibility(View.GONE);

                            holder.crdprof.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<Vakil> call, Throwable t) {

                    }
                });
            } else {

                Call<User> calluser = RI.getuser(content, callLogInfo.to, username);
                calluser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        holder.txtUsername.setText(response.body().getName());
//                        Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");

                        if (!response.body().getProfile().equals("empty")) {
                            holder.imgProf.setVisibility(View.VISIBLE);

                            Picasso.with(context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);

                        } else {
//                            holder.imgProf.setBackgroundResource(R.drawable.ic_profle);
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
//                            holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);

                            holder.txtprof.setText(ch1 + "");
                            holder.crdprof.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


            }
        } else {
            targertuser=callLogInfo.from;

            if (callLogInfo.status.equals("Missed")) {
                holder.imgstatus.setBackgroundResource(R.drawable.ic_missed);
            } else {
                holder.imgstatus.setBackgroundResource(R.drawable.ic_incall);
            }

//            holder.txtUsername.setText(callLogInfo.from);
            if (vu.equals("user")) {
                Call<Vakil> callvakil = RI.getvakil(content, callLogInfo.from, username);
                callvakil.enqueue(new Callback<Vakil>() {
                    @Override
                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                        holder.txtUsername.setText(response.body().getName());
//                        Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");
                        if (!response.body().getProfile().equals("empty")) {
                            holder.imgProf.setVisibility(View.VISIBLE);

                            Picasso.with(context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);

                        } else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
//                            holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);

                            holder.txtprof.setText(ch1 + "");
                            holder.crdprof.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<Vakil> call, Throwable t) {

                    }
                });
            } else {

                Call<User> calluser = RI.getuser(content, callLogInfo.from, username);
                calluser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        holder.txtUsername.setText(response.body().getName());
//                        Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");

                        if (!response.body().getProfile().equals("empty")) {
                            holder.imgProf.setVisibility(View.VISIBLE);

                            Picasso.with(context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);

                        } else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
//                            holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);

                            holder.txtprof.setText(ch1 + "");
                            holder.crdprof.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


            }
        }
        if (callLogInfo.type.equals("voise")) {
            holder.imgtype.setBackgroundResource(R.drawable.ic_voise_call);
        } else {
            holder.imgtype.setBackgroundResource(R.drawable.ic_video_call);
        }
        setdate(callLogInfo.starttime, holder.txtdate);
        holder.txttime.setText(callLogInfo.hours);
//        else {
//        holder.txtUsername.setText(callLogInfo.username);}
//        holder.txttime.setText(callLogInfo.time);
//        if (callLogInfo.type.equals("video")&& callLogInfo.status.equals("outcall")) {
//            holder.imgtype.setBackgroundResource(R.drawable.videooutcome);
//
//        } else if(callLogInfo.type.equals("video")&& callLogInfo.status.equals("incall")) {
//            holder.imgtype.setBackgroundResource(R.drawable.videoincome);
//
//        } else if(callLogInfo.type.equals("voise")&& callLogInfo.status.equals("incall")){
//            holder.imgtype.setBackgroundResource(R.drawable.voiseincome);
//        }else if(callLogInfo.type.equals("voise")&& callLogInfo.status.equals("outcall")){
//            holder.imgtype.setBackgroundResource(R.drawable.voiseoutcome);
//        }
//
//        setdate(callLogInfo.date, holder.txtdate);
//        if (!callLogInfo.imgurl.equals("empty")) {
//            Picasso.with(context).load(G.phpurl + "/" + callLogInfo.imgurl).into(holder.imgProf);
//
//        }else{
//            holder.imgProf.setBackgroundResource(R.drawable.ic_profle);
//
//        }

        holder.linearcall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(context, ActivityCalldetail.class);
                intent.putExtra("username",targertuser);
                intent.putExtra("room", callLogInfo.room);
                intent.putExtra("vu", vu);
                intent.putExtra("callid", callLogInfo._id);
                intent.putExtra("starttime", callLogInfo.starttime);
                intent.putExtra("endtime", callLogInfo.endtime);
                intent.putExtra("hours", callLogInfo.hours);
                intent.putExtra("status", callLogInfo.status);
                intent.putExtra("type", callLogInfo.type);
                intent.putExtra("condition", callLogInfo.condition);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


//                if (holder.layreport.getVisibility() == View.VISIBLE) {
//                    holder.layreport.setVisibility(View.GONE);
//
//                } else {
//                    holder.layreport.setVisibility(View.VISIBLE);
//
//                    if(vu.equals("vl")){
//                        holder.btncondition.setVisibility(View.VISIBLE);
//                        holder.btnreport.setVisibility(View.VISIBLE);
//                        holder.btncantinue.setVisibility(View.GONE);
//
//                    }else {
//                        holder.btncantinue.setVisibility(View.VISIBLE);
//                        holder.btnreport.setVisibility(View.VISIBLE);
//                        holder.btncondition.setVisibility(View.GONE);
//                    }
//
//
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return callLogInfos.size();
    }

    public class CallViewHolder extends RecyclerView.ViewHolder {


        TextView txtUsername,txtprof,txttime,txtdate;
        ImageView imgtype,imgstatus,imgProf;
        CardView crdprof;
        LinearLayout linearcall;

        public CallViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_call_username);
            txtprof = (TextView) itemView.findViewById(R.id.txtProfileCallList);
            txttime = (TextView) itemView.findViewById(R.id.txt_call_time);
            txtdate = (TextView) itemView.findViewById(R.id.txt_call_date);
            imgtype = (ImageView) itemView.findViewById(R.id.img_call_type);
            imgstatus = (ImageView) itemView.findViewById(R.id.img_call_status);
            linearcall = (LinearLayout) itemView.findViewById(R.id.lay_call);
            imgProf = (ImageView) itemView.findViewById(R.id.imgProfileCallList);
            crdprof = (CardView) itemView.findViewById(R.id.crdProfileCallList);

        }


    }

    private void setdate(long date, TextView dateText) {
        Calendar cal1 = Calendar.getInstance();
        cal1.getTimeZone();
        cal1.setTimeInMillis(date);
//Log.e("recdate",date+"");
        SolarCalendar sc = new SolarCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), cal1.get(Calendar.DAY_OF_WEEK));
        dateText.setText(sc.getCurrentShamsidate());
    }
}

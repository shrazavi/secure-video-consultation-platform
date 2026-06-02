package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityProfileVl;
import com.shrazavi.dadmehr.DataClass.Lawyer;
import com.shrazavi.dadmehr.DataClass.Rating;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterlawyer extends RecyclerView.Adapter<RecyclerAdapterlawyer.LawyerViewHolder> {
    Retrofitinformation RI;
    String type;

    public ArrayList<Lawyer> lawyerInfos = new ArrayList<>();
    Context context;
//    public SharedPreferences preferences;

    public RecyclerAdapterlawyer(ArrayList<Lawyer> lawyerInfos, Context context) {
        this.context = context;

        this.lawyerInfos = lawyerInfos;
    }

    @Override
    public LawyerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lawyer, parent, false);
        return new LawyerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LawyerViewHolder holder, final int position) {
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);

        final Lawyer lawyerInfo = lawyerInfos.get(position);
        Log.i("LOG", "recycler done");
        holder.txtname.setText(lawyerInfo.getName());
        holder.txtexperience.setText("با" + lawyerInfo.getExperience() + "سال سابقه");
        if (lawyerInfo.getType().equals("1")) {
            if (lawyerInfo.getOstan().equals("تهران")) {
                holder.txttype.setText("کانون وکلای دادگستری مرکز");
            } else {
                holder.txttype.setText("کانون وکلای دادگستری " + lawyerInfo.getOstan());
            }
            if (lawyerInfo.getBase().equals("1")) {
                holder.txtbase.setText("وکیل پایه یک دادگستری");
            } else if (lawyerInfo.getBase().equals("2")) {
                holder.txtbase.setText("وکیل پایه دوم دادگستری");
            } else {
                holder.txtbase.setText("کارآموز");
            }
        } else {
            holder.txttype.setText("مرکز مشاوران دادگستری " + lawyerInfo.getOstan());
        }
        holder.txtname.setTypeface(G.face);
        holder.txtexperience.setTypeface(G.face);
        holder.txttype.setTypeface(G.face);
        holder.txtbase.setTypeface(G.face);
        Log.e("lawyer is=", lawyerInfo.getUsername() + "");
        Call<Rating> callrating = RI.getRating(lawyerInfo.getUsername());
        callrating.enqueue(new Callback<Rating>() {
            @Override
            public void onResponse(Call<Rating> call, Response<Rating> response) {
                holder.txtrating.setText(response.body().getTotal()+"");
                holder.rate.setRating(response.body().getTotal());
//                Log.e("rate is=", response.body().getTotal()+ "");
            }

            @Override
            public void onFailure(Call<Rating> call, Throwable t) {
                Log.e("error rate", t + "");
            }
        });
//        holder.linearLawyer.setPadding(0,0,0,0);
//        holder.linearLawyer.setPaddingRelative(0,0,0,0);

//        type = preferences.getString("type", "not");
//        holder.txtrating.setText(lawyerInfo.getRating());
//        holder.txtrating.setText(lawyerInfo.getRating());

//        Log.e("img",G.nodeurl  +"/"+ lawyerInfo.getProfile()+"");
//        Picasso.with(context).load(G.nodeurl  +"/"+ lawyerInfo.getProfile()).into(holder.img);
        if (lawyerInfo.getProfile().equals("empty")) {
            char ch1 = lawyerInfo.getUsername().toUpperCase().charAt(0);
            Random rnd = new Random();
            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
//                  imgProf.setVisibility(View.GONE);
//                   holder.imgProf.setBackgroundResource(R.drawable.edtdetailkala);

            holder.txtprof.setText(ch1 + "");
            holder.crdprof.setCardBackgroundColor(color);
        } else {
            Picasso.with(G.context).load(G.nodeurl + "/" + lawyerInfo.getProfile()).transform(new ImageProfile()).into(holder.img);
        }
        holder.linearLawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(G.context, ActivityProfileVl.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("username", lawyerInfo.getUsername());
//                    intent.putExtra("imgurl", G.nodeurl + "/" + lawyerInfo.getProfile());
                G.context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return lawyerInfos.size();
    }

    public class LawyerViewHolder extends RecyclerView.ViewHolder {


        TextView txtname;
        TextView txtprof;
        TextView txtexperience;
        TextView txttype;
        TextView txtbase;
        ImageView img;
        ConstraintLayout linearLawyer;
        TextView txtrating;
        RatingBar rate;
        CardView crd, crdprof;

        public LawyerViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.txt_lawyer_name);
            txtprof = (TextView) itemView.findViewById(R.id.txt_lawyer_prof);
            crdprof = (CardView) itemView.findViewById(R.id.crd_lawyer_prof);
            txtexperience = (TextView) itemView.findViewById(R.id.txt_lawyer_experience);
            txttype = (TextView) itemView.findViewById(R.id.txt_lawyer_type);
            txtbase = (TextView) itemView.findViewById(R.id.txt_lawyer_base);
            txtrating = (TextView) itemView.findViewById(R.id.txt_lawyer_rating);
            img = (ImageView) itemView.findViewById(R.id.img_lawyer);
            linearLawyer = (ConstraintLayout) itemView.findViewById(R.id.lawyer_layout);
            rate = (RatingBar) itemView.findViewById(R.id.ratingbar_lawyer);
            crd = (CardView) itemView.findViewById(R.id.crd_lawyer);


        }


    }

}

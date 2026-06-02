package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivitySpecialty;
import com.shrazavi.dadmehr.DataClass.Experience;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;

import java.util.ArrayList;


public class RecyclerAdapterSpecialty extends RecyclerView.Adapter<RecyclerAdapterSpecialty.LawyerViewHolder> {

    String type;

    public ArrayList<Experience> exInfos = new ArrayList<>();
    Context context;
//    public SharedPreferences preferences;

    public RecyclerAdapterSpecialty(ArrayList<Experience> exInfos, Context context) {
        this.context = context;
        this.exInfos = exInfos;
    }

    @Override
    public LawyerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type, parent, false);
        return new LawyerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LawyerViewHolder holder, final int position) {

        final Experience exInfo = exInfos.get(position);
//        Log.i("LOG", "recycler done");
        holder.txtname.setText(exInfo.getName());
        holder.img.setBackgroundResource(exInfo.getDrawble());
        holder.layex.setGravity(Gravity.RIGHT);
        holder.layex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(G.context, ActivitySpecialty.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("specialty", exInfo.getName());
//                    intent.putExtra("imgurl", G.nodeurl + "/" + lawyerInfo.getProfile());
                G.context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return exInfos.size();
    }

    public class LawyerViewHolder extends RecyclerView.ViewHolder {


        TextView txtname;

        ImageView img;
        RelativeLayout layex;

        public LawyerViewHolder(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.txt_ex_type);

            img = (ImageView) itemView.findViewById(R.id.img_ex_type);
            layex = (RelativeLayout) itemView.findViewById(R.id.lay_ex);


        }


    }

}

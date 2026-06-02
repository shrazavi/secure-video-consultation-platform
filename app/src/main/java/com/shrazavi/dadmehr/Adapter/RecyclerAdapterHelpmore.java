package com.shrazavi.dadmehr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.Help;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.Retrofitinformation;

import java.util.ArrayList;


public class RecyclerAdapterHelpmore extends RecyclerView.Adapter<RecyclerAdapterHelpmore.HelpViewHolder> {

    public ArrayList<Help> helps = new ArrayList<>();
    Context context;
    Activity activity;
    Retrofitinformation RI;
    String vu = "";
    String myid;
    String content;
    String type;
//    public SharedPreferences preferences;

    public RecyclerAdapterHelpmore(Context context, ArrayList<Help> helps) {
        this.context = context;
        this.content = content;
        this.activity = activity;
        this.helps = helps;
        this.type = type;
    }

    @Override
    public HelpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law_more, parent, false);

        return new HelpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final HelpViewHolder holder, int position) {
        final Help help = helps.get(position);
        holder.txtlaw.setTypeface(G.face);
        holder.txtnum.setTypeface(G.face);
        holder.txtlaw.setText(help.getHelp());
        holder.txtnum.setText(position+1+"");

//        Log.e("law",help.getLaw());






    }

    @Override
    public int getItemCount() {
        return helps.size();
    }

    public class HelpViewHolder extends RecyclerView.ViewHolder {

        TextView txtlaw, txtnum;
        LinearLayout layitem;


        public HelpViewHolder(View itemView) {
            super(itemView);
            txtlaw = (TextView) itemView.findViewById(R.id.txt_item_law);
            txtnum = (TextView) itemView.findViewById(R.id.txt_item_law_num);



        }

    }


}

package com.shrazavi.dadmehr.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.Law;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.Retrofitinformation;

import java.util.ArrayList;


public class RecyclerAdapterLawmore extends RecyclerView.Adapter<RecyclerAdapterLawmore.LawViewHolder> {

    public ArrayList<Law> laws = new ArrayList<>();
    Context context;
    Activity activity;
    Retrofitinformation RI;
    String vu = "";
    String myid;
    String content;
    String type;
//    public SharedPreferences preferences;

    public RecyclerAdapterLawmore(Context context, ArrayList<Law> laws) {
        this.context = context;
        this.content = content;
        this.activity = activity;
        this.laws = laws;
        this.type = type;
    }

    @Override
    public LawViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law_more, parent, false);

        return new LawViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LawViewHolder holder, int position) {
        final Law law = laws.get(position);
        holder.txtlaw.setTypeface(G.face);
        holder.txtnum.setTypeface(G.face);
        holder.txtlaw.setText(law.getLaw());
        holder.txtnum.setText(position+1+"");

        Log.e("law",law.getLaw());






    }

    @Override
    public int getItemCount() {
        return laws.size();
    }

    public class LawViewHolder extends RecyclerView.ViewHolder {

        TextView txtlaw, txtnum;
        LinearLayout layitem;


        public LawViewHolder(View itemView) {
            super(itemView);
            txtlaw = (TextView) itemView.findViewById(R.id.txt_item_law);
            txtnum = (TextView) itemView.findViewById(R.id.txt_item_law_num);



        }

    }


}

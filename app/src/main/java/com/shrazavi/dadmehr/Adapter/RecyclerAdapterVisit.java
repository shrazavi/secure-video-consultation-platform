package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.Visitday;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.Retrofitinformation;

import java.util.ArrayList;


public class RecyclerAdapterVisit extends RecyclerView.Adapter<RecyclerAdapterVisit.ChatViewHolder> {
    String username = "";
//    public SharedPreference preferences;
    Retrofitinformation RI;
    String vu = "";
    public ArrayList<Visitday> dayInfos = new ArrayList<>();
    Context context;


    public RecyclerAdapterVisit(ArrayList<Visitday> dayInfos, Context context) {
        this.context = context;

        this.dayInfos = dayInfos;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {


        final Visitday dayInfo = dayInfos.get(position);
//        Log.i("LOG","recycler done");

        holder.txtstart.setText(dayInfo.getStarttime());
        holder.txtend.setText(dayInfo.getEndtime());
        holder.txtday.setText(dayInfo.getVisitday());


//        holder.txtUsername.setText(chatInfo.username);
//        if(chatInfo.count!=0){
//            holder.txtCountMessage.setVisibility(View.VISIBLE);
//           // Log.e("countR",chatInfo.count+"");
//            holder.txtCountMessage.setText(chatInfo.count+"");
//
//        }else{
//            holder.txtCountMessage.setVisibility(View.GONE);
//        }
//
//        if(!chatInfo.imgProfile.equals("empty")){
////            Picasso.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).into(holder.imgProf);
//            Picasso.with(G.context).load(G.phpurl+"/"+chatInfo.imgProfile).transform(new ImageProfile()).into(holder.imgProf);
////            Glide.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).placeholder(R.drawable.ic_profle).error(R.drawable.ic_profle).into(holder.imgProf);
//
//            //  Log.e("imguserurlm",G.phpurl+chatInfo.imgProfile);
//        }
//
//        holder.linearChats.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                chatInfo.count=0;
//                preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
//                du = preferences.getString("type", "not");
//                if(du.equals("user")) {
////                ActivityChat.laychat.setVisibility(View.GONE);
//                    Intent intent = new Intent(RecyclerAdapterBlackList.this.context, ActivityChat.class);
//                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
//                    //Intent intent=new Intent(G.context, ActivityMain.class);
//                    intent.putExtra("id", holder.txtUsername.getText().toString());
//                    intent.putExtra("imgurl", G.phpurl + "/" + chatInfo.imgProfile);
//                    intent.putExtra("timer", 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }else{
//                    Intent intent = new Intent(RecyclerAdapterBlackList.this.context, ActivityChat.class);
//                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
//                    //Intent intent=new Intent(G.context, ActivityMain.class);
//                    intent.putExtra("id", holder.txtUsername.getText().toString());
//                    intent.putExtra("imgurl", G.phpurl + "/" + chatInfo.imgProfile);
//                    intent.putExtra("timer", 6000000);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(intent);
//                }

        //((Activity)context).startActivityForResult(intent,1);


//            }
//        });

    }

    @Override
    public int getItemCount() {
        return dayInfos.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {


        TextView txtstart;
        TextView txtend;
        TextView txtday;


        public ChatViewHolder(View itemView) {
            super(itemView);
            txtstart = (TextView) itemView.findViewById(R.id.txt_visit_item_start);
            txtend = (TextView) itemView.findViewById(R.id.txt_visit_item_end);
            txtday = (TextView) itemView.findViewById(R.id.txt_visit_item_day);

        }


    }
}

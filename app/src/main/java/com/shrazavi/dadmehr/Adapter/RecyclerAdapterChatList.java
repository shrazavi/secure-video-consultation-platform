package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityChat;
import com.shrazavi.dadmehr.DataClass.ChatInfo;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;




public class RecyclerAdapterChatList extends RecyclerView.Adapter<RecyclerAdapterChatList.ChatViewHolder> {

    String vu = "";
    public ArrayList<ChatInfo> chatInfos = new ArrayList<>();
    Context context;
//    public SharedPreferences preferences;
    public RecyclerAdapterChatList(ArrayList<ChatInfo> chatInfos, Context context) {
        this.context = context;
        this.chatInfos = chatInfos;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {

        final ChatInfo chatInfo = chatInfos.get(position);
        Log.i("LOG","recycler done");
        holder.txtUsername.setText(chatInfo.username);
        if(chatInfo.count!=0){
            holder.txtCountMessage.setVisibility(View.VISIBLE);
           // Log.e("countR",chatInfo.count+"");
            holder.txtCountMessage.setText(chatInfo.count+"");

        }else{
            holder.txtCountMessage.setVisibility(View.GONE);
        }

        if(!chatInfo.imgProfile.equals("empty")){
//            Picasso.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).into(holder.imgProf);

//            Picasso.with(G.context).load(G.phpurl+"/"+chatInfo.imgProfile).transform(new ImageProfile()).into(holder.imgProf);
//            Glide.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).placeholder(R.drawable.ic_profle).error(R.drawable.ic_profle).into(holder.imgProf);

            //  Log.e("imguserurlm",G.phpurl+chatInfo.imgProfile);
        }

        holder.linearChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatInfo.count=0;
//                preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
                vu = BasicActivity.vu;
//                username = BasicActivity.userid;
                if(vu.equals("user")) {
//                ActivityChat.laychat.setVisibility(View.GONE);
                    Intent intent = new Intent(RecyclerAdapterChatList.this.context, ActivityChat.class);
                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(G.context, ActivityMain.class);
                    intent.putExtra("id", holder.txtUsername.getText().toString());
                    intent.putExtra("imgurl", G.phpurl + "/" + chatInfo.imgProfile);
                    intent.putExtra("timer", 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(RecyclerAdapterChatList.this.context, ActivityChat.class);
                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(G.context, ActivityMain.class);
                    intent.putExtra("id", holder.txtUsername.getText().toString());
                    intent.putExtra("imgurl", G.phpurl + "/" + chatInfo.imgProfile);
                    intent.putExtra("timer", 6000000);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

                //((Activity)context).startActivityForResult(intent,1);


            }
        });

    }

    @Override
    public int getItemCount() {
        return chatInfos.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {


        TextView txtUsername,txtprofile;
        TextView txtCountMessage;
        LinearLayout linearChats;
//        CircleImageView imgProf;


        public ChatViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.txtChatUsername);
            txtprofile = (TextView) itemView.findViewById(R.id.txt_chat_item_profile);
            txtCountMessage = (TextView) itemView.findViewById(R.id.txtCountMessage);
            linearChats = (LinearLayout) itemView.findViewById(R.id.linearChats);
//            imgProf = (CircleImageView) itemView.findViewById(R.id.imgProfileChatList);
        }


    }
}

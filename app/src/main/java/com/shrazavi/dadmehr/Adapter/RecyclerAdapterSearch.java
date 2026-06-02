package com.shrazavi.dadmehr.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.ChatsText;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;

import java.util.ArrayList;


public class RecyclerAdapterSearch extends RecyclerView.Adapter<RecyclerAdapterSearch.ChatViewHolder> {

    public ArrayList<ChatsText> chatsTexts = new ArrayList<>();
    Context context;

    public RecyclerAdapterSearch(Context context, ArrayList<ChatsText> chatsTexts) {
        this.context = context;
        this.chatsTexts = chatsTexts;

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats, parent, false);
        return new RecyclerAdapterSearch.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        final ChatsText chatsText = chatsTexts.get(position);
//        chatsText=m
//        Log.e("vidchat", G.nodeurl + chatsText.vidurl);
        long previousTs = 0;
        if (position > 2) {
            ChatsText pm = chatsTexts.get(position - 2);
            previousTs = pm.getDate();
        }
//        Log.e("chatdate", chatsText.date + "");


//        setTimeTextVisibility(long ts1, long ts2, TextView timeText,ImageView imgChatTick,ImageView imgChaturl,ImageView imgvoise , RelativeLayout layvid,TextView txtChatText,LinearLayout linearChat,LinearLayout linearChatClick) {

//         String date = "";
//        date = holder.txtChatTime.getText().toString();
//        Log.e("date", date + "");
//        holder.imgvoise.setBackgroundResource(R.drawable.voicepic);

        if (chatsText.getFrom().equals("me")) {

    holder.linearChatClick.setVisibility(View.VISIBLE);
//            holder.imgvoise.setVisibility(View.GONE);

//            holder.layvid.setVisibility(View.GONE);
//            holder.imgChaturl.setVisibility(View.GONE);
            holder.imgChatTick.setVisibility(View.VISIBLE);
            holder.txtChatText.setText(chatsText.getText());
            holder.txtChatTime.setText(chatsText.getTime());
//            holder.layvoisechat.setVisibility(View.VISIBLE);
            holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
            float size = holder.txtChatTime.getTextSize();
//                Log.e("texttimesize", "" + size);
            holder.linearChatClick.setBackgroundResource(R.drawable.mychat);
            //holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            //holder.linearChatClick.setPadding(10, 5, 20, 5);
            holder.linearChat.setGravity(Gravity.RIGHT);
            holder.linearChatClick.setGravity(Gravity.RIGHT);
            if (!chatsText.isRead()) {
                holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
            } else {
                // holder.imgChatTick.setImageResource(android.R.color.transparent);
                holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
            }

        }
        // int gravity=holder.txtChatText.getGravity();


        else {
//                setTimeTextVisibility(chatsText.date, previousTs, holder.txtChatTime, holder.imgChatTick, holder.imgChaturl, holder.imgvoise, holder.layvid, holder.txtChatText, holder.linearChat, holder.linearChatClick);


//                    setTimeTextVisibility(chatsText.date, previousTs, holder.txtChatTime, holder.imgChatTick, holder.imgChaturl, holder.imgvoise, holder.layvid, holder.txtChatText, holder.linearChat, holder.linearChatClick);
            holder.linearChatClick.setVisibility(View.VISIBLE);
//            holder.imgvoise.setVisibility(View.GONE);
//                holder.vidchat.setVisibility(View.GONE);
//            holder.layvid.setVisibility(View.GONE);
//            holder.imgChaturl.setVisibility(View.GONE);
            holder.imgChatTick.setVisibility(View.GONE);
            holder.txtChatText.setText(chatsText.getText());
            holder.txtChatTime.setText(chatsText.getTime());
//            holder.layvoisechat.setVisibility(View.VISIBLE);
            holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
            holder.linearChatClick.setGravity(Gravity.LEFT);
            holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));
            //holder.linearChatClick.setPadding(20, 5, 10, 5);
            //holder.txtChatText.setGravity(Gravity.LEFT);
            holder.linearChat.setGravity(Gravity.LEFT);
            float size = holder.txtChatTime.getTextSize();
//                Log.e("texttimesize", "" + size);


        }

        holder.linearChatClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//

            }
        });
        holder.linearChatClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("chat",chatsText.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return chatsTexts.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView txtChatText;
        LinearLayout linearChatClick;
        LinearLayout linearChat;
        LinearLayout layvoisechat;
        //LinearLayout imgvoise;
        ImageView imgChatTick;
        TextView txtChatTime;
        ImageView imgChaturl;
        ImageView imgvoise;
        //VideoView vidchat;
        RelativeLayout layvid;
        TextView txtTimevoise;

        public ChatViewHolder(View itemView) {
            super(itemView);
            txtChatText = (TextView) itemView.findViewById(R.id.txtChatText);
            txtChatTime = (TextView) itemView.findViewById(R.id.txtChatTime);
            imgChatTick = (ImageView) itemView.findViewById(R.id.imgChatTick);
//            imgChaturl = (ImageView) itemView.findViewById(R.id.img_chat);
            linearChatClick = (LinearLayout) itemView.findViewById(R.id.linearChatClick);
//            layvoisechat = (LinearLayout) itemView.findViewById(R.id.lay_voise_chat);
            linearChat = (LinearLayout) itemView.findViewById(R.id.lay_chat);
//            imgvoise = (LinearLayout) itemView.findViewById(R.id.img_play_voise);
//            vidchat = (VideoView) itemView.findViewById(R.id.vid_chat);
//            layvid = (RelativeLayout) itemView.findViewById(R.id.layvid);
//            imgvoise = (ImageView) itemView.findViewById(R.id.img_play_voise);
//            txtTimevoise = (TextView) itemView.findViewById(R.id.txt_time_record);
        }

    }



}

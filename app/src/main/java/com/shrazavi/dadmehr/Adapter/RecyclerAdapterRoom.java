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

import com.shrazavi.dadmehr.Activity.ActivityChat;
import com.shrazavi.dadmehr.DataClass.Room;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterRoom extends RecyclerView.Adapter<RecyclerAdapterRoom.RoomViewHolder> {
    Retrofitinformation RI;
    String vu = "";
    String myid;
    String content;
    public ArrayList<Room> roomInfos = new ArrayList<>();
    Context context;
//    public SharedPreferences preferences;

    public RecyclerAdapterRoom(ArrayList<Room> roomInfos, Context context, String content) {
        this.context = context;
        this.content = content;
        this.roomInfos = roomInfos;
    }

    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list, parent, false);
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RoomViewHolder holder, final int position) {
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        vu = BasicActivity.vu;
        myid = BasicActivity.userid;
        final Room roomInfo = roomInfos.get(position);
        holder.txtUsername.setTypeface(G.face);


        if (roomInfo.getUserA().equals(myid)) {
            if (roomInfo.getReadA() != 0) {
                holder.txtCountMessage.setVisibility(View.VISIBLE);
                // Log.e("countR",chatInfo.count+"");

                holder.txtCountMessage.setText(roomInfo.getReadA() + "");

            } else {
                holder.txtCountMessage.setVisibility(View.GONE);
            }
            if (vu.equals("user")) {
                Call<Vakil> callvakil = RI.getvakil(content, roomInfo.getUserB(), myid);
                callvakil.enqueue(new Callback<Vakil>() {
                    @Override
                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                        holder.txtUsername.setText(response.body().getName());
                        Log.e("imgprof", G.nodeurl + response.body().getProfile() + "");
                        if (!response.body().getProfile().equals("empty")) {
                            Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);
                            holder.imgProf.setVisibility(View.VISIBLE);

                        }else {
//                           Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
                            holder.txtprofile.setText(ch1+"");
                            holder.crdprofile.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<Vakil> call, Throwable t) {

                    }
                });
            } else {

                Call<User> calluser = RI.getuser(content, roomInfo.getUserB(), myid);
                calluser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        holder.txtUsername.setText(response.body().getName());
                        Log.e("imgprof", G.nodeurl + response.body().getProfile() + "");

                        if (!response.body().getProfile().equals("empty")) {
                            Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);
                            holder.imgProf.setVisibility(View.VISIBLE);

                        }else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
                            holder.txtprofile.setText(ch1+"");
                            holder.crdprofile.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });


            }

        } else {
            if (roomInfo.getReadB() != 0) {
                holder.txtCountMessage.setVisibility(View.VISIBLE);
//                 Log.e("countB",roomInfo.getReadB()+"");
                holder.txtCountMessage.setText(roomInfo.getReadB() + "");

            } else {
                holder.txtCountMessage.setVisibility(View.GONE);
//                Log.e("countB",roomInfo.getReadB()+"");
            }
            if (vu.equals("user")) {

                Call<Vakil> callvakil = RI.getvakil(content, roomInfo.getUserA(), myid);
                callvakil.enqueue(new Callback<Vakil>() {
                    @Override
                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                        holder.txtUsername.setText(response.body().getName());
//                    Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");
                        if (!response.body().getProfile().equals("empty")) {
                            Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);
                            holder.imgProf.setVisibility(View.VISIBLE);
                        }else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
                            holder.txtprofile.setText(ch1+"");
                            holder.crdprofile.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<Vakil> call, Throwable t) {

                    }
                });

            } else {


                Call<User> calluser = RI.getuser(content, roomInfo.getUserA(), myid);
                calluser.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        holder.txtUsername.setText(response.body().getName());

                        if (!response.body().getProfile().equals("empty")) {
                            Picasso.with(G.context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);
                            holder.imgProf.setVisibility(View.VISIBLE);

                        }else {
//                            Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                            char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                            Random rnd = new Random();
                            int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
                            holder.imgProf.setVisibility(View.GONE);
                            holder.txtprofile.setText(ch1+"");
                            holder.crdprofile.setCardBackgroundColor(color);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        }

//        Log.i("LOG", "recycler done");

//        if (roomInfo.count != 0) {
//            holder.txtCountMessage.setVisibility(View.VISIBLE);
//            // Log.e("countR",chatInfo.count+"");
//            holder.txtCountMessage.setText(roomInfo.count + "");
//
//        } else {
//            holder.txtCountMessage.setVisibility(View.GONE);
//        }
//
//        if (!roomInfo.imgProfile.equals("empty")) {
////            Picasso.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).into(holder.imgProf);
//            Picasso.with(G.context).load(G.phpurl + "/" + roomInfo.imgProfile).transform(new ImageProfile()).into(holder.imgProf);
////            Glide.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).placeholder(R.drawable.ic_profle).error(R.drawable.ic_profle).into(holder.imgProf);
//
//            //  Log.e("imguserurlm",G.phpurl+chatInfo.imgProfile);
//        }

        holder.linearChats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                du = preferences.getString("type", "not");
//                Log.e("vurec",vu);
                if (roomInfo.getUserA().equals(myid)) {
                    roomInfo.setReadA(0);
//                ActivityChat.laychat.setVisibility(View.GONE);
                    Intent intent = new Intent(RecyclerAdapterRoom.this.context, ActivityChat.class);
                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(G.context, ActivityMain.class);
                    intent.putExtra("id", roomInfo.getUserB());
//                    intent.putExtra("imgurl", G.phpurl + "/" + roomInfo.imgProfile);
                    intent.putExtra("timer", 0);
                    intent.putExtra("roomid", roomInfo.getId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    roomInfo.setReadB(0);
                    Intent intent = new Intent(RecyclerAdapterRoom.this.context, ActivityChat.class);
                    //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Intent intent=new Intent(G.context, ActivityMain.class);
                    intent.putExtra("id", roomInfo.getUserA());
                    intent.putExtra("roomid", roomInfo.getId());
//                    intent.putExtra("imgurl", G.phpurl + "/" + roomInfo.imgProfile);
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
        return roomInfos.size();
    }

    public class RoomViewHolder extends RecyclerView.ViewHolder {


        TextView txtUsername,txtprofile;
        TextView txtCountMessage;
        LinearLayout linearChats;
        ImageView imgProf;
        CardView crdprofile;

        public RoomViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.txtChatUsername);
            txtprofile = (TextView) itemView.findViewById(R.id.txt_chat_item_profile);
            txtCountMessage = (TextView) itemView.findViewById(R.id.txtCountMessage);
            linearChats = (LinearLayout) itemView.findViewById(R.id.linearChats);
            crdprofile = (CardView) itemView.findViewById(R.id.crd_item_chatlist);
            imgProf = (ImageView) itemView.findViewById(R.id.imgProfileChatList);
        }


    }
}

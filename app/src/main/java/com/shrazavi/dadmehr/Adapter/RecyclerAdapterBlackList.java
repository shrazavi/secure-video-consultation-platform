package com.shrazavi.dadmehr.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityBlockuser;
import com.shrazavi.dadmehr.DataClass.MessageSignup;
import com.shrazavi.dadmehr.DataClass.User;
import com.shrazavi.dadmehr.DataClass.Vakil;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.ImageProfile;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterBlackList extends RecyclerView.Adapter<RecyclerAdapterBlackList.ChatViewHolder> {
    String username = "";
//    public SharedPreferences preferences;
    Retrofitinformation RI;
    String vu = "";
    public ArrayList<String> userInfos = new ArrayList<>();
    Context context;
    String content;

    public RecyclerAdapterBlackList(ArrayList<String> userInfos, Context context, String content) {
        this.context = context;
        this.content = content;
        this.userInfos = userInfos;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blacklist, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, final int position) {
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        vu = BasicActivity.vu;
        username = BasicActivity.userid;

        final String userInfo = userInfos.get(position);
//        Log.i("LOG","recycler done");


        Call<User> calluser = RI.getuser(content, userInfo, username);
        calluser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                holder.txtUsername.setText(response.body().getName());
//                        Log.e("imgprof",G.nodeurl + response.body().getProfile()+"");

                if (!response.body().getProfile().equals("empty")) {
                    Picasso.with(context).load(G.nodeurl + response.body().getProfile()).transform(new ImageProfile()).into(holder.imgProf);

                } else {
//                            holder.imgProf.setBackgroundResource(R.drawable.ic_profle);
//                    Picasso.with(context).load(G.nodeurl + "/upload/profile/ic-profile.png").transform(new ImageProfile()).into(holder.imgProf);
                    char ch1 = response.body().getUsername().toUpperCase().charAt(0);
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(200), rnd.nextInt(200), rnd.nextInt(200));
//                            holder.imgProf.setVisibility(View.GONE);
                    holder.txtprofile.setText(ch1 + "");
                    holder.crdprofile.setCardBackgroundColor(color);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        holder.linearblock.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, holder.linearblock);
                popup.getMenuInflater().inflate(R.menu.pop_up_unblock, popup.getMenu());
                try {
                    Field mFieldPopup = popup.getClass().getDeclaredField("mPopup");
                    mFieldPopup.setAccessible(true);
                    MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                    mPopup.setForceShowIcon(true);
                } catch (Exception e) {
                }
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.unblock:
                                //handle menu1 click
                                Call<Vakil> callvakil = RI.getvakil(content, username, username);
                                callvakil.enqueue(new Callback<Vakil>() {
                                    @Override
                                    public void onResponse(Call<Vakil> call, Response<Vakil> response) {
                                        String blockuser = response.body().getBlockusers();
                                        StringBuilder result = new StringBuilder();

                                        String[] arrblock = blockuser.split(",");
                                        ArrayList<String> blackList = new ArrayList<String>(Arrays.asList(arrblock));
                                        blackList.remove(userInfo);
                                        for (int i = 0; i < blackList.size(); i++) {
                                            result.append(blackList.get(i));
                                            result.append(",");
                                        }


                                        Call<MessageSignup> callvakil = RI.upgradeblack(content,
                                                result.toString(),
                                                username);
                                        callvakil.enqueue(new Callback<MessageSignup>() {
                                            @Override
                                            public void onResponse(Call<MessageSignup> call, Response<MessageSignup> response) {
                                                Boolean status = response.body().getStatus();
                                                if (status) {
                                                    Toast.makeText(G.context, "مسدودی کاربر رفع شد", Toast.LENGTH_SHORT).show();
                                                    ActivityBlockuser.setdata(content, username, context);
                                                } else {

                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<MessageSignup> call, Throwable t) {
                                                Log.e("error???", t + "");
                                            }
                                        });
                                    }

                                    @Override
                                    public void onFailure(Call<Vakil> call, Throwable t) {
                                        Log.e("error???", t + "");
                                    }
                                });

                                break;
//                            case R.id.copy:
//                                //handle menu2 click
//                                ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
//                                ClipData clip = ClipData.newPlainText("chat", chatsText.getText());
//                                clipboard.setPrimaryClip(clip);
//                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.delete:
//                                //handle menu3 click
//
//                                ActivityChat.deleteitem(chatsText.getId(), context);
//
//                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

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
        return userInfos.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {


        TextView txtUsername, txtprofile;
        LinearLayout linearblock;
        ImageView imgProf;
        CardView crdprofile;

        public ChatViewHolder(View itemView) {
            super(itemView);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_black_user);
            txtprofile = (TextView) itemView.findViewById(R.id.txt_black_profile);
            linearblock = (LinearLayout) itemView.findViewById(R.id.linearblock);
            imgProf = (ImageView) itemView.findViewById(R.id.img_black_profile);
            crdprofile = (CardView) itemView.findViewById(R.id.crd_black_profile);
        }


    }
}

package com.shrazavi.dadmehr.Adapter;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;


import com.shrazavi.dadmehr.Activity.ActivityChat;
import com.shrazavi.dadmehr.Activity.ActivityImageView;
import com.shrazavi.dadmehr.Activity.ActivityPlayVideo;
import com.shrazavi.dadmehr.Activity.ActivityTicketMessage;
import com.shrazavi.dadmehr.DataClass.ChatsText;
import com.shrazavi.dadmehr.DownloadManager;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import saman.zamani.persiandate.PersianDate;


public class RecyclerAdapterChats extends RecyclerView.Adapter<RecyclerAdapterChats.ChatViewHolder> {

    public ArrayList<ChatsText> chatsTexts = new ArrayList<>();
    Context context;
    String activity;


    public RecyclerAdapterChats(Context context, ArrayList<ChatsText> chatsTexts, String activity) {
        this.context = context;
        this.activity = activity;
        this.chatsTexts = chatsTexts;

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chats, parent, false);

        return new RecyclerAdapterChats.ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        final ChatsText chatsText = chatsTexts.get(position);

        long previousTs = 0;
        if (position > 1) {
            ChatsText pm = chatsTexts.get(position - 2);
            previousTs = pm.getDate();
        }

//        holder.txtChatText.setTypeface(G.face, Typeface.NORMAL);
        if (chatsText.getFrom().equals("me")) {
            holder.linearChat.setPadding(100, 10, 5, 10);
            if (chatsText.getImgurl() != null && !chatsText.getImgurl().isEmpty()) {

                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                Picasso.with(context).load(G.nodeurl + chatsText.getImgurl()).into(holder.imgChaturl);
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.layvoisechat.setVisibility(View.VISIBLE);
                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.VISIBLE);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);

            } else if (!(chatsText.getDate() == 0)) {

                setTimeTextVisibility(previousTs, chatsText.getDate(), holder.txtChatTime, holder.linearChatClick);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.black));

                holder.linearChatClick.setBackgroundResource(R.drawable.datechat);

                holder.linearChat.setGravity(Gravity.CENTER);
                holder.linearChatClick.setGravity(Gravity.CENTER);
                holder.linearChat.setPadding(5, 10, 5, 10);
                holder.txtChatTime.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.isDeleted()) {
                holder.txtChatTime.setText("شما این پیام را پاک کرده اید");
                holder.linearChatClick.setBackgroundResource(R.drawable.deletechat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.black));
//                holder.txtChatTime.setTypeface(G.face, Typeface.ITALIC);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.laypdf.setVisibility(View.GONE);
                holder.txtChatTime.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.GONE);

            } else if (chatsText.getVidurl() != null && !chatsText.getVidurl().isEmpty()) {
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);
                holder.layvid.setVisibility(View.VISIBLE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getPdf() != null && !chatsText.getPdf().isEmpty()) {

                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vakil/Document/";
                String filename = chatsText.getPdf().replace("/upload/pdf/", "");
                String filePath = path + filename;

                if (fileExists(filePath)) {
                    holder.imgpdf.setBackgroundResource(R.drawable.picread);
                } else {
                    holder.imgpdf.setBackgroundResource(R.drawable.picdown);
                }

                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);
                holder.imgvoise.setBackgroundResource(R.drawable.ic_pdf);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.VISIBLE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getVoise() != null && !chatsText.getVoise().isEmpty()) {
                holder.txtChatText.setText("");
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);
                holder.imgvoise.setBackgroundResource(R.drawable.picvoice);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.VISIBLE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getAudio() != null && !chatsText.getAudio().isEmpty()) {
                holder.txtChatText.setText("");
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);
                holder.imgvoise.setBackgroundResource(R.drawable.picplay);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.VISIBLE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else {
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timemychat));
                holder.linearChatClick.setBackgroundResource(R.drawable.mychat);

                holder.linearChat.setGravity(Gravity.RIGHT);
                holder.linearChatClick.setGravity(Gravity.RIGHT);

                if (!chatsText.isRead()) {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_check);
                } else {
                    holder.imgChatTick.setBackgroundResource(R.drawable.ic_read);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.VISIBLE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            }

        } else {
            holder.linearChat.setPadding(5, 10, 100, 10);

            if (chatsText.getImgurl() != null && !chatsText.getImgurl().isEmpty()) {
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));
                Picasso.with(context).load(G.nodeurl + chatsText.getImgurl()).into(holder.imgChaturl);

                holder.linearChat.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.VISIBLE);
                holder.layvoisechat.setVisibility(View.VISIBLE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.GONE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (!(chatsText.getDate() == 0)) {
                setTimeTextVisibility(previousTs, chatsText.getDate(), holder.txtChatTime, holder.linearChatClick);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.black));
                holder.linearChat.setPadding(5, 10, 5, 10);
                holder.linearChatClick.setBackgroundResource(R.drawable.datechat);

                holder.linearChat.setGravity(Gravity.CENTER);
                holder.linearChatClick.setGravity(Gravity.CENTER);

                holder.txtChatTime.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.GONE);

            } else if (chatsText.isDeleted()) {
                holder.txtChatTime.setText("این پیام پاک شده است");
                holder.linearChatClick.setBackgroundResource(R.drawable.deletechat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.black));
//              holder.txtChatTime.setTypeface(G.face, Typeface.ITALIC);

                holder.linearChat.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.txtChatTime.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.GONE);

            } else if (chatsText.getVidurl() != null && !chatsText.getVidurl().isEmpty()) {
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));

                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                holder.linearChat.setGravity(Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.VISIBLE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getPdf() != null && !chatsText.getPdf().isEmpty()) {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vakil/Document/";
                String filename = chatsText.getPdf().replace("/upload/pdf/", "");
                String filePath = path + filename;

                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.imgvoise.setBackgroundResource(R.drawable.ic_pdf);

                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                holder.linearChat.setGravity(Gravity.LEFT);

                if (fileExists(filePath)) {
                    holder.imgpdf.setBackgroundResource(R.drawable.picread);
                } else {
                    holder.imgpdf.setBackgroundResource(R.drawable.picdown);
                }

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.VISIBLE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getVoise() != null && !chatsText.getVoise().isEmpty()) {
                holder.txtChatText.setText("");
                holder.txtChatTime.setText(chatsText.getTime());
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));
                holder.imgvoise.setBackgroundResource(R.drawable.picvoice);

                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                holder.linearChat.setGravity(Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else if (chatsText.getAudio() != null && !chatsText.getAudio().isEmpty()) {
                holder.txtChatText.setText("");
                holder.txtChatTime.setText(chatsText.getTime());
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));
                holder.imgvoise.setBackgroundResource(R.drawable.picplay);

                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                holder.linearChat.setGravity(Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.VISIBLE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            } else {
                holder.txtChatText.setText(chatsText.getText());
                holder.txtChatTime.setText(chatsText.getTime());
                holder.linearChatClick.setBackgroundResource(R.drawable.yourchat);
                holder.txtChatTime.setTextColor(G.context.getResources().getColor(R.color.timeyourchat));

                holder.linearChatClick.setGravity(Gravity.LEFT);
                holder.linearChatClick.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                holder.linearChat.setGravity(Gravity.LEFT);

                holder.linearChatClick.setVisibility(View.VISIBLE);
                holder.imgvoise.setVisibility(View.GONE);
                holder.layvid.setVisibility(View.GONE);
                holder.laypdf.setVisibility(View.GONE);
                holder.imgChaturl.setVisibility(View.GONE);
                holder.imgChatTick.setVisibility(View.GONE);
                holder.layvoisechat.setVisibility(View.VISIBLE);

//                holder.txtChatTime.setTypeface(G.face, Typeface.NORMAL);
            }
        }
        holder.linearChatClick.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public boolean onLongClick(View view) {
                if (chatsText.getText() != null && !chatsText.getText().isEmpty()) {

                    if (chatsText.getFrom().equals("me")) {
                        PopupMenu popup = new PopupMenu(context, holder.linearChatClick);
                        popup.getMenuInflater().inflate(R.menu.chat_option, popup.getMenu());
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
                                    case R.id.edit:
                                        //handle menu1 click
                                        if (activity.equals("chat")) {
                                            ActivityChat.objectid = "";
                                            ActivityChat.edtTextMessage.setText("");
                                            ActivityChat.btnVoice.setVisibility(View.GONE);
                                            ActivityChat.btnStop.setVisibility(View.GONE);
                                            ActivityChat.btnok.setVisibility(View.VISIBLE);
                                            ActivityChat.edit = true;
                                            ActivityChat.edtTextMessage.setText(chatsText.getText());
                                            ActivityChat.btnSend.setVisibility(View.GONE);
                                            ActivityChat.objectid = chatsText.getId();
                                        } else {
                                            ActivityTicketMessage.objectid = "";
                                            ActivityTicketMessage.edtTextMessage.setText("");
                                            ActivityTicketMessage.btnVoice.setVisibility(View.GONE);
                                            ActivityTicketMessage.btnStop.setVisibility(View.GONE);
                                            ActivityTicketMessage.btnok.setVisibility(View.VISIBLE);
                                            ActivityTicketMessage.edit = true;
                                            ActivityTicketMessage.edtTextMessage.setText(chatsText.getText());
                                            ActivityTicketMessage.btnSend.setVisibility(View.GONE);
                                            ActivityTicketMessage.objectid = chatsText.getId();
                                        }


                                        break;
                                    case R.id.copy:
                                        //handle menu2 click
                                        ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                                        ClipData clip = ClipData.newPlainText("chat", chatsText.getText());
                                        clipboard.setPrimaryClip(clip);
                                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                                        break;
                                    case R.id.delete:
                                        //handle menu3 click
                                        if (activity.equals("chat")) {
                                            ActivityChat.deleteitem(chatsText.getId(), context);
                                        }else {
                                            ActivityTicketMessage.deleteitem(chatsText.getId(), context);

                                        }
                                        break;
                                }
                                return true;
                            }
                        });
                        popup.show();


                    } else {

                        ClipboardManager clipboard = (ClipboardManager) G.context.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("chat", chatsText.getText());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
        holder.linearChatClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (chatsText.getImgurl() != null && !chatsText.getImgurl().isEmpty()) {
                    Intent intent = new Intent(RecyclerAdapterChats.this.context, ActivityImageView.class);
                    intent.putExtra("imgurl", G.nodeurl + "/" + chatsText.getImgurl());
                    intent.putExtra("down", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else if (chatsText.getVidurl() != null && !chatsText.getVidurl().isEmpty()) {
                    Intent intent2 = new Intent(RecyclerAdapterChats.this.context, ActivityPlayVideo.class);
                    intent2.putExtra("vidurl", G.nodeurl + chatsText.getVidurl());
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                } else if (chatsText.getPdf() != null && !chatsText.getPdf().isEmpty()) {

                    if (Build.VERSION.SDK_INT >= 30) {

                        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Mava/Document/";
//                        Log.e("pdfurl", G.nodeurl + chatsText.getPdf() + "");


                        String filename = chatsText.getPdf().replace("/upload/pdf/", "");
                        String filePath = path + filename;
                        Log.e("path30", filePath + "");
                        File file = new File(filePath);
                        if (fileExists(filePath)) {
                        } else {
                            new DownloadManager(G.context, G.nodeurl + chatsText.getPdf(), path);
                        }
                        Intent target = new Intent(Intent.ACTION_VIEW);

                        target.setDataAndType(Uri.fromFile(file), "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Intent intent = Intent.createChooser(target, "Open File");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        try {
                            context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                        }
                    } else if (Build.VERSION.SDK_INT >= 26 && Build.VERSION.SDK_INT < 30) {
                        String path = Environment.getExternalStorageDirectory() + "/Mava/Document/";
//                        Log.e("pdfurl", G.nodeurl + chatsText.getPdf() + "");


                        String filename = chatsText.getPdf().replace("/upload/pdf/", "");
                        String filePath = path + filename;
                        Log.e("path26", filePath + "");
                        File file = new File(filePath);
                        if (fileExists(filePath)) {
                        } else {
                            new DownloadManager(context, G.nodeurl + chatsText.getPdf(), path);
                        }
                        try {
                            Intent intentUrl = new Intent(Intent.ACTION_VIEW);
                            intentUrl.setDataAndType(Uri.parse(G.nodeurl + chatsText.getPdf()), "application/pdf");
                            intentUrl.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intentUrl);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        String path = Environment.getExternalStorageDirectory() + "/Mava/Document/";
                        Log.e("pdfurl", G.nodeurl + chatsText.getPdf() + "");


                        String filename = chatsText.getPdf().replace("/upload/pdf/", "");
                        String filePath = path + filename;
                        Log.e("path", filePath + "");
                        File file = new File(filePath);
                        if (fileExists(filePath)) {
                        } else {
                            new DownloadManager(context, G.nodeurl + chatsText.getPdf(), path);
                        }
                        Intent target = new Intent(Intent.ACTION_VIEW);

                        target.setDataAndType(Uri.fromFile(file), "application/pdf");
                        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        Intent intent = Intent.createChooser(target, "Open File");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            G.context.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            Toast.makeText(context, "No PDF Viewer Installed", Toast.LENGTH_LONG).show();
                        }

                    }

                } else if (chatsText.getVoise() != null && !chatsText.getVoise().isEmpty()) {

//                    if (isPlaying) {
//                        ActivityChat.mediaPlayer.stop();
//                        ActivityChat.mediaPlayer.reset();
//                        isPlaying=false;
////                        holder.txtChatText.setText("00:00");
////                        holder.imgvoise.setVisibility(View.GONE);
//
//                    } else {
                    try {
                        if (activity.equals("chat")) {
                            ActivityChat.mediaPlayer.reset();
//                            holder.imgvoise.setBackgroundResource(R.drawable.stoppic);
//                            ActivityChat.btnVoice.setVisibility(View.GONE);
                            ActivityChat.layrecord.setVisibility(View.VISIBLE);
                            ActivityChat.edtTextMessage.setVisibility(View.GONE);
                            ActivityChat.btnPlay.setVisibility(View.GONE);
                            ActivityChat.btnPuse.setVisibility(View.VISIBLE);
                            ActivityChat.btnVoice.setVisibility(View.GONE);
                            ActivityChat.btnStop.setVisibility(View.VISIBLE);
                            ActivityChat.btnDeletRec.setVisibility(View.GONE);
                            ActivityChat.mediaPlayer.setDataSource(G.nodeurl + chatsText.getVoise());
                            ActivityChat.isPlaying = true;
                            ActivityChat.mediaPlayer.prepare();
                            ActivityChat.mediaPlayer.start();
                            ActivityChat.lastFilename = G.nodeurl + chatsText.getVoise();
                            ActivityChat.txttimerrec.setVisibility(View.GONE);
                            ActivityChat.txttimerseek.setVisibility(View.VISIBLE);
                            ActivityChat.seekvoise.setVisibility(View.VISIBLE);
                            ActivityChat.btnTime.setText("");
//Make sure you update Seekbar on UI thread

                            ActivityChat ac = new ActivityChat();
                            ActivityChat.seekvoise.setMax(100);
                            ActivityChat.seekvoise.setProgress(0);
                            ac.updateProgress();
                        }else {
                            ActivityTicketMessage.mediaPlayer.reset();
                            ActivityTicketMessage.layrecord.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.edtTextMessage.setVisibility(View.GONE);
                            ActivityTicketMessage.btnPlay.setVisibility(View.GONE);
                            ActivityTicketMessage.btnPuse.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.btnVoice.setVisibility(View.GONE);
                            ActivityTicketMessage.btnStop.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.btnDeletRec.setVisibility(View.GONE);
                            ActivityTicketMessage.mediaPlayer.setDataSource(G.nodeurl + chatsText.getVoise());
                            ActivityTicketMessage.isPlaying = true;
                            ActivityTicketMessage.mediaPlayer.prepare();
                            ActivityTicketMessage.mediaPlayer.start();
                            ActivityTicketMessage.lastFilename = G.nodeurl + chatsText.getVoise();
                            ActivityTicketMessage.txttimerrec.setVisibility(View.GONE);
                            ActivityTicketMessage.txttimerseek.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.seekvoise.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.btnTime.setText("");
                            ActivityTicketMessage ac = new ActivityTicketMessage();
                            ActivityTicketMessage.seekvoise.setMax(100);
                            ActivityTicketMessage.seekvoise.setProgress(0);
                            ac.updateProgress();

                        }

//                                    ActivityChat.seekvoise.setMax(ActivityChat.mediaPlayer.getDuration());
//                                    ActivityChat.seekvoise.setProgress(ActivityChat.mediaPlayer.getCurrentPosition());
//                                    ActivityChat.btnTime.setText(ActivityChat.milliSecondsToTimer(mediaPlayer.getCurrentPosition()));

//                            yChat.mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//                                @Override
//                                public void onSeekComplete(MediaPlayer mediaPlayer) {
//                                    ActivityChat.mediaPlayer.reset();
//                                    ActivityChat.btnPlay.setVisibility(View.VISIBLE);
//                                }
//                            });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    }


                } else if (chatsText.getAudio() != null && !chatsText.getAudio().isEmpty()) {
                    Log.e("audiourl", G.nodeurl + chatsText.getAudio());
                    try {
                        if (activity.equals("chat")) {
                            ActivityChat.mediaPlayer.reset();
                            ActivityChat.layrecord.setVisibility(View.VISIBLE);
                            ActivityChat.edtTextMessage.setVisibility(View.GONE);
                            ActivityChat.btnPlay.setVisibility(View.GONE);
                            ActivityChat.btnPuse.setVisibility(View.VISIBLE);
                            ActivityChat.btnVoice.setVisibility(View.GONE);
                            ActivityChat.btnDeletRec.setVisibility(View.GONE);
                            ActivityChat.btnStop.setVisibility(View.VISIBLE);
                            ActivityChat.mediaPlayer.setDataSource(G.nodeurl + chatsText.getAudio());
                            ActivityChat.isPlaying = true;
                            ActivityChat.mediaPlayer.prepare();
                            ActivityChat.mediaPlayer.start();
                            ActivityChat.lastFilename = G.nodeurl + chatsText.getAudio();
                            ActivityChat.txttimerrec.setVisibility(View.GONE);
                            ActivityChat.txttimerseek.setVisibility(View.VISIBLE);
                            ActivityChat.seekvoise.setVisibility(View.VISIBLE);
                            ActivityChat.btnTime.setText("");


                            ActivityChat ac = new ActivityChat();
                            ActivityChat.seekvoise.setMax(100);
                            ActivityChat.seekvoise.setProgress(0);
                            ac.updateProgress();
                        }else {
                            ActivityTicketMessage.mediaPlayer.reset();
                            ActivityTicketMessage.layrecord.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.edtTextMessage.setVisibility(View.GONE);
                            ActivityTicketMessage.btnPlay.setVisibility(View.GONE);
                            ActivityTicketMessage.btnPuse.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.btnVoice.setVisibility(View.GONE);
                            ActivityTicketMessage.btnDeletRec.setVisibility(View.GONE);
                            ActivityTicketMessage.btnStop.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.mediaPlayer.setDataSource(G.nodeurl + chatsText.getAudio());
                            ActivityTicketMessage.isPlaying = true;
                            ActivityTicketMessage.mediaPlayer.prepare();
                            ActivityTicketMessage.mediaPlayer.start();
                            ActivityTicketMessage.lastFilename = G.nodeurl + chatsText.getAudio();
                            ActivityTicketMessage.txttimerrec.setVisibility(View.GONE);
                            ActivityTicketMessage.txttimerseek.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.seekvoise.setVisibility(View.VISIBLE);
                            ActivityTicketMessage.btnTime.setText("");
                            ActivityTicketMessage ac = new ActivityTicketMessage();
                            ActivityTicketMessage.seekvoise.setMax(100);
                            ActivityTicketMessage.seekvoise.setProgress(0);
                            ac.updateProgress();

                        }


                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else {


                }


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
        //        LinearLayout imgvoise;
        ImageView imgChatTick;
        TextView txtChatTime;
        ImageView imgChaturl;
        ImageView imgvoise;
        ImageView imgpdf;
        //        VideoView vidchat;
        RelativeLayout layvid;
        RelativeLayout laypdf;
        TextView txtTimevoise;


        public ChatViewHolder(View itemView) {
            super(itemView);
            txtChatText = (TextView) itemView.findViewById(R.id.txtChatText);
            txtChatTime = (TextView) itemView.findViewById(R.id.txtChatTime);
            imgChatTick = (ImageView) itemView.findViewById(R.id.imgChatTick);
            imgChaturl = (ImageView) itemView.findViewById(R.id.img_chat);
            linearChatClick = (LinearLayout) itemView.findViewById(R.id.linearChatClick);
            layvoisechat = (LinearLayout) itemView.findViewById(R.id.lay_voise_chat);
            linearChat = (LinearLayout) itemView.findViewById(R.id.lay_chat);
//            imgvoise = (LinearLayout) itemView.findViewById(R.id.img_play_voise);
//            vidchat = (VideoView) itemView.findViewById(R.id.vid_chat);
            laypdf = (RelativeLayout) itemView.findViewById(R.id.laypdf);
            layvid = (RelativeLayout) itemView.findViewById(R.id.layvid);
            imgvoise = (ImageView) itemView.findViewById(R.id.img_play_voise);
            imgpdf = (ImageView) itemView.findViewById(R.id.imgpdf);
//            txtTimevoise = (TextView) itemView.findViewById(R.id.txt_time_record);
        }

    }

    private void setTimeTextVisibility(long ts1, long ts2, TextView timeText, LinearLayout linearChatClick) {
        PersianDate cal1 = new PersianDate(ts1);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        PersianDate cal2 = new PersianDate(ts2);

        Calendar today1 = Calendar.getInstance();
        today1.getTimeZone();

        Calendar yesterday1 = Calendar.getInstance();
        yesterday1.getTimeZone();
        PersianDate today = new PersianDate(today1.getTimeInMillis());
        PersianDate yesterday = new PersianDate(yesterday1.getTimeInMillis());
//        boolean sametody1 = today.getShYear() == cal1.getShYear() &&
//                today.getShMonth() == cal1.getShMonth() &&
//                today.getShDay() == cal1.getShDay();
        int yesterdayday = yesterday.getShDay() - 1;


//        Calendar yesterday = Calendar.getInstance();
//        Calendar cal = GregorianCalendar.getInstance(tz);
//        cal1.setTimeInMillis(ts1);
//        cal2.setTimeInMillis(ts2);
//        cal1.get(Calendar.DAY_OF_WEEK);
        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        cal1.setTimeInMillis(ts1);
//         formatter.format(cal1.getTime());
//        Log.e("today", today.getShYear() + "/" + today.getShMonth() + "/" + today.getShDay());
//        Log.e("yesterday", yesterday.getShYear() + "/" + yesterday.getShMonth() + "/" + yesterdayday);
        if (ts2 == 0) {
            linearChatClick.setVisibility(View.VISIBLE);

            boolean sametody1 = today.getShYear() == cal1.getShYear() &&
                    today.getShMonth() == cal1.getShMonth() &&
                    today.getShDay() == cal1.getShDay();
            boolean sameyesterday1 = yesterday.getShYear() == cal1.getShYear() &&
                    yesterday.getShMonth() == cal1.getShMonth() &&
                    yesterdayday == cal1.getShDay();
//            Log.e("datechat1", cal1.getShYear() + "/" + cal1.getShMonth() + "/" + cal1.getShDay());
//            Log.e("flaq1today1",sametody1+"");
//
//            Log.e("flaq1yesterday1",sameyesterday1+"");

//            SolarCalendar sc=new SolarCalendar(cal1.get(Calendar.YEAR),cal1.get(Calendar.MONTH),cal1.get(Calendar.DATE),cal1.get(Calendar.DAY_OF_WEEK));
            if (sametody1) {
                timeText.setText("امروز  ");
            } else if (sameyesterday1) {
                timeText.setText("دیروز  ");
            } else {

                timeText.setText(cal1.getShYear() + " " + cal1.monthName() + " " + cal1.getShDay() + " ");
            }
//            timeText.setText(formatter.format(cal1.getTime()));
        } else {


            boolean sameMonth = cal1.getShYear() == cal2.getShYear() &&
                    cal1.getShMonth() == cal2.getShMonth() &&
                    cal1.getShDay() == cal2.getShDay();

            if (sameMonth) {
                linearChatClick.setVisibility(View.GONE);
                timeText.setText("");
            } else {

                linearChatClick.setVisibility(View.VISIBLE);
//                SolarCalendar sc2=new SolarCalendar(cal2.get(Calendar.YEAR),cal2.get(Calendar.MONTH),cal2.get(Calendar.DATE),cal2.get(Calendar.DAY_OF_WEEK));
//                timeText.setText(sc2.getCurrentShamsidate());
                boolean sametody2 = today.getShYear() == cal2.getShYear() &&
                        today.getShMonth() == cal2.getShMonth() &&
                        today.getShDay() == cal2.getShDay();
                boolean sameyesterday2 = today.getShYear() == cal2.getShYear() &&
                        yesterday.getShMonth() == cal2.getShMonth() &&
                        yesterdayday == cal2.getShDay();
//                Log.e("datechat2", cal2.getShYear() + "/" + cal2.getShMonth() + "/" + cal2.getShDay());
//                Log.e("flaq2today2",sametody2+"");
//
//                Log.e("flaq2yesterday2",sameyesterday2+"");
                if (sametody2) {
                    timeText.setText("امروز  ");
                } else if (sameyesterday2) {
                    timeText.setText("دیروز  ");
                } else {
                    timeText.setText(cal2.getShYear() + " " + cal2.monthName() + " " + cal2.getShDay() + " ");
                }
//                timeText.setText(formatter.format(cal2.getTime()));
            }

        }
    }

    public boolean fileExists(String filepath) {
        File file = new File(filepath);
        if (file == null || !file.exists()) {
            return false;
        }
        return true;
    }
}

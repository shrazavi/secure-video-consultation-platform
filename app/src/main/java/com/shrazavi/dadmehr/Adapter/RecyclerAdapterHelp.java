package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.DataClass.Help;
import com.shrazavi.dadmehr.DataClass.Helpmore;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.RetrofitFactorynode;
import com.shrazavi.dadmehr.Retrofitinformation;
import com.shrazavi.dadmehr.core.base.BasicActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecyclerAdapterHelp extends RecyclerView.Adapter<RecyclerAdapterHelp.ChatViewHolder> {

    public ArrayList<Helpmore> title = new ArrayList<>();
    Context context;
    Retrofitinformation RI;
    String vu = "";
    String myid;
    LinearLayoutManager linearLayoutManager;

//    public SharedPreferences preferences;
    RecyclerAdapterHelpmore recyclerAdapterhelpmore ;
    public RecyclerAdapterHelp(Context context, ArrayList<Helpmore> title) {
        this.context = context;
        this.title = title;

    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_law, parent, false);

        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        final Helpmore help = title.get(position);
//        preferences = PreferenceManager.getDefaultSharedPreferences(G.context);
        RI = RetrofitFactorynode.getclient().create(Retrofitinformation.class);
        vu = BasicActivity.vu;
        myid = BasicActivity.userid;
        long previousTs = 0;

        holder.txtlaw.setTypeface(G.face);
        holder.txtlaw.setText(help.getText());
//        holder.txtChatText.setTypeface(G.face, Typeface.NORMAL);

//        holder.linearChat.setPadding(100,10,5,10);

        if (help.getSelect()==0){
            holder.reclaw.setVisibility(View.GONE);
            holder.imglaw.setBackgroundResource(R.drawable.left_vec);
        }

        holder.layitem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (help.getSelect()==1){
                    help.setSelect(0);
                    holder.reclaw.setVisibility(View.GONE);
                    holder.imglaw.setBackgroundResource(R.drawable.left_vec);

                }else {
                    help.setSelect(1);
                    holder.reclaw.setVisibility(View.VISIBLE);
                    holder.imglaw.setBackgroundResource(R.drawable.down_vec);

                }

                Call<ArrayList<Help>> calllog = RI.gethelp(help.getType());
                calllog.enqueue(new Callback<ArrayList<Help>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Help>> call, Response<ArrayList<Help>> response) {

                        linearLayoutManager = new LinearLayoutManager(G.context);
                        holder.reclaw.setHasFixedSize(true);
                        holder.reclaw.setLayoutManager(linearLayoutManager);
                        holder.reclaw.setAdapter(recyclerAdapterhelpmore = new RecyclerAdapterHelpmore(G.context, response.body()));
                        recyclerAdapterhelpmore.notifyDataSetChanged();


                    }

                    @Override
                    public void onFailure(Call<ArrayList<Help>> call, Throwable t) {
                        Log.e("calllogerror", t + "");
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView txtlaw;
        ImageView imglaw;
        LinearLayout layitem;
        RecyclerView reclaw;

        public ChatViewHolder(View itemView) {
            super(itemView);
            txtlaw = (TextView) itemView.findViewById(R.id.txt_item_title);
            imglaw = (ImageView) itemView.findViewById(R.id.img_item_law);
            layitem = (LinearLayout) itemView.findViewById(R.id.lay_item_law);
            reclaw = (RecyclerView) itemView.findViewById(R.id.rec_item_law);

        }

    }


}

package com.shrazavi.dadmehr.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shrazavi.dadmehr.Activity.ActivityTransactionDetail;
import com.shrazavi.dadmehr.DataClass.Transaction;
import com.shrazavi.dadmehr.G;
import com.shrazavi.dadmehr.R;
import com.shrazavi.dadmehr.SolarCalendar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import saman.zamani.persiandate.PersianDate;

public class RecyclerAdapterTransAction extends RecyclerView.Adapter<RecyclerAdapterTransAction.transViewHolder> {


    public ArrayList<Transaction> trans = new ArrayList<>();
    Context context;
//    public SharedPreferences preferences;

    public RecyclerAdapterTransAction(ArrayList<Transaction> trans, Context context) {
        this.context = context;
        this.trans = trans;
    }

    @Override
    public transViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction, parent, false);
        return new transViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final transViewHolder holder, final int position) {

        final Transaction transaction = trans.get(position);
        Log.i("LOG", "recycler done");
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        holder.txtprice.setTypeface(G.face);
        holder.txttime.setTypeface(G.face);
        holder.txtstatus.setTypeface(G.face);
        holder.txtdate.setTypeface(G.face);
        holder.txtprice.setText(formatter.format(Integer.parseInt(transaction.getPrice())) + " تومان");
        holder.txttime.setText(gettime(transaction.getDate()));

        if (transaction.getStatus().equals("successful")) {
            holder.txtstatus.setText("موفق");
            holder.imgstatus.setBackgroundResource(R.drawable.picsucces);
            holder.txtstatus.setTextColor(G.context.getResources().getColor(R.color.timemychat));
        } else {
            holder.imgstatus.setBackgroundResource(R.drawable.picunsucces);
            holder.txtstatus.setTextColor(G.context.getResources().getColor(R.color.unsuccesful));
            holder.txtstatus.setText("ناموفق");
        }
        setdate(transaction.getDate(), holder.txtdate);
//        if(transaction.count!=0){
//            holder.txtCountMessage.setVisibility(View.VISIBLE);
//            // Log.e("countR",chatInfo.count+"");
//            holder.txtCountMessage.setText(transaction.count+"");
//
//        }else{
//            holder.txtCountMessage.setVisibility(View.GONE);
//        }
//
//        if(!transaction.imgProfile.equals("empty")){
//            Picasso.with(context).load(G.phpurl+"/"+transaction.imgProfile).into(holder.imgProf);
////            Glide.with(context).load(G.phpurl+"/"+chatInfo.imgProfile).placeholder(R.drawable.ic_profle).error(R.drawable.ic_profle).into(holder.imgProf);
//
//            //  Log.e("imguserurlm",G.phpurl+chatInfo.imgProfile);
//        }

        holder.lineartrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(context, ActivityTransactionDetail.class);
                //Toast.makeText(context, holder.txtUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                //Intent intent=new Intent(G.context, ActivityMain.class);
                intent.putExtra("price", holder.txtprice.getText().toString());
                intent.putExtra("status", transaction.getStatus());
                intent.putExtra("date", holder.txtdate.getText().toString());
                intent.putExtra("time", holder.txttime.getText().toString());
                intent.putExtra("code", transaction.getCode()+"");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
//
//
//
            }
        });

    }

    @Override
    public int getItemCount() {
        return trans.size();
    }

    public class transViewHolder extends RecyclerView.ViewHolder {


        TextView txtprice;
        TextView txtdate;
        TextView txttime;
        TextView txtstatus;
        LinearLayout lineartrans;
        ImageView imgstatus;


        public transViewHolder(View itemView) {
            super(itemView);
            txtprice = (TextView) itemView.findViewById(R.id.txt_tr_price);
            txtdate = (TextView) itemView.findViewById(R.id.txt_tr_date);
            txttime = (TextView) itemView.findViewById(R.id.txt_tr_time);
            txtstatus = (TextView) itemView.findViewById(R.id.txt_tr_status);
            lineartrans = (LinearLayout) itemView.findViewById(R.id.lay_transaction);
            imgstatus = (ImageView) itemView.findViewById(R.id.img_tr_status);
        }


    }

    private String gettime(long ts) {
        PersianDate cal = new PersianDate(ts);
        int h = cal.getHour();
        int m = cal.getMinute();
        String h2, m2;
        if (h < 10) {
            h2 = "0" + h;
        } else {
            h2 = "" + h;
        }
        if (m < 10) {
            m2 = "0" + m;
        } else {
            m2 = "" + m;
        }
        return h2 + ":" + m2;
    }

    private void setdate(long date, TextView dateText) {


        Calendar cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(date);

        SolarCalendar sc = new SolarCalendar(cal1.get(Calendar.YEAR), cal1.get(Calendar.MONTH), cal1.get(Calendar.DATE), cal1.get(Calendar.DAY_OF_WEEK));
        dateText.setText(sc.getCurrentShamsidate());

    }
}
